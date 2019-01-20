package com.umesh.flickrsearch;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SearchActivityMain extends AppCompatActivity {

    private static String TAG = SearchActivityMain.class.getName();
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int GRID_LAYOUT_ROWS = 3;

    private RecyclerView recyclerView;
    private String searchString = "";
    private SearchView searchView;
    private EndlessRecyclerViewScrollListener scrollListener;
    private ProgressBar progressBar;
    private TextView emptyView;

    private RestAPI manager;
    private GridLayoutManager layoutManager;
    private PhotoAdapter photoAdapter;
    private int totalPages;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = RestAPIFactory.create();

        recyclerView = findViewById(R.id.main_results);
        progressBar = findViewById(R.id.main_progress);
        emptyView = findViewById(R.id.empty_view);

        layoutManager = new GridLayoutManager(this, GRID_LAYOUT_ROWS, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        photoAdapter = new PhotoAdapter(getApplicationContext());
        recyclerView.setAdapter(photoAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNext(++currentPage);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (searchString.equals(query)) {
                    return false;
                }

                photoAdapter.clear();
                photoAdapter.clearCaches();
                searchString = query;

                loadFirstPage();

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        if (!checkPermission()) {
            requestPermission();
        }

        emptyDataCheck();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void loadNext(int requestedPage) {
        String page = String.valueOf(requestedPage);
        showProgress();

        if (currentPage <= totalPages) {
            manager.fetchSearchResponse("flickr.photos.search",
                                        "3e7cc266ae2b0e0d78e279ce8e361736",
                                        "json",
                                        "1",
                                        "1",
                                        page,
                                        "30",
                                        searchString).enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                    Log.i(TAG, "loadNext response");
                    SearchResponse searchResponse = response.body();
                    totalPages = searchResponse.getPhotos().getPages();
                    currentPage = searchResponse.getPhotos().getPage();

                    List<PhotoModel> photos = searchResponse.getPhotos().getPhoto();
                    photoAdapter.addAll(photos);

                    stopProgress();
                    emptyDataCheck();

                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    // handle error
                    Log.i(TAG, "loadNext failed");

                    stopProgress();
                    emptyDataCheck();

                }
            });
        }
    }

    private void loadFirstPage() {
        showProgress();
        manager.fetchSearchResponse("flickr.photos.search",
                                    "3e7cc266ae2b0e0d78e279ce8e361736",
                                    "json",
                                    "1",
                                    "1",
                                    "1",
                                    "60",
                                    searchString).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                Log.i(TAG, "loadFirstPage response");
                SearchResponse searchResponse = response.body();
                totalPages = searchResponse.getPhotos().getPages();
                currentPage = searchResponse.getPhotos().getPage();

                List<PhotoModel> photos = searchResponse.getPhotos().getPhoto();
                photoAdapter.setPhotos(photos);
                photoAdapter.notifyDataSetChanged();

                stopProgress();
                emptyDataCheck();

            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // handle error
                Log.i(TAG, "loadFirstPage failed");

                stopProgress();
                emptyDataCheck();
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (!storageAccepted) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to storage for app to work.",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},
                                                                                   PERMISSION_REQUEST_CODE);
                                                            }
                                                        }
                                                    });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SearchActivityMain.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void emptyDataCheck() {
        if (photoAdapter.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void showProgress() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    private void stopProgress() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }
}

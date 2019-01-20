package com.umesh.flickrsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<PhotoModel> photos;

    private boolean isLoadingAdded = false;
    private ImageLoader imageLoader;

    public PhotoAdapter(Context context) {
        photos = new ArrayList<>();
        imageLoader = new ImageLoader(context);
    }

    public void setPhotos(List<PhotoModel> photos) {
        this.photos = photos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_photo, parent, false);
        viewHolder = new PhotoViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PhotoModel photoModel = photos.get(position);
        String url = getPhotoURL(photoModel);

        switch (getItemViewType(position)) {
            case ITEM:
                final PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;
                imageLoader.DisplayImage(url, photoViewHolder.photoView);

                break;

            case LOADING:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return photos == null ? 0 : photos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(PhotoModel r) {
        photos.add(r);
        notifyItemInserted(photos.size() - 1);
    }

    public void addAll(List<PhotoModel> photos) {
        for (PhotoModel photo : photos) {
            add(photo);
        }
    }

    public void remove(PhotoModel r) {
        int position = photos.indexOf(r);
        if (position > -1) {
            photos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public PhotoModel getItem(int position) {
        return photos.get(position);
    }


    protected class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView photoView;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            photoView = itemView.findViewById(R.id.image_view);
        }
    }


    protected class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    // http://farm{farm}.static.flickr.com/{server}/{id}_{secret}.jpg
    private String getPhotoURL(PhotoModel model) {
        return "http://farm" + model.getFarm() + ".static.flickr.com/"
                + model.getServer() + "/"
                + model.getId()
                + "_"
                + model.getSecret() + ".jpg";
    }

    public void clearCaches() {
        imageLoader.clearCache();
    }

}

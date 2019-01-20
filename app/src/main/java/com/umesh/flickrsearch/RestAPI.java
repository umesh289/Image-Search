package com.umesh.flickrsearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestAPI {

    @GET("/services/rest/")
    Call<SearchResponse> fetchSearchResponse(@Query("method") String method,
                                             @Query("api_key") String apiKey,
                                             @Query("format") String format,
                                             @Query("nojsoncallback") String noJsonCallback,
                                             @Query("safe_search") String safeSearch,
                                             @Query("page") String page,
                                             @Query("per_page") String perPage,
                                             @Query("text") String searchString);
}

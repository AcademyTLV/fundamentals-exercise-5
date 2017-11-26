package com.example.exercise5.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Roee on 25/11/2017.
 */

public interface PixabayService {

    final String BASE_URL = "https://pixabay.com/api/";

    final String apiKey = "7143795-63f8097bd68601f11b7e06188";
    final String keyQuery= "?key=" + apiKey;

    /* image_type parameters */
    final String IMAGE_TYPE_ALL = "all";
    final String IMAGE_TYPE_PHOTO = "photo";
    final String IMAGE_TYPE_ILLUSTRATION = "illustration";
    final String IMAGE_TYPE_VECTOR = "vector";

    @GET(keyQuery)
    Call<ImageSearchResult> searchImage(@Query("q") String queryValue, @Query("image_type") String imageType);
}

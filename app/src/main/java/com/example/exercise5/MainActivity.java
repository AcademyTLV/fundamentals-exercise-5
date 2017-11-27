package com.example.exercise5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.exercise5.rest.Hit;
import com.example.exercise5.rest.HitsAdapter;
import com.example.exercise5.rest.ImageSearchResult;
import com.example.exercise5.rest.PixabayService;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // retrofit
    Retrofit retrofit;
    PixabayService service;

    // views
    EditText searchEditText;
    RecyclerView hitsRecyclerView;
    TextView hitsDescription;
    Button searchButton;

    int pageNumber = 0;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init views
        hitsRecyclerView = findViewById(R.id.hits_recycler_view);
        hitsDescription = findViewById(R.id.hits_description);
        searchEditText = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.search_button);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        hitsRecyclerView.setLayoutManager(mLayoutManager);


        // init interceptors to query different page every time.
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(PixabayService.PAGE, String.valueOf(++pageNumber))
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        // init retrofit.
        retrofit = new Retrofit.Builder().
                baseUrl(PixabayService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        service = retrofit.create(PixabayService.class);

    }

    public void searchImages (View view) {

        // let the user know something is happening
        hitsDescription.setText("Searching...");

        // create the call
        Call<ImageSearchResult> imageSearchResultCall = service.searchImage(searchEditText.getText().toString(), PixabayService.IMAGE_TYPE_ALL);

        // make the call
        imageSearchResultCall.enqueue(new Callback<ImageSearchResult>() {
            @Override
            public void onResponse (Call<ImageSearchResult> call, Response<ImageSearchResult> response) {

                // check if call went through and update the UI accordingly.
                if (response.code() == 200) {
                    hitsDescription.setText(response.body().getTotalHits() + " hits were found for " + searchEditText.getText().toString());
                    ArrayList<Hit> hits = (ArrayList<Hit>) response.body().getHits();
                    HitsAdapter hitsAdapter = new HitsAdapter(hits);
                    hitsRecyclerView.setAdapter(hitsAdapter);

                    // Update button text after first search succeeded
                    if (pageNumber == 1) {
                        searchButton.setText(R.string.show_next_results);
                    }
                } else {
                    hitsDescription.setText("Something went wrong, error code: " + response.code());
                }

            }

            @Override
            public void onFailure (Call<ImageSearchResult> call, Throwable t) {
                // HTTP call failed, show something to the user.
                hitsDescription.setText("Something went wrong");
            }
        });
    }
}

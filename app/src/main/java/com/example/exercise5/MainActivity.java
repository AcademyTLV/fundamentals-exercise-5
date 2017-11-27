package com.example.exercise5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.exercise5.rest.Hit;
import com.example.exercise5.rest.HitsAdapter;
import com.example.exercise5.rest.ImageSearchResult;
import com.example.exercise5.rest.PixabayService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // retrofit
    private Retrofit retrofit;
    private PixabayService service;

    // views
    private EditText searchEditText;
    private RecyclerView hitsRecyclerView;
    private TextView hitsDescription;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init views
        hitsRecyclerView = findViewById(R.id.hits_recycler_view);
        hitsDescription = findViewById(R.id.hits_description);
        searchEditText = findViewById(R.id.search_edit_text);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        hitsRecyclerView.setLayoutManager(mLayoutManager);

        // init retrofit.
        retrofit = new Retrofit.Builder().
                baseUrl(PixabayService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PixabayService.class);



    }

    public void searchImages (View view) {

        // let the user know something is happening
        hitsDescription.setText(R.string.searching);

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
                } else {
                    hitsDescription.setText("Something went wrong, error code: " + response.code());
                }

            }

            @Override
            public void onFailure (Call<ImageSearchResult> call, Throwable t) {
                // HTTP call failed, show something to the user.
                hitsDescription.setText(R.string.something_went_wrong);
            }
        });
    }
}

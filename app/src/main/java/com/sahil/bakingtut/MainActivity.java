package com.sahil.bakingtut;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sahil.bakingtut.Adapter.MainActivityAdapter;
import com.sahil.bakingtut.Model.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    List<Recipe> recipesList = new ArrayList<>();
    Recipe[] recipes;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.pro_bar)
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        if(savedInstanceState!=null)
        {
            if(savedInstanceState.getSerializable(Constants.HOME_ACTIVITY_SERIALIZABLE_KEY)!=null)
                recipesList = (List<Recipe>) savedInstanceState.getSerializable(Constants.HOME_ACTIVITY_SERIALIZABLE_KEY);
            else if(isNetworkAvailable())
            {
                loadData();
            }
            else{
                showAlert();
            }
        }
        else if(isNetworkAvailable())
        {
            loadData();
        }
        else {
            showAlert();
        }

    }

    private void showAlert()
    {
        new AlertDialog.Builder(this)
                .setTitle("INTERNET IS NOT AVAILABLE")
                .setMessage("Kindly connect to internet to proceed Further. Click 'yes' to close app!")
                .setIcon(R.drawable.ic_internet_not_available)
                .setPositiveButton("yes", (dialog, which) -> finish())
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void loadData()
    {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(URL, response -> {
            progressBar.setVisibility(View.GONE);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            recipes = gson.fromJson(response, Recipe[].class);
            recipesList.addAll(Arrays.asList(recipes));
            MainActivityAdapter maa = new MainActivityAdapter(MainActivity.this,recipesList);
            recyclerView.setAdapter(maa);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        }, error -> {

        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if(recipesList!=null)
        {
            outState.putSerializable(Constants.HOME_ACTIVITY_SERIALIZABLE_KEY, (Serializable) recipesList);
        }
    }
}


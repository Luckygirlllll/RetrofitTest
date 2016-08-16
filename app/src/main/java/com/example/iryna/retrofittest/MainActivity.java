package com.example.iryna.retrofittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity implements Callback<StackOverflowQuestions> {

    private Button button;
    private ListView listView;
    private ArrayAdapter<Question> arrayAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        listView =(ListView) findViewById(R.id.listView);

        arrayAdapter =
                new ArrayAdapter<Question>(this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new ArrayList<Question>());
       // setListAdapter(arrayAdapter);
        setProgressBarIndeterminateVisibility(true);
        setProgressBarVisibility(true);

        listView.setAdapter(arrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.stackexchange.com")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                // prepare call in Retrofit 2.0
                StackOverflowAPI stackOverflowAPI = retrofit.create(StackOverflowAPI.class);

                Call<StackOverflowQuestions> call = stackOverflowAPI.loadQuestions("android");
                //asynchronous call
                call.enqueue(MainActivity.this);

                // synchronous call would be with execute, in this case you
                // would have to perform this outside the main thread
                // call.execute()

                // to cancel a running request
                // call.cancel();
                // calls can only be used once but you can easily clone them
                //Call<StackOverflowQuestions> c = call.clone();
                //c.enqueue(this);

               // return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setProgressBarIndeterminateVisibility(true);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        StackOverflowAPI stackOverflowAPI = retrofit.create(StackOverflowAPI.class);

        Call<StackOverflowQuestions> call = stackOverflowAPI.loadQuestions("android");
        //asynchronous call
        call.enqueue(this);

        // synchronous call would be with execute, in this case you
        // would have to perform this outside the main thread
        // call.execute()

        // to cancel a running request
        // call.cancel();
        // calls can only be used once but you can easily clone them
        //Call<StackOverflowQuestions> c = call.clone();
        //c.enqueue(this);

        return true;
    }


    @Override
    public void onResponse(Call<StackOverflowQuestions> call, Response<StackOverflowQuestions> response) {
        setProgressBarIndeterminateVisibility(false);
//        ArrayAdapter<Question> adapter = (ArrayAdapter<Question>) getListAdapter();
        arrayAdapter.clear();
//        Log.wtf("Response",  response.body().items);
        arrayAdapter.addAll(response.body().items);
    }

    @Override
    public void onFailure(Call<StackOverflowQuestions> call, Throwable t) {
        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}

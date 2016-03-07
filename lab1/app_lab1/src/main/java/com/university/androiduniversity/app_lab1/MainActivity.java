package com.university.androiduniversity.app_lab1;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


public class MainActivity extends Activity {

    private Spinner currency1Spinner;
    private Spinner currency2Spinner;

    private EditText currency1Value;
    private EditText currency2Value;

    private Button converButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpComponents();
        setUpSpinnerAdapters();
    }

    private void setUpComponents(){
        currency1Spinner = (Spinner) findViewById(R.id.currency1Spinner);
        currency2Spinner = (Spinner) findViewById(R.id.currency2Spinner);

        currency1Value = (EditText) findViewById(R.id.currency1Value);
        currency2Value = (EditText) findViewById(R.id.currency2Value);

        converButton = (Button) findViewById(R.id.convertButton);
        converButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( currency1Value.getText().toString().matches("^[0-9]*$") ){
                    if ( isNetworkAvailable() ){
                        if ( currency1Spinner.getSelectedItem() != currency2Spinner.getSelectedItem() ) {
                            String currencyFrom = currency1Spinner.getSelectedItem().toString();
                            String currencyTo = currency2Spinner.getSelectedItem().toString();

                            Float valueFrom = Float.parseFloat(currency1Value.getText().toString());

                            Float rate = getCurrentRateForCurrencies(currencyFrom, currencyTo, valueFrom);
                            currency2Value.setText(rate.toString());

                        } else Toast.makeText(getApplicationContext(), "Error: same currencies", Toast.LENGTH_SHORT)
                                .show();
                    } else
                        Toast.makeText(getApplicationContext(), "Error: no Internet connection", Toast.LENGTH_SHORT)
                                .show();
                } else Toast.makeText(getApplicationContext(), "Error: not valid number", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setUpSpinnerAdapters(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currencies,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        currency1Spinner.setAdapter(adapter);
        currency2Spinner.setAdapter(adapter);
        currency2Spinner.setSelection(1);
    }

    private Float getCurrentRateForCurrencies(String from, String to, Float howMuch){
        String result = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // TODO better to use AsyncTask

            String query = "https://www.google.com.ua/search?q=" + howMuch.intValue() + "+" + from + "+to+" + to;
            Document doc = Jsoup.connect(query).get();
            Elements values = doc.getElementsByClass("vk_ans");
            Element value = values.get(0);
            String[] resultValues = value.text().split(" ");
            result = resultValues[0].replaceAll("\\s+", "");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Float.parseFloat(result);

    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

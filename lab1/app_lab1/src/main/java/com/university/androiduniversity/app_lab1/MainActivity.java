package com.university.androiduniversity.app_lab1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;


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
                if ( currency1Spinner.getSelectedItem() != currency2Spinner.getSelectedItem() ){
                    // TODO get from internet?
                } else Toast.makeText(getApplicationContext(), "Error: same currencies", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpSpinnerAdapters(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currencies,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        currency1Spinner.setAdapter(adapter);
        currency2Spinner.setAdapter(adapter);
    }


}

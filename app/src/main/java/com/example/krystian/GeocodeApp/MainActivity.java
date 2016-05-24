package com.example.krystian.GeocodeApp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by: Krystian Klimek
 * Date: 26.02.2016.
 */

public class MainActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private CityAdapter cityAdapter;
    private TextView addNewCity;
    private EditText newCityEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeRecyclerView();
        initializeComponents();
        onAddNewCity();
        new HideKeyboard(getApplicationContext(), this.getCurrentFocus());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar();
        return super.onCreateOptionsMenu(menu);
    }

    public void onAddNewCity() {
        addNewCity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getEditText();
                        if (validation()) {
                            new HideKeyboard(getApplicationContext(), v);
                            cityAdapter.saveNewCity(newCityEditText);
                            clearEditText();
                        }
                    }
                }
        );
    }

    private void clearEditText() {
        newCityEditText.setText("");
        newCityEditText.clearFocus();
    }

    private void toolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
    }

    private void getEditText() {
        newCityEditText = (EditText) findViewById(R.id.newCityEditText);
    }

    private void initializeRecyclerView () {
        recyclerView = (RecyclerView) findViewById(R.id.cityListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        cityAdapter = new CityAdapter(getBaseContext());
        recyclerView.setAdapter(cityAdapter);
    }

    private void initializeComponents() {
        addNewCity = (TextView) findViewById(R.id.addNewCity);
    }

    private boolean validation() {
        return Validation.test(newCityEditText, 0, getString(R.string.write_city_name));
    }
}

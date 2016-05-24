package com.example.krystian.GeocodeApp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Krystian Klimek
 * Date: 26.02.2016.
 */

public class DetailActivity extends AppCompatActivity {
    TextView localityTextView, area3TextView, area2TextView, area1TextView, countryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Map<String, String> cityDetail = (HashMap<String, String>) intent.getSerializableExtra(getString(R.string.city_detail));

        initizalizeTextViews();
        setTextViewValues(cityDetail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar();
        return super.onCreateOptionsMenu(menu);
    }

    private void toolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.detailToolbar);
        setSupportActionBar(mainToolbar);
    }

    private void setTextViewValues(Map<String, String> cityDetail) {
        Resources res = getApplicationContext().getResources();
        localityTextView.setText(res.getString(R.string.locality_text) + cityDetail.get(res.getString(R.string.locality)));
        area3TextView.setText(res.getString(R.string.administrative_area_level_3_text) + cityDetail.get(res.getString(R.string.administrative_area_level_3)));
        area2TextView.setText(res.getString(R.string.administrative_area_level_2_text) + cityDetail.get(res.getString(R.string.administrative_area_level_2)));
        area1TextView.setText(res.getString(R.string.administrative_area_level_1_text) + cityDetail.get(res.getString(R.string.administrative_area_level_1)));
        countryTextView.setText(res.getString(R.string.country_text) + cityDetail.get(res.getString(R.string.country)));
    }

    private void initizalizeTextViews() {
        localityTextView = (TextView) findViewById(R.id.localityTextView);
        area3TextView = (TextView) findViewById(R.id.area3TextView);
        area2TextView = (TextView) findViewById(R.id.area2TextView);
        area1TextView = (TextView) findViewById(R.id.area1TextView);
        countryTextView = (TextView) findViewById(R.id.countryTextView);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}

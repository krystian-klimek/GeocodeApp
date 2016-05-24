package com.example.krystian.GeocodeApp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krystian.GeocodeApp.DetailActivity;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Krystian on 26.02.2016.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private static Context context;
    private static View view;
    private static final String START_URL_PART = "http://maps.googleapis.com/maps/api/geocode/json?address=";
    private static final String END_URL_PART = "&sensor=true&language=pl";
    private List<String> cityList;
    private boolean statusOK;

    public CityAdapter(Context context) {
        this.context = context;
        toList();
    }

    private void toList() {
        cityList = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.cityStartList)));
        if (cityList == null) {
            Toast.makeText(context, context.getResources().getString(R.string.no_saved_cities), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_row, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        String cityName = cityList.get(position);
        holder.cityTextView.setText(cityName);

        if (position%2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTransparent));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrey));
        }
    }

    @Override
    public int getItemCount() {
        return cityList == null ? 0 : cityList.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView cityTextView;

        public CityViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cityTextView = (TextView) itemView.findViewById(R.id.cityTextView);
        }

        @Override
        public void onClick(View v) {
            new HideKeyboard(context, v);
            if (isOnline()) {
                new WebServiceHandler().execute(START_URL_PART + Uri.encode(cityList.get(getAdapterPosition()).trim()) + END_URL_PART);
            }
            else {
                Toast.makeText(context, context.getResources().getString(R.string.internet_connection_is_required), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    public void saveNewCity (EditText newCityEditText) {
        String cityName = newCityEditText.getText().toString().trim();
        cityList.add(0, cityName);
        notifyDataSetChanged();
        notifyItemInserted(0);
    }

    public boolean isStatusOK() {
        return statusOK;
    }

    public void setStatusOK(boolean statusOK) {
        this.statusOK = statusOK;
    }

    private class WebServiceHandler extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();

                InputStream in = new BufferedInputStream(
                        connection.getInputStream());

                return streamToString(in);

            } catch (Exception e) {
                Log.d(MainActivity.class.getSimpleName(), e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                Map<String, String> city = parseResponse(result);

                if (isStatusOK()) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(context.getResources().getString(R.string.city_detail), (Serializable) city);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(context, R.string.wrong_city_name, Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Log.d(MainActivity.class.getSimpleName(), e.toString());
            }
        }
    }

    private Map<String, String> parseResponse (String responseString) throws JSONException {
        Map<String, String> city = new HashMap<>();

        GeoLocationResult geoLocationResult = new Gson().fromJson(responseString, GeoLocationResult.class);

        city.put(context.getResources().getString(R.string.locality), geoLocationResult.getResults().get(0).getAddress_components().get(0).getLong_name());
        city.put(context.getResources().getString(R.string.administrative_area_level_3), geoLocationResult.getResults().get(0).getAddress_components().get(1).getLong_name());
        city.put(context.getResources().getString(R.string.administrative_area_level_2), geoLocationResult.getResults().get(0).getAddress_components().get(2).getLong_name());
        city.put(context.getResources().getString(R.string.administrative_area_level_1), geoLocationResult.getResults().get(0).getAddress_components().get(3).getLong_name());
        city.put(context.getResources().getString(R.string.country), geoLocationResult.getResults().get(0).getAddress_components().get(4).getLong_name());

        setStatusOK(geoLocationResult.getStatus().equals(context.getString(R.string.ok)));

        return city;
    }

    public static String streamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try {

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            reader.close();

        } catch (IOException e) {
            Log.d(MainActivity.class.getSimpleName(), e.toString());
        }

        return stringBuilder.toString();
    }
}
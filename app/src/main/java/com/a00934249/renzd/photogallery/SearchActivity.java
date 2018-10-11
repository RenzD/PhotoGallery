package com.a00934249.renzd.photogallery;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


//import static com.a00934249.renzd.photogallery.MainActivity.SEARCH_LIST_REQUEST_CODE;

public class SearchActivity extends AppCompatActivity {

    private EditText fromDate;
    private EditText toDate;
    private EditText captionText;
    private EditText fromTime;
    private EditText toTime;
    private EditText lat;
    private EditText lon;

    private Calendar fromCalendar;
    private Calendar toCalendar;
    private DatePickerDialog.OnDateSetListener fromListener;
    private DatePickerDialog.OnDateSetListener toListener;

    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fromDate = (EditText) findViewById(R.id.search_fromDate);
        toDate   = (EditText) findViewById(R.id.search_toDate);
        captionText = (EditText) findViewById(R.id.search_caption);
        fromTime = (EditText) findViewById(R.id.search_fromTime);
        toTime = (EditText) findViewById(R.id.search_toTime);
        lat = (EditText) findViewById(R.id.search_lat);
        lon = (EditText) findViewById(R.id.search_lon);
    }


    public void cancel(final View v) {
        finish();
    }

    public void captionSearch(final View v) {
        Intent i = new Intent();
        i.putExtra("GET_CAPTION_TEXT", captionText.getText().toString());
        i.putExtra("GET_FROM_DATE", fromDate.getText().toString());
        i.putExtra("GET_TO_DATE", toDate.getText().toString());
        i.putExtra("GET_FROM_TIME", fromTime.getText().toString());
        i.putExtra("GET_TO_TIME", toTime.getText().toString());
        i.putExtra("GET_LAT", lat.getText().toString());
        i.putExtra("GET_LON", lon.getText().toString());
        setResult(RESULT_OK, i);
        finish();
    }

    public void search(final View v) {
        Intent i = new Intent();
        i.putExtra("STARTDATE", fromDate.getText().toString());
        i.putExtra("ENDDATE", toDate.getText().toString());
        setResult(RESULT_OK, i);
        finish();
    }
}

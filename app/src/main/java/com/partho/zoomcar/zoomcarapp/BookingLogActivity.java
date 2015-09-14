package com.partho.zoomcar.zoomcarapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.partho.zoomcar.zoomcarapp.adapters.BookingLogAdapter;
import com.partho.zoomcar.zoomcarapp.models.Booking;
import com.partho.zoomcar.zoomcarapp.models.ZoomcarDatabaseHelper;

import java.util.ArrayList;


public class BookingLogActivity extends AppCompatActivity {

    private RecyclerView bookingLog;
    private LinearLayoutManager bookingLogLayoutManager;
    private BookingLogAdapter bookingLogAdapter;
    private ArrayList<Booking> bookings;
    private ZoomcarDatabaseHelper dbo;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_log);
        dbo=ZoomcarApplication.getInstance().getDbo();
        initWidgets();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Show Bookings");
        bookings=dbo.findAllBookings(dbo.getReadableDatabase());
        bookingLogAdapter=new BookingLogAdapter(this,bookings,dbo);
        bookingLog.setAdapter(bookingLogAdapter);


    }

    private void initWidgets()
    {
        toolbar=(Toolbar)findViewById(R.id.zc_toolbar);
        bookingLog=(RecyclerView)findViewById(R.id.zc_booking_log);
        bookingLogLayoutManager =new LinearLayoutManager(this);
        bookingLogLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bookingLog.setLayoutManager(bookingLogLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_booking_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

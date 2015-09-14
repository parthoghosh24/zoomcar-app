package com.partho.zoomcar.zoomcarapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.partho.zoomcar.zoomcarapp.fragments.SendMessageFragment;
import com.partho.zoomcar.zoomcarapp.models.Booking;
import com.partho.zoomcar.zoomcarapp.models.Car;
import com.partho.zoomcar.zoomcarapp.models.ZoomcarDatabaseHelper;
import com.partho.zoomcar.zoomcarapp.utils.ui.BitmapLruCache;
import com.partho.zoomcar.zoomcarapp.utils.ui.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CarDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Car car;
    private ImageLoader imageLoader;
    private NetworkImageView carImage;
    private TextView  price, seater;
    private RatingBar rating;
    private CustomTextView share,link,message,ac;
    private ScrollView zcScroller;
    private ImageView veil;
    private Toolbar toolbar;
    private static final String TAG="zc_car_detail";
    private ZoomcarDatabaseHelper dbo;
    private Button bookNow, history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        final Long id = getIntent().getLongExtra("id",-1);
        dbo=ZoomcarApplication.getInstance().getDbo();
        car=dbo.get(dbo.getReadableDatabase(), id);
        initWidgets();
        zcScroller.requestDisallowInterceptTouchEvent(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(car.getName());
        getSupportActionBar().setSubtitle(car.getType());
        carImage.setImageUrl(car.getImage(), imageLoader);
        price.setText(car.getHourlyRate() + "/hr");
        seater.setText(car.getSeater() + "");
        rating.setRating(car.getRating().floatValue());
        ac.setText(car.getAc() == 0 ? getResources().getString(R.string.fa_cancel) : getResources().getString(R.string.fa_check));
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, car.getImage());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(car.getImage()));
                startActivity(browserIntent);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendMessageFragment sms = new SendMessageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("link", car.getImage());
                sms.setArguments(bundle);
                sms.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
                sms.show(getSupportFragmentManager().beginTransaction(), "SendSms");

            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.zc_car_map);
        mapFragment.getMapAsync(this);
        veil.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        zcScroller.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        zcScroller.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        zcScroller.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
        bookNow.getBackground().setColorFilter(getResources().getColor(R.color.zc_bg_light_blue), PorterDuff.Mode.MULTIPLY);
        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Booking booking = new Booking();
                booking.setCarId(id);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String now = sdf.format(new Date());
                booking.setBookedOn(now);
                Toast.makeText(getApplicationContext(),"Booking confirmed",Toast.LENGTH_SHORT).show();
                dbo.addBooking(dbo.getWritableDatabase(),booking);

            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(CarDetailActivity.this, BookingLogActivity.class);
                startActivity(intent);
            }
        });


    }

    private void initWidgets()
    {
        toolbar=(Toolbar)findViewById(R.id.zc_toolbar);
        price=(TextView)findViewById(R.id.zc_rate_per_hour);
        rating=(RatingBar)findViewById(R.id.zc_car_rating);
        seater=(TextView)findViewById(R.id.zc_seater);
        ac=(CustomTextView)findViewById(R.id.zc_car_ac);
        imageLoader=new ImageLoader(ZoomcarApplication.getInstance().getAppRequestQueue(),new BitmapLruCache());
        carImage=(NetworkImageView)findViewById(R.id.zc_car_image);
        share=(CustomTextView)findViewById(R.id.zc_car_share);
        link=(CustomTextView)findViewById(R.id.zc_car_link);
        message=(CustomTextView)findViewById(R.id.zc_car_sms);
        zcScroller=(ScrollView)findViewById(R.id.zc_scroller);
        veil=(ImageView)findViewById(R.id.zc_veil_image);
        bookNow=(Button)findViewById(R.id.zc_book_now);
        history=(Button)findViewById(R.id.zc_booking_history);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng =new LatLng(car.getLatitude(), car.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng).title(car.getName()+" @ Rs. "+car.getHourlyRate()+"/hr only"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        //Can add animating logic here for moving marker. Current API is not friendly for this.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_detail, menu);
        MenuItem settings = menu.getItem(0);
        settings.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                finish();
        }


        return super.onOptionsItemSelected(item);
    }
}

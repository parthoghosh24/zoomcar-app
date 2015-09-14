package com.partho.zoomcar.zoomcarapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.partho.zoomcar.zoomcarapp.models.Car;
import com.partho.zoomcar.zoomcarapp.models.JsonToModel;
import com.partho.zoomcar.zoomcarapp.models.ZoomcarDatabaseHelper;
import com.partho.zoomcar.zoomcarapp.utils.ui.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity {

    Animation fadeIn = null;
    CustomTextView car = null;
    private static final String ZC_URL="https://zoomcar.0x10.info/api/zoomcar?type=json&query=list_cars";
    private static final String TAG="zc_splash";
    ZoomcarDatabaseHelper dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbo=ZoomcarApplication.getInstance().getDbo();
        dbo.reset(dbo.getWritableDatabase());
        setContentView(R.layout.activity_splash);
        fadeIn= AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        car =(CustomTextView)findViewById(R.id.porter_app_intro);
        car.startAnimation(fadeIn);
        FetchZCCars request = new FetchZCCars();
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ZoomcarApplication.getInstance().getAppRequestQueue().add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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

    class FetchZCCars extends JsonObjectRequest
    {
        public FetchZCCars()
        {
            super(Method.GET,
                    ZC_URL,
                    new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e(TAG, "IN HERE..............");
                            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                            try {
                                ArrayList<Car> cars = JsonToModel.getCars(response.getJSONArray("cars"));
                                dbo.addAllCars(dbo.getWritableDatabase(),cars);
                                startActivity(intent);
                                finish();
                            }
                            catch (JSONException jse)
                            {
                                Log.e(TAG,jse.getLocalizedMessage());
                                Toast.makeText(getApplicationContext(),"Something went wrong! Please try again",Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG,"here "+error.getLocalizedMessage());
                            if(error.networkResponse!=null)
                            {
                                Log.e(TAG,"from server->"+new String(error.networkResponse.data));
                            }
                            Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}

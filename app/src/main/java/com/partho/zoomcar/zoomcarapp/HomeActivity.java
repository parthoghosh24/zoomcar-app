package com.partho.zoomcar.zoomcarapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.partho.zoomcar.zoomcarapp.adapters.CarListAdapter;
import com.partho.zoomcar.zoomcarapp.adapters.HomeMenuAdapter;
import com.partho.zoomcar.zoomcarapp.models.Car;
import com.partho.zoomcar.zoomcarapp.models.ZoomcarDatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG="zc_home";
    private static final String ZC_API_COUNT="https://zoomcar.0x10.info/api/zoomcar?type=json&query=api_hits";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle menuToggle;
    private RecyclerView carListView;
    private LinearLayoutManager carListLayoutManager;
    private CarListAdapter carListAdapter;
    private TextView carsCount, apiCount;
    private Spinner sortSelector;
    private Toolbar toolbar;
    private HomeMenuAdapter homeMenuAdapter;
    private List<HashMap<String,String>> menus;
    private ArrayList<Car> cars;
    private ZoomcarDatabaseHelper dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initWidget();
        dbo=ZoomcarApplication.getInstance().getDbo();
        cars=dbo.findAllCars(dbo.getReadableDatabase(),null);
        carsCount.setText(cars.size() + "");
        carListAdapter = new CarListAdapter(HomeActivity.this, cars);
        carListView.setAdapter(carListAdapter);
        menuToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };

        mDrawerLayout.setDrawerListener(menuToggle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        menuToggle.syncState();
        String sortChoices[]={"Name","Price","Rating"};
        ArrayAdapter<String> sortSelectorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,sortChoices);
        sortSelectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSelector.setAdapter(sortSelectorAdapter);
        sortSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                switch (pos) {

                    case 0: //Name
                        Collections.sort(cars, new Comparator<Car>() {
                            @Override
                            public int compare(Car car, Car car2) {
                                return car.getName().compareToIgnoreCase(car2.getName());
                            }
                        });
                        carListAdapter = new CarListAdapter(HomeActivity.this, cars);
                        carListAdapter.notifyDataSetChanged();
                        carListView.setAdapter(carListAdapter);

                        break;

                    case 1: //Price
                        Collections.sort(cars, new Comparator<Car>() {
                            @Override
                            public int compare(Car car, Car car2) {
                                return car.getHourlyRate().compareTo(car2.getHourlyRate());
                            }
                        });
                        carListAdapter = new CarListAdapter(HomeActivity.this, cars);
                        carListAdapter.notifyDataSetChanged();
                        carListView.setAdapter(carListAdapter);
                        break;

                    case 2: //Rating
                        Collections.sort(cars, new Comparator<Car>() {
                            @Override
                            public int compare(Car car, Car car2) {
                                return car2.getRating().compareTo(car.getRating());
                            }
                        });
                        carListAdapter = new CarListAdapter(HomeActivity.this, cars);
                        carListAdapter.notifyDataSetChanged();
                        carListView.setAdapter(carListAdapter);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initWidget()
    {
        toolbar=(Toolbar)findViewById(R.id.zc_toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.zc_drawer);
        mDrawerList=(ListView)findViewById(R.id.zc_home_nav_menu);
        carListView =(RecyclerView)findViewById(R.id.zc_cars_list);
        apiCount=(TextView)findViewById(R.id.zc_api_count);
        carsCount =(TextView)findViewById(R.id.zc_car_count);
        carListLayoutManager =new LinearLayoutManager(this);
        carListLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        carListView.setLayoutManager(carListLayoutManager);
        sortSelector=(Spinner)findViewById(R.id.zc_list_sort_selector);
        menus=new ArrayList<>();
        HashMap<String, String> homeMap = new HashMap<>();
        homeMap.put("icon", getResources().getString(R.string.fa_home));
        homeMap.put("title","Home");
        menus.add(homeMap);
        homeMenuAdapter=new HomeMenuAdapter(this,menus);
        mDrawerList.setAdapter(homeMenuAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                mDrawerLayout.closeDrawer(mDrawerList);

            }
        });

        FetchZCAPICount count = new FetchZCAPICount();
        count.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ZoomcarApplication.getInstance().getAppRequestQueue().add(count);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        menuToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        menuToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem settings = menu.getItem(0);
        settings.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                if(mDrawerLayout.isDrawerOpen(mDrawerList))
                {
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
                else
                {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    class FetchZCAPICount extends JsonObjectRequest
    {
        public FetchZCAPICount()
        {
            super(Method.GET,
                    ZC_API_COUNT,
                    new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e(TAG, "IN HERE..............");
                            try {
                                int count = Integer.parseInt(response.getString("api_hits"));
                                apiCount.setText(count+"");
                            }
                            catch (JSONException jse)
                            {
                                Log.e(TAG,jse.getLocalizedMessage());
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

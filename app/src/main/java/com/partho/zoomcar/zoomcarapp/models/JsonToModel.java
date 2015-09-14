package com.partho.zoomcar.zoomcarapp.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by partho on 13/9/15.
 */
public class JsonToModel {
    private static final String TAG="json_to_model";

    /**
     * Parsing JSON Array and converting into car list
     *
     * @param list
     * @return
     */
    public static ArrayList<Car> getCars(JSONArray list)
    {
        ArrayList<Car> cars=new ArrayList<>();
        try {

            for(int index=0; index<list.length();++index)
            {
                JSONObject object=list.getJSONObject(index);
                Car car=getCar(object);
                cars.add(car);

            }
            return cars;

        }
        catch (JSONException jse)
        {
            jse.printStackTrace();
            Log.e(TAG, jse.getLocalizedMessage());
            return null;
        }
        catch (Exception e)
        {

            e.printStackTrace();
            Log.e(TAG, e.getLocalizedMessage());
            return null;
        }

    }

    /**
     * Parsing JSON Object and converting into car
     *
     * @param object
     * @return
     */
    public static Car getCar(JSONObject object)
    {
        Car car=null;
        try {
            car=new Car();
            car.setName(object.getString("name"));
            car.setImage(object.getString("image"));
            car.setType(object.getString("type"));
            car.setHourlyRate(Double.parseDouble(object.getString("hourly_rate")));
            car.setRating(Double.parseDouble(object.getString("rating")));
            car.setSeater(Integer.parseInt(object.getString("seater")));
            car.setAc(Integer.parseInt(object.getString("ac")));
            JSONObject location = object.getJSONObject("location");
            car.setLatitude(Double.parseDouble(location.getString("latitude")));
            car.setLongitude(Double.parseDouble(location .getString("longitude")));
            return car;


        }
        catch (JSONException jse)
        {
            jse.printStackTrace();
            Log.e(TAG, jse.getLocalizedMessage());

            return car;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e(TAG, e.getLocalizedMessage());
            return car;
        }

    }
}

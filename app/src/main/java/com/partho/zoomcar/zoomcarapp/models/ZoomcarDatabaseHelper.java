package com.partho.zoomcar.zoomcarapp.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by partho on 13/9/15.
 */
public class ZoomcarDatabaseHelper extends SQLiteOpenHelper implements ZoomcarDatabaseInterface {

    private static final String TAG= "zoomcar_db";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "zoomcar";

    //TABLE CAR
    private static final String TABLE_CAR= "cars";
//    private static final String CAR_ID = "id";//Integer (PK)
    private static final String CAR_NAME= "name"; //String
    private static final String CAR_IMAGE= "image"; //String
    private static final String CAR_TYPE= "type";//String
    private static final String CAR_HOURLY_RATE= "hourly_rate";//Double
    private static final String CAR_RATING= "rating";//Double
    private static final String CAR_SEATER= "seater";//Integer
    private static final String CAR_AC= "ac";//Boolean
    private static final String CAR_LAT= "latitude";//Double
    private static final String CAR_LNG= "longitude";//Double

    //TABLE BOOKING
    private static final String TABLE_BOOKING= "bookings";
//    private static final String BOOKING_ID = "id";//Integer (PK)
    private static final String BOOKING_CAR_ID= "car_id";//Integer (Foreign key to car)
    private static final String BOOKING_ON= "booked_on";//Datetime

    private static final String CREATE_TABLE_CAR="CREATE VIRTUAL TABLE "+TABLE_CAR+" USING FTS3("
                                                 +CAR_NAME+" TEXT, "
                                                 +CAR_IMAGE+" TEXT, "
                                                 +CAR_TYPE+" TEXT, "
                                                 +CAR_HOURLY_RATE+" REAL, "
                                                 +CAR_RATING+" REAL, "
                                                 +CAR_SEATER+" INTEGER, "
                                                 +CAR_AC+" INTEGER DEFAULT 0, "
                                                 +CAR_LAT+" REAL, "
                                                 +CAR_LNG+" REAL);";

    private static final String CREATE_TABLE_BOOKING="CREATE TABLE "+TABLE_BOOKING+"("
                                                     +BOOKING_CAR_ID+" INTEGER, "
                                                     +BOOKING_ON+" TEXT, "
                                                     +"FOREIGN KEY("+BOOKING_CAR_ID+") REFERENCES "+TABLE_CAR+"(docid));";


    public ZoomcarDatabaseHelper(Context context)
    {
            super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CAR);
        db.execSQL(CREATE_TABLE_BOOKING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CAR);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BOOKING);
        onCreate(db);
    }

    @Override
    public Car get(SQLiteDatabase db,Long id) {
        Log.e(TAG,"ID IS "+id);
        Car car =new Car();
        Cursor result = db.rawQuery("select docid as _id, name, image, type, hourly_rate, rating, seater, ac, latitude, longitude from "+TABLE_CAR+" where docid="+id,null);
        result.moveToFirst();
        car.setId(result.getLong(0));
        car.setName(result.getString(1));
        car.setImage(result.getString(2));
        car.setType(result.getString(3));
        car.setHourlyRate(result.getDouble(4));
        car.setRating(result.getDouble(5));
        car.setSeater(result.getInt(6));
        car.setAc(result.getInt(7));
        car.setLatitude(result.getDouble(8));
        car.setLongitude(result.getDouble(9));
        result.close();
        db.close();
        return car;
    }

    @Override
    public ArrayList<Car> findAllCars(SQLiteDatabase db, String pattern) {
        ArrayList<Car> cars = new ArrayList<>();
        Cursor result;
        if(TextUtils.isEmpty(pattern))
        {
            result=db.rawQuery("select docid as _id, name, image, type, hourly_rate, rating, seater, ac, latitude, longitude from "+TABLE_CAR+";",null);
        }
        else
        {
            result=db.rawQuery("select docid as _id, name, image, type, hourly_rate, rating, seater, ac, latitude, longitude from "+TABLE_CAR+" where "+TABLE_CAR+" match '"+pattern+"';",null);
        }
        if(result.moveToFirst())
        {
            do {
//                Log.e(TAG,"result is "+result.getInt(result.getColumnIndex("docid")));
                Log.e(TAG, "result is "+result.getLong(0));
                Car car = new Car();
                car.setId(result.getLong(0));
                car.setName(result.getString(1));
                car.setImage(result.getString(2));
                car.setType(result.getString(3));
                car.setHourlyRate(result.getDouble(4));
                car.setRating(result.getDouble(5));
                car.setSeater(result.getInt(6));
                car.setAc(result.getInt(7));
                car.setLatitude(result.getDouble(8));
                car.setLongitude(result.getDouble(9));
                cars.add(car);
            }
            while (result.moveToNext());
        }
        if(!result.isClosed())
        {

           result.close();
        }
        db.close();
        return cars;
    }



    @Override
    public ArrayList<Booking> findAllBookings(SQLiteDatabase db) {
        ArrayList<Booking> bookings = new ArrayList<>();
        Cursor result;
        result=db.rawQuery("select * from "+TABLE_BOOKING+";",null);
        if(result.moveToFirst())
        {
            Log.e(TAG,"booking is "+result.getLong(0));
            do {
                Booking booking = new Booking();
                booking.setCarId(result.getLong(0));
                booking.setBookedOn(result.getString(1));
                bookings.add(booking);
            }
            while (result.moveToNext());
        }
        if(!result.isClosed())
        {

            result.close();
        }
        db.close();
        return bookings;
    }

    @Override
    public void addCar(SQLiteDatabase db, Car car) {

        ContentValues carValues = new ContentValues();
        carValues.put(CAR_NAME, car.getName());
        carValues.put(CAR_IMAGE, car.getImage());
        carValues.put(CAR_TYPE, car.getType());
        carValues.put(CAR_HOURLY_RATE, car.getHourlyRate());
        carValues.put(CAR_RATING, car.getRating());
        carValues.put(CAR_SEATER, car.getSeater());
        carValues.put(CAR_AC, car.getAc());
        carValues.put(CAR_LAT, car.getLatitude());
        carValues.put(CAR_LNG, car.getLongitude());

        db.insert(TABLE_CAR, null, carValues);
    }

    @Override
    public void addBooking(SQLiteDatabase db, Booking booking) {

        ContentValues bookingValues = new ContentValues();
        bookingValues.put(BOOKING_CAR_ID, booking.getCarId());
        bookingValues.put(BOOKING_ON, booking.getBookedOn());
        db.insert(TABLE_BOOKING, null, bookingValues);

    }

    @Override
    public void addAllCars(SQLiteDatabase db, ArrayList<Car> cars) {

        for(Car car: cars)
        {
            addCar(db,car);
        }

    }

    @Override
    public void reset(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CAR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);

        onCreate(db);
    }
}


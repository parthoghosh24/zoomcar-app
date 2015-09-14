package com.partho.zoomcar.zoomcarapp.models;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by partho on 13/9/15.
 */
public interface ZoomcarDatabaseInterface {
    public Car get(SQLiteDatabase db, Long id); //find car by id
    public ArrayList<Car> findAllCars(SQLiteDatabase db,String pattern);//find all cars by pattern or all
    public void reset(SQLiteDatabase db);//reset database
    public ArrayList<Booking> findAllBookings(SQLiteDatabase db); //find all bookings
    public void addCar(SQLiteDatabase db,Car car); //add car to database
    public void addAllCars(SQLiteDatabase db,ArrayList<Car> cars); //add car to database
    public void addBooking(SQLiteDatabase db,Booking booking); //add booking to database

}

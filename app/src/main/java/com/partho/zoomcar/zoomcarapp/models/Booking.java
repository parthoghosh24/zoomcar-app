package com.partho.zoomcar.zoomcarapp.models;

/**
 * Created by partho on 13/9/15.
 */
public class Booking {
    private Long id;
    private Long carId;
    private String bookedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(String bookedOn) {
        this.bookedOn = bookedOn;
    }
}

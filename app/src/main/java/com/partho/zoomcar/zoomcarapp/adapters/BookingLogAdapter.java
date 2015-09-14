package com.partho.zoomcar.zoomcarapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partho.zoomcar.zoomcarapp.CarDetailActivity;
import com.partho.zoomcar.zoomcarapp.R;
import com.partho.zoomcar.zoomcarapp.ZoomcarApplication;
import com.partho.zoomcar.zoomcarapp.models.Booking;
import com.partho.zoomcar.zoomcarapp.models.Car;
import com.partho.zoomcar.zoomcarapp.models.ZoomcarDatabaseHelper;
import com.partho.zoomcar.zoomcarapp.utils.ui.BitmapLruCache;

import java.util.List;

/**
 * Created by partho on 13/9/15.
 */
public class BookingLogAdapter extends RecyclerView.Adapter<BookingLogAdapter.BookingLogItemViewHolder>{

    private Activity mContext;
    private List<Booking> bookings;
    private ImageLoader imageLoader;
    private ZoomcarDatabaseHelper dbo;
    private static final String TAG="zc_bl_adptr";

    public BookingLogAdapter(Activity mContext, List<Booking> bookings, ZoomcarDatabaseHelper dbo)
    {
        this.mContext=mContext;
        this.bookings = bookings;
        this.dbo=dbo;
        imageLoader=new ImageLoader(ZoomcarApplication.getInstance().getAppRequestQueue(),new BitmapLruCache());
    }

    @Override
    public BookingLogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_log_item,parent,false);
        return new BookingLogItemViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final BookingLogItemViewHolder holder, final int position) {
        Log.e(TAG,"Booking car id is "+bookings.get(0).getCarId());
        Car car =dbo.get(dbo.getReadableDatabase(),bookings.get(position).getCarId());
        Log.e(TAG,"Car is "+car);
        holder.carImage.setImageUrl(car.getImage(), imageLoader);
        holder.carName.setText(car.getName());
        holder.bookedOn.setText("Booked on "+bookings.get(position).getBookedOn());
        Log.e(TAG, "ID IS----> " + bookings.get(position).getId());
        holder.view.setTag(bookings.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class BookingLogItemViewHolder extends RecyclerView.ViewHolder
    {
        public NetworkImageView carImage;
        public TextView carName, bookedOn;
        public View view;

        public BookingLogItemViewHolder(View itemView)
        {
            super(itemView);
            carImage =(NetworkImageView)itemView.findViewById(R.id.zc_book_log_item_image);
            carName =(TextView)itemView.findViewById(R.id.zc_book_log_item_title);
            bookedOn =(TextView)itemView.findViewById(R.id.zc_book_log_item_time);
            view=itemView;
        }

    }
}

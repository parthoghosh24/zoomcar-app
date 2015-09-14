package com.partho.zoomcar.zoomcarapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partho.zoomcar.zoomcarapp.CarDetailActivity;
import com.partho.zoomcar.zoomcarapp.R;
import com.partho.zoomcar.zoomcarapp.ZoomcarApplication;
import com.partho.zoomcar.zoomcarapp.models.Car;
import com.partho.zoomcar.zoomcarapp.utils.ui.BitmapLruCache;

import java.util.List;

/**
 * Created by partho on 13/9/15.
 */
public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarListItemViewHolder>{

    private Activity mContext;
    private List<Car> cars;
    private ImageLoader imageLoader;
    private static final String TAG="zc_car_adptr";

    public CarListAdapter(Activity mContext, List<Car> cars)
    {
        this.mContext=mContext;
        this.cars = cars;
        imageLoader=new ImageLoader(ZoomcarApplication.getInstance().getAppRequestQueue(),new BitmapLruCache());
    }

    @Override
    public CarListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_list_item,parent,false);
        return new CarListItemViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final CarListItemViewHolder holder, final int position) {
        holder.carImage.setImageUrl(cars.get(position).getImage(), imageLoader);
        holder.rating.setRating(cars.get(position).getRating().floatValue());
        holder.carName.setText(cars.get(position).getName());
        holder.carPrice.setText(cars.get(position).getHourlyRate() + "/hr");
        Log.e(TAG,"ID IS----> " +cars.get(position).getId());
        holder.view.setTag(cars.get(position).getId());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CarDetailActivity.class);
                intent.putExtra("id", (Long)holder.view.getTag());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public static class CarListItemViewHolder extends RecyclerView.ViewHolder
    {
        public NetworkImageView carImage;
        public RatingBar rating;
        public TextView carName, carPrice, carLocation;
        public View view;

        public CarListItemViewHolder(View itemView)
        {
            super(itemView);
            rating=(RatingBar)itemView.findViewById(R.id.zc_car_list_item_rating);
            carImage =(NetworkImageView)itemView.findViewById(R.id.zc_car_list_item_image);
            carName =(TextView)itemView.findViewById(R.id.zc_car_list_item_title);
            carPrice =(TextView)itemView.findViewById(R.id.zc_car_list_item_price);
            view=itemView;
        }

    }
}

package com.ttu.appathon.request.Food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ttu.appathon.request.R;

import java.util.List;

/**
 * Created by Vinay on 29-10-2015.
 */
public class RestaurantDetailsJsonParserAdapter extends ArrayAdapter<RestaurantDetailsJsonParser> {

    private Context ctx;
    private List<RestaurantDetailsJsonParser> restaurnt_list;

    public RestaurantDetailsJsonParserAdapter(Context ctx, List<RestaurantDetailsJsonParser> restaurnt_list) {
        super(ctx, R.layout.restaurant_list, restaurnt_list);
        this.restaurnt_list = restaurnt_list;
        this.ctx = ctx;
    }


    @Override
    public int getCount() {
        return restaurnt_list.size();
    }


    @Override
    public RestaurantDetailsJsonParser getItem(int position) {

        return restaurnt_list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.restaurant_list, parent, false);
        }

        RestaurantDetailsJsonParser Location = restaurnt_list.get(position);
        TextView location_name = (TextView) convertView.findViewById(R.id.place_name);
        TextView location_addr = (TextView) convertView.findViewById(R.id.place_addr);
        TextView location_phone = (TextView) convertView.findViewById(R.id.place_type);
        TextView location_rating=(TextView) convertView.findViewById(R.id.rating);

        //ImageView location_icon= (ImageView)convertView.findViewById(R.id.place_icon);
       //location_icon.setImageBitmap(Location.getIcon_Bitmap());
        location_name.setText(Location.getName());
        //location_icon.setImageBitmap(Location.getMainIcon());
        location_addr.setText(Location.getAddress());
        location_phone.setText(Location.getPhoneNumber());
        location_rating.setText(Double.toString(Location.getRating()));
        //location_icon.setImageBitmap(Location.getMainIcon());

        return convertView;
    }
    public void setPlacesList(List<RestaurantDetailsJsonParser> restaurnt_list) {
        this.restaurnt_list = restaurnt_list;
    }



}

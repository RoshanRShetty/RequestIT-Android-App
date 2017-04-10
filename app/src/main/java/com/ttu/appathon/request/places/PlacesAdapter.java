package com.ttu.appathon.request.places;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttu.appathon.request.R;

import java.net.URL;
import java.util.List;

/**
 * Created by Manohar on 27-Oct-15.
 */
public class PlacesAdapter extends ArrayAdapter<Place> {

private Context ctx;
private List<Place> places_list;
         ImageView location_icon;

public PlacesAdapter(Context ctx, List<Place> places_list) {
        super(ctx, R.layout.attraction_list_item, places_list);
        this.places_list = places_list;
        this.ctx = ctx;
        }


@Override
public int getCount() {
        return places_list.size();
        }


@Override
public Place getItem(int position) {

        return places_list.get(position);
        }


@Override
public long getItemId(int position) {
        return position;
        }

        private static class ViewHolder {
                ImageView imageView;
                String imageurl;
                Bitmap bitmap;
        }


@Override
public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        Place Location = places_list.get(position);

        if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.attraction_list_item, parent, false);
                viewHolder.imageView = (ImageView)convertView.findViewById(R.id.place_icon);
                convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder)convertView.getTag();
        TextView location_name = (TextView) convertView.findViewById(R.id.place_name);
        TextView location_addr = (TextView) convertView.findViewById(R.id.place_addr);
        TextView location_type = (TextView) convertView.findViewById(R.id.place_type);
        TextView location_rating=(TextView) convertView.findViewById(R.id.rating);

        location_icon= (ImageView) convertView.findViewById(R.id.place_icon);
        location_icon.setImageBitmap(Location.getMainIcon());
        location_name.setText(Location.getName());
        location_type.setText(Location.getTypes().get(0));
        location_rating.setText(Double.toString(Location.getRating()));
        location_addr.setText(Location.getVicinity());;
        viewHolder.imageurl=Location.getMainIcon_url();
       // new DownloadImage().execute(viewHolder);
        return convertView;
        }
    public void setPlacesList(List<Place> places_list) {
        this.places_list = places_list;
    }
private class DownloadImage extends AsyncTask<ViewHolder,Void,ViewHolder>{
        private Bitmap bitmap;
        @Override
        protected ViewHolder doInBackground(ViewHolder... params) {
                ViewHolder viewHolder = params[0];
                try {
                        URL imageURL = new URL(viewHolder.imageurl);
                        viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());
                }catch (Exception E){

                }

                return viewHolder;
        }

        @Override
        protected void onPostExecute(ViewHolder viewHolder) {
                if (viewHolder.bitmap == null) {
                } else {
                        viewHolder.imageView.setImageBitmap(viewHolder.bitmap);
                                }

        }
}

        }

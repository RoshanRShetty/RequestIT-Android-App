package com.ttu.appathon.request.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.ttu.appathon.request.R;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final String[] MENU_ITEMS;

	public ImageAdapter(Context context, String[] MENU_ITEMS) {
		this.context = context;
		this.MENU_ITEMS = MENU_ITEMS;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		//View menu_item;

		if (convertView == null) {
		//	menu_item = new View(context);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// get layout reference from menu_item
			convertView = inflater.inflate(R.layout.menu_item, parent,false);
			// Get Textview from menu_item.xml and set the title to it

        } else {
			}
		String title;
		String color="#000000";
		TextView menu_item_textview = (TextView) convertView
				.findViewById(R.id.grid_item_text);
		switch (position){
			case 2: title= "Need a Room?";
				break;
			case 0: title= "Are you Hungry?";
				break;
			case 3: title= "Need Directions?";
				color="#FFFFFF";
				break;

			case 1: title= "Want to Explore?";
				color="#FFFFFF";
				break;

			case 4: title= "Emergency Help!";
				break;
				 default:
					 title="Other Requests?";

		}
		menu_item_textview.setText(title);
		menu_item_textview.setTextColor(Color.parseColor(color));
		// set image based on selected text
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.grid_item_image);
		String menutype = MENU_ITEMS[position];
		int drawableResource=getDrawableResourceId(menutype);
		imageView.setImageResource(drawableResource);
	//	menu_item.setPressed(true);
		return convertView;
	}

	@Override
	public int getCount() {
		return MENU_ITEMS.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
private int getDrawableResourceId(String menu_Type) {
    Resources res = this.context.getResources();
    int id = res.getIdentifier("food_menu", "drawable", this.context.getPackageName());
    //
    if (menu_Type.equals("FOOD")) {
        id = res.getIdentifier("restaurant", "drawable", this.context.getPackageName());
    } else if (menu_Type.equals("ATTRACTIONS")) {
        id = res.getIdentifier("places", "drawable", this.context.getPackageName());
    } else if (menu_Type.equals("TRANSPORT")) {
        id = res.getIdentifier("directions", "drawable", this.context.getPackageName());
    }
    else if (menu_Type.equals("ACCOMMODATION")) {
        id = res.getIdentifier("accommodation", "drawable", this.context.getPackageName());
    }
    else if(menu_Type.equals("ENTERTAINMENT")){
        id = res.getIdentifier("places", "drawable", this.context.getPackageName());
    }
	else if(menu_Type.equals("EMERGENCY")){
		id = res.getIdentifier("emergency", "drawable", this.context.getPackageName());
	}

    //
    return id;
}
}

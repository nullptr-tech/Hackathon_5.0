package com.meeting.meetingapp.Model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meeting.meetingapp.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    private Context mContext;
    private List<Item> itemList = new ArrayList<>();

    public ItemAdapter(@NonNull Context context, @LayoutRes ArrayList<Item> list) {
        super(context, 0 , list);
        mContext = context;
        itemList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item,parent,false);

        Item currentItem = itemList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.itemImage);
        image.setImageResource(currentItem.getImgDrawable());

        TextView name = (TextView) listItem.findViewById(R.id.itemName);
        name.setText(currentItem.getName());

        TextView description = (TextView) listItem.findViewById(R.id.itemDesc);
        description.setText(currentItem.getDesc());

        TextView itemPrice = (TextView) listItem.findViewById(R.id.itemPrice);
        itemPrice.setText("£" + Integer.toString(currentItem.getPrice()));


        return listItem;
    }
}
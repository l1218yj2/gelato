
package com.gelato.gelato;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gelato.gelato.models.Mission;

import java.util.ArrayList;


public class MissionSpinnerAdapter extends ArrayAdapter<Mission> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<Mission> values;

    public MissionSpinnerAdapter(Context context, int textViewResourceId,
                                 ArrayList<Mission> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount() {
        return values.size();
    }

    public Mission getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        final TextView v =
                (TextView) ((LayoutInflater)getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.item_missions,parent,false);
        v.setText(values.get(position).getMissionName());

        // And finally return your dynamic (or custom) view for each spinner item
        return v;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        final TextView v =
                (TextView) ((LayoutInflater)getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.item_missions,parent,false);
        v.setText(values.get(position).getMissionName());
        return v;
    }
}
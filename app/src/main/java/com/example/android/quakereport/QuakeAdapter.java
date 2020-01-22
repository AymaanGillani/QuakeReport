package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuakeAdapter extends ArrayAdapter<Quake> {
    public QuakeAdapter(Context context, ArrayList<Quake> array){
        super(context,0,array);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView=convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Quake q=getItem(position);

        DecimalFormat formatter = new DecimalFormat("0.0");
        Date dateObject = new Date(q.getTimems());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String timetoDisplay = timeFormat.format(dateObject);
        String magn = formatter.format(q.getMagnitude());

        TextView magtv=(TextView)listItemView.findViewById(R.id.magTV);
        magtv.setText(""+magn);

        GradientDrawable magnitudeCircle = (GradientDrawable) magtv.getBackground();
        int magnitudeColor = getMagnitudeColor(q.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        TextView distTV=(TextView)listItemView.findViewById(R.id.distTV);
        distTV.setText(""+split(1,q.getLocation()));

        TextView refTV=(TextView)listItemView.findViewById(R.id.refTV);
        refTV.setText(""+split(2,q.getLocation()));

        TextView dtTV=(TextView)listItemView.findViewById(R.id.dtTV);
        dtTV.setText(""+dateToDisplay);

        TextView timeTV=(TextView)listItemView.findViewById(R.id.timeTV);
        timeTV.setText(""+timetoDisplay);

        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    private static String split(int no,String loc){
        String dist,ref;
        boolean ofthere=loc.contains("of");
        if (ofthere){
            dist=loc.substring(0,loc.indexOf("of")+3);
            ref=loc.substring(loc.indexOf("of")+3);
        }
        else {
            dist="Near the ";
            ref=loc;
        }
        if (no==1)return dist;
        else return ref;
    }
}

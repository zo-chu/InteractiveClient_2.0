package com.kitanasoftware.interactiveclient.information;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitanasoftware.interactiveclient.R;

import java.util.ArrayList;


/**
 * Created by dasha on 27/01/16.
 */
public class InformationAdapter extends ArrayAdapter<Information> {
    private boolean isEdit;

    public void setList(ArrayList list) {
        this.list = list;
    }

    private ArrayList list;
    int type;

    public InformationAdapter(Context context, ArrayList<Information> informations) {
        super(context, 0, informations);
        list=informations;
    }
    public void startEdit(){
        isEdit=true;
        this.notifyDataSetChanged();
    }
    public void stoptEdit(){
        isEdit=false;
    }


// Return an integer representing the type by fetching the enum type ordinal
        @Override
        public int getItemViewType(int position) {
                return getItem(position).getType().ordinal();
        }

// Total number of types is the number of enum values
        @Override
        public int getViewTypeCount() {
            return Information.InformType.values().length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Information information = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                // Get the data item type for this position
                type = getItemViewType(position);
                // Inflate XML layout based on the type
                convertView = getInflatedLayoutForType(type);
            }
            if(position<3){
                if (type == Information.InformType.GUIDE.ordinal()) {
                    return getGuideView(convertView, position);
                } else if (type == Information.InformType.TOUR.ordinal()) {
                     return getTourView(convertView,position);
                } else if (type == Information.InformType.ADD.ordinal()) {
                    return getAdditionalView(convertView,position);
                } else {
                    return null;
                }
            }else {
                return null;
            }
        }

// Given the item type, responsible for returning the correct inflated XML layout file
        private View getInflatedLayoutForType(int type) {
                if (type == Information.InformType.GUIDE.ordinal()) {
                    return LayoutInflater.from(getContext()).inflate(R.layout.guide_inform, null);
                } else if (type == Information.InformType.TOUR.ordinal()) {
                    return LayoutInflater.from(getContext()).inflate(R.layout.tour_inform, null);
                } else if (type == Information.InformType.ADD.ordinal()) {
                    return LayoutInflater.from(getContext()).inflate(R.layout.additional_inform, null);
                } else {
                    return  null;
                }
            }

            public View getGuideView(View gideView, int position){
                if(position==0) {
                    GuideInform information = (GuideInform) list.get(position);
                    System.out.println(information.getFull_name());
                    TextView tvFullName = (TextView) gideView.findViewById(R.id.tvFullName);
                    TextView tvPhone = (TextView) gideView.findViewById(R.id.tvPhone);

//                    EditText etFullName = (EditText) gideView.findViewById(R.id.etFullName);
//                    EditText etPhone = (EditText) gideView.findViewById(R.id.etPhone);

                    ImageView im = (ImageView) gideView.findViewById(R.id.ivPhoto);


                    if (!isEdit) {
                        tvFullName.setText(information.getFull_name());
                        tvFullName.setTextColor(Color.BLACK);
                        im.setImageBitmap(information.getPhoto());
                        tvPhone.setText(information.getPhone());
                        tvPhone.setTextColor(Color.BLACK);
                    } else {
                        tvFullName.setVisibility(View.GONE);
//                        etFullName.setVisibility(View.VISIBLE);
//                        etFullName.setText(information.getFull_name());
//                        etFullName.setTextColor(Color.BLACK);
                        im.setImageBitmap(information.getPhoto());

                        tvPhone.setVisibility(View.GONE);
//                        etPhone.setVisibility(View.VISIBLE);
//                        etPhone.setText(information.getPhone());
//                        etPhone.setTextColor(Color.BLACK);
                    }
                }
                return gideView;
            }

    public View getTourView(View gideView,int position){
        if(position==1) {
            TourInform information = (TourInform) getItem(position);

            TextView tvTour = (TextView) gideView.findViewById(R.id.tvTour);
            TextView tvTourGoal = (TextView) gideView.findViewById(R.id.tvTourGoal);

            EditText etTour = (EditText) gideView.findViewById(R.id.etTour);
            EditText etTourGoal = (EditText) gideView.findViewById(R.id.etTourGoal);

            if (!isEdit) {
                tvTour.setVisibility(View.VISIBLE);
                etTour.setVisibility(View.GONE);
                tvTour.setText(information.getName());
                tvTour.setTextColor(Color.BLACK);


                tvTourGoal.setVisibility(View.VISIBLE);
                etTourGoal.setVisibility(View.GONE);
                tvTourGoal.setText(information.getGoal());
                tvTourGoal.setTextColor(Color.BLACK);

            } else {
                tvTour.setVisibility(View.GONE);
                etTour.setVisibility(View.VISIBLE);
                etTour.setText(information.getName());
                etTour.setTextColor(Color.BLACK);

                tvTourGoal.setVisibility(View.GONE);
                etTourGoal.setVisibility(View.VISIBLE);
                etTourGoal.setText(information.getGoal());
                etTourGoal.setTextColor(Color.BLACK);
            }
        }
        return gideView;
    }
    public View getAdditionalView(View gideView,int position){

        if(position==2){
            AdditionalInform information = (AdditionalInform) getItem(position);
            gideView= LayoutInflater.from(getContext()).inflate(R.layout.additional_inform, null);
            TextView tvCompanyName = (TextView) gideView.findViewById(R.id.tvCompanyName1);
            tvCompanyName.setText(information.getCompanyName());
            tvCompanyName.setTextColor(Color.BLACK);
        }

        return gideView;
    }



}

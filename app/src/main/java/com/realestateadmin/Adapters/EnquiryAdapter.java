package com.realestateadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.realestateadmin.Models.EnquiryModel;
import com.realestateadmin.PropertyMoreDetails_Activity;
import com.realestateadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EnquiryAdapter extends RecyclerView.Adapter<EnquiryAdapter.EnquiryViewHolder>        {
    private List<EnquiryModel> enquiryList;
    private Context context;

    public EnquiryAdapter(Context context, List<EnquiryModel> enquiryList) {
        this.context = context;
        this.enquiryList = enquiryList;
    }

    @NonNull
    @Override
    public EnquiryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enquiry, parent, false);
        return new EnquiryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnquiryViewHolder holder, int position) {
        EnquiryModel enquiry = enquiryList.get(position);
        Picasso.get().load(enquiry.getImageurl()).into(holder.image);

        holder.propertyname.setText(enquiry.getPropertyName());

        String[] timestampParts = enquiry.getTimestamp().split(" ");
        String enquiredDate = timestampParts[0];
        String enquiredTime = timestampParts[1];

        holder.timestampDateTextView.setText("Enquired Date: " + enquiredDate);
        holder.timestampTimeTextView.setText("Enquired Time: " + enquiredTime);

        holder.usernameTextView.setText("Enquired By: " + enquiry.getUsername());

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PropertyMoreDetails_Activity.class);
                intent.putExtra("propertyId", enquiry.getPropertyId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return enquiryList.size();
    }

    public static class EnquiryViewHolder extends RecyclerView.ViewHolder {

        TextView timestampDateTextView;
        TextView timestampTimeTextView;
        TextView usernameTextView;
        TextView propertyname;
        ImageView image;

        LinearLayout linear;

        public EnquiryViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.leftIcon);
            propertyname = itemView.findViewById(R.id.dishname);
            timestampDateTextView = itemView.findViewById(R.id.cooktime);
            timestampTimeTextView = itemView.findViewById(R.id.price);
            usernameTextView = itemView.findViewById(R.id.name);
            linear = itemView.findViewById(R.id.linear);
        }
    }
}

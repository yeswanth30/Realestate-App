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

import com.realestateadmin.Models.PaymentModel;
import com.realestateadmin.PropertyMoreDetails_Activity;
import com.realestateadmin.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private List<PaymentModel> paymentList;
    private Context context;

    public PaymentAdapter(List<PaymentModel> paymentList, Context context) {
        this.paymentList = paymentList;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        PaymentModel payment = paymentList.get(position);
        holder.bind(payment);

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PropertyMoreDetails_Activity.class);
                intent.putExtra("propertyId", payment.getPropertyId());
                context.startActivity(intent);            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textViewBrokerName;
        private TextView textViewPropertyName;
        private TextView textViewPurchasedBy;
        private TextView textViewTotalAmount;

        LinearLayout linear;

        private TextView textViewTwoPercentAmount; // New TextView for 2% amount



        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.leftIcon);
            textViewBrokerName = itemView.findViewById(R.id.name);
            textViewPropertyName = itemView.findViewById(R.id.dishname);
            textViewPurchasedBy = itemView.findViewById(R.id.cooktime);
            textViewTotalAmount = itemView.findViewById(R.id.price);
            linear = itemView.findViewById(R.id.linear);
            textViewTwoPercentAmount = itemView.findViewById(R.id.two_percent_amount); // Initialize the new TextView


        }

        public void bind(PaymentModel payment) {
            Picasso.get().load(payment.getImageurl()).into(imageView);
            textViewBrokerName.setText("Broker: " + payment.getBroker_name());
            textViewPropertyName.setText("Property: " + payment.getPropertyName());
             textViewPurchasedBy.setText("Purchased by: " + payment.getUsername());
            textViewTotalAmount.setText("Total Amount: Rs." + payment.getTotal_amount());
            double totalAmount = Double.parseDouble(payment.getTotal_amount());
            double twoPercentAmount = totalAmount * 0.02;
            textViewTwoPercentAmount.setText("Revenue : Rs." + twoPercentAmount);
        }
    }
}

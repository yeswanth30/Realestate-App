package com.realestateadmin.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.realestateadmin.Models.SaleType;
import com.realestateadmin.R;

import java.util.List;

public class SaleTypeAdapter extends RecyclerView.Adapter<SaleTypeAdapter.ViewHolder> {

    protected List<SaleType> saleTypeList;
    private DatabaseReference databaseReference;


    public SaleTypeAdapter(List<SaleType> saleTypeList) {
        this.saleTypeList = saleTypeList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("sale_types");
    }

    public void updateSaleTypes(List<SaleType> saleTypes) {
        this.saleTypeList.clear();
        this.saleTypeList.addAll(saleTypes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_type_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        SaleType saleType = saleTypeList.get(position);
        holder.textViewSaleType.setText(saleType.getType());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndDeleteSaleType(context, saleType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return saleTypeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSaleType;
        ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSaleType = itemView.findViewById(R.id.textViewRoomType);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private void checkAndDeleteSaleType(Context context, SaleType saleType) {
        saleTypeIsAssociated(saleType.getType(), new SaleTypeCheckCallback() {
            @Override
            public void onResult(boolean associated) {
                if (associated) {
                    Toast.makeText(context, "This sale type is associated with properties and cannot be deleted", Toast.LENGTH_SHORT).show();
                } else {
                    showDeleteDialog(context, saleType);
                }
            }
        });
    }

    private void saleTypeIsAssociated(String saleType, SaleTypeCheckCallback callback) {
        DatabaseReference propertyReference = FirebaseDatabase.getInstance().getReference().child("Property");
        Query query = propertyReference.orderByChild("saleType").equalTo(saleType);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onResult(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onResult(false);
            }
        });
    }

    private void showDeleteDialog(Context context, SaleType saleType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this sale type?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteSaleType(saleType);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteSaleType(SaleType saleType) {
        saleTypeList.remove(saleType);
        notifyDataSetChanged();
        databaseReference.child(saleType.getId()).removeValue();
    }

    interface SaleTypeCheckCallback {
        void onResult(boolean associated);
    }
}

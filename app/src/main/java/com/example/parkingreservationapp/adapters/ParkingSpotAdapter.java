package com.example.parkingreservationapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingreservationapp.R;
import com.example.parkingreservationapp.models.ParkingSpot;

import java.util.List;

public class ParkingSpotAdapter extends RecyclerView.Adapter<ParkingSpotAdapter.ViewHolder> {
    private List<ParkingSpot> parkingSpots;
    private Context context;
    private OnSpotClickListener listener;

    public interface OnSpotClickListener {
        void onSpotClick(ParkingSpot spot);
    }

    public ParkingSpotAdapter(List<ParkingSpot> parkingSpots, Context context, OnSpotClickListener listener) {
        this.parkingSpots = parkingSpots;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking_spot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkingSpot spot = parkingSpots.get(position);
        holder.spotId.setText(spot.getId());
        holder.spotLocation.setText(spot.getLocation());

        // Set different appearance for available vs occupied spots
        if (spot.isAvailable()) {
            holder.itemView.setAlpha(1f);
            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(v -> listener.onSpotClick(spot));
            holder.statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.available));
        } else {
            holder.itemView.setAlpha(0.6f);
            holder.itemView.setClickable(false);
            holder.statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.occupied));
        }
    }

    @Override
    public int getItemCount() {
        return parkingSpots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView spotId, spotLocation;
        View statusIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spotId = itemView.findViewById(R.id.spotId);
            spotLocation = itemView.findViewById(R.id.spotLocation);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }
    }
}
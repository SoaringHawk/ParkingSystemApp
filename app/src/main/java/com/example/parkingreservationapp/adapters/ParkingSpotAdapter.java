package com.example.parkingreservationapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingreservationapp.R;
import com.example.parkingreservationapp.models.ParkingSpot;

import java.util.List;

public class ParkingSpotAdapter extends RecyclerView.Adapter<ParkingSpotAdapter.ViewHolder> {
    private List<ParkingSpot> parkingSpots;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ParkingSpot parkingSpot);
    }

    public ParkingSpotAdapter(List<ParkingSpot> parkingSpots, Context context, OnItemClickListener listener) {
        this.parkingSpots = parkingSpots;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parking_spot_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkingSpot spot = parkingSpots.get(position);
        holder.spotId.setText(spot.getId());
        holder.spotLocation.setText(spot.getLocation());
        holder.spotStatus.setText(spot.isAvailable() ? "Available" : "Occupied");


        holder.itemView.setOnClickListener(v -> listener.onItemClick(spot));
    }

    @Override
    public int getItemCount() {
        return parkingSpots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView spotId;
        TextView spotLocation;
        TextView spotStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spotId = itemView.findViewById(R.id.spotId);
            spotLocation = itemView.findViewById(R.id.spotLocation);
            spotStatus = itemView.findViewById(R.id.spotStatus);
        }
    }
}
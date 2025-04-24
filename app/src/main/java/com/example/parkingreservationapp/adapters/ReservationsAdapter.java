package com.example.parkingreservationapp.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingreservationapp.R;
import com.example.parkingreservationapp.models.Reservation;
import com.example.parkingreservationapp.utils.TimeUtils;

import java.util.List;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder> {

    private final Context context;
    private List<Reservation> reservationList;

    public ReservationsAdapter(Context context, List<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);

        // Set user information
        holder.tvUserName.setText(reservation.getUser().getName());
        holder.tvLicensePlate.setText(reservation.getUser().getLicensePlate());

        // Set spot information
        holder.tvSpotId.setText(reservation.getParkingSpot().getId());

        // Set time information
        holder.tvTimeRange.setText(String.format("%s - %s",
                TimeUtils.formatTime(reservation.getStartTime()),
                TimeUtils.formatTime(reservation.getEndTime())));
        holder.tvDate.setText(TimeUtils.formatDate(reservation.getStartTime()));

        // Set price and status
        holder.tvPrice.setText(String.format("¥%.2f", reservation.getPrice()));

        if (reservation.isPaid()) {
            holder.tvStatus.setText(context.getString(R.string.paid));
            holder.tvStatus.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_light)));
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green_dark));
        } else {
            holder.tvStatus.setText(context.getString(R.string.pending));
            holder.tvStatus.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_light)));
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public void updateData(List<Reservation> newReservations) {
        reservationList = newReservations;
        notifyDataSetChanged();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvLicensePlate, tvSpotId;
        TextView tvTimeRange, tvDate, tvPrice, tvStatus;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvLicensePlate = itemView.findViewById(R.id.tv_license_plate);
            tvSpotId = itemView.findViewById(R.id.tv_spot_id);
            tvTimeRange = itemView.findViewById(R.id.tv_time_range);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
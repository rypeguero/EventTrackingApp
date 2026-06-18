package com.example.eventtrackingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.VH> {
    public interface OnItemClick { void onClick(long eventId); }

    private List<Event> data = new ArrayList<>();
    private final OnItemClick listener;

    public EventAdapter(OnItemClick listener){
        this.listener = listener;
    }

    public void submit(List<Event> d){
        data = d != null ? d : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate;
        VH(@NonNull View v){
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvDate  = v.findViewById(R.id.tvDate);
        }
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_event, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position){
        Event e = data.get(position);
        h.tvTitle.setText(e.title);
        String dt = (e.date == null ? "" : e.date) + (e.time == null ? "" : " " + e.time);
        h.tvDate.setText(dt.trim());
        h.itemView.setOnClickListener(v -> listener.onClick(e.id));
    }

    @Override
    public int getItemCount(){ return data.size(); }
}

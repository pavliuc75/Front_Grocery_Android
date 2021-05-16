package com.example.front_grocery_android.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.front_grocery_android.R;
import com.example.front_grocery_android.models.Item;

import java.util.ArrayList;

public class CompletedListAdapter extends RecyclerView.Adapter<CompletedListAdapter.ViewHolder> {

    ArrayList<Item> items;

    public CompletedListAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CompletedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.completed_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedListAdapter.ViewHolder holder, int position) {
        holder.textViewItemCompletedTitle.setText(items.get(position).name);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewItemCompletedTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemCompletedTitle = itemView.findViewById(R.id.text_view_item_completed_title);
            textViewItemCompletedTitle.setPaintFlags(textViewItemCompletedTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}

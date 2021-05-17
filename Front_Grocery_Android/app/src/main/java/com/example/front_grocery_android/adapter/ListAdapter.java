package com.example.front_grocery_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.front_grocery_android.R;
import com.example.front_grocery_android.models.Item;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    ArrayList<Item> items;
    OnListIncompleteItemClickListener listener;

    public ListAdapter(ArrayList<Item> items, OnListIncompleteItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        holder.textViewItemTitle.setText(items.get(position).name);
        holder.textViewItemDescription.setText(getDescription(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewItemTitle;
        private TextView textViewItemDescription;
        private ImageButton imageButtonSetComplete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemTitle = itemView.findViewById(R.id.text_view_item_complete_title);
            textViewItemDescription = itemView.findViewById(R.id.text_view_item_incomplete_description);
            imageButtonSetComplete = itemView.findViewById(R.id.image_button_set_complete);
            imageButtonSetComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCompleteClick(getAbsoluteAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAbsoluteAdapterPosition());
                }
            });

        }
    }

    private String getDescription(int i) {
        Item item = items.get(i);

        String details = item.details; //1
        int qty = item.quantity; //2
        double weight = item.weight; //3
        String unit = item.unit;

        String description = "";

        if (StringUtils.isEmpty(details) && qty == 1 && weight == 0) { //000
            description = "";
        } else if (StringUtils.isEmpty(details) && qty == 1 && weight != 0) { //001
            description = "Weight: " + weight + unit;
        } else if (StringUtils.isEmpty(details) && qty != 1 && weight == 0) { //010
            description = "Quantity: " + qty;
        } else if (StringUtils.isEmpty(details) && qty != 1 && weight != 0) { //011
            description = "Quantity: " + qty + "   Weight: " + weight + unit;
        } else if (!StringUtils.isEmpty(details) && qty == 1 && weight == 0) { //100
            description = "Details: " + details;
        } else if (!StringUtils.isEmpty(details) && qty != 1 && weight == 0) { //110
            description = "Details: " + details + "   Quantity: " + qty;
        } else if (!StringUtils.isEmpty(details) && qty == 1 && weight != 0) { //101
            description = "Details: " + details + "   Weight: " + weight + unit;
        } else if (!StringUtils.isEmpty(details) && qty != 1 && weight != 0) { //111
            description = "Details: " + details + "   Quantity: " + qty + "   Weight: " + weight + unit;
        }
        return description;
    }

    public interface OnListIncompleteItemClickListener {
        void onItemClick(int index);

        void onCompleteClick(int index);
    }
}

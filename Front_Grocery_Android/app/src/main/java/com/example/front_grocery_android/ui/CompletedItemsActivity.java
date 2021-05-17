package com.example.front_grocery_android.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.example.front_grocery_android.R;
import com.example.front_grocery_android.adapter.CompletedListAdapter;
import com.example.front_grocery_android.adapter.ListAdapter;
import com.example.front_grocery_android.models.Item;
import com.example.front_grocery_android.models.List;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CompletedItemsActivity extends AppCompatActivity implements CompletedListAdapter.OnListCompleteItemClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List selectedList;
    private CompletedItemsActivityViewModel viewModel;
    private ArrayList<Item> completedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_items);

        viewModel = new ViewModelProvider(this).get(CompletedItemsActivityViewModel.class);
        recyclerView = findViewById(R.id.recycler_view_completed_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        //Toolbar
        toolbar = findViewById(R.id.completed_items_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get listener
        viewModel.getLists().observe(this, lists -> {
            for (int i = 0; i < lists.getBody().size(); i++) {
                if (lists.getBody().get(i).id == viewModel.getSelectedListId()) {
                    selectedList = lists.getBody().get(i);
                }
            }

            //recyclerView
            //TODO: check if empty and display label
            //TODO: display in reverse order
            if (selectedList != null) {
                if (selectedList.items != null) {
                    if (!selectedList.items.isEmpty()) {
                        completedItems = new ArrayList<>();
                        for (int i = 0; i < selectedList.items.size(); i++) {
                            if (selectedList.items.get(i).isCompleted)
                                completedItems.add(selectedList.items.get(i));
                        }
                        if (!completedItems.isEmpty()) {
                            CompletedListAdapter adapter = new CompletedListAdapter(completedItems, this);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Intent toListActivity = new Intent(this, ListActivity.class);
                            startActivity(toListActivity);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCompleteItemClick(int index) {
        Item updItem = completedItems.get(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_add_item, null);
        builder.setView(dialogView);
        builder.setTitle("Edit item");

        AtomicInteger qty = new AtomicInteger(updItem.quantity);
        AtomicReference<String> unit = new AtomicReference<>(updItem.unit);

        NumberPicker pickerQty = dialogView.findViewById(R.id.picker_qty);
        NumberPicker pickerUnit = dialogView.findViewById(R.id.picker_unit);
        EditText editTextAddItemName = dialogView.findViewById(R.id.edit_text_add_item_name);
        EditText editTextAddItemDetails = dialogView.findViewById(R.id.edit_text_add_item_details);
        EditText editTextAddItemWeight = dialogView.findViewById(R.id.edit_text_add_item_weight);

        pickerQty.setMinValue(1);
        pickerQty.setMaxValue(99);
        pickerQty.setValue(updItem.quantity);
        pickerQty.setOnValueChangedListener((numberPicker, i, i1) -> qty.set(pickerQty.getValue()));

        String[] unitValues = new String[]{"g", "kg", "ml", "l"};

        int stringValue = 0;
        if (updItem.unit != null) {
            if (updItem.unit.equals("kg")) {
                stringValue = 1;
            } else if (updItem.unit.equals("ml")) {
                stringValue = 2;
            } else if (updItem.unit.equals("l")) {
                stringValue = 3;
            }
        }

        pickerUnit.setMinValue(0);
        pickerUnit.setMaxValue(3);
        pickerUnit.setValue(stringValue);
        pickerUnit.setDisplayedValues(unitValues);
        pickerUnit.setOnValueChangedListener((numberPicker, i, i1) -> {
            int position = pickerUnit.getValue();
            unit.set(unitValues[position]);
        });

        editTextAddItemName.setText(updItem.name);
        editTextAddItemDetails.setText(updItem.details);
        editTextAddItemWeight.setText(Double.toString(updItem.weight));

        builder.setPositiveButton("Save",
                (dialog, which) -> {
                    //Do nothing here because we override this button later to change the close behaviour.
                });

        builder.setNegativeButton("Cancel", (dialog, id) -> {
        });

        builder.setNeutralButton("Delete", (dialog, id) -> {
            viewModel.deleteItem(updItem);
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
            if (StringUtils.isEmpty(editTextAddItemName.getText().toString())) {
                editTextAddItemName.setError("This field is required");
            } else {
                updItem.name = editTextAddItemName.getText().toString();
                updItem.details = editTextAddItemDetails.getText().toString();
                if (StringUtils.isEmpty(editTextAddItemWeight.getText().toString())) {
                    updItem.weight = 0;
                } else
                    updItem.weight = Double.parseDouble(editTextAddItemWeight.getText().toString());
                updItem.quantity = qty.get();
                updItem.unit = unit.get();
                viewModel.updateItem(updItem);
                dialog.dismiss();
            }
        });
        //TODO:autofocus+keyboard
    }

    @Override
    public void onIncompleteClick(int index) {
        Item updItem = completedItems.get(index);
        updItem.isCompleted = false;
        viewModel.updateItem(updItem);
    }
}
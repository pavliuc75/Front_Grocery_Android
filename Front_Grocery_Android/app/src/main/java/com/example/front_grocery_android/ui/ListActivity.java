package com.example.front_grocery_android.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.front_grocery_android.R;
import com.example.front_grocery_android.models.Item;
import com.example.front_grocery_android.models.List;
import com.example.front_grocery_android.adapter.ListAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ListActivity extends AppCompatActivity implements ListAdapter.OnListIncompleteItemClickListener {

    //TODO: mby transform some child activities to fragments
    private ListActivityViewModel viewModel;
    private TextView textViewListId;
    private FloatingActionButton fabAdd;
    private ImageButton imageButtonSettings;
    private ImageButton imageButtonSwitch;
    private ImageButton imageButtonCopy;
    private TextView textViewDescription;
    private List selectedList;
    private ArrayList<Item> incompleteItemsList;
    private MaterialButton buttonToCompletedItems;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        viewModel = new ViewModelProvider(this).get(ListActivityViewModel.class);
        textViewListId = findViewById(R.id.textView_list_id);
        fabAdd = findViewById(R.id.fab_add);
        imageButtonSettings = findViewById(R.id.imageButtonSettings);
        imageButtonSwitch = findViewById(R.id.imageButtonSwitch);
        imageButtonCopy = findViewById(R.id.image_button_copy);
        textViewDescription = findViewById(R.id.text_view_description);
        buttonToCompletedItems = findViewById(R.id.button_to_completed_items);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        //get listener
        viewModel.getLists().observe(this, lists -> {
            for (int i = 0; i < lists.getBody().size(); i++) {
                if (lists.getBody().get(i).id == viewModel.getSelectedListId()) {
                    selectedList = lists.getBody().get(i);
                }
            }

            // description label
            if (selectedList != null) {
                setDescriptionLabel();
            }

            //recyclerView
            //TODO: check if empty and display label
            //TODO: display in reverse order
            if (selectedList != null) {
                if (selectedList.items != null) {
                    if (!selectedList.items.isEmpty()) {
                        incompleteItemsList = new ArrayList<>();
                        for (int i = 0; i < selectedList.items.size(); i++) {
                            if (!selectedList.items.get(i).isCompleted)
                                incompleteItemsList.add(selectedList.items.get(i));
                        }
                        if (!incompleteItemsList.isEmpty()) {
                            ListAdapter adapter = new ListAdapter(incompleteItemsList, this);
                            recyclerView.setAdapter(adapter);
                        } else {
                            ListAdapter adapter = new ListAdapter(new ArrayList<>(), this);
                            recyclerView.setAdapter(adapter);
                        }
                    } else {
                        ListAdapter adapter = new ListAdapter(new ArrayList<>(), this);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    ListAdapter adapter = new ListAdapter(new ArrayList<>(), this);
                    recyclerView.setAdapter(adapter);
                }
            } else {
                ListAdapter adapter = new ListAdapter(new ArrayList<>(), this);
                recyclerView.setAdapter(adapter);
            }

            //toCompletedListButton
            if (selectedList != null) {
                if (selectedList.items != null) {
                    if (!selectedList.items.isEmpty()) {
                        ArrayList<Item> completeItemsList = new ArrayList<>();
                        for (int i = 0; i < selectedList.items.size(); i++) {
                            if (selectedList.items.get(i).isCompleted)
                                completeItemsList.add(selectedList.items.get(i));
                        }
                        if (!completeItemsList.isEmpty()) {
                            buttonToCompletedItems.setEnabled(true);
                        }
                    }
                }
            }
        });

        //set list id label
        String listId = String.valueOf(viewModel.getSelectedListId());
        textViewListId.setText(listId);

        //add item
        fabAdd.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_add_item, null);
            builder.setView(dialogView);
            builder.setTitle("Add item");

            AtomicInteger qty = new AtomicInteger(1);
            AtomicReference<String> unit = new AtomicReference<>("g");

            NumberPicker pickerQty = dialogView.findViewById(R.id.picker_qty);
            NumberPicker pickerUnit = dialogView.findViewById(R.id.picker_unit);
            EditText editTextAddItemName = dialogView.findViewById(R.id.edit_text_add_item_name);
            EditText editTextAddItemDetails = dialogView.findViewById(R.id.edit_text_add_item_details);
            EditText editTextAddItemWeight = dialogView.findViewById(R.id.edit_text_add_item_weight);

            pickerQty.setMinValue(1);
            pickerQty.setMaxValue(99);
            pickerQty.setOnValueChangedListener((numberPicker, i, i1) -> qty.set(pickerQty.getValue()));

            String[] unitValues = new String[]{"g", "kg", "ml", "l"};
            pickerUnit.setMinValue(0);
            pickerUnit.setMaxValue(3);
            pickerUnit.setDisplayedValues(unitValues);
            pickerUnit.setOnValueChangedListener((numberPicker, i, i1) -> {
                int index = pickerUnit.getValue();
                unit.set(unitValues[index]);
            });


            builder.setPositiveButton("Add",
                    (dialog, which) -> {
                        //Do nothing here because we override this button later to change the close behaviour.
                    });

            builder.setNegativeButton("Cancel", (dialog, id) -> {
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                if (StringUtils.isEmpty(editTextAddItemName.getText().toString())) {
                    editTextAddItemName.setError("This field is required");
                } else {
                    Item newItem = new Item();
                    newItem.name = editTextAddItemName.getText().toString();
                    newItem.details = editTextAddItemDetails.getText().toString();
                    newItem.isCompleted = false;
                    if (StringUtils.isEmpty(editTextAddItemWeight.getText().toString())) {
                        newItem.weight = 0;
                    } else
                        newItem.weight = Double.parseDouble(editTextAddItemWeight.getText().toString());
                    newItem.quantity = qty.get();
                    newItem.unit = unit.get();
                    viewModel.addItemToList(newItem);
                    dialog.dismiss();
                }
            });
            //TODO:autofocus+keyboard
        });

        //settings
        imageButtonSettings.setOnClickListener(v -> {
            Intent toSettings = new Intent(this, ListSettingsActivity.class);
            startActivity(toSettings);
        });

        //listSwitch
        imageButtonSwitch.setOnClickListener(v -> {
            Intent toMain = new Intent(this, MainActivity.class);
            startActivity(toMain);
        });

        //copyId
        //TODO: mby share button instead
        imageButtonCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Shopply list ID", String.valueOf(viewModel.getSelectedListId()));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "List ID copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        //toCompletedItemsList
        buttonToCompletedItems.setOnClickListener(v -> {
            Intent toCompletedItems = new Intent(this, CompletedItemsActivity.class);
            startActivity(toCompletedItems);
        });
    }

    public void setDescriptionLabel() {
        if (!StringUtils.isEmpty(selectedList.description)) {
            textViewDescription.setText(selectedList.description);
        } else {
            textViewDescription.setText("");
        }
    }

    //updateItem
    @Override
    public void onItemClick(int index) {
        Item updItem = incompleteItemsList.get(index);
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
    public void onCompleteClick(int index) {
        Item updItem = incompleteItemsList.get(index);
        updItem.isCompleted = true;
        viewModel.updateItem(updItem);
    }
}
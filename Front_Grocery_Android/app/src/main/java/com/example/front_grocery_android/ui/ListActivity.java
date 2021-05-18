package com.example.front_grocery_android.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

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

    private ListActivityViewModel viewModel;
    private TextView textViewListId;
    private FloatingActionButton fabAdd;
    private ImageButton imageButtonSettings;
    private ImageButton imageButtonSwitch;
    private ImageButton imageButtonShare;
    private TextView textViewDescription;
    private List selectedList;
    private ArrayList<Item> incompleteItemsList;
    private MaterialButton buttonToCompletedItems;
    private RecyclerView recyclerView;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        viewModel = new ViewModelProvider(this).get(ListActivityViewModel.class);
        textViewListId = findViewById(R.id.textView_list_id);
        fabAdd = findViewById(R.id.fab_add);
        imageButtonSettings = findViewById(R.id.imageButtonSettings);
        imageButtonSwitch = findViewById(R.id.imageButtonSwitch);
        imageButtonShare = findViewById(R.id.image_button_share);
        textViewDescription = findViewById(R.id.text_view_description);
        buttonToCompletedItems = findViewById(R.id.button_to_completed_items);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        emptyView = findViewById(R.id.empty_view);

        //get listener
        viewModel.getLists().observe(this, lists -> {
            for (int i = 0; i < lists.getBody().size(); i++) {
                if (lists.getBody().get(i).id == viewModel.getSelectedListId()) {
                    selectedList = lists.getBody().get(i);
                }
            }

            // description label
            if (selectedList != null) {
                if (!StringUtils.isEmpty(selectedList.description)) {
                    textViewDescription.setText(selectedList.description);
                    textViewDescription.setVisibility(View.VISIBLE);
                } else {
                    textViewDescription.setVisibility(View.GONE);
                }
            }

            //recyclerView
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
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                            ListAdapter adapter = new ListAdapter(incompleteItemsList, this);
                            recyclerView.setAdapter(adapter);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            } else {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
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
            builder.setTitle(R.string.add_item);

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


            builder.setPositiveButton(R.string.add,
                    (dialog, which) -> {
                        //Do nothing here because we override this button later to change the close behaviour.
                    });

            builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            });

            editTextAddItemName.requestFocus();
            AlertDialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                if (StringUtils.isEmpty(editTextAddItemName.getText().toString())) {
                    editTextAddItemName.setError(getString(R.string.required_field));
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
        });

        //settings
        imageButtonSettings.setOnClickListener(v -> {
            Intent toSettings = new Intent(this, ListSettingsActivity.class);
            startActivity(toSettings);
        });

        //listSwitch
        imageButtonSwitch.setOnClickListener(v -> {
            //save shared preferences
            SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();
            editor.putInt("id", -1);
            editor.apply();

            Intent toMain = new Intent(this, MainActivity.class);
            startActivity(toMain);
        });

        //share
        imageButtonShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shopply_list_id) + " " + viewModel.getSelectedListId());
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        //toCompletedItemsList
        buttonToCompletedItems.setOnClickListener(v -> {
            Intent toCompletedItems = new Intent(this, CompletedItemsActivity.class);
            startActivity(toCompletedItems);
        });
    }

    //updateItem
    @Override
    public void onItemClick(int index) {
        Item updItem = incompleteItemsList.get(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_add_item, null);
        builder.setView(dialogView);
        builder.setTitle(R.string.edit_item);

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

        builder.setPositiveButton(R.string.save,
                (dialog, which) -> {
                    //Do nothing here because we override this button later to change the close behaviour.
                });

        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
        });

        builder.setNeutralButton(R.string.delete, (dialog, id) -> {
            viewModel.deleteItem(updItem);
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
            if (StringUtils.isEmpty(editTextAddItemName.getText().toString())) {
                editTextAddItemName.setError(getString(R.string.required_field));
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
    }

    @Override
    public void onCompleteClick(int index) {
        Item updItem = incompleteItemsList.get(index);
        updItem.isCompleted = true;
        viewModel.updateItem(updItem);
    }
}
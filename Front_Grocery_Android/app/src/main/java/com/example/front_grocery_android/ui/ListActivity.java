package com.example.front_grocery_android.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ListActivity extends AppCompatActivity {

    private ListActivityViewModel viewModel;
    private TextView textViewListId;
    private FloatingActionButton fabAdd;
    private ImageButton imageButtonSettings;
    private ImageButton imageButtonSwitch;
    private ImageButton imageButtonCopy;
    private TextView textViewDescription;
    private List selectedList;

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
            AtomicReference<String> unit = new AtomicReference<>("none");

            NumberPicker pickerQty = dialogView.findViewById(R.id.picker_qty);
            NumberPicker pickerUnit = dialogView.findViewById(R.id.picker_unit);
            EditText editTextAddItemName = dialogView.findViewById(R.id.edit_text_add_item_name);
            EditText editTextAddItemDetails = dialogView.findViewById(R.id.edit_text_add_item_details);
            EditText editTextAddItemWeight = dialogView.findViewById(R.id.edit_text_add_item_weight);

            pickerQty.setMinValue(1);
            pickerQty.setMaxValue(99);
            pickerQty.setOnValueChangedListener((numberPicker, i, i1) -> qty.set(pickerQty.getValue()));

            String[] unitValues = new String[]{"none", "g", "kg", "ml", "l"};
            pickerUnit.setMinValue(0);
            pickerUnit.setMaxValue(4);
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
                        newItem.weight = -1;
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
        imageButtonCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Shopply list ID", String.valueOf(viewModel.getSelectedListId()));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "List ID copied to clipboard", Toast.LENGTH_SHORT).show();
        });
    }

    public void setDescriptionLabel() {
        if (!StringUtils.isEmpty(selectedList.description)) {
            textViewDescription.setText(selectedList.description);
        } else {
            textViewDescription.setText("");
        }
    }

}
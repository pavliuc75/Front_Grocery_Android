package com.example.front_grocery_android.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.front_grocery_android.R;
import com.example.front_grocery_android.models.List;

import org.apache.commons.lang3.StringUtils;

public class ListSettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageButton imageButtonHelp;
    private Button buttonWipe;
    private ListSettingsActivityViewModel viewModel;
    private TextView textViewListIdLabelSettings;
    private List selectedList;
    private TextView textViewListDescriptionSettings;
    private ImageButton imageButtonChangeDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_settings);

        viewModel = new ViewModelProvider(this).get(ListSettingsActivityViewModel.class);
        imageButtonHelp = findViewById(R.id.image_button_settings_help);
        buttonWipe = findViewById(R.id.button_wipe);
        textViewListIdLabelSettings = findViewById(R.id.text_view_list_id_label_settings);
        textViewListDescriptionSettings = findViewById(R.id.text_view_list_description_settings);
        imageButtonChangeDescription = findViewById(R.id.image_button_change_description);

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

        //Toolbar
        toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //list id label
        textViewListIdLabelSettings.setText(String.valueOf(viewModel.getSelectedListId()));

        //change description button
        imageButtonChangeDescription.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_change_description, null);
            builder.setView(dialogView);

            EditText editTextChangeDescription = dialogView.findViewById(R.id.edit_text_change_description);

            builder.setTitle(R.string.change_list_description);
            builder.setPositiveButton(R.string.save, (dialog, id) -> {
                viewModel.changeListDescription(editTextChangeDescription.getText().toString());
            });
            builder.setNegativeButton(R.string.cancel, (dialog, id) -> {

            });

            String defaultText = "";
            if (!StringUtils.isEmpty(selectedList.description)) {
                defaultText = selectedList.description;
            }
            editTextChangeDescription.setText(defaultText);

            AlertDialog dialog = builder.create();
            dialog.show();
            //TODO:autofocus+keyboard
        });

        //help button
        imageButtonHelp.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.this_will_remove_all_items);
            builder.setNegativeButton(getString(R.string.close_alert_text), (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        //wipe button
        buttonWipe.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.are_you_sure_wipe);
            builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            });
            builder.setPositiveButton(R.string.wipe, (dialog, id) -> {
                viewModel.wipeList();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    public void setDescriptionLabel() {
        if (!StringUtils.isEmpty(selectedList.description)) {
            textViewListDescriptionSettings.setText(selectedList.description);
        } else {
            textViewListDescriptionSettings.setText(getString(R.string.no_description));
        }
    }

    //for correct back animation
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

}

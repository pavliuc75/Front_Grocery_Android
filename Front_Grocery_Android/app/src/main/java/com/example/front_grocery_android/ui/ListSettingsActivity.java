package com.example.front_grocery_android.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.front_grocery_android.R;

public class ListSettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageButton imageButtonHelp;
    private Button buttonWipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_settings);

        imageButtonHelp = findViewById(R.id.image_button_settings_help);
        buttonWipe = findViewById(R.id.button_wipe);

        //Toolbar
        toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //help button
        imageButtonHelp.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This will remove all items from the current list");
            builder.setNegativeButton("Close", (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        //wipe button
        buttonWipe.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure that you want to wipe the current list?");
            builder.setNegativeButton("Close", (dialog, id) -> {
            });
            builder.setPositiveButton("Delete", (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}

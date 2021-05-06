package com.example.front_grocery_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListActivity extends AppCompatActivity {

    private TextView textViewListId;
    private FloatingActionButton fabAdd;
    private ImageButton imageButtonSettings;
    private ImageButton imageButtonSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        textViewListId = findViewById(R.id.textView_list_id);
        fabAdd = findViewById(R.id.fab_add);
        imageButtonSettings = findViewById(R.id.imageButtonSettings);
        imageButtonSwitch = findViewById(R.id.imageButtonSwitch);

        //extract params
        Bundle bundle = getIntent().getExtras();
        String listId = bundle.getString("listId");
        textViewListId.setText(listId);

        //fab
        fabAdd.setOnClickListener(v -> {
            Intent toAdd = new Intent(this, AddItemActivity.class);
            startActivity(toAdd);
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
    }
}
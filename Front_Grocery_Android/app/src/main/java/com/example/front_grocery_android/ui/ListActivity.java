package com.example.front_grocery_android.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.front_grocery_android.R;
import com.example.front_grocery_android.Repository.CachedList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListActivity extends AppCompatActivity {

    private TextView textViewListId;
    private FloatingActionButton fabAdd;
    private ImageButton imageButtonSettings;
    private ImageButton imageButtonSwitch;
    private ImageButton imageButtonCopy;
    private CachedList cachedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        textViewListId = findViewById(R.id.textView_list_id);
        fabAdd = findViewById(R.id.fab_add);
        imageButtonSettings = findViewById(R.id.imageButtonSettings);
        imageButtonSwitch = findViewById(R.id.imageButtonSwitch);
        imageButtonCopy = findViewById(R.id.image_button_copy);
        cachedList = CachedList.getInstance();

        //set list id
        String listId = String.valueOf(cachedList.getCachedList());
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

        //copyId
        imageButtonCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Shopply list ID", String.valueOf(cachedList.getCachedList()));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "List ID copied to clipboard", Toast.LENGTH_SHORT).show();
        });
    }
}
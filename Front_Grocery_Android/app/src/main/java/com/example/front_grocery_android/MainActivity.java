package com.example.front_grocery_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton imageButtonHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButtonHelp = findViewById(R.id.imageButtonHelp);

        imageButtonHelp.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("//TODO");
            builder.setPositiveButton("Close", (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}
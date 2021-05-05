package com.example.front_grocery_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton imageButtonHelp;
    private Button buttonGoToList;
    private Button buttonGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButtonHelp = findViewById(R.id.imageButtonHelp);
        buttonGoToList = findViewById(R.id.button_go_to_list);
        buttonGenerate = findViewById(R.id.button_generate);

        //help button
        imageButtonHelp.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("If you know the code to an existing list, enter it into the input field. Otherwise, press the \"Generate\" button to create a new list.");
            builder.setPositiveButton("Close", (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        //go to list button
        buttonGoToList.setOnClickListener(v -> {
            System.out.println("");
        });

        //generate button
    }
}
package com.example.front_grocery_android.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.front_grocery_android.R;
import com.example.front_grocery_android.Repository.CachedList;

public class MainActivity extends AppCompatActivity {

    private ImageButton imageButtonHelp;
    private Button buttonGoToList;
    private Button buttonGenerate;
    private EditText editTextListId;
    private CachedList cachedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButtonHelp = findViewById(R.id.imageButtonHelp);
        buttonGoToList = findViewById(R.id.button_go_to_list);
        buttonGenerate = findViewById(R.id.button_generate);
        editTextListId = findViewById(R.id.editText_list_id);
        cachedList = CachedList.getInstance();

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
            if (TextUtils.isEmpty(editTextListId.getText())) {
                editTextListId.setError("This field is required");
            } else if ((Integer.parseInt(editTextListId.getText().toString()) > 9999) || (Integer.parseInt(editTextListId.getText().toString()) < 1000)) {
                editTextListId.setError("The value should be between 1000 and 9999");
            } else {
                Intent toList = new Intent(this, ListActivity.class);
                //toList.putExtra("listId", editTextListId.getText().toString());
                cachedList.setCachedList(Integer.parseInt(editTextListId.getText().toString()));
                startActivity(toList);
            }
        });

        //generate button
    }
}
package com.example.front_grocery_android.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.front_grocery_android.R;
import com.example.front_grocery_android.models.Item;
import com.example.front_grocery_android.models.List;
import com.example.front_grocery_android.models.Lists;
import com.example.front_grocery_android.repository.ListsRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private ImageButton imageButtonHelp;
    private Button buttonGoToList;
    private Button buttonGenerate;
    private EditText editTextListId;
    private Lists listsScoped;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.init();
        //TODO: auto start list activity with last list id from device memory
        //TODO: lock vertical
        //TODO: lock light mode
        //TODO: add languages

        imageButtonHelp = findViewById(R.id.imageButtonHelp);
        buttonGoToList = findViewById(R.id.button_go_to_list);
        buttonGenerate = findViewById(R.id.button_generate);
        editTextListId = findViewById(R.id.editText_list_id);

        //get listener
        viewModel.getLists().observe(this, lists -> {
            listsScoped = lists;
        });


        //help button
        imageButtonHelp.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.main_help_text);
            builder.setPositiveButton(R.string.close_alert_text, (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        //go to list button
        buttonGoToList.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editTextListId.getText())) {
                editTextListId.setError(getString(R.string.field_required));
            } else if ((Integer.parseInt(editTextListId.getText().toString()) > 9999) || (Integer.parseInt(editTextListId.getText().toString()) < 1000)) {
                editTextListId.setError(getString(R.string.main_set_error_2));
            } else if (!checkIfListExists(Integer.parseInt(editTextListId.getText().toString()))) {
                editTextListId.setError(getString(R.string.main_set_error_3));
            } else {
                Intent toList = new Intent(this, ListActivity.class);
                viewModel.setSelectedListId(Integer.parseInt(editTextListId.getText().toString()));
                startActivity(toList);
            }
        });

        //generate button
        buttonGenerate.setOnClickListener(v -> {
            if (listsScoped != null) {
                int lastListId = listsScoped.getBody().get(listsScoped.lists.size() - 1).id;
                viewModel.setSelectedListId(lastListId + 1);
                viewModel.generateNewList(viewModel.getSelectedListId());
                Intent toList = new Intent(this, ListActivity.class);
                startActivity(toList);
            }
        });
    }

    public boolean checkIfListExists(int listId) {
        if (listsScoped != null) {
            for (int i = 0; i < listsScoped.getBody().size(); i++) {
                if (listsScoped.getBody().get(i).id == listId)
                    return true;
            }
        }
        return false;
    }
}
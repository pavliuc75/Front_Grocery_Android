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

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        //viewModel.init();


        imageButtonHelp = findViewById(R.id.imageButtonHelp);
        buttonGoToList = findViewById(R.id.button_go_to_list);
        buttonGenerate = findViewById(R.id.button_generate);
        editTextListId = findViewById(R.id.editText_list_id);

        //get listener
        /*
        viewModel.getLists().observe(this, lists -> {
            System.out.println(lists.getBody());
        });

         */

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
                viewModel.setSelectedListId(Integer.parseInt(editTextListId.getText().toString()));
                startActivity(toList);
            }
        });

        //generate button
        buttonGenerate.setOnClickListener(v -> {
            ArrayList<List> lists = new ArrayList<>();
            List l1 = new List();
            l1.id = 1000;
            l1.description = "first";
            Item i1 = new Item();
            i1.id = 1;
            i1.name = "firstItem";
            l1.items = new ArrayList<>();
            l1.items.add(i1);
            lists.add(l1);
            //viewModel.saveLists(lists);
            basicReadWrite();
        });
    }

    public void basicReadWrite() {
        // [START write_message]
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("kek2");
        Log.d(TAG, "Firebase " + database);
        Log.d(TAG, "Firebase " + myRef);
        Log.d(TAG, "Firebase " + database.getReference());
        Log.d(TAG, "Firebase " + database.getApp());
        Log.d(TAG, "Firebase " + myRef.getKey());
        Log.d(TAG, "Firebase " + myRef.getRoot());

        myRef.setValue("Hello, World!");
        // [END write_message]

        // [START read_message]
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        // [END read_message]
    }
}
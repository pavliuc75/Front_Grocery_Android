package com.example.front_grocery_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {

    private TextView textViewListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        textViewListId = findViewById(R.id.textView_list_id);

        //extract params
        Bundle bundle = getIntent().getExtras();
        String listId = bundle.getString("listId");
        textViewListId.setText(listId);
    }
}
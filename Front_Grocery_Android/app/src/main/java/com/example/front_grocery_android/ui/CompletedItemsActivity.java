package com.example.front_grocery_android.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.front_grocery_android.R;
import com.example.front_grocery_android.adapter.CompletedListAdapter;
import com.example.front_grocery_android.adapter.ListAdapter;
import com.example.front_grocery_android.models.Item;
import com.example.front_grocery_android.models.List;

import java.util.ArrayList;

public class CompletedItemsActivity extends AppCompatActivity implements CompletedListAdapter.OnListCompleteItemClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List selectedList;
    private CompletedItemsActivityViewModel viewModel;
    private ArrayList<Item> completedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_items);

        viewModel = new ViewModelProvider(this).get(CompletedItemsActivityViewModel.class);
        recyclerView = findViewById(R.id.recycler_view_completed_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        //Toolbar
        toolbar = findViewById(R.id.completed_items_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get listener
        viewModel.getLists().observe(this, lists -> {
            for (int i = 0; i < lists.getBody().size(); i++) {
                if (lists.getBody().get(i).id == viewModel.getSelectedListId()) {
                    selectedList = lists.getBody().get(i);
                }
            }

            //recyclerView
            //TODO: check if empty and display label
            //TODO: display in reverse order
            if (selectedList != null) {
                if (selectedList.items != null) {
                    if (!selectedList.items.isEmpty()) {
                        completedItems = new ArrayList<>();
                        for (int i = 0; i < selectedList.items.size(); i++) {
                            if (selectedList.items.get(i).isCompleted)
                                completedItems.add(selectedList.items.get(i));
                        }
                        if (!completedItems.isEmpty()) {
                            CompletedListAdapter adapter = new CompletedListAdapter(completedItems, this);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Intent toListActivity = new Intent(this, ListActivity.class);
                            startActivity(toListActivity);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCompleteItemClick(int index) {
        System.out.println("kek");
    }

    @Override
    public void onIncompleteClick(int index) {
        Item updItem = completedItems.get(index);
        updItem.isCompleted = false;
        viewModel.updateItem(updItem);
    }
}
package com.example.front_grocery_android.repository;

import com.example.front_grocery_android.data.ListsLiveData;
import com.example.front_grocery_android.models.List;
import com.example.front_grocery_android.models.Lists;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListsRepository {
    private static ListsRepository instance;
    private int selectedListId;
    private DatabaseReference myRef;
    private ListsLiveData lists;

    private ListsRepository() {
    }

    public static synchronized ListsRepository getInstance() {
        if (instance == null) {
            instance = new ListsRepository();
        }
        return instance;
    }

    public void init() {
        System.out.println("inito");
        myRef = FirebaseDatabase.getInstance().getReference();
        System.out.println(myRef);
        lists = new ListsLiveData(myRef);
        System.out.println(lists.getValue());
    }

    //list id label
    public void setSelectedListId(int listId) {
        this.selectedListId = listId;
    }

    public int getSelectedListId() {
        return selectedListId;
    }

    //firebase data
    public ListsLiveData getLists() {
        return lists;
    }

    public void saveLists(ArrayList<List> lists) {
        myRef.setValue(new Lists(lists));
    }
}

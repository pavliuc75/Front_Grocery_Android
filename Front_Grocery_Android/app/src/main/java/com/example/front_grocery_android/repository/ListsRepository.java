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
        myRef = FirebaseDatabase.getInstance().getReference();
        lists = new ListsLiveData(myRef);
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

    public void generateNewList(int listId) {
        try {
            ArrayList<List> lists = this.lists.getValue().lists;
            List newList = new List();
            newList.id = listId;
            newList.items = new ArrayList<>();
            lists.add(newList);
            saveLists(lists);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void changeListDescription(int listId, String newDescription) {
        try {
            ArrayList<List> lists = this.lists.getValue().lists;
            for (int i = 0; i < lists.size(); i++) {
                if (listId == lists.get(i).id) {
                    lists.get(i).description = newDescription;
                    saveLists(lists);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}

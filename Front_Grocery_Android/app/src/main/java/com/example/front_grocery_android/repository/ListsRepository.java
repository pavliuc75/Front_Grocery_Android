package com.example.front_grocery_android.repository;

import com.example.front_grocery_android.data.ListsLiveData;
import com.example.front_grocery_android.models.Item;
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
    //TODO: replace sending the entire list back with individual parts
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
            newList.description = "My shopping list";
            newList.items = new ArrayList<>();
            lists.add(newList);
            saveLists(lists);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateItem(Item updItem) {
        try {
            ArrayList<List> lists = this.lists.getValue().lists;
            List listToBeUpdated = new List();
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).id == getSelectedListId())
                    listToBeUpdated = lists.get(i);
            }
            for (int i = 0; i < listToBeUpdated.items.size(); i++) {
                if (listToBeUpdated.items.get(i).id == updItem.id) {
                    listToBeUpdated.items.get(i).name = updItem.name;
                    listToBeUpdated.items.get(i).isCompleted = updItem.isCompleted;
                    listToBeUpdated.items.get(i).unit = updItem.unit;
                    listToBeUpdated.items.get(i).weight = updItem.weight;
                    listToBeUpdated.items.get(i).quantity = updItem.quantity;
                    listToBeUpdated.items.get(i).details = updItem.details;
                    break;
                }
            }
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).id == getSelectedListId()) {
                    lists.get(i).items = listToBeUpdated.items;
                }
            }
            saveLists(lists);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void changeListDescription(String newDescription) {
        try {
            ArrayList<List> lists = this.lists.getValue().lists;
            for (int i = 0; i < lists.size(); i++) {
                if (selectedListId == lists.get(i).id) {
                    lists.get(i).description = newDescription;
                }
            }
            saveLists(lists);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void wipeList() {
        try {
            ArrayList<List> lists = this.lists.getValue().lists;
            for (int i = 0; i < lists.size(); i++) {
                if (selectedListId == lists.get(i).id) {
                    lists.get(i).description = "My shopping list";
                    lists.get(i).items = new ArrayList<>();
                }
            }
            saveLists(lists);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addItemToList(Item item) {
        try {
            ArrayList<List> lists = this.lists.getValue().lists;
            ArrayList<Item> itemsScoped = null;
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).id == selectedListId) {
                    itemsScoped = lists.get(i).items;
                    break;
                }
            }

            if (itemsScoped == null) {
                itemsScoped = new ArrayList<>();
            }
            if (itemsScoped.isEmpty()) {
                item.id = 0;
            } else {
                item.id = itemsScoped.get(itemsScoped.size() - 1).id + 1;
            }

            itemsScoped.add(item);
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).id == selectedListId) {
                    lists.get(i).items = itemsScoped;
                    break;
                }
            }

            saveLists(lists);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

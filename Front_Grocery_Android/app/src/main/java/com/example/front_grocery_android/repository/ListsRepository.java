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
            newList.description = "";
            newList.items = new ArrayList<>();
            lists.add(newList);
            myRef.child("lists").setValue(lists);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateItem(Item updItem) {
        int index = -1;
        try {
            ArrayList<List> lists = this.lists.getValue().lists;
            List listToBeUpdated = new List();
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).id == getSelectedListId()) {
                    index = i;
                    listToBeUpdated = lists.get(index);
                    break;
                }
            }

            ArrayList<Item> items = listToBeUpdated.items;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).id == updItem.id) {
                    items.get(i).unit = updItem.unit;
                    items.get(i).isCompleted = updItem.isCompleted;
                    items.get(i).name = updItem.name;
                    items.get(i).details = updItem.details;
                    items.get(i).quantity = updItem.quantity;
                    items.get(i).weight = updItem.weight;
                    break;
                }
            }

            if (index != -1) {
                myRef.child("lists").child(String.valueOf(index)).child("items").setValue(items);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteItem(Item item) {
        int index = -1;
        try {
            ArrayList<List> lists = this.lists.getValue().lists;
            List listToBeUpdated = new List();
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).id == getSelectedListId()) {
                    index = i;
                    listToBeUpdated = lists.get(index);
                    break;
                }
            }
            for (int i = 0; i < listToBeUpdated.items.size(); i++) {
                if (listToBeUpdated.items.get(i).id == item.id) {
                    listToBeUpdated.items.remove(i);
                    break;
                }
            }
            if (index != -1) {
                myRef.child("lists").child(String.valueOf(index)).child("items").setValue(listToBeUpdated.items);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void changeListDescription(String newDescription) {
        int index = -1;
        try {
            ArrayList<List> lists = this.lists.getValue().lists;
            for (int i = 0; i < lists.size(); i++) {
                if (selectedListId == lists.get(i).id) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                myRef.child("lists").child(String.valueOf(index)).child("description").setValue(newDescription);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void wipeList() {
        int index = -1;
        try {
            ArrayList<List> lists = this.lists.getValue().lists;
            for (int i = 0; i < lists.size(); i++) {
                if (selectedListId == lists.get(i).id) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                List list = new List();
                list.id = selectedListId;
                list.description = "My shopping list";
                list.items = new ArrayList<>();
                myRef.child("lists").child(String.valueOf(index)).setValue(list);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addItemToList(Item item) {
        int index = -1;
        try {
            ArrayList<List> lists = this.lists.getValue().lists;
            List listToBeUpdated = new List();
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).id == getSelectedListId()) {
                    index = i;
                    listToBeUpdated = lists.get(index);
                    break;
                }
            }

            ArrayList<Item> items = listToBeUpdated.items;
            if (items == null) {
                items = new ArrayList<>();
            }
            if (items.isEmpty()) {
                item.id = 0;
            } else {
                item.id = items.get(items.size() - 1).id + 1;
            }

            items.add(item);

            if (index != -1) {
                myRef.child("lists").child(String.valueOf(index)).child("items").setValue(items);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

package com.example.front_grocery_android.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.front_grocery_android.models.Item;
import com.example.front_grocery_android.models.List;
import com.example.front_grocery_android.models.Lists;
import com.example.front_grocery_android.repository.ListsRepository;

import java.util.ArrayList;

public class ListActivityViewModel extends ViewModel {
    private final ListsRepository listsRepository;

    public ListActivityViewModel() {
        listsRepository = ListsRepository.getInstance();
    }

    //local list id label
    public int getSelectedListId() {
        return listsRepository.getSelectedListId();
    }

    //firebase data
    public LiveData<Lists> getLists() {
        return listsRepository.getLists();
    }

    public void addItemToList(Item item) {
        listsRepository.addItemToList(item);
    }

    public void updateItem(Item updItem) {
        listsRepository.updateItem(updItem);
    }

    public void deleteItem(Item item) {
        listsRepository.deleteItem(item);
    }
}

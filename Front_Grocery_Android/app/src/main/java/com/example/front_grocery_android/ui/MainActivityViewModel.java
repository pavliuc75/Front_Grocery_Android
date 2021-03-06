package com.example.front_grocery_android.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.front_grocery_android.models.List;
import com.example.front_grocery_android.models.Lists;
import com.example.front_grocery_android.repository.ListsRepository;

import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel {
    private final ListsRepository listsRepository;

    public MainActivityViewModel() {
        listsRepository = ListsRepository.getInstance();
    }


    //local list id label
    public void setSelectedListId(int listId) {
        listsRepository.setSelectedListId(listId);
    }

    public int getSelectedListId() {
        return listsRepository.getSelectedListId();
    }

    //firebase data
    public void init() {
        listsRepository.init();
    }

    public LiveData<Lists> getLists() {
        return listsRepository.getLists();
    }

    public void generateNewList(int listId) {
        listsRepository.generateNewList(listId);
    }
}

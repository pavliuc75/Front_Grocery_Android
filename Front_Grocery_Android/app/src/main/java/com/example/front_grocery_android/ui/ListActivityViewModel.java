package com.example.front_grocery_android.ui;

import androidx.lifecycle.ViewModel;

import com.example.front_grocery_android.repository.ListsRepository;

public class ListActivityViewModel extends ViewModel {
    private final ListsRepository listsRepository;

    public ListActivityViewModel() {
        listsRepository = ListsRepository.getInstance();
    }

    public void setSelectedListId(int listId) {
        listsRepository.setSelectedListId(listId);
    }

    public int getSelectedListId() {
        return listsRepository.getSelectedListId();
    }
}

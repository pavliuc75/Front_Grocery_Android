package com.example.front_grocery_android.Repository;

public class CachedList {
    private static CachedList instance;
    private int cachedList;

    private CachedList() {
    }

    public static synchronized CachedList getInstance() {
        if (instance == null) {
            instance = new CachedList();
        }
        return instance;
    }

    public int setCachedList(int listId) {
        this.cachedList = listId;
        return this.cachedList;
    }

    public int getCachedList() {
        return cachedList;
    }
}

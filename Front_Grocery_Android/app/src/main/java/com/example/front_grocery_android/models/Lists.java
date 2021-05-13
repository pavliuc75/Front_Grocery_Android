package com.example.front_grocery_android.models;

import java.util.ArrayList;

public class Lists {
    public ArrayList<List> lists;

    public Lists(ArrayList<List> lists) {
        this.lists = lists;
    }

    public ArrayList<List> getBody() {
        return lists;
    }

    public void setBody(ArrayList<List> lists) {
        this.lists = lists;
    }
}

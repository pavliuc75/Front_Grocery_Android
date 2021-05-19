package com.example.front_grocery_android.models;

import java.util.Comparator;

public class Item {
    public int id;
    public String name;
    public String details;
    public boolean isCompleted;
    public int quantity;
    public double weight;
    public String unit;

    public static Comparator<Item> AZComparator = (o1, o2) -> o1.name.compareTo(o2.name);

    public static Comparator<Item> ZAComparator = (o1, o2) -> o2.name.compareTo(o1.name);

    public static Comparator<Item> NewOldComparator = (o1, o2) -> o2.id - o1.id;

    public static Comparator<Item> OldNewComparator = (o1, o2) -> o1.id - o2.id;

}

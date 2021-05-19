package com.example.front_grocery_android.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;


class ItemTest {
    private ArrayList<Item> list;
    private ArrayList<Item> expectedList;

    @BeforeEach
    public void setUp() {
        list = new ArrayList<>();
        expectedList = new ArrayList<>();
    }

    @Test
    public void sortAz() {
        Item a = new Item();
        a.name = "a";
        Item b = new Item();
        b.name = "b";
        Item c = new Item();
        c.name = "c";
        list.add(a);
        list.add(c);
        list.add(b);
        Collections.sort(list, Item.AZComparator);
        expectedList.add(a);
        expectedList.add(b);
        expectedList.add(c);
        assertArrayEquals(list.toArray(), expectedList.toArray());
    }

    @Test
    public void sortZa() {
        Item a = new Item();
        a.name = "a";
        Item b = new Item();
        b.name = "b";
        Item c = new Item();
        c.name = "c";
        list.add(a);
        list.add(b);
        list.add(c);
        Collections.sort(list, Item.ZAComparator);
        expectedList.add(c);
        expectedList.add(b);
        expectedList.add(a);
        assertArrayEquals(list.toArray(), expectedList.toArray());
    }

    @Test
    public void sortOldNew() {
        Item a = new Item();
        a.id = 1;
        Item b = new Item();
        b.id = 2;
        Item c = new Item();
        c.id = 3;
        list.add(c);
        list.add(b);
        list.add(a);
        Collections.sort(list, Item.OldNewComparator);
        expectedList.add(a);
        expectedList.add(b);
        expectedList.add(c);
        assertArrayEquals(list.toArray(), expectedList.toArray());
    }

    @Test
    public void sortNewOld() {
        Item a = new Item();
        a.id = 1;
        Item b = new Item();
        b.id = 2;
        Item c = new Item();
        c.id = 3;
        list.add(a);
        list.add(b);
        list.add(c);
        Collections.sort(list, Item.NewOldComparator);
        expectedList.add(c);
        expectedList.add(b);
        expectedList.add(a);
        assertArrayEquals(list.toArray(), expectedList.toArray());
    }

    @Test
    public void sortAzOneElement() {
        Item a = new Item();
        a.name = "a";
        list.add(a);
        Collections.sort(list, Item.AZComparator);
        expectedList.add(a);
        assertArrayEquals(list.toArray(), expectedList.toArray());
    }

    @Test
    public void sortAzZeroElements() {
        Collections.sort(list, Item.AZComparator);
        assertArrayEquals(list.toArray(), expectedList.toArray());
    }

    @Test
    public void sortAzNullList() {
        list = null;
        Assertions.assertThrows(NullPointerException.class, () -> {
            Collections.sort(list, Item.AZComparator);
        });
    }
}
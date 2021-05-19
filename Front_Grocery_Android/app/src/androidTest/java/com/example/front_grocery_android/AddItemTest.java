package com.example.front_grocery_android;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.front_grocery_android.ui.ListActivity;
import com.example.front_grocery_android.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


//!!!
//this should be only tested when a list id is saved into device memory
//once you enter any list without going back to MainActivity, device memory is set
//!!!

@RunWith(AndroidJUnit4.class)
public class AddItemTest {
    @Rule
    public ActivityTestRule<MainActivity> mListActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void AddItemUi() throws Exception {
        onView(withId(R.id.fab_add))
                .perform(click());
        onView(withId(R.id.edit_text_add_item_name))
                .perform(typeText("espressoTest"));
        onView(withId(R.id.edit_text_add_item_details))
                .perform(click());
        onView(withId(R.id.edit_text_add_item_details))
                .perform(typeText("espressoTestDetails"));
        onView(withText("Add")).perform(click());

        //alert dialog exit
        onView(withId(R.id.textView_list_id))
                .check(matches(isDisplayed()));
    }
}

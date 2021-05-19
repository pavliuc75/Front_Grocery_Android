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

//!!!
//this should be only tested when no list id is saved into device memory
//to clear local memory, press switch list button in the list activity
//!!!

@RunWith(AndroidJUnit4.class)
public class GoToListTest {
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void goToListUi() throws Exception {
        onView(withId(R.id.editText_list_id))
                .perform(click());
        onView(withId(R.id.editText_list_id))
                .perform(typeText("1002"));
        onView(withId(R.id.button_go_to_list))
                .perform(click());

        //checks if the textView from ListActivity is displayed (meaning that the activity has been started)
        onView(withId(R.id.textView_list_id))
                .check(matches(isDisplayed()));
    }
}

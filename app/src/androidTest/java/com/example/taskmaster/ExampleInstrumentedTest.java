package com.example.taskmaster;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import android.content.Context;

import androidx.room.Room;
import androidx.test.espresso.PerformException;
//import androidx.test.espresso.contrib.RecyclerViewActions;
//import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.Set;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void checkComponentsIsVisible() {
        onView(withText("هلا والله")).check(matches(isDisplayed()));
        onView(withId(R.id.myTasks)).check(matches(isDisplayed()));
        onView(withId(R.id.settingButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addTaskButton)).check(matches(isDisplayed()));
        onView(withId(R.id.allTasksRecyclerView)).check(matches(isDisplayed()));
    }
    @Test
    public void settingActivityTests() {
        onView(withId(R.id.settingButton)).perform(click());
        onView(withId(R.id.userNameInput))
                .perform(typeText("saify"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());
        onView(withId(R.id.userNameView)).check(matches(withText("saify\'s Tasks")));
    }
    @Test
    public void recyclerViewTest(){
        onView(withId(R.id.allTasksRecyclerView))
                .perform(actionOnItemAtPosition(0, click()));
        TaskDataBase db = Room.databaseBuilder(getApplicationContext(), TaskDataBase.class, "tasks-db").allowMainThreadQueries().build();
        TaskDao taskDao = db.taskDao();
//        onView(withText(taskDao.getAll().get(0).title)).check(matches(isDisplayed()));
        onView(withText(taskDao.getAll().get(0).title));
    }

    @Test
    public void itemWithTextInRecycler() {
        // Attempt to scroll to an item that contains the special text.
        onView(withId(R.id.allTasksRecyclerView))
                // scrollTo will fail the test if no item matches.
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText("ayyoub"))
                ));
    }
    @Test
    public void addTaskActivityTests() {
        onView(withId(R.id.addTaskButton)).perform(click());
        onView(withId(R.id.taskTitle))
                .perform(typeText("batool"), closeSoftKeyboard());
        onView(withId(R.id.submitButton)).perform(click());
        onView(withId(R.id.allTasksRecyclerView)).perform(RecyclerViewActions.scrollTo(
                hasDescendant(withText("batool"))
        ));
    }

//
    //Setting

//    @Rule
//    public ActivityScenarioRule<SettingPage> activityRule =
//            new ActivityScenarioRule<>(SettingPage.class);

//    @Test
//    public void checkSaveButton(){
//        onView(withId(R.id.saveButton)).perform(click());
//    }
//
//    @Test
//    public void mainActivityTests(){
//        onView(withId(R.id.userNameInput))
//                .perform(typeText("saify"), closeSoftKeyboard());
//        onView(withId(R.id.saveButton)).perform(click());
//        onView(withId(R.id.userNameView)).check(matches(withText("saify\'s Tasks")));
//    }


//    @Test
//    public void scrollToItemBelowFold_checkItsText() {
//        // First scroll to the position that needs to be matched and click on it.
//        onView(ViewMatchers.withId(R.id.recyclerView))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BELOW_THE_FOLD, click()));
//
//        // Match the text in an item below the fold and check that it's displayed.
//        String itemElementText = getApplicationContext().getResources().getString(
//                R.string.item_element_text) + String.valueOf(ITEM_BELOW_THE_FOLD);
//        onView(withText(itemElementText)).check(matches(isDisplayed()));
//    }


}
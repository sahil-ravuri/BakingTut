package com.sahil.bakingtut;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test() {
        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.recyclerview),
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        1)),
                        1),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.steps_list),
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3)),
                        5),
                        isDisplayed()));
        linearLayout2.perform(click());

        ViewInteraction linearLayout3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.steps_list),
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3)),
                        1),
                        isDisplayed()));
        linearLayout3.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

package com.example.tubes1.Kiriman


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.tubes1.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class EditKirimanActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(EditKirimanActivity::class.java)

    @Test
    fun editKirimanActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(500)

        val materialButton = onView(
            allOf(
                withId(R.id.SaveButon), withText("Save"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.card.MaterialCardView")),
                        0
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
        onView(isRoot()).perform(waitFor(5000))

        val textInputEditText = onView(
            allOf(
                withId(R.id.OutDBNamaBarangEdit),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.card.MaterialCardView")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("Makanan ringan"), closeSoftKeyboard())

        val materialButton1 = onView(
            allOf(
                withId(R.id.SaveButon), withText("Save"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.card.MaterialCardView")),
                        0
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        materialButton1.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val appCompatEditText = onView(
            allOf(
                withId(R.id.OutDBBeratEdit),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.card.MaterialCardView")),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("300"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.SaveButon), withText("Save"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.card.MaterialCardView")),
                        0
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val appCompatAutoCompleteTextView = onView(
            allOf(
                withId(R.id.OutDBPecahBelahEdit),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.card.MaterialCardView")),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatAutoCompleteTextView.perform(replaceText("Tidak"), closeSoftKeyboard())

        val materialButton3 = onView(
            allOf(
                withId(R.id.SaveButon), withText("Save"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.card.MaterialCardView")),
                        0
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.OutDBCoverEdit),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.card.MaterialCardView")),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("bubblewarp"), closeSoftKeyboard())
        onView(isRoot()).perform(waitFor(3000))

        val appCompatAutoCompleteTextView2 = onView(
            allOf(
                withId(R.id.OutDBAsuransiEdit),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.card.MaterialCardView")),
                        0
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        appCompatAutoCompleteTextView2.perform(replaceText("Tidak"), closeSoftKeyboard())
        onView(isRoot()).perform(waitFor(3000))

        val materialButton5 = onView(
            allOf(
                withId(R.id.SaveButon), withText("Save"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.card.MaterialCardView")),
                        0
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())
        onView(isRoot()).perform(waitFor(3000))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for " + delay + "millisecods"
            }

            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(delay)
            }
        }
    }
}

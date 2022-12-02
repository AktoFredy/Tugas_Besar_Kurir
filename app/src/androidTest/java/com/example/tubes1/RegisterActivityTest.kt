package com.example.tubes1


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun registerActivityTest() {
            // Added a sleep statement to match the app's execution delay.
            // The recommended way to handle such scenarios is to use Espresso idling resources:
            // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
            Thread.sleep(500)

        val materialButton = onView(
            allOf(
                withId(R.id.btnRegis), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_1),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
        //onView(isRoot()).perform(waitFor(3000))

        val textInputEditText = onView(
            allOf(
                withId(R.id.edtemail),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_email),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText.perform(scrollTo(), replaceText("aka"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btnRegis), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_1),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
        //onView(isRoot()).perform(waitFor(3000))

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.edtemail), withText("aka"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_email),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText2.perform(scrollTo(), click())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.edtemail), withText("aka"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_email),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText3.perform(scrollTo(), replaceText("aka@gmail.com"))

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.edtemail), withText("aka@gmail.com"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_email),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(closeSoftKeyboard())

        val materialButton3 = onView(
            allOf(
                withId(R.id.btnRegis), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_1),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
        //onView(isRoot()).perform(waitFor(3000))

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.edtusername),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_username),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText5.perform(scrollTo(), replaceText("adaY"), closeSoftKeyboard())


        val materialButton4 = onView(
            allOf(
                withId(R.id.btnRegis), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_1),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
        //onView(isRoot()).perform(waitFor(3000))

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.edtusername), withText("adaY"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_username),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText6.perform(scrollTo(), click())

        val textInputEditText7 = onView(
            allOf(
                withId(R.id.edtusername), withText("adaY"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_username),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText7.perform(scrollTo(), replaceText("adaK"))

        val textInputEditText8 = onView(
            allOf(
                withId(R.id.edtusername), withText("adaK"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_username),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText8.perform(closeSoftKeyboard())

        val materialButton5 = onView(
            allOf(
                withId(R.id.btnRegis), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_1),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())
        //onView(isRoot()).perform(waitFor(3000))

        val textInputEditText9 = onView(
            allOf(
                withId(R.id.edtusername), withText("adaK"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_username),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText9.perform(scrollTo(), click())

        val textInputEditText10 = onView(
            allOf(
                withId(R.id.edtusername), withText("adaK"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_username),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText10.perform(scrollTo(), replaceText("adaG"))

        val textInputEditText11 = onView(
            allOf(
                withId(R.id.edtusername), withText("adaG"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_username),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText11.perform(closeSoftKeyboard())

        val materialButton6 = onView(
            allOf(
                withId(R.id.btnRegis), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_1),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())
        //onView(isRoot()).perform(waitFor(3000))

        val textInputEditText12 = onView(
            allOf(
                withId(R.id.edtpassword),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_password1),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText12.perform(scrollTo(), replaceText("123"), closeSoftKeyboard())

        val textInputEditText13 = onView(
            childAtPosition(
                childAtPosition(
                    withId(R.id.input_confirm_password),
                    0
                ),
                0
            )
        )
        textInputEditText13.perform(scrollTo(), replaceText("12"), closeSoftKeyboard())

        val materialButton7 = onView(
            allOf(
                withId(R.id.btnRegis), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_1),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton7.perform(click())
        //onView(isRoot()).perform(waitFor(3000))

        val textInputEditText14 = onView(
            allOf(
                withText("12"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_confirm_password),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText14.perform(scrollTo(), click())

        val textInputEditText15 = onView(
            allOf(
                withText("12"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_confirm_password),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText15.perform(scrollTo(), replaceText("123"))

        val textInputEditText16 = onView(
            allOf(
                withText("123"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_confirm_password),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText16.perform(closeSoftKeyboard())

        val materialButton8 = onView(
            allOf(
                withId(R.id.btnRegis), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_1),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton8.perform(click())
        //onView(isRoot()).perform(waitFor(3000))

        val textInputEditText17 = onView(
            allOf(
                withId(R.id.edttgllahir),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_tanggal_lahir),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText17.perform(scrollTo(), click())

        val textInputEditText18 = onView(
            allOf(
                withId(R.id.edttgllahir),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_tanggal_lahir),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText18.perform(scrollTo(), click())

        val materialButton9 = onView(
            allOf(
                withId(android.R.id.button1), withText("Oke"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        materialButton9.perform(scrollTo(), click())

        val materialButton11 = onView(
            allOf(
                withId(R.id.btnRegis), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_1),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton11.perform(click())
        //onView(isRoot()).perform(waitFor(3000))

        val textInputEditText19 = onView(
            allOf(
                withId(R.id.edttlp),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_tlp),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText19.perform(scrollTo(), replaceText("085669865"), closeSoftKeyboard())

        val materialButton12 = onView(
            allOf(
                withId(R.id.btnRegis), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_1),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton12.perform(click())
        //onView(isRoot()).perform(waitFor(3000))


        val textInputEditText20 = onView(
            allOf(
                withId(R.id.edttlp), withText("085669865"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_tlp),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText20.perform(scrollTo(), click())

        val textInputEditText21 = onView(
            allOf(
                withId(R.id.edttlp), withText("085669865"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_tlp),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText21.perform(scrollTo(), replaceText("085669865451"))

        val textInputEditText22 = onView(
            allOf(
                withId(R.id.edttlp), withText("085669865451"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_tlp),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText22.perform(closeSoftKeyboard())


        val materialButton13 = onView(
            allOf(
                withId(R.id.btnRegis), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_1),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton13.perform(click())
        //onView(isRoot()).perform(waitFor(3000))
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
}

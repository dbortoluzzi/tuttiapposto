package it.dbortoluzzi.tuttiapposto.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import it.dbortoluzzi.data.R
import it.dbortoluzzi.tuttiapposto.ui.activities.LoginActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain


@HiltAndroidTest
class LoginActivityTest {

    val hiltRule = HiltAndroidRule(this)

    val loginActivityRule = ActivityTestRule(LoginActivity::class.java, true, false)

    @get:Rule
    var testRules = RuleChain
                        .outerRule(hiltRule)
                        .around(loginActivityRule)

    @Before
    fun init() {
        hiltRule.inject();
        loginActivityRule.launchActivity(null)
    }

    @Test
    fun testMainActivity() {
        onView(withId(R.id.LoginBtn)).run {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        onView(withId(R.id.LoginEmail)).run {
            perform(typeText("danieleb88@gmail.com"), closeSoftKeyboard())
        }
        onView(withId(R.id.LoginPassword)).run {
            perform(typeText("123456"), closeSoftKeyboard())
        }
        onView(withId(R.id.LoginBtn)).run {
            perform(click())
        }
        // TODO: change with Idling Resource and Registry
        Thread.sleep(1000)
        onView(withId(R.id.main_nav_host)).run {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        onView(withId(R.id.activityOptions)).run {
            check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
            onView(withId(R.id.main_drawer_layout)).perform(DrawerActions.open())
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            onView(withId(R.id.main_drawer_layout)).perform(DrawerActions.close())
            check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        }

    }
}

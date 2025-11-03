package com.mobilitydevai.simpsonsinfo.view.simpsons

import android.util.Log
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test


class SimpsonsEmptyListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun simpsonsEmptyListScreen() {
        composeTestRule.setContent {
            SimpsonsEmptyListScreen(onGetCharacters = { mockFun() })
        }
        composeTestRule
            .onNodeWithContentDescription(label = "doh_homer_simpson", useUnmergedTree = true)
            .assertExists()

        composeTestRule
            .onNodeWithText("opssss", useUnmergedTree = true)
            .assertTextContains("opssss")

        composeTestRule
            .onNodeWithTag(testTag = "simpsonsNoResults", useUnmergedTree = true)
            .assertTextContains("No results were found. Please try again.", ignoreCase = true)

        composeTestRule
            .onNodeWithTag(testTag = "getCharacters", useUnmergedTree = true)
            .assertTextContains("get characters", ignoreCase = true)

        composeTestRule
            .onNodeWithContentDescription(label = "Download characters", useUnmergedTree = true)
            .assertExists()

        Thread.sleep(5000)
    }

    fun mockFun() {
        Log.i("info", "Get characters function called")
    }

}
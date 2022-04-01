package com.example.tddkata.main

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.logintdd.main.LoginView
import com.example.tddkata.ui.theme.TDDKataTheme
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    fun SemanticsNodeInteraction.imeActionValue(): String? {
        for ((key, value) in fetchSemanticsNode().config) {
            if (key.name == "ImeAction") {
                return value.toString()
            }
        }
        return null
    }

    fun SemanticsNodeInteraction.currentText(): String? {
        for ((key, value) in fetchSemanticsNode().config) {
            if (key.name == "EditableText") {
                return value.toString()
            }
        }
        return null
    }

    @Before
    fun setup() {
        composeTestRule.setContent {
            TDDKataTheme {
                LoginView()
            }
        }
    }

    @Test
    fun loginViewIsLoaded() {
        val username = composeTestRule.onNodeWithText("Username")
        val password = composeTestRule.onNodeWithText("Password")
        val login = composeTestRule.onNodeWithText("Login")

        username.assertIsDisplayed()
        password.assertIsDisplayed()
        login.assertIsDisplayed()

        login.assertIsEnabled()

        assertEquals("Next", username.imeActionValue())
        assertEquals("Done", password.imeActionValue())

        assertTrue(username.currentText().isNullOrEmpty())
        assertTrue(password.currentText().isNullOrEmpty())
    }

    @Test
    fun credentialsAreAddingCorrectly() {
        val username = composeTestRule.onNodeWithText("Username")
        val password = composeTestRule.onNodeWithText("Password")

        username.performTextInput("john")
        password.performTextInput("1234")

        assertEquals("john", username.currentText())
        assertEquals("1234", password.currentText())
    }

}
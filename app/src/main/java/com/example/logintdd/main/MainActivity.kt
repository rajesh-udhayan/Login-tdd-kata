package com.example.logintdd.main

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.tddkata.ui.theme.TDDKataTheme
import com.example.tddkata.ui.theme.Typography
import kotlinx.coroutines.launch

private lateinit var mainViewModel: MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TDDKataTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LoginView()
                }
            }
        }
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Welcome", style = Typography.h3)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextInputsAndButton()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextInputsAndButton() {
    val focusRequester = remember {
        FocusRequester()
    }

    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    OutlinedTextField(value = username,
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        label = { Text(text = "Username") },
        onValueChange = {
            username = it
        },
        keyboardActions = KeyboardActions(
            onNext = {
                focusRequester.requestFocus()
            }
        )
    )

    OutlinedTextField(
        value = password,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
        label = { Text(text = "Password") },
        onValueChange = {
            password = it
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        )
    )

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Button(onClick = {
        val validation: CredentialValidation =
            mainViewModel.validateCredentials(username.text, password.text)
        val msg: String
        when (validation) {
            is CredentialValidation.EmptyUserNameAndPassword -> msg =
                "Empty Username and Password"
            is CredentialValidation.EmptyUserName -> msg = "Empty Username"
            is CredentialValidation.EmptyPassword -> msg = "Empty Password"
            is CredentialValidation.InvalidUserNameAndPassword -> msg = "Invalid credentials"
            is CredentialValidation.InvalidUserName -> msg = "Invalid Username"
            is CredentialValidation.InvalidPassword -> msg = "Invalid Password"
            is CredentialValidation.ValidCredentials -> msg = "Success!"
        }
        scope.launch {
            snackbarHostState.showSnackbar(msg,duration = SnackbarDuration.Short)
        }
        keyboardController?.hide()
    }) {
        Text(text = "Login", color = Color.White)
    }
    SnackbarHost(hostState = snackbarHostState)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TDDKataTheme {
        Column {
            LoginView()
        }
    }
}
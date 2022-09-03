package com.example.helloworld2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.solver.state.helpers.AlignHorizontallyReference
import com.example.helloworld2.ui.theme.HelloWorld2Theme
import androidx.compose.material.Text as Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloWorld2Theme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    var click = remember { mutableStateOf(false) }
    var text = remember { mutableStateOf(TextFieldValue("")) }
    var name = remember { mutableStateOf("") }
    var showSnackbar = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val notify = MyNotifications(context = context,
        "Update name",
        "This is Notification for updating the name")

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(
        rememberScrollState())) {
        Box(
            Modifier
                .background(color = MaterialTheme.colors.secondary)
                .size(LocalConfiguration.current.screenWidthDp.dp, 400.dp)
                .padding(5.dp)
        ) {
            Text(
                text = if (name.value != "") (stringResource(R.string.hello) + " " + name.value)
                else (if (click.value) stringResource(R.string.helloWorld)
                else stringResource(R.string.goodbye)),
                modifier = Modifier
                    .shadow(elevation = 2.dp)
                    .align(alignment = Alignment.TopCenter)
            )
            Box(
                Modifier
                    .background(color = MaterialTheme.colors.onSurface)
                    .size((LocalConfiguration.current.screenWidthDp * 0.5).dp, 200.dp)
                    .align(alignment = Alignment.BottomCenter)
                    .padding(5.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.two), color = MaterialTheme.colors.surface,
                    modifier = Modifier
                        .shadow(elevation = 8.dp)
                        .align(alignment = Alignment.Center)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = text.value,
            onValueChange = { text.value = it },
            label = { Text(stringResource(R.string.input)) },
            singleLine = true,
            modifier = Modifier.background(color = MaterialTheme.colors.primaryVariant)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                click.value = !click.value
                showSnackbar.value = true
                if (text.value.text != "") {
                    name.value = text.value.text
                    text.value = TextFieldValue("")
                }
            }) {
            Text(text = stringResource(id = R.string.click))
        }
        if (showSnackbar.value) {
            Snackbar(action = {
                TextButton({
                    Log.d("Hello App", "onClick Action...")
                    showSnackbar.value = false
                    notify.notification()
                }) {
                    Text(stringResource(R.string.undo))
                }
            }) {
                Text(stringResource(R.string.snackbarMessage))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HelloWorld2Theme {
        Greeting()
    }
}
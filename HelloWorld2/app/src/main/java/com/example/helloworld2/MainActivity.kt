package com.example.helloworld2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helloworld2.ui.theme.ContentColorComponent
import com.example.helloworld2.ui.theme.HelloWorld2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloWorld2Theme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.app_name)) },
                            navigationIcon = {
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(Icons.Filled.Menu, contentDescription = null)
                                }
                            },
                            actions = {
                                // RowScope here, so these icons will be placed horizontally
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = "Localized description"
                                    )
                                }
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(
                                        Icons.Filled.Search,
                                        contentDescription = "Localized description"
                                    )
                                }
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(
                                        Icons.Filled.MoreVert,
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        )
                    }
                ) {
                    // Screen content

                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        Greeting()
                    }
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

    Column() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp,
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Image(painter = painterResource(id = R.drawable.picture),
                    contentDescription = "natural",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize())
                Box(
                    Modifier
                        .background(color = MaterialTheme.colors.secondary)
                        .padding(5.dp)
                        .align(CenterHorizontally)
                ) {
                    Column() {
                        Text(
                            text = if (name.value != "") (stringResource(R.string.hello) + " " + name.value)
                            else (if (click.value) stringResource(R.string.helloWorld)
                            else stringResource(R.string.goodbye)),
                            modifier = Modifier
                                .shadow(elevation = 2.dp)
                                .align(CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Just another Material design lab")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                ContentColorComponent(contentColor = Color.Yellow) {
                    TextField(
                        value = text.value,
                        onValueChange = { text.value = it },
                        label = {
                            Text(stringResource(id = R.string.input))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp)
                            .background(MaterialTheme.colors.secondary)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp)
                ) {
                    Button(
                        onClick = {
                            click.value = !click.value
                            if (text.value.text != "") {
                                name.value = text.value.text
                                text.value = TextFieldValue("")
                            }
                        }) {
                        Icon(
                            Icons.Filled.Refresh,
                            contentDescription = "Localized description",
                            Modifier.padding(end = 8.dp)
                        )
                        Text(text = stringResource(id = R.string.click))
                    }
                    TextButton(
                        onClick = { }
                    ) {
                        Text(text = "ANOTHER BUTTON")
                    }
                }
            }
        }
        OutlinedButton(onClick = { /*TODO*/ }, shape = CircleShape, modifier = Modifier.align(
            alignment = Alignment.End)) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "content description")
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

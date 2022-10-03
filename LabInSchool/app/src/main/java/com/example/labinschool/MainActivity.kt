package com.example.labinschool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.labinschool.ui.theme.LabInSchoolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabInSchoolTheme {

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Simple TopAppBar") },
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
                                        Icons.Filled.Favorite,
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        )
                    }
                ) {
                    // Screen content

                    // A surface container using the 'background' color from the theme
                    Surface(
//                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
//                        Greeting("Android")
                        CardDemo()
                    }
                }


            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LabInSchoolTheme {
        Greeting("Android")
    }
}

@Composable
fun CardDemo() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { },
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = "What a superduper car",
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.h4
            )
            Text(
                buildAnnotatedString {
                    append("welcome to ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.W900,
//                            color = Color(0xFF4552B8)
                            color = MaterialTheme.colors.secondary
                        )
                    ) {
                        append("Jetpack Compose Playground")
                    }
                }
            )
            Text(
                buildAnnotatedString {
                    append("Now you are in the ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) {
                        append("Card")
                    }
                    append(" section")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /*TODO*/ }, shape = MaterialTheme.shapes.small.copy(
                bottomStart = ZeroCornerSize,
                bottomEnd = ZeroCornerSize,
                topEnd = CornerSize(16.dp),
                topStart = ZeroCornerSize
            )) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text("Like")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardDemo() {
    LabInSchoolTheme {
        CardDemo()
    }
}
package com.example.w3_d3_internalexternalstorage

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.w3_d3_internalexternalstorage.ui.theme.W3_d3_InternalExternalStorageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            W3_d3_InternalExternalStorageTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting(application)
                }
            }
        }
    }
}

@Composable
fun Greeting(app: Application) {
    val fileName = "myFile.txt"
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(readFile(fileName = fileName, app = app)) }
    Column() {
        TextField(value = text,
            onValueChange = { text = it },
            label = { Text("Input") },
            singleLine = true)
        Button(onClick = {
            app.openFileOutput(fileName, Context.MODE_APPEND).use {
                it.write("${text.text}\n".toByteArray())
            }
            content = readFile(fileName = fileName, app = app)
            text = TextFieldValue("")
        }) {
            Text(text = "Write to file")
        }
        LazyColumn() {
            content?.let {
                items(it.split("\n")) { item ->
                    Text(text = item,
                        modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

fun readFile(fileName: String, app: Application): String {
    var string = ""
    try {
        string = app.openFileInput(fileName)?.bufferedReader().use {
            it?.readText() ?: "Read file failed"
        }
    } catch (error: Exception) {
        Log.d("internalFile", "readFile: file not found")
    }
    return string
}


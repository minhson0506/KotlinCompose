package com.example.w3_d5_saf

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.example.w3_d5_saf.ui.theme.W3_d5_safTheme
import java.io.File

class MainActivity : ComponentActivity() {
    val TAG = "w3_d5_saf"

    var list = mutableStateListOf<DocumentFile>()

    companion object {
        private lateinit var uri: Uri
//        fun isInitialized(): Boolean = ::uri.isInitialized
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.also { data ->
                    uri = data.data
                        ?: return@registerForActivityResult contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    Log.d(TAG, "file uri: $uri")
                    val documentsTree = DocumentFile.fromTreeUri(this, uri)
                    val childDocuments = documentsTree?.listFiles()
                    childDocuments?.forEach {
                        if (it.isDirectory)
                            Log.d(TAG, "onCreate: folder ${it.name}")
                        else Log.d(TAG, "onCreate: file ${it.name}")
                        list.add(it)
                    }

                }
            }
        }

    private fun openTree() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startForResult.launch(intent)
    }

    // create file for listing
    private fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("text/plain"))
            } else {
                type = "plain/text"
            }
            putExtra(Intent.EXTRA_TITLE, "sharedFile.txt")
        }
        startForResult.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            W3_d5_safTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Column() {
                        Button(onClick = { openTree() }) {
                            Text(text = "openDirectory")
                        }
                        Button(onClick = { createFile() }) {
                            Text(text = "create File")
                        }
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())) {
                            list?.forEach {
                                it?.let { it1 ->
                                    Text(text = if (it1.isDirectory) "Folder: ${it1.name}"
                                    else "File: ${it1.name}")
                                }
                            }
                        }
                    }

                }
            }
        }

    }
}

@Composable
fun Greeting() {
    val TAG = "w3_d5_saf"
}

fun listFiles(directory: File) {
    val TAG = "w3_d5_saf"
    Log.d(TAG, "listFiles: start to list")
    val files = directory.listFiles()
    if (files != null) {
        Log.d(TAG, "listFiles: notnull")
        for (file in files) {
            if (file != null) {
                if (file.isDirectory) {
                    listFiles(file)
                } else {
                    Log.d(TAG, "listFiles: ${file.absolutePath}")
                }
            }
        }
    } else
        Log.d(TAG, "listFiles: null")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    W3_d5_safTheme {
        Greeting()
    }
}
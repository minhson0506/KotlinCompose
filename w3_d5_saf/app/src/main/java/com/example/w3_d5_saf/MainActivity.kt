package com.example.w3_d5_saf

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.documentfile.provider.DocumentFile
import com.example.w3_d5_saf.ui.theme.W3_d5_safTheme

class MainActivity : ComponentActivity() {
    val TAG = "w3_d5_saf"
    var list = mutableStateListOf<DocumentFile>()

    companion object {
        private lateinit var uri: Uri
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
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())) {
                            list.forEach {
                                Text(text = if (it.isDirectory) "Folder: ${it.name}"
                                else "File: ${it.name}")
                            }
                        }
                    }

                }
            }
        }

    }
}
package com.example.w2_d2_camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.w2_d2_camera.ui.theme.W2_d2_cameraTheme
import java.io.File

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            W2_d2_cameraTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    TakePicture()
                }
            }
        }
    }
}

@Composable
fun TakePicture() {
    val TAG = "w2_d2_camera"

    val fileName = "temp_photo"
    val imgPath = LocalContext.current.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File.createTempFile(fileName, ".jpg", imgPath)

    val photoUri: Uri =
        FileProvider.getUriForFile(LocalContext.current, "com.example.w2_d2_camera.fileprovider", imageFile)
    val currentPhotoPath = imageFile!!.absolutePath

    val result = remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) result.value = BitmapFactory.decodeFile(currentPhotoPath)
        else Log.i(TAG, "Picture not taken")
    }

    Column() {
        Button(onClick = { launcher.launch(photoUri) }) {
            Text(text = "Take a picture")
        }
        Spacer(modifier = Modifier.height(16.dp))
        result.value?.let { image ->
            Image(image.asImageBitmap(),
                null,
                modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    W2_d2_cameraTheme {
        TakePicture()
    }
}
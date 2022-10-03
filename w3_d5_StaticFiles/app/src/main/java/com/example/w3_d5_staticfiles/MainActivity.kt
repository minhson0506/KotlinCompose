package com.example.w3_d5_staticfiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.documentfile.provider.DocumentFile
import com.example.w3_d5_staticfiles.ui.theme.W3_d5_StaticFilesTheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            W3_d5_StaticFilesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello ${factoriel(4)}!")
}

fun factoriel(n: Int):Int {
    if (n <=0) throw Error()
//    var fac = 1
//    for (i in n downTo 1) {
//        fac *= i
//    }
//    return fac

    if (n==1) return n
    return n * factoriel(n-1)

}

fun trees(file: DocumentFile){

}

fun tree(file: File): File {
    if (file.isDirectory) tree(file)
    else // print file name and return null
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    W3_d5_StaticFilesTheme {
        Greeting("Android ${factoriel(4)}")
    }
}
package com.example.w2_d2_retrofit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.w2_d2_retrofit.ui.theme.W2_d2_retrofitTheme

class MainActivity : ComponentActivity() {
    private var presidents = DataProvider.presidents
    override fun onCreate(savedInstanceState: Bundle?) {
        val model = SearchViewModel()
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            W2_d2_retrofitTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavHost(navController = navController, startDestination = "presidentList") {
                        composable("presidentList") {
                            PresidentList(presidents = presidents, navController = navController)
                        }
                        composable("details/{presidentName}") { navBackStackEntry ->
                            Greeting(name = navBackStackEntry.arguments?.getString("presidentName")!!,
                                model = model)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PresidentList(presidents: List<President>, navController: NavController) {
    val TAG = "w2_d2_retrofit"
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        presidents.forEach { president ->
            Text(
                president.name,
                modifier = Modifier
                    .selectable(
                        selected = true,
                        onClick = {
                            Log.i(TAG, "Button clicked! ( ${president.name})")
                            navController.navigate("details/${president.name}")
                        })
            )
        }
    }
}

@Composable
fun Greeting(name: String, model: SearchViewModel) {
    model.getNumberOfSearch(name)
    val number: Int? by model.numbersOfSearch.observeAsState(null)
    if (number != null) Text(text = "Number of link is $number")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    W2_d2_retrofitTheme {
        val presidents = DataProvider.presidents
        val navController = rememberNavController()
        PresidentList(presidents = presidents, navController = navController)
    }
}

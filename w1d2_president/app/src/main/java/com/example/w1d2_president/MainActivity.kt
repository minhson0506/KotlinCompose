package com.example.w1d2_president

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.w1d2_president.ui.theme.W1d2_presidentTheme

class MainActivity : ComponentActivity() {
    private var presidents = DataProvider.presidents
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            W1d2_presidentTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavHost(navController = navController, startDestination = "presidentList") {
                        composable("presidentList") {
                            PresidentList(presidents = presidents, navController = navController)
                        }
                        composable("details/{presidentName}") {
                            navBackStackEntry -> Greeting(name = navBackStackEntry.arguments?.getString("presidentName")!!)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PresidentList(presidents: List<President>, navController: NavController) {

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        presidents.forEach { president ->
            Text(
                president.name,
                modifier = Modifier
                    .selectable(
                        selected = true,
                        onClick = {
                            Log.i("DBG", "Button clicked! ( ${president.name})")
                            navController.navigate("details/${president.name}")
                        })
            )
        }
    }
}


@Composable
fun Greeting(name: String) {
    var president = DataProvider.presidents.find { it.name == name }
    if (president != null) {
        Card() {
            Column() {
                Text(text = "President: " + president.name)
                Text(text = "Start date: " + president.start)
                Text(text = "End date: " + president.end)
                Text(text = "Description: " + president.desc)
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    W1d2_presidentTheme {
        var presidents = DataProvider.presidents
        val navController = rememberNavController()
        PresidentList(presidents = presidents, navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun SecondPreview() {
    W1d2_presidentTheme {
        Greeting(name = "Kaarlo Stahlberg")
    }
}
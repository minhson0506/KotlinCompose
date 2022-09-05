package com.example.w1_d5_roomdatabase

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.w1_d5_roomdatabase.ui.theme.W1_d5_RoomDatabaseTheme

class MainActivity : ComponentActivity() {
    companion object {
        private lateinit var movieViewModel: MovieViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel = MovieViewModel(application = application)

        setContent {
            W1_d5_RoomDatabaseTheme {
                // A surface container using the 'background' color from the theme
                MainAppNav(movieViewModel, application)
            }
        }
    }
}

@Composable
fun InsertMovie(movieViewModel: MovieViewModel) {
    var title by remember { mutableStateOf("") }
    var year by remember { mutableStateOf(0) }
    var director by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        TextField(value = title, onValueChange = { title = it }, label = { Text(text = "Title") })
        TextField(value = (if (year != 0) year.toString() else ""),
            onValueChange = { year = it.toIntOrNull() ?: 0 },
            label = { Text(text = "Year") })
        TextField(value = director,
            onValueChange = { director = it },
            label = { Text(text = "Director") })
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            movieViewModel.insert(Movie(0,
                title = title,
                year = year,
                director = director))
        }) {
            Text(text = "INSERT MOVIE")
        }
    }
}

@Composable
fun ListMovies(movieViewModel: MovieViewModel, navController: NavController) {
    var movieList = movieViewModel.getALl().observeAsState(listOf())
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        item {
            Row() {
                Text(text = " MOVIES")
            }
        }

        items(movieList.value) {
            Text(text = "Movie: ${it.title}", Modifier.clickable {
                navController.navigate("details/${it.id}")
            })
        }
    }
}

@Composable
fun InsertActor(actorViewModel: ActorViewModel, id: Int) {
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        TextField(value = name, onValueChange = { name = it }, label = { Text(text = "Name") })
        TextField(value = role, onValueChange = { role = it }, label = { Text(text = "Role") })
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            actorViewModel.insert(Actor(id, name = name, role = role))
        }) {
            Text(text = "INSERT ACTOR")
        }
    }
}

@Composable
fun ListActors(actorViewModel: ActorViewModel, id: Int, navController: NavController) {
    var actorList = actorViewModel.getById(id = id).observeAsState(listOf())
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        item {
            Row() {
                Text(text = " ACTORS")
            }
        }

        items(actorList.value) {
            Text(text = "Name: ${it.name} - Role: ${it.role}", Modifier.clickable {
                navController.navigate("details/${it.id}")
            })
        }
    }
}

@Composable
fun MainAppNav(movieViewModel: MovieViewModel, application: Application) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            Column() {
                InsertMovie(movieViewModel = movieViewModel)
                ListMovies(movieViewModel = movieViewModel, navController = navController)
            }
        }
        composable("details/{id}") {
            val id = it.arguments?.getString("id")?.toInt() ?: 0
            var actorViewModel = ActorViewModel(application = application)
            Column() {
                InsertActor(actorViewModel = actorViewModel, id = id)
                ListActors(actorViewModel = actorViewModel, id = id, navController = navController)
            }
        }
    }
}
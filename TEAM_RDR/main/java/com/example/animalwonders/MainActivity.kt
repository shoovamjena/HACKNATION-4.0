package com.example.animalwonders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.animalwonders.room.QuizDatabase
import com.example.animalwonders.ui.theme.AnimalWondersTheme
import com.example.animalwonders.uicomponents.navigation.NavigationGraph
import com.example.animalwonders.viewmodel.AnimalViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val animalViewModel = AnimalViewModel(application)
        val animalRepository = animalViewModel.animalRepository
        val database = Room.databaseBuilder(
            applicationContext,
            QuizDatabase::class.java,
            "quiz-database"
        ).build()
        val quizDao = database.quizDao()

        setContent {
            AnimalWondersTheme {
                // Initialize the NavController
                val navController = rememberNavController()
                NavigationGraph(navController = navController, animalViewModel, animalRepository,quizDao)
            }
        }
    }
}


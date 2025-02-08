package com.example.animalwonders.uicomponents.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codewithfk.animalwanderers.ui.screens.ARScreen
import com.example.animalwonders.room.AnimalDao
import com.example.animalwonders.room.AnimalRepository
import com.example.animalwonders.room.QuizDao
import com.example.animalwonders.room.QuizScreen
import com.example.animalwonders.screen.animaldetailscreen.AnimalDetailScreen
import com.example.animalwonders.screen.homescreen.HomeScreen
import com.example.animalwonders.screen.loginscreen.LoginScreen
import com.example.animalwonders.screen.signupscreen.SignupScreen
import com.example.animalwonders.screen.welcomescreen.welcomeScreen
import com.example.animalwonders.viewmodel.AnimalViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationGraph(navController: NavHostController, animalViewModel: AnimalViewModel, animalRepository: AnimalRepository, quizDao: QuizDao ) {
    val navController = rememberNavController()

    // Firebase Auth instance to check the login state
    val auth = FirebaseAuth.getInstance()

    // Define the NavHost with the startDestination logic
    NavHost(
        navController = navController,
        startDestination = if (auth.currentUser != null) "home_screen" else "welcome_screen" // Check if user is logged in
    ) {
        // Welcome Screen
        composable("welcome_screen") {
            welcomeScreen(navController = navController)
        }

        // Home Screen
        composable("home_screen") {
            val animalViewModel: AnimalViewModel = viewModel()
            HomeScreen(
                viewModel = animalViewModel,
                onLogout = {
                    auth.signOut()
                    navController.navigate("welcome_screen") {
                        // Prevent back navigation to home screen
                        popUpTo("home_screen") { inclusive = true }
                    }
                },
                navController = navController
            )
        }

        // Signup Screen
        composable("signup_screen") {
            SignupScreen(navController = navController)
        }

        // Login Screen
        composable("login_screen") {
            LoginScreen(navController = navController)
        }

        //AnimalDetailScreen
        composable(
            "animal_detail/{animalId}",
            arguments = listOf(navArgument("animalId") { type = NavType.IntType })
        ) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getInt("animalId") ?: return@composable
            // Passing the repository to the AnimalDetailScreen
            AnimalDetailScreen(animalId, animalViewModel,navController)
        }
        composable(
            "ar_screen/{animalName}",
            arguments = listOf(navArgument("animalName") { type = NavType.StringType })
        ) { backStackEntry ->
            val animalName = backStackEntry.arguments?.getString("animalName") ?: return@composable
            ARScreen(
                navController = navController,
                model = animalName
            )
        }

        composable("quiz_screen") {
            QuizScreen(quizDao,navController)
        }

    }
}
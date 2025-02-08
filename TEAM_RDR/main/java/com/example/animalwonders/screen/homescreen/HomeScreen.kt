package com.example.animalwonders.screen.homescreen

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.animalwonders.ui.theme.PrimaryPink
import com.example.animalwonders.ui.theme.PrimaryPinkBlended
import com.example.animalwonders.ui.theme.PrimaryPinkDark
import com.example.animalwonders.ui.theme.PrimaryVioletDark
import com.example.animalwonders.uicomponents.home.TimeBasedGreeting
import com.example.animalwonders.uicomponents.home.moduleButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.animalwonders.R
import com.example.animalwonders.uicomponents.home.bottomBarNav
import com.example.animalwonders.viewmodel.AnimalViewModel
import androidx.compose.runtime.livedata.observeAsState




@Composable
fun HomeScreen(
    viewModel: AnimalViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    onLogout: () -> Unit
){

    val wildAnimals by viewModel.animals.observeAsState(emptyList())
    val domesticAnimals by viewModel.animals.observeAsState(emptyList())

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    // State to store the user's name
    val userName = remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    //for module button
    var selectedModule by remember { mutableStateOf("WILD") }

    val gradientOffset by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 4000, easing = LinearEasing), label = "",
    )

    val gradientColors = listOf(PrimaryPinkBlended, PrimaryPink)
    val animatedBrush = Brush.verticalGradient(
        colors = gradientColors,
        startY = 0f,
        endY = with(LocalDensity.current) { 1000.dp.toPx() * gradientOffset },
        tileMode = TileMode.Clamp
    )

    // Fetch the user's name from Firestore when the screen is first launched
    LaunchedEffect(auth.currentUser?.uid) {
        auth.currentUser?.uid?.let { userId ->
            try {
                val userDoc = firestore.collection("users").document(userId).get().await()
                userName.value = userDoc.getString("name") // Retrieve the name from Firestore
            } catch (e: Exception) {
                Toast.makeText(navController.context, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(selectedModule) {
        viewModel.fetchAnimalsByCategory(selectedModule)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            bottomBarNav(navController,0)
        }

    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(animatedBrush)
                .systemBarsPadding()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp)
            ){
                if (userName.value != null) {
                    Column {
                        TimeBasedGreeting()
                        Text(
                            text = "${userName.value}",
                            color = PrimaryVioletDark,
                            style = MaterialTheme.typography.headlineLarge
                        )  }

                } else {
                    Text(text = "Loading...")
                }
                Spacer(
                    modifier = Modifier.width(width = 50.dp)
                )
                Box{
                    IconButton(
                        onClick = {expanded = true },
                        colors = IconButtonDefaults.iconButtonColors(PrimaryPinkDark),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Settings,contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {expanded = false},
                        modifier = Modifier
                            .background(PrimaryPink)
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Logout", fontSize = 18.sp)},
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.logout),
                                    contentDescription = null,
                                    modifier = Modifier.size(34.dp))
                            },
                            onClick = {
                                onLogout()
                                expanded = false },
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                moduleButton(
                    text = "WILD",
                    onclicked = {
                        selectedModule = "WILD"
                        viewModel.fetchAnimalsByCategory("WILD")
                        },
                    color = ButtonDefaults.buttonColors(
                        containerColor = if (selectedModule == "WILD") PrimaryPinkDark else Color.White,
                        contentColor = if (selectedModule == "WILD") Color.White else PrimaryPinkDark
                    ),
                    shadowColor = PrimaryPinkDark,
                    borderColor = if (selectedModule == "WILD") Color.White else PrimaryPinkDark
                )
                Spacer(modifier = Modifier.weight(1f))
                moduleButton(
                    text = "DOMESTIC",
                    onclicked = {
                        selectedModule = "DOMESTIC"
                        viewModel.fetchAnimalsByCategory("DOMESTIC")
                        },
                    color = ButtonDefaults.buttonColors(
                        containerColor = if (selectedModule == "DOMESTIC") PrimaryPinkDark else Color.White,
                        contentColor = if (selectedModule == "DOMESTIC") Color.White else PrimaryPinkDark
                    ),
                    shadowColor = PrimaryPinkDark,
                    borderColor = if (selectedModule == "DOMESTIC") Color.White else PrimaryPinkDark
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(0.5f)
                    .padding(top = 24.dp)
                    .padding(horizontal = 14.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.7f))
            ){
                // Animal Grid
                if (selectedModule == "WILD") {
                    AnimalGrid(
                        animals = wildAnimals,
                        deleteAnimal = { animal -> viewModel.deleteAnimal(animal)},
                        navController = navController
                    )
                } else {
                    AnimalGrid(
                        animals = domesticAnimals,
                        deleteAnimal = { animal -> viewModel.deleteAnimal(animal) },
                        navController = navController
                    )
                }

            }

        }
    }
}

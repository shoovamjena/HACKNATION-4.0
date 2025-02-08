package com.example.animalwonders.screen.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.animalwonders.room.Animal
import com.example.animalwonders.viewmodel.AnimalViewModel

@Composable
fun AnimalGrid(
    animals: List<Animal>,
    deleteAnimal: (Animal) -> Unit,
    navController: NavController
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(animals) { animal ->
            AnimalCard(animal = animal, deleteAnimal = deleteAnimal, navController = navController)
        }
    }
}

@Composable
fun AnimalCard(
    animal: Animal,
    deleteAnimal: (Animal) -> Unit,
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        // Circular Image of Animal
        Image(
            painter = rememberAsyncImagePainter(animal.imageResId),
            contentDescription = animal.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .clickable {
                    navController.navigate("animal_detail/${animal.id}")
                }
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Animal Name
        Text(text = animal.name)
    }
}

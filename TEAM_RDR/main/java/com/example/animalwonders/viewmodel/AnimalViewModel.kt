package com.example.animalwonders.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.animalwonders.R
import com.example.animalwonders.room.Animal
import com.example.animalwonders.room.AnimalDatabase
import com.example.animalwonders.room.AnimalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnimalViewModel(application: Application) : AndroidViewModel(application) {

     val animalRepository: AnimalRepository

    init {
        val animalDao = AnimalDatabase.getDatabase(application).animalDao()
        animalRepository = AnimalRepository(animalDao)

        addStaticAnimals()
    }

    private val _animals = MutableLiveData<List<Animal>>()
    val animals: LiveData<List<Animal>> get() = _animals

    private val _selectedAnimal = MutableLiveData<Animal?>()
    val selectedAnimal: LiveData<Animal?> get() = _selectedAnimal


    private fun addStaticAnimals() {
        viewModelScope.launch(Dispatchers.IO) {
            // Check if there are any animals in the WILD category
            val wildAnimals = animalRepository.getAnimalsByCategory("WILD")
            if (wildAnimals.isEmpty()) {
                // Add static wild animals if no wild animals exist in the database
                val staticWildAnimals = listOf(
                    Animal(name = "Lion", imageResId = R.drawable.lion, category = "WILD", description = "The king of the jungle with a majestic golden mane and a powerful roar.", food = "Carnivore", color = "Golden", model = "models/Lion.glb"),
                    Animal(name = "Tiger", imageResId = R.drawable.tiger, category = "WILD", description = "A big cat with orange fur and bold black stripes, known for its stealthy movements.", food = "Carnivore", color = "Orange with black stripes", model = "models/Tiger.glb"),
                    Animal(name = "Elephant", imageResId = R.drawable.elephant, category = "WILD", description = "The largest land animal with a long trunk, big ears, and thick grey skin.", food = "Herbivore", color = "Grey", model = "models/Elephant.glb")
                )
                staticWildAnimals.forEach { animal ->
                    animalRepository.addAnimal(animal)
                }
            }

            // Check if there are any animals in the DOMESTIC category
            val domesticAnimals = animalRepository.getAnimalsByCategory("DOMESTIC")
            if (domesticAnimals.isEmpty()) {
                // Add static domestic animals if no domestic animals exist in the database
                val staticDomesticAnimals = listOf(
                    Animal(name = "Dog", imageResId = R.drawable.dog, category = "DOMESTIC", description = "A loyal and friendly animal known as a human's best friend, with a wagging tail.", food = "Omnivore", color = "Many colours", model = "models/Dog.glb"),
                    Animal(name = "Cat", imageResId = R.drawable.cat, category = "DOMESTIC", description = "A cute and curious pet with soft fur, sharp claws, and a playful nature.", food = "Omnivore", color = "Many colours", model = "models/Cat.glb")
                )
                staticDomesticAnimals.forEach { animal ->
                    animalRepository.addAnimal(animal)
                }
            }
        }
    }

    fun fetchAnimalsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO){
            val fetchedAnimals = animalRepository.getAnimalsByCategory(category)
            _animals.postValue(fetchedAnimals)
        }
    }

    fun deleteAnimal(animal: Animal) {
        viewModelScope.launch {
            animalRepository.deleteAnimal(animal)
        }
    }

    fun fetchAnimalById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val fetchedAnimal = animalRepository.getAnimalById(id)
            // Post the animal result to LiveData
            _selectedAnimal.postValue(fetchedAnimal)
        }
    }

}

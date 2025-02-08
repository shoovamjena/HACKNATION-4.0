package com.example.animalwonders.room

class AnimalRepository(val animalDao: AnimalDao) {

    // Add a new animal to the database
    suspend fun addAnimal(animal: Animal) {
        animalDao.addAnimal(animal)
    }

    // View all animals by category (wild or domestic)
    fun getAnimalsByCategory(category: String): List<Animal> {
        return animalDao.getAnimalsByCategory(category)
    }

    suspend fun deleteAnimal(animal: Animal) {
        animalDao.deleteAnimal(animal.id)
    }

     fun getAnimalById(animalId: Int): Animal {
        return animalDao.getAnimalById(animalId)
    }

     fun getModelForAnimal(animalName: String): String? {
        return animalDao.getModel(animalName)
    }
}

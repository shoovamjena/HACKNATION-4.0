package com.example.animalwonders.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AnimalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAnimal(animal: Animal)

    @Query("SELECT * FROM animals WHERE category = :category")
    fun getAnimalsByCategory(category: String): List<Animal>

    @Query("SELECT * FROM animals WHERE id = :id")
    fun getAnimalById(id: Int): Animal

    @Query("Delete FROM animals WHERE id = :id")
    suspend fun deleteAnimal(id : Int)

    @Query("SELECT model FROM animals WHERE name = :animalName")
     fun getModel(animalName: String): String?
}

package com.example.animalwonders.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animals")
data class Animal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val imageResId: Int,
    val category: String,
    val food: String,
    val color: String,
    val model: String
)


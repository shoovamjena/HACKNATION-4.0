package com.example.animalwonders.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Animal::class,], version = 1, exportSchema = false)
abstract class AnimalDatabase : RoomDatabase() {
    abstract fun animalDao(): AnimalDao

    companion object {
        @Volatile
        private var INSTANCE: AnimalDatabase? = null

        fun getDatabase(context: Context): AnimalDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AnimalDatabase :: class.java,
                        "animals_database"
                    ).build()
                }
                return instance
            }
        }
    }
}

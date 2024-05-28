package com.example.soundnotes.Database

import android.content.Context
import androidx.room. Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.soundnotes.Models.Note
import com.example.soundnotes.Utilities.DATABASE_NAME

@Database(entities = arrayOf(Note :: class), version = 1, exportSchema = false)
abstract class DB : RoomDatabase(){

    abstract fun getNoteDao(): Dao

    companion object {

        @Volatile
        private var INSTANCE: DB? = null

        fun getDatabase(context: Context): DB{

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DB::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}
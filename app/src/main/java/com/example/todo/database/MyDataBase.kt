package com.example.todo.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//import androidx.room.TypeConverters
import com.example.todo.database.dao.TaskDao
import com.example.todo.database.model.Task


@Database(entities = [Task::class],version = 1
    //, exportSchema = false,autoMigrations = [
   // AutoMigration (from = 1, to = 2)
//]
)
//@TypeConverters(DateConverter :: class)
abstract class MyDataBase: RoomDatabase() {
    abstract fun tasksDao(): TaskDao
    companion object{
        private val DATABASE_NAME= "todo-Database"
        private var myDataBase:MyDataBase?=null

        fun getInstance(context: Context):MyDataBase{
            // Single object from database (Singleton Pattern)
            if (myDataBase==null){
                myDataBase = Room.databaseBuilder(context,MyDataBase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build()

            }
            return myDataBase!!
        }
    }
}
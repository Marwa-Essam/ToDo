package com.example.todo.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Task (
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    @ColumnInfo
    var title:String?=null,
    @ColumnInfo
    var desc:String?=null,
    @ColumnInfo
    var date:Long?=null,
    @ColumnInfo
    var isDone:Boolean?=false
):Serializable
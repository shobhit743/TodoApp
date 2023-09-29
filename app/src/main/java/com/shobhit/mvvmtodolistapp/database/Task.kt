package com.shobhit.mvvmtodolistapp.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat
import java.util.*

@Parcelize
@Entity(tableName = "task")
data class Task(@PrimaryKey(autoGenerate = true) val id:Int,val taskName:String,val isImportant:Boolean,
                val isCompleted:Boolean) :  Parcelable {
    var createdDate:String
    init {
        val simpleDateFormatter = DateFormat.getDateTimeInstance()
        createdDate = simpleDateFormatter.format(Date(System.currentTimeMillis()))
    }
}
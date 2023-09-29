package com.shobhit.mvvmtodolistapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.shobhit.mvvmtodolistapp.database.TaskDatabase
import com.shobhit.mvvmtodolistapp.util.PreferenceManager

class TaskApp : Application() {


    override fun onCreate() {
        super.onCreate()
        taskInstance = this
    }

    companion object {
        private var taskInstance:TaskApp? = null

        fun getInstance() : TaskApp {
            return taskInstance!!
        }

        @Volatile
        var taskDatabaseInstance: TaskDatabase? = null

        fun getDatabase(context:Context) : TaskDatabase {
            var taskInstance = taskDatabaseInstance
            if(taskInstance!=null){
                return taskInstance
            }
            synchronized(this){
                val taskInstance = Room.databaseBuilder(context.applicationContext,TaskDatabase::class.java,"task_db").build()
                taskDatabaseInstance = taskInstance
                return taskInstance
            }
        }
    }
}
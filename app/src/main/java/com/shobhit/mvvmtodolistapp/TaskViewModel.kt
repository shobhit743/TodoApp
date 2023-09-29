package com.shobhit.mvvmtodolistapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.shobhit.mvvmtodolistapp.database.Task
import com.shobhit.mvvmtodolistapp.util.PreferenceManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(private val application:Application,private val state:SavedStateHandle) : AndroidViewModel(application){

    val task:Task? = state["task"]
    val taskApp = TaskApp.getInstance()
    val taskDao = TaskApp.getDatabase(application).taskDao()
    private val preferenceManager = PreferenceManager(taskApp)
    val searchQuery = MutableStateFlow("")
    val sort = MutableStateFlow(SortBy.SORT_BY_DATE)
    val hideCompleted = preferenceManager.hideCompletedFlow

    val tasks = combine(searchQuery,sort,hideCompleted) { search,sort, hideCompleted ->
        Triple(search,sort,hideCompleted)
    }.flatMapLatest {
        taskDao.getTasks(it.first,it.second,it.third)
    }

    fun updateHideCompletedPref(hideCompleted:Boolean){
        viewModelScope.launch {
            preferenceManager.saveHideCompleted(hideCompleted)
        }
    }

    fun saveTask(saveTask: Task) {
        viewModelScope.launch {
            if(task==null) {
                taskDao.insertTask(saveTask)
            } else {
                taskDao.updateTask(saveTask)
            }
        }
    }

    fun updateTask(task:Task){
        viewModelScope.launch {
            taskDao.updateTask(task)
        }

    }
}

enum class SortBy {
    SORT_BY_NAME, SORT_BY_DATE
}
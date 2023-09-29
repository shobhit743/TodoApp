package com.shobhit.mvvmtodolistapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shobhit.mvvmtodolistapp.SortBy
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    fun getTasks(taskName:String, sortBy:SortBy, hideCompleted:Boolean):Flow<List<Task>> {
        return if(sortBy == SortBy.SORT_BY_DATE){
            getTasksSortByDate(taskName,hideCompleted)
        } else {
            getTasksSortByName(taskName,hideCompleted)
        }
    }

    @Query("Select * from task where taskName like '%'||:taskName||'%' and (isCompleted !=:hideCompleted or isCompleted = 0) order by isImportant DESC,taskName")
    fun getTasksSortByName(taskName:String,hideCompleted: Boolean):Flow<List<Task>>

    @Query("Select * from task where taskName like '%'||:taskName||'%' and (isCompleted !=:hideCompleted or isCompleted = 0) order by isImportant DESC,createdDate")
    fun getTasksSortByDate(taskName:String,hideCompleted: Boolean):Flow<List<Task>>



    }
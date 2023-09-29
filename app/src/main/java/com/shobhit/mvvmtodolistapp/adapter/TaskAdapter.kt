package com.shobhit.mvvmtodolistapp.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shobhit.mvvmtodolistapp.database.Task
import com.shobhit.mvvmtodolistapp.databinding.TaskItemLayoutBinding
import com.shobhit.mvvmtodolistapp.fragment.TasksFragmentDirections

class TaskAdapter(private val taskUpdateListener: TaskUpdateListener) :  ListAdapter<Task,TaskAdapter.TaskViewHolder>(DIFF_UTIL_CALLBACK) {

    companion object {
         val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<Task>(){
             override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
              return oldItem.id == newItem.id
             }

             override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
              return oldItem==newItem
             }
         }
    }

    inner class TaskViewHolder(private val binding:TaskItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    val item = getItem(adapterPosition)
                    it.findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(item,"Update Task" ))
                }
                binding.taskCheckbox.setOnClickListener {
                    val item = getItem(adapterPosition)
                    taskUpdateListener.onTaskUpdate(item.copy(isCompleted = binding.taskCheckbox.isChecked))
                }
            }

            fun bindData(task: Task){
                binding.apply {
                    txtTask.text = task.taskName
                    ivImportant.isVisible = task.isImportant
                    taskCheckbox.isChecked = task.isCompleted
                    txtTask.paint.isStrikeThruText = task.isCompleted
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
       val binding = TaskItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
       val item = getItem(position)
        holder.bindData(item)
    }

    interface TaskUpdateListener{
        fun onTaskUpdate(task: Task)
    }
}
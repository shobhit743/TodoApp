package com.shobhit.mvvmtodolistapp.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shobhit.mvvmtodolistapp.R
import com.shobhit.mvvmtodolistapp.TaskViewModel
import com.shobhit.mvvmtodolistapp.database.Task
import com.shobhit.mvvmtodolistapp.databinding.AddEditTaskFragmentBinding

class AddEditTaskFragment : Fragment(R.layout.add_edit_task_fragment) {

    private var _binding:AddEditTaskFragmentBinding? = null
    private val binding get() = _binding!!
    private val taskViewModel by viewModels<TaskViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = AddEditTaskFragmentBinding.bind(view)
        binding.apply {
            taskViewModel.task?.let {
                edTask.setText(it.taskName)
                chImportant.isChecked = it.isImportant
                txtCreatedDate.text = it.createdDate
            }
            fabSave.setOnClickListener {
                val task = Task(taskViewModel.task?.id ?: 0,edTask.text.toString(),chImportant.isChecked,false)
                taskViewModel.saveTask(task)
                findNavController().navigateUp()
            }
        }
    }

}
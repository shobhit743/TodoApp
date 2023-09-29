package com.shobhit.mvvmtodolistapp.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shobhit.mvvmtodolistapp.R
import com.shobhit.mvvmtodolistapp.SortBy
import com.shobhit.mvvmtodolistapp.TaskViewModel
import com.shobhit.mvvmtodolistapp.adapter.TaskAdapter
import com.shobhit.mvvmtodolistapp.database.Task
import com.shobhit.mvvmtodolistapp.databinding.TasksFragmentBinding
import com.shobhit.mvvmtodolistapp.util.OnQueryTextChangeListener

class TasksFragment : Fragment(R.layout.tasks_fragment), TaskAdapter.TaskUpdateListener {

    private var _binding:TasksFragmentBinding? = null
    private val binding get() = _binding!!
    private val taskAdapter by lazy {
        TaskAdapter(this)
    }
    private val taskViewModel by viewModels<TaskViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = TasksFragmentBinding.bind(view)
        binding.apply {
            addTaskFab.setOnClickListener {
                findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(null,"Add Task"))
            }
            taskRecycler.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = taskAdapter
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            taskViewModel.tasks.collect{
                taskAdapter.submitList(it)
            }
        }

        requireActivity().addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu,menu)
                val searchItem = menu.findItem(R.id.menu_search)
                val searchView = searchItem.actionView as SearchView
                searchView.OnQueryTextChangeListener{
                    taskViewModel.searchQuery.value = it
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId){
                    R.id.menu_sort -> {
                        return true
                    }
                    R.id.menu_sort_name -> {
                        taskViewModel.sort.value = SortBy.SORT_BY_NAME
                        return true
                    }
                    R.id.menu_sort_date -> {
                        taskViewModel.sort.value = SortBy.SORT_BY_DATE
                        return true
                    }
                    R.id.menu_hide_all_completed -> {
                        menuItem.isChecked = !menuItem.isChecked
                        taskViewModel.updateHideCompletedPref(menuItem.isChecked)
                        return true
                    }
                    else -> {
                        return false
                    }
                }
            }
        },viewLifecycleOwner,Lifecycle.State.CREATED)
    }

    override fun onTaskUpdate(task: Task) {
        taskViewModel.saveTask(task)
    }


}
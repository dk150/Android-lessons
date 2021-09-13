package com.raywenderlich.listmaker.dkatarina

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskDetailFragment : Fragment() {

    private var list: TaskList? = null
    private var listPos: Int = -1
    private lateinit var taskListRecyclerView: RecyclerView
    private lateinit var listDataManager: ListDataManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listDataManager = ViewModelProvider(this).get(ListDataManager::class.java)

        arguments?.let {
            val args = TaskDetailFragmentArgs.fromBundle(it)
            val lists = listDataManager.readLists()
            for(l in lists) {
                if(l.name == args.listString) {
                    listPos = lists.indexOf(l) + 1
                    list = l
                    break
                }
            }
        }

        activity?.let{
            taskListRecyclerView = view.findViewById(R.id.task_list_recyclerview)
            taskListRecyclerView.layoutManager = LinearLayoutManager(it)
            taskListRecyclerView.adapter = TaskListAdapter(list!!)

            it.title = list!!.name

            val addTaskButton: FloatingActionButton = view.findViewById(R.id.add_task_fab)
            addTaskButton.setOnClickListener {
                showCreateTaskDialog()
            }
        }
    }

    private fun showCreateTaskDialog() {
        activity?.let{
            val taskDialog = AlertDialog.Builder(it)
            taskDialog.setTitle(R.string.name_of_task)
            val editTaskName = EditText(it)
            editTaskName.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            taskDialog.setView(editTaskName)
            taskDialog.setPositiveButton(R.string.add_task) { dialog, _ ->
                val adapter = taskListRecyclerView.adapter as TaskListAdapter
                val taskName = adapter.taskName(editTaskName.text.toString())
                addTask(taskName)
                listDataManager.saveList(list!!, listPos)
                dialog.dismiss()
            }
            taskDialog.create().show()
        }
    }

    private fun addTask(taskName: String) {
        val adapter = taskListRecyclerView.adapter as TaskListAdapter
        adapter.addTask(taskName)
    }
}
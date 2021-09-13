package com.raywenderlich.listmaker.dkatarina

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TodoListFragment : Fragment(), ToDoListAdapter.TodoListClickListener {

    private lateinit var toDoListRecyclerView: RecyclerView
    private lateinit var listDataManager: ListDataManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let{
            listDataManager = ViewModelProvider(this).get(ListDataManager::class.java)
        }
        val lists = listDataManager.readLists()

        toDoListRecyclerView = view.findViewById(R.id.lists_recyclerview)
        toDoListRecyclerView.layoutManager = LinearLayoutManager(activity)
        toDoListRecyclerView.adapter = ToDoListAdapter(lists, this)

        view.findViewById<FloatingActionButton>(R.id.add_list_fab).setOnClickListener {
            showCreateToDoListDialog()
        }

    }

    private fun showCreateToDoListDialog() {
        activity?.let {
            val dialogTitle = getString(R.string.name_of_list)
            val positiveButtonTitle = getString(R.string.create_list)
            val myDialog = AlertDialog.Builder(it)
            val todoTitleEditText = EditText(it)

            todoTitleEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS

            myDialog.setTitle(dialogTitle)
            myDialog.setView(todoTitleEditText)
            myDialog.setPositiveButton(positiveButtonTitle
            ) { dialog, _ ->
                val listName = todoTitleEditText.text.toString()
                val taskList = addList(listName)
                saveList(taskList)
                dialog.dismiss()
                showTasklistItems(taskList)
            }
            myDialog.create().show()
        }
    }

    private fun showTasklistItems(list: TaskList) {
        view?.let {
            val action = TodoListFragmentDirections.actionTodoListFragmentToTaskDetailFragment(list.name)
            it.findNavController().navigate(action)
        }
    }

    override fun listItemClicked(list: TaskList) {
        showTasklistItems(list)
    }

    private fun addList(listName: String): TaskList {
        val adapter = toDoListRecyclerView.adapter as ToDoListAdapter
        val name = adapter.listName(listName)
        val taskList = TaskList(name)
        adapter.addList(taskList)
        return taskList
    }

    private fun saveList(list: TaskList) {
        val adapter = toDoListRecyclerView.adapter as ToDoListAdapter
        val listPos = adapter.itemCount
        listDataManager.saveList(list, listPos)
    }
}
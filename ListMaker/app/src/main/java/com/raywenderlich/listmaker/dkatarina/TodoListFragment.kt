package com.raywenderlich.listmaker.dkatarina

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.RuntimeException

class TodoListFragment : Fragment(), ToDoListAdapter.TodoListClickListener {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var toDoListRecyclerView: RecyclerView
    private lateinit var listDataManager: ListDataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lists = listDataManager.readLists()

        toDoListRecyclerView = view.findViewById(R.id.lists_recyclerview)
        toDoListRecyclerView.layoutManager = LinearLayoutManager(activity)
        toDoListRecyclerView.adapter = ToDoListAdapter(lists, this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentInteractionListener) {
            listener = context
            listDataManager = ListDataManager(context)
        } else {
            throw RuntimeException(context.toString() + "must implement OnFragmentInteractionListerner")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onTodoListClicked(list: TaskList)
    }


    companion object {
        @JvmStatic
        fun newInstance(): TodoListFragment {
            return TodoListFragment()
        }
    }

    override fun listItemClicked(list: TaskList) {
        listener?.onTodoListClicked(list)
    }

    private fun updateLists() {
        val lists = listDataManager.readLists()
        toDoListRecyclerView.adapter = ToDoListAdapter(lists, this)
    }

    fun addList(listName: String): TaskList {
        val adapter = toDoListRecyclerView.adapter as ToDoListAdapter
        val name = adapter.listName(listName)
        val taskList = TaskList(name)
        listDataManager.saveList(taskList, adapter.itemCount)
        adapter.addList(taskList)
        return taskList
    }

    fun listPos(list: TaskList): Int {
        val adapter = toDoListRecyclerView.adapter as ToDoListAdapter
        return adapter.listPos(list)
    }

    fun saveList(list: TaskList, listPos: Int) {
        listDataManager.saveList(list, listPos)
        updateLists()
    }
}
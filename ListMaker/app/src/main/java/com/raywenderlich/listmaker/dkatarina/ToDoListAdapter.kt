package com.raywenderlich.listmaker.dkatarina

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ToDoListAdapter(private val lists: ArrayList<TaskList>) : RecyclerView.Adapter<ToDoListViewHolder>() {

    fun listName(name: String) =
        if (name.isEmpty()) {
            "Todo List ${lists.size+1}"
        } else {
            name
        }

    fun addList(taskList: TaskList) {
        lists.add(taskList)
        notifyItemInserted(lists.lastIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_view_holder, parent, false)
        return ToDoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoListViewHolder, position: Int) {
        holder.listPositionTextView.text = (position + 1).toString()
        holder.listTitleTextView.text = lists[position].name
    }

    override fun getItemCount(): Int = lists.size

}
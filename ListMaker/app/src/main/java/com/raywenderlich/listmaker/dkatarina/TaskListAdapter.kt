package com.raywenderlich.listmaker.dkatarina

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter

class TaskListAdapter(private val list: TaskList) : Adapter<TaskListViewHolder>() {

    fun addTask(task: String) {
        list.tasks.add(task)
        // notifyItemInserted(list.tasks.lastIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_view_holder, parent,false)
        return TaskListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        holder.itemNameTextView.text = list.tasks[position]
    }

    override fun getItemCount() = list.tasks.size
}
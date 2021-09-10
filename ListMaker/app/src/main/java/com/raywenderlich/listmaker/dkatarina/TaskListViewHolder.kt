package com.raywenderlich.listmaker.dkatarina

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val itemNameTextView: TextView = itemView.findViewById(R.id.textView_task)

}

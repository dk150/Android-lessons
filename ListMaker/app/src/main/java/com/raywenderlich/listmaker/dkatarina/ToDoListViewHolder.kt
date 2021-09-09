package com.raywenderlich.listmaker.dkatarina

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var listPositionTextView: TextView = itemView.findViewById(R.id.itemNumber)
    var listTitleTextView: TextView = itemView.findViewById(R.id.itemString)

}
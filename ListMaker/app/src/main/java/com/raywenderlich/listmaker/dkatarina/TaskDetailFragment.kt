package com.raywenderlich.listmaker.dkatarina

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TaskDetailFragment : Fragment() {

    private lateinit var list: TaskList
    private var listPos: Int = -1
    private lateinit var taskListRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            list = it.getParcelable(ARG_LIST)!!
            listPos = it.getInt(ARG_LIST_POS)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskListRecyclerView = view.findViewById(R.id.task_list_recyclerview)
        taskListRecyclerView.layoutManager = LinearLayoutManager(activity)
        taskListRecyclerView.adapter = TaskListAdapter(list)
    }

    fun addTask(taskName: String) {
        val adapter = taskListRecyclerView.adapter as TaskListAdapter
        adapter.addTask(taskName)
    }

    companion object {

        private const val ARG_LIST = "list"
        private const val ARG_LIST_POS = "list_pos"

        @JvmStatic
        fun newInstance(list: TaskList, listPos: Int) : TaskDetailFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARG_LIST, list)
            bundle.putInt(ARG_LIST_POS, listPos)
            val fragment = TaskDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
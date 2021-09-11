package com.raywenderlich.listmaker.dkatarina

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailActivity : AppCompatActivity() {

    private lateinit var list: TaskList
    private var listPos: Int = -1
    private lateinit var taskDetailFragment: TaskDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)!!
        listPos = intent.getIntExtra(MainActivity.INTENT_LIST_POS_KEY, -1)
        title = list.name

        taskDetailFragment = TaskDetailFragment.newInstance(list, listPos)

        val addTaskButton: FloatingActionButton = findViewById(R.id.add_task_fab)
        addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.task_fragment_container, taskDetailFragment)
            .commit()
    }

    private fun showCreateTaskDialog() {
        val taskDialog = AlertDialog.Builder(this)
        taskDialog.setTitle(R.string.name_of_task)
        val editTaskName = EditText(this)
        editTaskName.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        taskDialog.setView(editTaskName)
        taskDialog.setPositiveButton(R.string.add_task) { dialog, _ ->
            taskDetailFragment.addTask(editTaskName.text.toString())
            dialog.dismiss()
        }
        taskDialog.create().show()
    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY, list)
        bundle.putInt(MainActivity.INTENT_LIST_POS_KEY, listPos)
        val intent  = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

}
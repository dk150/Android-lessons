package com.raywenderlich.listmaker.dkatarina

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raywenderlich.listmaker.dkatarina.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TodoListFragment.OnFragmentInteractionListener {

    private lateinit var binding: ActivityMainBinding
    // private var todoListFragment = TodoListFragment.newInstance()
    private lateinit var todoListFragment: TodoListFragment

    companion object {
        const val INTENT_LIST_KEY = "list"
        const val INTENT_LIST_POS_KEY = "list_pos"
        const val LIST_DETAIL_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        findViewById<FloatingActionButton>(R.id.add_list_fab).setOnClickListener {
            showCreateToDoListDialog()
        }

        todoListFragment = supportFragmentManager.findFragmentById(R.id.todo_list_fragment) as TodoListFragment
        /*supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, todoListFragment)
            .commit()*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LIST_DETAIL_REQUEST_CODE) {
            data?.let {
                val list = data.getParcelableExtra<TaskList>(INTENT_LIST_KEY)!!
                val listPos = data.getIntExtra(INTENT_LIST_POS_KEY, -1)
                todoListFragment.saveList(list, listPos)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showCreateToDoListDialog() {
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)
        val myDialog = AlertDialog.Builder(this)
        val todoTitleEditText = EditText(this)

        todoTitleEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS

        myDialog.setTitle(dialogTitle)
        myDialog.setView(todoTitleEditText)
        myDialog.setPositiveButton(positiveButtonTitle
        ) { dialog, _ ->
            val listName = todoTitleEditText.text.toString()
            val taskList = todoListFragment.addList(listName)
            dialog.dismiss()
            showTasklistItems(taskList)
        }
        myDialog.create().show()
    }

    private fun showTasklistItems(list: TaskList) {
        val listPos = todoListFragment.listPos(list)
        val taskListItem = Intent(this, DetailActivity::class.java)
        taskListItem.putExtra(INTENT_LIST_KEY, list)
        taskListItem.putExtra(INTENT_LIST_POS_KEY, listPos)
        startActivityForResult(taskListItem, LIST_DETAIL_REQUEST_CODE)
    }

    override fun onTodoListClicked(list: TaskList) {
        showTasklistItems(list)
    }

}
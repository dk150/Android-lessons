package com.raywenderlich.listmaker.dkatarina

import android.content.Context
import androidx.preference.PreferenceManager
import java.util.*
import kotlin.collections.ArrayList

class ListDataManager(private val context: Context) {

    fun saveList(list: TaskList, listPosition: Int) {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context).edit()
        val tasksSet = HashSet<String>()
        for(task in list.tasks) {
            tasksSet.add("${list.tasks.indexOf(task)} $task")
        }
        sharedPrefs.putStringSet("$listPosition ${list.name}", tasksSet)
        sharedPrefs.apply()
    }

    fun readLists(): ArrayList<TaskList> {
        val lists = ArrayList<TaskList>()
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        for(taskList in sharedPrefs.all) {
            val taskItems = ArrayList(taskList.value as HashSet<String>)
            lists.add(TaskList(taskList.key, taskItems))
        }
        lists.sortBy { taskList -> taskList.name.split(" ")[0].toInt() }
        for(taskList in lists) {
            taskList.name = taskList.name.replaceFirst(Regex("[0-9]+ "), "")
            taskList.tasks.sortBy{ task -> task.split(" ")[0].toInt() }
            for(i in 0 until taskList.tasks.size) {
                val task = taskList.tasks[i]
                taskList.tasks[i] = task.replaceFirst(Regex("[0-9]+ "), "")
            }
        }
        return lists
    }

}
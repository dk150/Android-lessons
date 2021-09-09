package com.raywenderlich.listmaker.dkatarina

import android.content.Context
import androidx.preference.PreferenceManager
import java.util.*
import kotlin.collections.ArrayList

class ListDataManager(private val context: Context) {

    fun saveList(list: TaskList, listPosition: Int) {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context).edit()
        val tasksSet = LinkedHashSet<String>()
        tasksSet.addAll(list.tasks)
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
        }
        return lists
    }

}
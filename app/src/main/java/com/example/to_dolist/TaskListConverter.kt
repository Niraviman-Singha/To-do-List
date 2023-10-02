package com.example.to_dolist

import org.json.JSONArray
import org.json.JSONObject

object TaskListConverter {
    fun taskListToString(tasklist: List<Task>):String{

        val jsonArray = JSONArray()
        for (task in tasklist){
            val jsonObject = JSONObject()
            jsonObject.put("title",task.title)
            jsonObject.put("isCompleted", task.isComplete)
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()

    }

    fun stringToTaskList(taskListString:String):List<Task>{
        val taskList = mutableListOf<Task>()
        val jsonArray = JSONArray(taskListString)
        for (i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            val title = jsonObject.getString("title")
            val isCompleted = jsonObject.getBoolean("isCompleted")
            val task = Task(title, isCompleted)
            taskList.add(task)
        }
        return taskList
    }
}
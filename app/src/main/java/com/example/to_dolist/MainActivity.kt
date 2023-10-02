package com.example.to_dolist

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewTasks: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var sharedPrefereces:SharedPreferences
    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewTasks = findViewById(R.id.taskRecyclerView)
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(tasks)
        recyclerViewTasks.adapter = adapter

        sharedPrefereces = getSharedPreferences("My Tasks", Context.MODE_PRIVATE)

        val buttonAddTask = findViewById<Button>(R.id.addBtn)
        val editTextTask = findViewById<EditText>(R.id.taskET)

        buttonAddTask.setOnClickListener {
            val taskTitle = editTextTask.text.toString()

            if(taskTitle.isNotEmpty()){
                val task = Task(taskTitle, false)
                tasks.add(task)
                adapter.notifyItemInserted(tasks.size - 1)
                saveTasksToSharedPreferences()
                editTextTask.text.clear()
            }
            else{
                Toast.makeText(this,"Task title can not be empty", Toast.LENGTH_SHORT).show()
            }
        }
        loadTasksFromSharedPreferences()

        adapter.setOnEditClickListener(object : TaskAdapter.OnEditClickListener {
            override fun onEditClick(position: Int) {
                // Handle task editing
                val task = tasks[position]
                val editDialog = AlertDialog.Builder(this@MainActivity)
                val editText = EditText(this@MainActivity)
                editText.setText(task.title)
                editDialog.setTitle("Edit Task")
                editDialog.setView(editText)
                editDialog.setPositiveButton("Save") { _, _ ->
                    val newTaskTitle = editText.text.toString()
                    if (newTaskTitle.isNotEmpty()) {
                        editTask(position, newTaskTitle)
                    }
                }
                editDialog.setNegativeButton("Cancel") { _, _ -> }
                editDialog.show()
            }
        })
        adapter.setOnDeleteClickListener(object : TaskAdapter.OnDeleteClickListener {
            override fun onDeleteClick(position: Int) {
                // Handle task deletion
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle("Delete Task")
                alertDialog.setMessage("Are you sure you want to delete this task?")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    deleteTask(position)
                }
                alertDialog.setNegativeButton("No") { _, _ -> }
                alertDialog.show()
            }
        })

        adapter.setOnCheckedChangeListener(object : TaskAdapter.OnCheckedChangeListener {
            override fun onCheckedChange(position: Int, isChecked: Boolean) {
                // Handle task completion
                tasks[position].isComplete = isChecked
                saveTasksToSharedPreferences()
            }
        })


    }

    private fun deleteTask(position: Int) {
        // Implement logic to delete the task
        tasks.removeAt(position)
        adapter.notifyItemRemoved(position)
        saveTasksToSharedPreferences()
    }
    private fun editTask(position: Int, newTaskTitle: String) {
        // Implement logic to edit the task
        tasks[position].title = newTaskTitle
        adapter.notifyItemChanged(position)
        saveTasksToSharedPreferences()
    }
    private fun loadTasksFromSharedPreferences() {
        val taskListString = sharedPrefereces.getString("task_list", "")
        if (!taskListString.isNullOrEmpty()) {
            val loadedTasks = TaskListConverter.stringToTaskList(taskListString)
            tasks.clear()
            tasks.addAll(loadedTasks)
            adapter.notifyDataSetChanged()
        }
    }

    private fun saveTasksToSharedPreferences() {
        val editor = sharedPrefereces.edit()
        val taskListString = TaskListConverter.taskListToString(tasks)
        editor.putString("task_list", taskListString)
        editor.apply()
    }
}
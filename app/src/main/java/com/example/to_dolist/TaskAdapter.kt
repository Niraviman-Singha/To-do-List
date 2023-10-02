package com.example.to_dolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    interface OnEditClickListener {
        fun onEditClick(position: Int)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    interface OnCheckedChangeListener {
        fun onCheckedChange(position: Int, isChecked: Boolean)
    }

    private var editClickListener: OnEditClickListener? = null
    private var deleteClickListener: OnDeleteClickListener? = null
    private var checkedChangeListener: OnCheckedChangeListener? = null

    fun setOnEditClickListener(listener: OnEditClickListener) {
        editClickListener = listener
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        deleteClickListener = listener
    }
    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        checkedChangeListener = listener
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTaskTitle)
        val checkBoxTask: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val editButton: ImageButton = itemView.findViewById(R.id.imageButtonEdit)
        val deleteButton: ImageButton = itemView.findViewById(R.id.imageButtonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title
        holder.checkBoxTask.isChecked = task.isComplete

        // Implement click listeners for edit and delete buttons
        holder.editButton.setOnClickListener {
            editClickListener?.onEditClick(position)
        }

        holder.deleteButton.setOnClickListener {
            deleteClickListener?.onDeleteClick(position)
        }

        holder.checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
            checkedChangeListener?.onCheckedChange(position, isChecked)
        }

    }


    override fun getItemCount(): Int {
        return tasks.size
    }
}
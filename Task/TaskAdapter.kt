package com.example.todo.Task

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import kotlin.math.absoluteValue

class TaskAdapter(private val context: Context, private val tasks: MutableList<Task>): RecyclerView.Adapter<TaskAdapter.ViewHolder>()
    , ItemTouchHelperAdapter {

    companion object {
        private val highPriorityColor = Color.YELLOW
        private val mediumPriorityColor = Color.GRAY
        private val lowPriorityColor = Color.WHITE
        private val texthighPriorityColor = Color.RED
        private val textmediumPriorityColor = Color.BLUE
        private val textlowPriorityColor = Color.GREEN
    }



    // 实现ItemTouchHelperAdapter接口中的方法
    override fun onItemDismiss(position: Int) {
        val taskId = tasks[position].id
        val dbHelper = TaskDbHelper(context)
        dbHelper.deleteTask(taskId)
        tasks.removeAt(position)
        notifyItemRemoved(position)

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.text_view_title)
        val dueDateView: TextView = view.findViewById(R.id.text_view_due_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_my_dialog, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        //val num = task.category.toIntOrNull() ?: 0
        val num = task.category.hashCode().absoluteValue%6

        println(num)
        when (num % 6) {
            1 -> holder.itemView.setBackgroundResource(R.drawable.chat_bubble_1)
            2 -> holder.itemView.setBackgroundResource(R.drawable.chat_bubble_2)
            3 -> holder.itemView.setBackgroundResource(R.drawable.chat_bubble_3)
            4 -> holder.itemView.setBackgroundResource(R.drawable.chat_bubble_4)
            5 -> holder.itemView.setBackgroundResource(R.drawable.chat_bubble_5)
            else -> holder.itemView.setBackgroundResource(R.drawable.chat_bubble)
        }

        //holder.itemView.setBackgroundResource(R.drawable.chat_bubble)
        holder.titleView.text = task.title
        holder.dueDateView.text = task.dueDate
        when (task.priority) {
            1 -> holder.titleView.setTextColor(textlowPriorityColor)
            2 -> holder.titleView.setTextColor(textmediumPriorityColor)
            3 -> holder.titleView.setTextColor(texthighPriorityColor)
            else -> holder.titleView.setTextColor(Color.BLACK)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditTaskActivity::class.java)
            intent.putExtra("taskId", task.id)
            context.startActivity(intent)
        }
        // Add bottom margin to each item except the last one
        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (position != tasks.size - 1) {
            layoutParams.bottomMargin = 16 // Replace with your desired margin value
        } else {
            layoutParams.bottomMargin = 0
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    // 定义ItemTouchHelperCallback类
    class ItemTouchHelperCallback(private val adapter: ItemTouchHelperAdapter) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            // 不需要实现
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.onItemDismiss(viewHolder.adapterPosition)
        }


    }


}

package com.example.todo.Task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R


class TaskListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val tasks = getTasksFromDatabase()
        val adapter = TaskAdapter(this, tasks as MutableList<Task>)
        recyclerView.adapter = adapter

        val itemTouchHelperCallback = TaskAdapter.ItemTouchHelperCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)


    }
    override fun onResume() {
        super.onResume()
        updateTaskList()
    }
    fun updateTaskList() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val tasks = getTasksFromDatabase()
        val adapter = TaskAdapter(this, tasks as MutableList<Task>)
        recyclerView.adapter = adapter
    }

    private fun getTasksFromDatabase(): List<Task> {
        // 获取数据库实例
        val dbHelper = TaskDbHelper(this)
        val db = dbHelper.readableDatabase

        // 定义要查询的列
        val projection = arrayOf(
            TaskContract.TaskEntry._ID,
            TaskContract.TaskEntry.COLUMN_TITLE,
            TaskContract.TaskEntry.COLUMN_DESCRIPTION,
            TaskContract.TaskEntry.COLUMN_DUE_DATE,
            TaskContract.TaskEntry.COLUMN_PRIORITY,
            TaskContract.TaskEntry.COLUMN_CATEGORY
        )

        // 查询数据库
        val cursor = db.query(
            TaskContract.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            "${TaskContract.TaskEntry.COLUMN_PRIORITY} DESC"
        )

        // 将查询结果转换为List<Task>
        val tasks = mutableListOf<Task>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(TaskContract.TaskEntry._ID))
                val title =
                    getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_TITLE))
                val description =
                    getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DESCRIPTION))
                val dueDate =
                    getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DUE_DATE))
                val priority =
                    getInt(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_PRIORITY))
                val category =
                    getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_CATEGORY))
                tasks.add(Task(id, title, description, dueDate, priority, category))
            }
        }

        // 关闭查询
        cursor.close()


        tasks.sortWith(compareBy<Task> { it.dueDate }.thenByDescending { it.priority })

        return tasks
    }


}


package com.example.todo.Task

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.todo.Task.TaskContract.TaskEntry._ID

object TaskContract {
    // 表名
    const val TABLE_NAME = "tasks"

    // 列名
    object TaskEntry : BaseColumns {
        const val _ID = BaseColumns._ID
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_DUE_DATE = "due_date"
        const val COLUMN_PRIORITY = "priority"
        const val COLUMN_CATEGORY = "category"
    }

    fun deleteTask(db: SQLiteDatabase, taskId: Long): Int {
        return db.delete(TABLE_NAME, "$_ID=?", arrayOf(taskId.toString()))
    }

}


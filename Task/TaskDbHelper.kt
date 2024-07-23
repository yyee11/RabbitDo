package com.example.todo.Task

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todo.Task.TaskContract.TABLE_NAME


class TaskDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Task.db"
        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${TaskContract.TABLE_NAME} (" +
                    "${TaskContract.TaskEntry._ID} INTEGER PRIMARY KEY," +
                    "${TaskContract.TaskEntry.COLUMN_TITLE} TEXT," +
                    "${TaskContract.TaskEntry.COLUMN_DESCRIPTION} TEXT," +
                    "${TaskContract.TaskEntry.COLUMN_DUE_DATE} TEXT," +
                    "${TaskContract.TaskEntry.COLUMN_PRIORITY} INTEGER," +
                    "${TaskContract.TaskEntry.COLUMN_CATEGORY} TEXT)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
    fun deleteTask(id: Long) {
        val db = writableDatabase
        db.delete(TaskContract.TABLE_NAME, "${TaskContract.TaskEntry._ID}=?", arrayOf(id.toString()))
    }
    fun getTask(taskId: Long): Task? {
        val db = readableDatabase
        val columns = arrayOf(
            TaskContract.TaskEntry._ID,
            TaskContract.TaskEntry.COLUMN_TITLE,
            TaskContract.TaskEntry.COLUMN_DESCRIPTION,
            TaskContract.TaskEntry.COLUMN_DUE_DATE,
            TaskContract.TaskEntry.COLUMN_PRIORITY,
            TaskContract.TaskEntry.COLUMN_CATEGORY
        )
        val selection = "${TaskContract.TaskEntry._ID} = ?"
        val selectionArgs = arrayOf(taskId.toString())
        val cursor = db.query(
            TaskContract.TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        var task: Task? = null
        with(cursor) {
            if (moveToFirst()) {
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
                task = Task(id, title, description, dueDate, priority, category)
            }
        }
        cursor.close()
        return task
    }

    fun updateTask(taskId: Long, title: String, description: String, dueDate: String, priority: Int, category: String): Boolean {
        val values = ContentValues().apply {
            put(TaskContract.TaskEntry.COLUMN_TITLE, title)
            put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, description)
            put(TaskContract.TaskEntry.COLUMN_DUE_DATE, dueDate)
            put(TaskContract.TaskEntry.COLUMN_PRIORITY, priority)
            put(TaskContract.TaskEntry.COLUMN_CATEGORY, category)
        }
        val selection = "${TaskContract.TaskEntry._ID} = ?"
        val selectionArgs = arrayOf(taskId.toString())
        val count = writableDatabase.update(
            TaskContract.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
        return count > 0
    }



}

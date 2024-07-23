package com.example.todo.Task

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.R

class EditTaskActivity : AppCompatActivity() {

    private lateinit var taskTitle: EditText
    private lateinit var taskCategory: EditText
    private lateinit var taskDate: EditText
    private lateinit var taskDescription: EditText
    private lateinit var priorityRadioGroup: RadioGroup
    private lateinit var lowPriorityRadioButton: RadioButton
    private lateinit var mediumPriorityRadioButton: RadioButton
    private lateinit var highPriorityRadioButton: RadioButton

    private lateinit var btnEdit: Button

    private lateinit var databaseHelper: TaskDbHelper

    private var taskId: Long = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        // 初始化控件
        taskTitle = findViewById(R.id.edit_text_title)
        taskDescription = findViewById(R.id.edit_text_description)
        taskDate = findViewById(R.id.edit_text_date)
        taskCategory = findViewById(R.id.edit_text_category)
        priorityRadioGroup = findViewById(R.id.priority_radio_group)
        lowPriorityRadioButton = findViewById(R.id.radio_button_low)
        mediumPriorityRadioButton = findViewById(R.id.radio_button_medium)
        highPriorityRadioButton = findViewById(R.id.radio_button_high)
        btnEdit = findViewById(R.id.button_save)

        // 初始化数据库帮助类
        databaseHelper = TaskDbHelper(this)

        // 从 intent 中获取传递的任务 ID
        taskId = intent.getLongExtra("taskId", 0)

        // 加载任务信息
        loadTaskInfo()

        // 设置保存按钮的点击事件
        btnEdit.setOnClickListener { saveTask() }
    }

    /**
     * 加载任务信息
     */
    private fun loadTaskInfo() {
        // 根据任务 ID 从数据库中获取任务信息
        val task = databaseHelper.getTask(taskId)

        // 将任务信息填充到控件中
        if (task != null) {
            taskTitle.setText(task.title)
        }
        if (task != null) {
            taskDescription.setText(task.description)
        }
        if (task != null) {
            taskDate.setText(task.dueDate)
        }
        // 设置优先级
        if (task != null) {
            when (task.priority) {
                1 -> lowPriorityRadioButton.isChecked = true
                2 -> mediumPriorityRadioButton.isChecked = true
                3 -> highPriorityRadioButton.isChecked = true
            }
        }

        if (task != null) {
            taskCategory.setText(task.category)//setSelection(getCategoryIndex(task.category))
        }
    }

    /**
     * 将任务信息保存到数据库中
     */
    private fun saveTask() {
        // 获取任务信息
        val title = taskTitle.text.toString().trim()
        val description = taskDescription.text.toString().trim()
        val dueDate = taskDate.text.toString().trim()
        val priority = when (priorityRadioGroup.checkedRadioButtonId) {
            R.id.radio_button_low -> 1
            R.id.radio_button_medium -> 2
            R.id.radio_button_high -> 3
            else -> 0
        }
        val category = taskCategory.text.toString().trim()

        // 更新任务信息到数据库中
        databaseHelper.updateTask(taskId, title, description, dueDate, priority, category)

        // 关闭当前界面
        finish()
    }


}

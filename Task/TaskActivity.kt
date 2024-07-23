package com.example.todo.Task

import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.todo.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TaskActivity : AppCompatActivity(), OnSelectedDateChangedListener {
    override fun onSelectedDateChanged(nowselectedDate: String?) {
        this.selectedDate = nowselectedDate
        // 进行其他操作
    }

    private lateinit var titleEditText:EditText
    private lateinit var descriptionEditText:EditText

    var listener: OnSelectedDateChangedListener? = null
    private var selectedDate: String? = null

    private lateinit var priorityRadioGroup: RadioGroup
    private lateinit var lowPriorityRadioButton: RadioButton
    private lateinit var mediumPriorityRadioButton: RadioButton
    private lateinit var highPriorityRadioButton: RadioButton

    private var addtheCategory: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        titleEditText = findViewById<EditText>(R.id.edit_text_title)
        descriptionEditText = findViewById<EditText>(R.id.edit_text_description)

        priorityRadioGroup = findViewById(R.id.priority_radio_group)
        lowPriorityRadioButton = findViewById(R.id.radio_button_low)
        mediumPriorityRadioButton = findViewById(R.id.radio_button_medium)
        highPriorityRadioButton = findViewById(R.id.radio_button_high)

        // 设置按钮点击事件
        val btnPickDate = findViewById<Button>(R.id.btn_pick_date)

        val saveButton = findViewById<Button>(R.id.button_save)

        val buttonCategory = findViewById<Button>(R.id.button_category)


        btnPickDate.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.setOnSelectedDateChangedListener(this)
            datePickerFragment.show(supportFragmentManager, "datePicker")
        }


        buttonCategory.setOnClickListener {
            showAddCategoryDialog()
        }


        saveButton.setOnClickListener {

            val dbHelper = TaskDbHelper(this)
            val db = dbHelper.writableDatabase

            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val dueDate = selectedDate.toString()
            val priority = when (priorityRadioGroup.checkedRadioButtonId) {
                R.id.radio_button_low -> 1
                R.id.radio_button_medium -> 2
                R.id.radio_button_high -> 3
                else -> 0
            }
            val category=addtheCategory.toString()

            if (title.isEmpty()) {
                //Toast.makeText(this, "The title can not be empty", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, R.string.title_empty_error, Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            val values = ContentValues().apply {
                put(TaskContract.TaskEntry.COLUMN_TITLE, title)
                put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, description)
                put(TaskContract.TaskEntry.COLUMN_DUE_DATE, dueDate)
                put(TaskContract.TaskEntry.COLUMN_PRIORITY, priority)
                put(TaskContract.TaskEntry.COLUMN_CATEGORY, category)
            }
            println(title)
            println(dueDate)
            val newRowId = db.insert(TaskContract.TABLE_NAME, null, values)

            if (newRowId == -1L) {
                Toast.makeText(this, R.string.error_saving_task, Toast.LENGTH_SHORT).show();

            } else {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }


    private fun showAddCategoryDialog() {
        //val builder = AlertDialog.Builder(this)
        val builder = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
        //builder.setBackground(ContextCompat.getDrawable(this, R.drawable.dialog_bg))

        //builder.setTitle("Add Category")

        //val builder: AlertDialog.Builder = Builder(this)
        builder.setTitle(R.string.add_category_title)


        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(R.string.confirm_button) { dialog, which ->
            val category = input.text.toString()
            // 将种类保存到数据库或其他地方
            addtheCategory= category
            // 然后更新UI
        }
        builder.setNegativeButton("取消") { dialog, which -> dialog.cancel() }

        val dialog = builder.create()
        dialog.show()

        val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(ContextCompat.getColor(this, R.color.purple_500))
    }




}
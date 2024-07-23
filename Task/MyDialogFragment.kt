package com.example.todo.Task

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.todo.R

class MyDialogFragment : DialogFragment() {

    private lateinit var titleTextView: TextView
    private lateinit var dueDateTextView: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_dialog, container, false)
        titleTextView = view.findViewById(R.id.text_view_title)
        dueDateTextView = view.findViewById(R.id.text_view_due_date)
        val title = arguments?.getString("title")
        val dueDate = arguments?.getString("due_date")

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.fragment_my_dialog, null)
        builder.setView(view)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                // 点击确定按钮的处理逻辑
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                // 点击取消按钮的处理逻辑
            })
        val dialog = builder.create()

        return dialog
    }

}

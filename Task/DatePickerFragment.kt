package com.example.todo.Task

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.todo.R
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    var nowselectedDate: String? = null
    private var mListener: OnSelectedDateChangedListener? = null

    fun setOnSelectedDateChangedListener(listener: OnSelectedDateChangedListener) {
        mListener = listener
    }

    fun getOnSelectedDateChangedListener(): OnSelectedDateChangedListener? {
        return mListener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 获取当前日期
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // 创建DatePickerDialog实例
        val datePickerDialog = DatePickerDialog(requireActivity(), this, year, month, day)

        // 获取“确定”按钮并设置可见性
        datePickerDialog.setOnShowListener {
            val positiveButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            positiveButton.visibility = ViewGroup.VISIBLE
        }
        datePickerDialog.setOnShowListener { dialogInterface ->
            val positiveButton = (dialogInterface as DatePickerDialog).getButton(DatePickerDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(resources.getColor(R.color.black)) // 设置为你需要的颜色
        }
        return datePickerDialog
    }



    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // 在这里处理日期选择后的操作
        val dayOfMonthString = String.format("%02d", dayOfMonth)
        val MonthString = String.format("%02d", month+1)
        nowselectedDate = "$year-$MonthString -$dayOfMonthString"
        mListener?.onSelectedDateChanged(nowselectedDate)
        // 例如：tvSelectedDate.text = selectedDate
    }




}

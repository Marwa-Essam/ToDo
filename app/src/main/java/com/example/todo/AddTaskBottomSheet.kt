package com.example.todo

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import com.example.todo.base.BaseBottomSheet
import com.example.todo.database.MyDataBase
import com.example.todo.database.model.Task
import com.example.todo.databinding.AddTaskBottomsheetBinding
import java.util.*


class AddTaskBottomSheet :BaseBottomSheet() {

    lateinit var viewBinding:AddTaskBottomsheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = AddTaskBottomsheetBinding.inflate(inflater,
            container,false);
        return viewBinding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.submit.setOnClickListener{
            addTask()
        }
        viewBinding.date.setOnClickListener{
            showDatePicker();
        }
    }
    val currentDate = Calendar.getInstance();
    init {
        currentDate.set(Calendar.HOUR,0)
        currentDate.set(Calendar.MINUTE,0)
        currentDate.set(Calendar.SECOND,0)
        currentDate.set(Calendar.MILLISECOND,0)
    }
    fun showDatePicker(){
        val dialoge = DatePickerDialog(
            requireContext() ,object: DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                    currentDate.set(Calendar.YEAR,year);
                    currentDate.set(Calendar.MONTH,month)
                    currentDate.set(Calendar.DAY_OF_MONTH,day)
                    viewBinding.date.setText("$day / ${month+1} / $year")
                }
            },currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH))
        dialoge.show()
    }
    fun addTask(){
        if(!isValidForm())
            return;
        showLoadingDialog();

        val task = Task(
            title = viewBinding.titleLayout.editText?.text.toString(),
            desc = viewBinding.detailsLayout.editText?.text.toString(),
            date = currentDate.timeInMillis,
            isDone = false
        )
        MyDataBase.getInstance(requireContext()).tasksDao()
            .insertTask(task)
        Toast.makeText(requireContext(),"todo successfully", Toast.LENGTH_LONG).show()
        dismiss()
        ////////////////
        hideLoading()
        showMessage("Task Added Successfully",
            posActionTitle = "ok",posAction = { dialogInterface, i ->
                dialogInterface.dismiss()
                dismiss()
                taskAddedLister?.onTaskAdded()
            },cancelable = false);
    }
    fun isValidForm():Boolean{
        var isValid = true;
        if(viewBinding.titleLayout.editText?.text.toString().isNullOrBlank()){
            viewBinding.detailsLayout.error = getString(R.string.please_enter_title);
            isValid = false;
        }else {
            viewBinding.titleLayout.error=null
        }

        if(viewBinding.detailsLayout.editText?.text.toString().isNullOrBlank()){
            viewBinding.detailsLayout.error = getString(R.string.please_enter_desc);
            isValid = false;
        }else {
            viewBinding.detailsLayout.error=null
        }

        if(viewBinding.date.text.isNullOrBlank()){
            viewBinding.date.error = getString(R.string.please_select_date);
            isValid = false;
        }else {
            viewBinding.date.error=null
        }

        return isValid;
    }

    var taskAddedLister : OnTaskAddedListener?=null
    interface OnTaskAddedListener{
        fun onTaskAdded();
    }
}
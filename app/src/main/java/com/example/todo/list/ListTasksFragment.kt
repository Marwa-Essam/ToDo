package com.example.todo.list

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.base.BaseFragment
import com.example.todo.database.MyDataBase
import com.example.todo.database.model.Task
import com.example.todo.databinding.ListTasksFragmentBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*


class ListTasksFragment : BaseFragment() {
    lateinit var viewBinding: ListTasksFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = ListTasksFragmentBinding.inflate(layoutInflater,container,false);
        return viewBinding.root;
    }

    lateinit var adapter :TasksListAdapter
    lateinit var tasksList :List<Task>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksList = MyDataBase.getInstance(requireContext()).tasksDao().selectAllTasks()
        //selectTasksByDate(currentDate.timeInMillis)
        adapter = TasksListAdapter(tasksList)
        adapter.onDeleteClickListener = object :TasksListAdapter.OnItemClickListener{
            override fun onItemClick(pos: Int, item: Task) {
                deleteTask(item)
            }
        }
        viewBinding.todoRecycler.adapter = adapter
        viewBinding.calendarView.setOnDateChangedListener { widget, selectedDate, selected ->
            // when user clicks on date
            currentDate.set(Calendar.MONTH,selectedDate.month-1)
            currentDate.set(Calendar.DAY_OF_MONTH,selectedDate.day)
            currentDate.set(Calendar.YEAR,selectedDate.year)
            reloadTasks()
        }
        viewBinding.calendarView.setDateSelected(CalendarDay.today(),true)
    }
    val currentDate = Calendar.getInstance();
    init {
        currentDate.set(Calendar.HOUR,0)
        currentDate.set(Calendar.MINUTE,0)
        currentDate.set(Calendar.SECOND,0)
        currentDate.set(Calendar.MILLISECOND,0)
    }
    override fun onResume() {
        super.onResume()
        reloadTasks()
    }
    fun reloadTasks(){
        tasksList = MyDataBase.getInstance(requireContext())
            .tasksDao()
            .selectTasksByDate(currentDate.timeInMillis);
        adapter.reloadTasks(tasksList)
    }
    fun deleteTask(task: Task){

        showMessage("Are you sure you want to delete this task ?",
            posActionTitle = "yes",
            posAction = { dialogInterface, i ->
                dialogInterface?.dismiss()
                MyDataBase.getInstance(requireContext())
                    .tasksDao()
                    .deleteTask(task)
                reloadTasks() },
            negActionTitle = "cancel",
            negAction = DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface?.dismiss()
            }
        )
    }
    companion object{
        val TAG = "Tasks-Fragment"
    }
}
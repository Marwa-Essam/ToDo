package com.example.todo.list

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.UpdateActivity
import com.example.todo.base.BaseFragment
import com.example.todo.database.MyDataBase
import com.example.todo.database.model.Task
import com.example.todo.databinding.ListTasksFragmentBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*


class ListTasksFragment : BaseFragment() {
    lateinit var fragmentListTasksBinding: ListTasksFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentListTasksBinding = ListTasksFragmentBinding.inflate(layoutInflater,container,false);
        return fragmentListTasksBinding.root
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

        adapter.onItemClickedToUpdate=object  : TasksListAdapter.OnItemClickedToUpdate{
            override fun onClickToUpdate(task: Task) {
                val inttent: Intent= Intent(requireContext(),UpdateActivity::class.java)
                 inttent.putExtra("todo",task)
                startActivity(inttent)
            }

        }
        fragmentListTasksBinding.todoRecycler.adapter = adapter
        fragmentListTasksBinding.calendarView.setOnDateChangedListener { widget, selectedDate, selected ->
            // when user clicks on date
            currentDate.set(Calendar.MONTH,selectedDate.month-1)
            currentDate.set(Calendar.DAY_OF_MONTH,selectedDate.day)
            currentDate.set(Calendar.YEAR,selectedDate.year)
            reloadTasks()
        }
        fragmentListTasksBinding.calendarView.setDateSelected(CalendarDay.today(),true)
    }

    private fun makeTaskDane(task: Task){
        task.isDone=true
        MyDataBase.getInstance(requireContext()).tasksDao().updateTask(task)
        refreshTasks()
    }

    private fun refreshTasks() {
        adapter.reloadTasks(MyDataBase.getInstance(requireContext()).tasksDao().selectAllTasks())
        adapter.notifyDataSetChanged()}

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
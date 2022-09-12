package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todo.database.MyDataBase
import com.example.todo.database.model.Task
import com.example.todo.databinding.ActivityUpdateBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.milliseconds

class UpdateActivity : AppCompatActivity() {
    lateinit var activityUpdateBinding:ActivityUpdateBinding

    lateinit var task: Task
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUpdateBinding= ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(activityUpdateBinding.root)

        task = (intent.getSerializableExtra("todo") as? Task)!!
        showData(task)
        activityUpdateBinding.Chang.setOnClickListener {
           var task= updateTask()
        }
    }

    private fun updateTask() {
        if(isValidForm()==true){
            task!!.title=activityUpdateBinding.titleLayout.editText!!.text.toString()
            task!!.desc=activityUpdateBinding.detailsLayout.editText!!.text.toString()
            task!!.date=activityUpdateBinding.date.text.toString().toLong()

            MyDataBase.getInstance(this@UpdateActivity).tasksDao()
                .updateTask(task)
            Toast.makeText(this@UpdateActivity,"todo updated successfully", Toast.LENGTH_LONG).show()

        }

    }

    fun isValidForm():Boolean{
        var isValid = true;
        if(activityUpdateBinding.titleLayout.editText?.text.toString().isNullOrBlank()){
            activityUpdateBinding.detailsLayout.error = getString(R.string.please_enter_title);
            isValid = false;
        }else {
            activityUpdateBinding.titleLayout.error=null
        }

        if(activityUpdateBinding.detailsLayout.editText?.text.toString().isNullOrBlank()){
            activityUpdateBinding.detailsLayout.error = getString(R.string.please_enter_desc);
            isValid = false;
        }else {
            activityUpdateBinding.detailsLayout.error=null
        }

        if(activityUpdateBinding.date.text.isNullOrBlank()){
            activityUpdateBinding.date.error = getString(R.string.please_select_date);
            isValid = false;
        }else {
            activityUpdateBinding.date.error=null
        }

        return isValid;
    }


    private fun showData(task: Task) {
        val date=convertLongToTime(task.date!!)
        activityUpdateBinding.titleLayout.editText!!.setText(task.title)
        activityUpdateBinding.detailsLayout.editText!!.setText(task.desc)
        activityUpdateBinding.date.setText(task.date.toString())
       // activityUpdateBinding.date.setText(date)
    }
    fun convertLongToTime(time:Long):String{
        val date=Date(time)
        val format=SimpleDateFormat("yyyy.MM.dd HH.mm")
        return format.format(date)
    }
}
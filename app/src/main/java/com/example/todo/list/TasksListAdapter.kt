package com.example.todo.list

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Color.GREEN
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.database.model.Task
import com.example.todo.databinding.ItemTaskBinding
import com.zerobranch.layout.SwipeLayout


class TasksListAdapter (var items:List<Task>): RecyclerView.Adapter<TasksListAdapter.ViewHolder> (){
    var onItemClickedToUpdate:OnItemClickedToUpdate?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewBinding.title.text = items[position].title
        holder.viewBinding.description.text = items[position].desc

        if (items[position].isDone==true){
            holder.viewBinding.verticalLine.setBackgroundColor(Color.GREEN)
            holder.viewBinding.title.setTextColor(Color.GREEN)
            holder.viewBinding.markDone.setBackgroundColor(Color.GREEN)
        }

        if (onItemClickedToUpdate!=null){
            holder.viewBinding.title.setOnClickListener {
                onItemClickedToUpdate?.onClickToUpdate(items[position])
            }
        }

        holder.viewBinding.delete.setOnClickListener{
            onDeleteClickListener?.onItemClick(position,items[position])
        }
        holder.viewBinding.swipeLayout.setOnActionsListener(object : SwipeLayout.SwipeActionsListener{
                override fun onClose() {
                }

                override fun onOpen(direction: Int, isContinuous: Boolean) {
                    if(direction == SwipeLayout.RIGHT){

                    }else if(direction== SwipeLayout.LEFT){
                    }
                }
            })
    }
    var onDeleteClickListener:OnItemClickListener? =null
    interface OnItemClickListener{
        fun onItemClick(pos:Int,item: Task)
    }

    fun reloadTasks(newTasks:List<Task>){
        items = newTasks
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val viewBinding:ItemTaskBinding): RecyclerView.ViewHolder(viewBinding.root)

public  interface OnItemClickedToUpdate{
    fun onClickToUpdate(task: Task)
}


}
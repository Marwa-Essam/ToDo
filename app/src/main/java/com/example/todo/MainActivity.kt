package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.list.ListTasksFragment


class MainActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.bottomNavigationView.setOnItemSelectedListener{
            val id  = it.itemId;
            when(id){
                R.id.navigation_list ->{
                    showFragment(ListTasksFragment())
                }
                R.id.navigation_settings->{
                    showFragment(SettingsFragment())
                }
            }
            true
//            return@setOnItemSelectedListener
        }
        viewBinding.bottomNavigationView.selectedItemId = R.id.navigation_list
        viewBinding.addButton.setOnClickListener{
            showAddTaskSheet();
        }
    }

    fun showAddTaskSheet(){
        val bottomSheet = AddTaskBottomSheet()
        bottomSheet.taskAddedLister =
            object :AddTaskBottomSheet.OnTaskAddedListener{
                override fun onTaskAdded() {

                    val fragment =  supportFragmentManager
                        .findFragmentByTag(ListTasksFragment.TAG)

                    if(fragment!=null){
                        (fragment as ListTasksFragment).reloadTasks()
                    }
                }
            }
        bottomSheet.show(supportFragmentManager,null)
    }
    fun showFragment(fragment: Fragment){

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,fragment,

                if (fragment is SettingsFragment)
                    SettingsFragment.TAG
                else
                    ListTasksFragment.TAG)
            .commit()
    }
}
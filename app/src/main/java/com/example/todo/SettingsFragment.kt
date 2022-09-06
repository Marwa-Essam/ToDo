package com.example.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.databinding.SettingsFragmentBinding

class SettingsFragment: Fragment() {
    lateinit var fragmentSettingsBinding: SettingsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSettingsBinding =SettingsFragmentBinding.inflate(layoutInflater,container,false);
        return fragmentSettingsBinding.root;
    }
    companion object{
        val TAG = "Settings-Fragment"
    }
}
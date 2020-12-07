package com.example.housingapp.Interfaces

import androidx.fragment.app.Fragment

interface IFragmentChangeEventListener {

    fun fragmentShouldChange(fragment:Fragment, addToBackStack:Boolean)

}
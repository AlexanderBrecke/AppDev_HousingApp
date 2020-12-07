package com.example.housingapp.Interfaces

import com.example.housingapp.Housing

interface IRecyclerViewEventListener {

    fun onCellClickListener(housingList:MutableList<Housing>, position: Int)
    fun onCellDeleteClickListener(housingList: MutableList<Housing>, position: Int)

}
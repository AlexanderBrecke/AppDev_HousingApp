package com.example.housingapp.Interfaces

import com.example.housingapp.housing.Amenities
import com.example.housingapp.housing.HousingType

interface IFilterEventListener {
    fun filterEventListener(fromPrice:Double?, toPrice:Double?, amenities: MutableList<Amenities>, housingType: HousingType?)
}
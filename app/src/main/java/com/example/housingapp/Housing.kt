package com.example.housingapp

class Housing(val images:MutableList<String>, val address:String, val type:HousingType, val price:Double, val amenities:List<String>, val size:Int) {
}

enum class HousingType{
    Room,
    Apartment,
    House,
    Townhouse,
    Mansion,
    Cabin,
    Penthouse,
    Hotel,
}
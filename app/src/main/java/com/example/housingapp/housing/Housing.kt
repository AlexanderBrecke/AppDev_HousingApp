package com.example.housingapp.housing

class Housing(val address: String, val price:Double, val type:HousingType, val amenities:List<Amenities>, val images: MutableList<String>, val rentPayment: RentPayment? = null)

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

enum class Amenities{
    Electricity,
    Heating,
    Water,
    WiFi,
    Utilities,
    Fireplace,
}

enum class RentPayment{
    Daily,
    Weekend,
    Weekly,
    Fortnight,
    Monthly,
    Yearly
}
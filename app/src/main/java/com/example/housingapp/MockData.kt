package com.example.housingapp

import com.example.housingapp.housing.Amenities
import com.example.housingapp.housing.Housing
import com.example.housingapp.housing.HousingType
import com.example.housingapp.housing.RentPayment

class MockData {
    val cabinInTheWoods = Housing(
        "Dark forest road 666",
        666.666,
        HousingType.Cabin,
        listOf(Amenities.Electricity, Amenities.Water, Amenities.Fireplace),
        mutableListOf("https://fanart.tv/fanart/movies/22970/moviebackground/the-cabin-in-the-woods-505e68fc36f67.jpg"),
        RentPayment.Fortnight
    )

    val hotelRoom1 = Housing(
        "27500 West Leg Road",
        66.6,
        HousingType.Room,
        listOf(Amenities.Electricity, Amenities.Water, Amenities.Heating, Amenities.WiFi),
        mutableListOf("https://www.movie-locations.com/movies/s/Shining-Timberline-Lodge.jpg",
            "https://mir-s3-cdn-cf.behance.net/projects/404/ba140686487471.Y3JvcCw4MjMsNjQ0LDU0OCwyMTg.jpg",
            "https://d279m997dpfwgl.cloudfront.net/wp/2018/01/Shining_1980_20.jpg",
            "https://www.nme.com/wp-content/uploads/2016/10/overlook-hotel.png"
        ),
        RentPayment.Night
    )

    val conjuringHouse = Housing(
        "1677 Round Top Road, Harrisville",
        666666.666,
        HousingType.House,
        listOf(Amenities.Water, Amenities.Utilities),
        mutableListOf("https://frightfind.com/wp-content/uploads/2015/11/real-conjuring-house.jpg",
            "https://i.insider.com/5d2cd80221a8611063177087?width=1136&format=jpeg",
            "https://filmdaily.co/wp-content/uploads/2020/05/the-conjuring-house-2.jpg"
        )
    )

    val underTheGround = Housing(
        "--- Redacted ---",
        666.toDouble(),
        HousingType.Apartment,
        listOf(),
        mutableListOf("https://roadtovrlive-5ea0.kxcdn.com/wp-content/uploads/2016/01/neverout-airlock.jpg",
            "https://img-eshop.cdn.nintendo.net/i/579e4aceeb7fcf1f57e0e55b6b616c22425fa3d9081a5db9db4a895ac38dde4e.jpg"
        ),
        RentPayment.Week
    )

    val elmStret = Housing(
        "1428 Elm Street",
        66666.6,
        HousingType.House,
        listOf(Amenities.Heating, Amenities.Utilities),
        mutableListOf("https://www.screengeek.net/wp-content/uploads/2019/10/a-nightmare-on-elm-street-house.jpg",
            "https://filmschoolrejects.com/wp-content/uploads/2020/03/Blood-Bed-A-nightmare-on-elm-street-1280x720.png"
        ),
        RentPayment.Year

    )


}
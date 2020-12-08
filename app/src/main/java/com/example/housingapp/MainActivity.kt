package com.example.housingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.housingapp.fragments.HousingListFragment
import com.example.housingapp.housing.Amenities
import com.example.housingapp.housing.Housing
import com.example.housingapp.housing.HousingType
import com.example.housingapp.housing.RentPayment

class MainActivity : AppCompatActivity() {

    private val housingList: MutableList<Housing> = mutableListOf()
    private val cabinInTheWoods = Housing(
            "Dark forest road 666",
            666.666,
            HousingType.Cabin,
            listOf(Amenities.Water, Amenities.Electricity, Amenities.Fireplace),
            mutableListOf("https://fanart.tv/fanart/movies/22970/moviebackground/the-cabin-in-the-woods-505e68fc36f67.jpg"),
            RentPayment.Fortnight
    )

    private val hotelRoom1 = Housing(
            "27500 West Leg Road",
            66.6,
            HousingType.Room,
            listOf(Amenities.Electricity, Amenities.Water, Amenities.Heating, Amenities.WiFi),
            mutableListOf("https://www.movie-locations.com/movies/s/Shining-Timberline-Lodge.jpg", "https://www.themarysue.com/wp-content/uploads/2014/07/OverlookHotelShining.png"),
            RentPayment.Daily
    )



    //Setting up some values for views.
    private lateinit var containerView:FrameLayout
    private lateinit var welcomeView:TextView
    private lateinit var descriptionView:TextView
    private lateinit var browseButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 1..5){
//            housingList.add(cabinInTheWoods)
            if(i % 2 == 0){
                housingList.add(hotelRoom1)
            } else housingList.add(cabinInTheWoods)

        }

        //Initializing the views of main activity
        containerView = findViewById(R.id.container)
        welcomeView = findViewById(R.id.welcome_textView)
        descriptionView = findViewById(R.id.description_textView)
        browseButton = findViewById(R.id.browse_button)

        //Setting up some logic for the browse button
        browseButton.setOnClickListener {

            //Make it hide all front-page views
            showOrHideViews(listOf(welcomeView,descriptionView,browseButton), true)

            //Show container view we will use to put fragments in
            showOrHideViews(listOf(containerView),false)
            //and then show the fragment.
            showFragment(HousingListFragment(housingList),true)

        }

    }

    override fun onBackPressed() {
        //Setup some extra logic, that if you press back when in list view it will take you back to the "front page" instead of exiting the app
        if(supportFragmentManager.backStackEntryCount == 1){
            showOrHideViews(listOf(containerView), true)
            showOrHideViews(listOf(welcomeView,descriptionView,browseButton), false)
        }


//        for(i:Int in 0 until supportFragmentManager.backStackEntryCount){
//            if(i > 1){
//                supportFragmentManager.popBackStackImmediate()
//            }
//        }

//        if(supportFragmentManager.backStackEntryCount > 1){
//
//            supportFragmentManager.getBackStackEntryAt(1)
//        }

//        supportFragmentManager.popBackStack()

        //Make sure it will still run normally
        super.onBackPressed()
    }

    //Function to show selected fragment, and choose if it should be added to back-stack or not.
    fun showFragment(fragmentToShow: Fragment, addToBackStack:Boolean){
        if(addToBackStack){
            supportFragmentManager.beginTransaction().replace(R.id.container,fragmentToShow).addToBackStack(null).commit()
        }
        else supportFragmentManager.beginTransaction().replace(R.id.container,fragmentToShow).commit()
    }

    //Create a function to show or hide a list of views.
    //This is made with the DRY (Don't Repeat Yourself) principle in mind
    private fun showOrHideViews(listToShowOrHide: List<View>, hide:Boolean){
        for(view in listToShowOrHide){
            if(hide){
                view.visibility = View.GONE
            } else view.visibility = View.VISIBLE
        }
    }

    fun removeFragment(fragmentToRemove:Fragment){
        supportFragmentManager.beginTransaction().remove(fragmentToRemove).commit()
//        supportFragmentManager.popBackStack()
    }


}
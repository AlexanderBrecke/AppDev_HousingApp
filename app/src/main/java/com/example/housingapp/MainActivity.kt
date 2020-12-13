package com.example.housingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.housingapp.extras.carouselview.CarouselView
import com.example.housingapp.fragments.HousingAddFragment
import com.example.housingapp.fragments.HousingCurrentFragment
import com.example.housingapp.fragments.HousingListFragment
import com.example.housingapp.housing.Amenities
import com.example.housingapp.housing.Housing
import com.example.housingapp.housing.HousingType
import com.example.housingapp.housing.RentPayment
import com.example.housingapp.views.CustomFilterBarLayout
import com.example.housingapp.views.CustomHeaderBarLayout

class MainActivity : AppCompatActivity() {

    private val housingList: MutableList<Housing> = mutableListOf()
    private val cabinInTheWoods = Housing(
            "Dark forest road 666",
            666.666,
            HousingType.Cabin,
            listOf(Amenities.Electricity, Amenities.Water, Amenities.Fireplace),
            mutableListOf("https://fanart.tv/fanart/movies/22970/moviebackground/the-cabin-in-the-woods-505e68fc36f67.jpg"),
            RentPayment.Fortnight
    )

    private val hotelRoom1 = Housing(
            "27500 West Leg Road",
            66.6,
            HousingType.Room,
            listOf(Amenities.Electricity, Amenities.Water, Amenities.Heating, Amenities.WiFi),
            mutableListOf("https://www.movie-locations.com/movies/s/Shining-Timberline-Lodge.jpg",
                    "https://mir-s3-cdn-cf.behance.net/projects/404/ba140686487471.Y3JvcCw4MjMsNjQ0LDU0OCwyMTg.jpg",
                    "https://d279m997dpfwgl.cloudfront.net/wp/2018/01/Shining_1980_20.jpg"
            ),
            RentPayment.Night
    )

    private var currentFragment:Fragment? = null
    private lateinit var housingListFragment: HousingListFragment

    //Setting up some values for views.
    private lateinit var headerBar:CustomHeaderBarLayout
    private lateinit var filterBar:CustomFilterBarLayout
    private lateinit var containerView:FrameLayout
    private lateinit var welcomeView:TextView
    private lateinit var descriptionView:TextView
    private lateinit var browseButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Hack in a way to show 5 house listings.
        for (i in 1..5){
            if(i % 2 == 0){
                housingList.add(hotelRoom1)
            } else housingList.add(cabinInTheWoods)
        }

//        var carouselView = CarouselView(this)

        //Initialize the housing list fragment
        housingListFragment = HousingListFragment(housingList)

        //Initialize the header and filter bars
        headerBar = CustomHeaderBarLayout(findViewById(R.id.mainHeaderBar))



        filterBar = CustomFilterBarLayout(findViewById(R.id.filterBar), this, housingListFragment)
        val dropDownAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, HousingType.values())
        filterBar.setDropDownAdapter(dropDownAdapter)


        //Initializing the views of main activity
        containerView = findViewById(R.id.container)
        welcomeView = findViewById(R.id.welcome_textView)
        descriptionView = findViewById(R.id.description_textView)
        browseButton = findViewById(R.id.browse_button)

        setClickListeners()

        //Set the housings list fragment to the container view so we have its functionality available
        setFragment(housingListFragment,true)
        //Then hide the container view
        showOrHideViews(listOf(containerView),true)
    }

    override fun onBackPressed() {
        //Setup some extra logic, that if you press back when in list view it will take you back to the "front page" instead of exiting the app
        if(supportFragmentManager.backStackEntryCount == 1){
            setFragment(housingListFragment,true)
            showOrHideViews(listOf(containerView), true)
            showOrHideViews(listOf(welcomeView,descriptionView,browseButton), false)
        }

        //Make sure it will still run normally
        super.onBackPressed()

        //Set the current fragment to the correct one
        currentFragment = supportFragmentManager.fragments[0]
        //Make sure we show the right title.
        viewsHaveChanged()
    }

    //Create a function to set the click listeners
    private fun setClickListeners(){
        browseButton.setOnClickListener {

            //Make it hide all front-page views
            showOrHideViews(listOf(welcomeView,descriptionView,browseButton), true)

            //Show container view we use to put fragments in
            showOrHideViews(listOf(containerView),false)

            viewsHaveChanged()
        }

        headerBar.filterIconButton.setOnClickListener {
            filterBar.view.isVisible = !filterBar.view.isVisible
        }

    }

    //Create a function to show or hide a list of views.
    //This is made with the DRY (Don't Repeat Yourself) principle in mind
    private fun showOrHideViews(listToShowOrHide: List<View>, hide:Boolean){
        for(view in listToShowOrHide){
            if(hide){
                view.visibility = View.GONE
            } else view.visibility = View.VISIBLE
        }
        viewsHaveChanged()
    }

    //Need a way to know if views have changed
    private fun viewsHaveChanged(){
        if(!containerView.isVisible) headerBar.setTitle("Housing App")
        else fragmentHasChanged()
    }

    //Function to show selected fragment, and choose if it should be added to back-stack or not.
    fun setFragment(fragmentToShow: Fragment, addToBackStack:Boolean){
        if(addToBackStack){
            supportFragmentManager.beginTransaction().replace(R.id.container,fragmentToShow).addToBackStack(null).commit()
        }
        else supportFragmentManager.beginTransaction().replace(R.id.container,fragmentToShow).commit()
        currentFragment = fragmentToShow
        fragmentHasChanged()
    }

    //Set the correct title according to what fragment we are on
    private fun fragmentHasChanged(){
        if(currentFragment is HousingListFragment){
            headerBar.setTitle("House Listings")
        }
        if(currentFragment is HousingAddFragment){
            headerBar.setTitle("Add a Listing")
        }
        if(currentFragment is HousingCurrentFragment){
            headerBar.setTitle("Listing:")
        }
    }

    //simple way to clear filters from the main activity.
    fun clearFilters(){
        filterBar.clearFilter()
    }

}
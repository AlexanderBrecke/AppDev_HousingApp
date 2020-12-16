package com.example.housingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.housingapp.fragments.HousingAddFragment
import com.example.housingapp.fragments.HousingCurrentFragment
import com.example.housingapp.fragments.HousingListFragment
import com.example.housingapp.housing.Housing
import com.example.housingapp.housing.HousingType
import com.example.housingapp.views.CustomFilterBarLayout
import com.example.housingapp.views.CustomHeaderBarLayout
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private val mockData = mutableListOf<Housing>()
    private val mock = MockData()


    //Setup some variables we will use in the activity
    private val housingList: MutableList<Housing> = mutableListOf()

    private var currentFragment:Fragment? = null
    private lateinit var housingListFragment: HousingListFragment

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
        mockData.add(mock.cabinInTheWoods)
        mockData.add(mock.hotelRoom1)
        mockData.add(mock.conjuringHouse)
        mockData.add(mock.underTheGround)
        mockData.add(mock.elmStret)

        getHousingsInSharedPref(true)
        if(housingList.isEmpty()){
            addMockData()
            getHousingsInSharedPref(true)
        }

        //Initialize the housing list fragment
        housingListFragment = HousingListFragment(housingList)

        //Initialize the header and filter bars
        headerBar = CustomHeaderBarLayout(findViewById(R.id.mainHeaderBar))

        //Initialize the filter bar
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

    override fun onResume() {
        super.onResume()

    }

    override fun onBackPressed() {
        //Setup some extra logic, that if you press back when in list view it will take you back to the "front page"
        if(supportFragmentManager.backStackEntryCount == 1 && containerView.isVisible){
            setFragment(housingListFragment,true)
            showOrHideViews(listOf(containerView), true)
            showOrHideViews(listOf(welcomeView,descriptionView,browseButton), false)
        }

        //Make sure it will still run normally
        super.onBackPressed()

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

    private fun getHousingsInSharedPref(allowDuplicate:Boolean){
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val sharedPrefHousings = sharedPref.getStringSet(getString(R.string.shared_pref_housings), setOf())
        val gson = Gson()
        housingList.clear()

        sharedPrefHousings?.let {
            for(house in it){
                val housing  = gson.fromJson(house, Housing::class.java)
                Log.d("FOO", "${housing.address}")

                //If we want to not have duplicates
                if(!allowDuplicate){
                    var doesHousingExist = false
                    for(housingInList in housingList){
                        if(housingInList == housing){
                            doesHousingExist = true
                        }
                    }
                    if(!doesHousingExist) housingList.add(housing)
                }else{
                    housingList.add(housing)
                }
            }
        }
    }

    private fun addMockData(){

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = sharedPref.edit()
        val gson = Gson()

        //Get housings from sharedPref
        val housings = sharedPref.getStringSet(getString(R.string.shared_pref_housings), setOf())?.toMutableList()
        for(mock in mockData){
            val housingJson = gson.toJson(mock)
            housings?.add(housingJson)
        }
        editor.putStringSet(getString(R.string.shared_pref_housings), housings?.toSet())
        editor.commit()

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

    //simple way to clear filters from the main activity.
    fun clearFilters(){
        filterBar.clearFilter()
    }

}
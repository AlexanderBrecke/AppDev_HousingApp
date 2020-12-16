package com.example.housingapp.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housingapp.extras.HelperClass
import com.example.housingapp.extras.indicator.Indicator

//Make a carousel view class that extends from recycler view.
class HorizontalView(context: Context, attrs: AttributeSet? = null):RecyclerView(context, attrs) {

    //Keep a list of all visible views,
    // as well as indicator to what adapter position is the first and last visible views.
    private var allViews = mutableListOf<View>()
    var firstView:Int = 0
    var lastView:Int = 0

    //Make an initialization function.
    //It is of the type view holder, and takes a new adapter of the view holder type
    fun<T:ViewHolder> initialize(newAdapter:Adapter<T>, indicator: Indicator){

        //We want the recycler view to scroll horizontally instead of vertically
        //Set this in the layout manager
        layoutManager = LinearLayoutManager(context, HORIZONTAL,false)

        //Put a data observer on the adapter as we want to do some things if the data changes.
        newAdapter.registerAdapterDataObserver(object :RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                post {
                    //Make sure we have access to the visible views in our list
                    getAllViews()

                    //Run the on scroll changed function to make sure we access to the first and last views,
                    // as well as scale the images from the start.
                    onScrollChanged(indicator)

                    addOnScrollListener(object :OnScrollListener(){
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)

                            onScrollChanged(indicator)
                        }
                    })
                }
            }
        })
        //Set the adapter to the new adapter
        adapter = newAdapter
    }

    //The indicators need to know what is the first and last visible views.
    //Let's make a function to see if the views have changed.
    //It gets the first and last visible item positions from the layout manager
    //Then it checks if the class variable matches this.
    //If not, we set them to be the found values and return true
    private fun viewHasChanged():Boolean{
        var viewHasChanged = false
        var shouldBeFirstView = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        var shouldBeLastView = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        if(firstView != shouldBeFirstView || lastView != shouldBeLastView){
            firstView = shouldBeFirstView
            lastView = shouldBeLastView
            viewHasChanged = true

        }
        return viewHasChanged
    }

    //As we will hold a list of all visible views, we will also make a simple function to update the list.
    //Simply put, if the views have changed, we will run this function to make sure our list is up to date.
    private fun getAllViews(){
        allViews.clear()
        (0 until childCount).forEach {
            allViews.add(getChildAt(it))
        }
    }

    //Function to run when scroll happens
    private fun onScrollChanged(indicator: Indicator){
        if(viewHasChanged()) getAllViews()
        setImagesGaussianScale(indicator)
    }

    //Make a function to scale the images according to their position within the recycler view.
    private fun setImagesGaussianScale(indicator: Indicator){
        post{
            //We want to scale the items in the list according to a gaussian scale.
            //The gaussian scale gives us a bell curve with the center being bigger and the sides tapering off (To read more, see the Helper class)
            (0 until childCount).forEach{ position ->
                //We will keep a reference to the current image's view, as well as the center of it and the recycler view.
                val child = getChildAt(position)
                val childCenterX = (child.left + child.right) / 2
                val recyclerViewCenterX:Int = (left + right) / 2

                //In this case, we will use the child's center as the variable for our gaussian scale
                //We will give the scale a magnitude from 1 to 1.5.
                //We will use the recycler view's center on the x axis as the point for the center of the bell curve.
                //Then we will give the bell curve a width of 150.
                val scaleValue = HelperClass.getGaussianScale(childCenterX.toFloat(),1f,0.5f,recyclerViewCenterX.toFloat(),150.0)

                //We will then scale the image according to the value.
                child.scaleX = scaleValue
                child.scaleY = scaleValue

            }
            //As we have indicators for what image is being viewed, we will also make sure to set the indicator animations.
            indicator.setIndicatorAnimations(allViews,firstView,lastView)
        }
    }

}
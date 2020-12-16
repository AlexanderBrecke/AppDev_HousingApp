package com.example.housingapp.extras.indicator

import android.util.Log
import android.view.View
import android.widget.TableRow
import androidx.core.view.get
import androidx.core.view.size
import com.example.housingapp.extras.HelperClass
import kotlin.math.max

class Indicator(private val indicatorTable: TableRow) {

    private var dataSize: Int = 0
    private var minScale:Float = 0.33f
    private var maxScale:Float = 0.5f

    //Make a function that retrieves the data size
    fun getDataSize():Int{
        return dataSize
    }

    //Also needs one to set the data size.
    fun setDataSize(dataSize:Int){
        this.dataSize = dataSize
    }

    //Make set functions for the min and max scales
    fun setMinScale(minScale:Float){
        this.minScale = minScale
    }

    fun setMaxScale(maxScale:Float){
        this.maxScale = maxScale
    }

    //Make a function to fill the indicator table with indicators.
    fun fillIndicatorTable(listOfIndicators:MutableList<View>){
        for(indicator in listOfIndicators){
            indicatorTable.addView(indicator)
        }
    }

    //Now for the main reason of this class.
    //Functionality to animate the indicators by scaling them.
    //This function will need to know about all the visible views, as we will use the scale of these to scale the indicators.
    //It will also need to know the adapter position of the first and last views.
    //All hard-coded numbers in this function would ideally have been using variables as this is intended to be flexible and generic.
    fun setIndicatorAnimations(allViews:List<View>, firstVisible:Int, lastVisible:Int){

        //We will use the count of indicators as the steps we will loop through.
        val steps = indicatorTable.childCount

        //Need an iterator to loop with
        var i:Int = 0
        //Start a while loop, as we will manipulate the iterator in ways we are not usually allowed in for loops.
        while (i < steps){
            //Set an arbitrary scale-by value, and scale the indicator at the index of the iterator.
            //This is to get all indicators scaled even if there is no image at this index to scale by
            var scaleBy = minScale*(steps/2)
            var currentIndicator:View = indicatorTable[i]
            currentIndicator.scaleX = scaleBy
            currentIndicator.scaleY = scaleBy

            //We will then check if the iterator is between the first and last visible image values.
            if(i in firstVisible until lastVisible){
                //If it is, we will want to loop through the views we have and scale accordingly
                for (v:Int in allViews.indices){
                    //We will use the image scale as the variable and multiply it by ten, then scale it between 0 and 10.
                    //We will set the center position to be 15, as this is approximately the highest number of the multiplication by 10
                    //We will then use the steps as the spread of the curve.
                    scaleBy = HelperClass.getGaussianScale(allViews[v].scaleX*10,0f,10f,15f,steps.toDouble())

                    //When we have gotten the scale value for the current image, we will get a reference to the current indicator.
                    //We will create a new scale, using the previously created value as the variable
                    //Using the class's own minimum and maximum scales to set the high and low points of the scale, the call to this function won't be too long.
                    //We will then set the center position, in this case 10 is the highest we can get from the variable so we set it to that.
                    //Then we will use steps times one and a half to smooth the curve out a little by widening the spread.
                    //Lastly we will scale the current indicator.
                    currentIndicator = indicatorTable[i]
                    val currentScale = HelperClass.getGaussianScale(scaleBy,minScale,maxScale,10f, steps.toDouble()*1.5)
                    currentIndicator.scaleX = currentScale
                    currentIndicator.scaleY = currentScale

                    //We will add to our iterator as long as we are not on the last point of this current loop.
                    if(v < allViews.size - 1) i++
                }
            }
            //Add to the iterator normally
            i++
        }

    }

}
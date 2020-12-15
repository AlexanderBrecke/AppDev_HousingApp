package com.example.housingapp.extras.indicator

import android.util.Log
import android.view.View
import android.widget.TableRow
import androidx.core.view.get
import com.example.housingapp.extras.HelperClass
import kotlin.math.max

class Indicator(private val indicatorTable: TableRow) {

    private var dataSize: Int = 0

    fun getDataSize():Int{
        return dataSize
    }

    fun setDataSize(dataSize: Int){
        this.dataSize = dataSize
    }

    //Make a function to fill the indicator table with indicators.
    //Make it public so we can call it from where we create the instance of this class
    fun fillIndicatorTable(listOfIndicators:MutableList<View>){
        for(indicator in listOfIndicators){
            indicatorTable.addView(indicator)
        }
    }

    fun setIndicatorAnimations(allViews:List<View>, minScale:Float, maxScale:Float, firstVisible:Int, lastVisible:Int){

        val steps = indicatorTable.childCount

        var i:Int = 0
        while (i < steps){
            var scaleBy = 0f
            var currentIndicator:View = indicatorTable[i]
            val currentScale = HelperClass.getGaussianScale(scaleBy,minScale, maxScale,10f,steps.toDouble()*1)
            currentIndicator.scaleX = currentScale
            currentIndicator.scaleY = currentScale
            if(i in firstVisible until lastVisible){
                for (v:Int in allViews.indices){
                    scaleBy = getTheScalingInView(allViews[v].scaleX*10,steps.toDouble())
                    currentIndicator = indicatorTable[i]
                    val currentScale = HelperClass.getGaussianScale(scaleBy,minScale,maxScale,10f, steps.toDouble()*1.5)
                    currentIndicator.scaleX = currentScale
                    currentIndicator.scaleY = currentScale
                    if(v < allViews.size - 1) i++
                }
            }
            i++
        }

    }

    private fun getTheScalingInView(viewScale:Float,steps:Double):Float{
        return HelperClass.getGaussianScale(viewScale, 0f,10f,15f,steps)

    }


}
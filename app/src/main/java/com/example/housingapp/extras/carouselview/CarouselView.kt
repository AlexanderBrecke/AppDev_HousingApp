package com.example.housingapp.extras.carouselview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housingapp.extras.HelperClass
import com.example.housingapp.extras.carouselview.old.CarouselLinearLayoutManager1
import kotlin.math.pow


//Make a carousel view class that extends from recycler view.
class CarouselView(context: Context, attrs: AttributeSet? = null):RecyclerView(context, attrs) {


    //Make an initialization function.
    //It is of the type view holder, and takes a new adapter of the view holder type
    fun<T:ViewHolder> initialize(newAdapter:Adapter<T>){

        //We want the recycler view to scroll horizontally instead of vertically
        //Set this in the layout manager
        layoutManager = LinearLayoutManager(context, HORIZONTAL,false)

        //Put a data observer on the adapter as we want to do some things if the data changes.
        newAdapter.registerAdapterDataObserver(object :RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                post {
                    //Set padding to the items in the list
                    pad()
                    //Make it scroll to the first image
                    scrollToPosition(0)
                    //Hack to make sure the on scroll changed function is still run if there is only one item
                    onScrollChanged()

                    addOnScrollListener(object :OnScrollListener(){
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            onScrollChanged()
                        }
                    })

                }
            }
        })

        //Set the adapter to the new adapter
        adapter = newAdapter
    }

    private fun pad(){
        (0 until childCount).forEach{
            //Make the padding 1/4 of the image size, so we get half the image size in spacing between images
            val sidePadding = (getChildAt(it).width/4)
            //Make the first image use twice the padding on the left so it is centered in the view,
            //and do the same for the last image with the padding on the right.
            when(it){
                0 -> getChildAt(it).setPadding((sidePadding*2),0,sidePadding,0)
                (childCount - 1) -> getChildAt(it).setPadding(sidePadding,0,(sidePadding*2),0)
                else -> getChildAt(it).setPadding(sidePadding,0,sidePadding,0)
            }
        }
    }

    //Function to run when scroll happens
    private fun onScrollChanged(){
        post{
            //We want to scale the items in the list according to a gaussian scale.
            //The gaussian scale gives us a bell curve with the center being bigger and the sides tapering off.
            (0 until childCount).forEach{ position ->
                val child = getChildAt(position)
                val childCenterX = (child.left + child.right) / 2
                val recyclerViewCenterX:Int = (left + right) / 2

                //In this case, we will use the child's center as the variable for our gaussian scale
                //We will give the scale a magnitude from 1 to 1.5.
                //We will use the recycler view's center on the x axis as the point for the center of the bell curve.
                //Then we will give the bell curve a width of 150.
                val scaleValue = HelperClass.getGaussianScale(childCenterX,1f,0.5f,recyclerViewCenterX.toFloat(),150.0)

                //Then we will scale the child according to the value.
                child.scaleX = scaleValue
                child.scaleY = scaleValue
            }
        }
    }

    //Function to get the gaussian scale to scale the images
    private fun getGaussianScale(childCenterX:Int, minScaleOffset:Float,scaleFactor:Float,spreadFactor:Double):Float{

        val recyclerCenterX = (left + right) / 2
        return (Math.E.pow(-(childCenterX - recyclerCenterX.toDouble()).pow(2.toDouble()) / (2 * spreadFactor.pow(2.toDouble()))) *scaleFactor + minScaleOffset).toFloat()
    }


}
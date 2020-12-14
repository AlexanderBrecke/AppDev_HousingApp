package com.example.housingapp.extras.carouselview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TableRow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housingapp.extras.HelperClass

//Make a carousel view class that extends from recycler view.
class CarouselView(context: Context, attrs: AttributeSet? = null):RecyclerView(context, attrs) {

    val allViews = mutableListOf<View>()

    //Make an initialization function.
    //It is of the type view holder, and takes a new adapter of the view holder type
    fun<T:ViewHolder> initialize(newAdapter:Adapter<T>, currentImageTable:TableRow){

        //We want the recycler view to scroll horizontally instead of vertically
        //Set this in the layout manager
        layoutManager = LinearLayoutManager(context, HORIZONTAL,false)


        //Put a data observer on the adapter as we want to do some things if the data changes.
        newAdapter.registerAdapterDataObserver(object :RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                post {
                    //Set padding to the items in the list
                    pad()
                    //Make a reference list to all views in the data set.
                    //We will use this in the scaling later
                    (0 until childCount).forEach{
                        allViews.add(getChildAt(it))
                    }
                    //Make it scroll to the first image
                    scrollToPosition(0)

                    addOnScrollListener(object :OnScrollListener(){
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            onScrollChanged(currentImageTable)
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
    private fun onScrollChanged(allCirclesInTable:TableRow){
        setImagesGaussianScale(allCirclesInTable)
    }

    private fun setImagesGaussianScale(allCirclesInTable: TableRow){
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
                val scaleValue = HelperClass.getGaussianScale(childCenterX.toFloat(),1f,0.5f,recyclerViewCenterX.toFloat(),150.0)

                //Then we will scale the child according to the value.
                child.scaleX = scaleValue
                child.scaleY = scaleValue
            }
            setCircleAnimations(allCirclesInTable)
        }
    }

    private fun setCircleAnimations(allCirclesInTable: TableRow){

        //We want to scale the circles underneath the images according to what image is scaled up.

        //We will need to know how many steps we iterate through.
        //We set this to the count of images, or in this case count of circles in table as they have been programmed to be the same amount.
        val steps = allCirclesInTable.childCount

        //In this case, we will need the scale of all the images in the form of a gaussian scale.
        //We will loop through all the circles in the table that represents all the images.
        (0 until allCirclesInTable.childCount).forEach {

            //We want to get a scale of the current image between 0 and 10.
            //We will use the x scale of the image at the index we are looking at and multiply it by 10 to get more fluctuation.
            //We will have an offset of 0 because we want to get an answer between 0 and 10, and for the same reason, our magnitude will be 10.
            //The center position of the curve will be the highest scale we can get when we have multiplied the image scale by 10.
            //In this case it is about 15, so we will set it to that.
            //We will then use the above mentioned steps as the spread of the curve.
            //Over the course of all the images, this will give us a curve of scales between 0 and 10
            val currentImageScale = HelperClass.getGaussianScale(allViews[it].scaleX*10, 0f,10f, 15f, steps.toDouble())

            //We will then want to scale the current circle according to the scale of the current image.
            //We will get a reference to the current circle so we can scale it.
            val currentCircle = allCirclesInTable.getChildAt(it)

            //We will then create a scale value for said circle by using a new gaussian scale.
            //By inputting the previously created image scale as the variable,
            //and setting the scale between 0.25 and 0.75, with the center position being the highest output of the image scale
            //we should get a scale value for the currently viewed image between 0.25 and 1.
            //We will use twice the number of steps for the width to not get a dramatic drop between the outputs.
            val currentCircleScale = HelperClass.getGaussianScale(currentImageScale, 0.25f, 0.75f, 10f, steps.toDouble()*2)

            //We will then use this value as the scale for the current circle.
            currentCircle.scaleY = currentCircleScale
            currentCircle.scaleX = currentCircleScale

        }
    }



}
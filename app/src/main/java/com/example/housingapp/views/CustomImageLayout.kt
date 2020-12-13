package com.example.housingapp.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.housingapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_custom_image.view.*

class CustomImageLayout(context: Context, attrs:AttributeSet? = null):FrameLayout(context,attrs){
    private val image: ImageView

    init {
        //Defines XML layout for this custom view
        val view = LayoutInflater.from(context).inflate(R.layout.layout_custom_image, this)
        image = view.customImage_imageView
    }

    fun setImage(imageUrl:String){
        Picasso.get().load(imageUrl).into(image)
    }
}
package com.example.housingapp.views

import android.view.View
import android.widget.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_custom_header_bar.view.*

class CustomHeaderBarLayout(view: View){

    private val title:TextView = view.title_textView
    val filterIconButton: ImageView = view.headerBar_filterIcon_imageView

    init {
        Picasso.get().load("https://www.freeiconspng.com/uploads/settings-icon-13.png").into(filterIconButton)
    }

    fun setTitle(newTitle:String){
        title.text = newTitle
    }




}
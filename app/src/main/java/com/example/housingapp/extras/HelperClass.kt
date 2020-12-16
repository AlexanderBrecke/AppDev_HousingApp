package com.example.housingapp.extras

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.marginStart
import org.w3c.dom.Text
import kotlin.math.pow

class HelperClass {

    companion object{

        //Create an abstract function that will fill a table with views from an array of enum types.
        //Loop through all the items in the enum array.
        //If the current index of the loop is the modulo of max items per row, create a new table row.
        //Create a view for each enum item, set the text to the name of the enum item.
        //If we have a list we should add to, add the view to the list for easy access.
        //Add the view to the table row for them to show in the UI.
        fun setUpTableLayoutFromEnum(context: Context, tableToSetUp: TableLayout, viewType:String, enum: Array<Enum<*>>, maxItemsPerRow: Int, listToPutItIn:MutableList<*>? = null){
            var tableRow:TableRow? = null
            for(i:Int in enum.indices){
                if(i % maxItemsPerRow == 0){
                    tableRow = TableRow(context)
                    tableToSetUp.addView(tableRow)
                }

                var view:View

                when(viewType.toLowerCase()){
                    "checkbox" ->{
                        view = CheckBox(context)
                        view.text = enum[i].name
                        if(listToPutItIn != null){
                            listToPutItIn as MutableList<CheckBox>
                            listToPutItIn.add(view)
                        }
                        tableRow?.let {
                            tableRow.addView(view)
                        }
                    }

                    "text view" -> {
                        var currentView:TextView = TextView(context)
                        currentView.text = enum[i].name
                        currentView.textSize = 18f
                        if(i < enum.indices.last){
                            currentView.text = currentView.text.toString() + ", "
                        }

                        if(listToPutItIn != null){
                            listToPutItIn as MutableList<TextView>
                            listToPutItIn.add(currentView)
                        }
                        tableRow?.let {
                            tableRow.addView(currentView)
                        }
                    }

                    else -> {
                        Toast.makeText(context,"You should not get here!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        //Need to make an abstract function to create a gaussian scale.

        // Gaussian scale formulae: f(x) = a * exp( - ( (x-b)^2 / 2c^2 ) )

        // "f" defines a function
        // x is the variable

        // a - magnitude of the bell curve (height of the peak) - The highest output of the scale
        // b - position of the center of the peak
        // c - width of the curve (standard deviation aka RMS width)

        //This function will take an input variable as a float - (x),
        // a minimum scale offset as a float,
        // a magnitude as a float - (a),
        // a center position as a float - (b),
        // as well as a width as a double - (c)

        //Using these inputs, we will create a gaussian scale.
        //The gaussian scale will use the (x) variable, and measure it against the (b) center position.
        //Using the c width as a standard deviation, we will know how far the (x) variable is from the (b) center position
        //The mathematical function will then return a float value (f(x)) between the minimum scale offset and (a) magnitude
        // depending on how close the (x) variable is to the (b) center position.
        fun getGaussianScale(variable_x: Float, minScaleOffset: Float, magnitude_a:Float, centerPos_b:Float, width_c:Double):Float{

            return(
                    Math.E.pow(
                            -(variable_x - centerPos_b).pow(2)
                                    /
                                    (2 * width_c.pow(2.toDouble()))
                    )*magnitude_a + minScaleOffset
                    ).toFloat()

        }





    }


}
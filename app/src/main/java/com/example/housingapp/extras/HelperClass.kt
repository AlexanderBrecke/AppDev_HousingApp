package com.example.housingapp.extras

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.TableLayout
import android.widget.TableRow

class HelperClass {

    companion object{

        //Create an abstract function that will fill a table with checkboxes from an enum class.
        //Loop through all the items in the enum class.
        //If the current index of the loop is the modulo of max checkboxes per row, create a new table row.
        //Create a checkbox for each enum item, set the text to the name of the enum item.
        //Add the checkbox to the set list of checkboxes for easy access.
        //Add the checkbox to the table row for them to show in the UI
        fun setUpTableLayoutWithCheckboxesFromEnum(context: Context, tableToSetUp: TableLayout, enum: Array<Enum<*>>, maxItemsPerRow: Int, checkBoxes:MutableList<CheckBox>){
            var tableRow:TableRow? = null
            for(i:Int in enum.indices){
                if(i % maxItemsPerRow == 0){
                    tableRow = TableRow(context)
                    tableToSetUp.addView(tableRow)
                }
                var checkBox:CheckBox = CheckBox(context)
                checkBox.text = enum[i].name
                checkBoxes.add(checkBox)

                tableRow?.let{
                    tableRow.addView(checkBox)
                }
            }
        }

    }


}
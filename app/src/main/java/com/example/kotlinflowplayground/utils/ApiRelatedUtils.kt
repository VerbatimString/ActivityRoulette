package com.example.kotlinflowplayground.utils

object ApiRelatedUtils {
    fun convertAccessibilityFloatToARelatedString(accessibility : Float) : String{
        var stringToReturn = ""

        if(accessibility >= 0 && accessibility <= 0.33){
            stringToReturn = "No effort"
        }

        else if(accessibility >= 0.34 && accessibility <= 0.66)
            stringToReturn = "Slight effort"

        else{
            stringToReturn = "Tiring but rewarding"
        }

        return stringToReturn
    }
}
package info.fekri.dunibazaar.util

import android.annotation.SuppressLint
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import java.text.SimpleDateFormat
import java.util.Calendar

val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.v("error", "Error -> ${throwable.message}")
}

fun stylePrice(oldPrice: String): String {

    if (oldPrice.length > 3) {
        val reversed = oldPrice.reversed()
        var newPrice = ""
        for (i in oldPrice.indices) {
            newPrice += reversed[i]
            if ((i+1) % 3 == 0) {
                newPrice += ','
            }
        }
        val readyToGo = newPrice.reversed()

        // if you don't use it, when the number is (for example) 532,234
        // it will be like this: ,532,234
        // so we need it :-)
        if (readyToGo.first() == ',') {
            return readyToGo.substring(1) + " Tomans"
        }

        return "$readyToGo Tomans"
    }

    return "$oldPrice Tomans"
}

@SuppressLint("SimpleDateFormat")
fun styleTime(timeInMillis: Long): String {

    val dateFormatter = SimpleDateFormat("yyyy/MM/dd-hh:mm")

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis

    return dateFormatter.format(calendar.time)
}
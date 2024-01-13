package gr.ihu.iee.aboard.android.util.date

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    private const val DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm"

    fun isDateValid(dateString: String): Boolean {
        val formatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())

        return try {
            val date = formatter.parse(dateString)

            date?.after(Date()) ?: false
        } catch (e: Exception) {
            false
        }
    }
}
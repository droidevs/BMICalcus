package io.droidevs.bmicalc.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toJavaZoneId
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@Composable
fun rememberSmartDateFormat(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) : (Instant) -> String{
    val formatDate = rememberDateFormatter(timeZone = timeZone)
    val today = remember { Clock.System.now().toLocalDateTime(timeZone = timeZone).date }

    return { instant ->
        val date = instant.toLocalDateTime(timeZone).date
        when (date) {
            today -> "Today"
            today.minus(1, DateTimeUnit.DAY) -> "Yesterday"
            else -> formatDate(instant)
        }
    }
}
@Composable
fun rememberDateFormatter(
    formatType: DateFormatType = DateFormatType.SHORT,
    timeZone : TimeZone = TimeZone.currentSystemDefault()
): (Instant) -> String {

    val formatter = remember(formatType, timeZone) {
        SimpleDateFormat(formatType.pattern,Locale.getDefault()).apply {
            this.timeZone = timeZone.toJavaTimeZone()
        }
    }

    return { instant ->
        formatter.format(Date(instant.toEpochMilliseconds()))
    }
}


@Composable
fun FormattedDateDisplay(
    modifier: Modifier = Modifier,
    instant: Instant
){
    val formatDate = rememberSmartDateFormat()
    Text(
        modifier = modifier,
        text = formatDate(instant)
    )
}

fun TimeZone.toJavaTimeZone(): java.util.TimeZone{
    return java.util.TimeZone.getTimeZone(id)
}

enum class DateFormatType(val pattern: String) {
    SHORT("MMM dd, yyyy"),
    MEDIUM("EEE, MMM d, yyyy"),
    LONG("EEEE, MMMM d, yyyy"),
    FULL("yyyy-MM-dd HH:mm:ss"),
    ISO("yyyy-MM-dd'T'HH:mm:ssZ")
}
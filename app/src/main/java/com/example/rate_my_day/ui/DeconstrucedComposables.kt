package com.example.rate_my_day.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rate_my_day.data.db.RateDayEntity
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun RateMyDayHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp) // Adjust height to your liking
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF7B801), // Bright gradient start color
                        Color(0xFFF18701)  // Gradient end color
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Star Icon",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = "Rate My Day",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Screens.View.name,
            onClick = { navController.navigate(Screens.View.name) },
            icon = { Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Calendar") },
            label = { Text("View")}
        )
        NavigationBarItem(
            selected = currentRoute?.startsWith(Screens.Rate.name) == true,
            onClick = {
                navController.navigate(Screens.Rate.name) {
                    popUpTo(Screens.View.name) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(imageVector = Icons.Filled.Star, contentDescription = "Star") },
            label = { Text("Rate")}
        )
        NavigationBarItem(
            selected = currentRoute == Screens.Theme.name,
            onClick = { navController.navigate(Screens.Theme.name) },
            icon = { Icon(imageVector = Icons.Filled.Edit, contentDescription = "Theme") },
            label = { Text("Theme")}
        )
    }
}

@Composable
fun Day(day: CalendarDay, stars: Int, onDayClick: () -> Unit) {
    //Determine the color based on the rating
    val ratingColor = when (stars) {
        5 -> Color(0xFFF35B04) //Persimmon
        4 -> Color(0xFFF18701) //Tangerine
        3 -> Color(0xFFF7B801) //Selective Yellow
        2 -> Color(0xFF7678ED) //Medium Slate Blue
        1 -> Color(0xFF3D348B) //Tekhelet
        else -> Color.Transparent
    }

    Box(modifier = Modifier
        .aspectRatio(1f)
        .padding(4.dp)
        .clickable(onClick = onDayClick), // Handle day click
        contentAlignment = Alignment.Center
    ) {
        //show the coloured circle if the date has a rating
        if (stars > 0) {
            Box (
                modifier = Modifier
                    .size(36.dp)
                    .background(color = ratingColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {}
        }
        Text(
            text = day.date.dayOfMonth.toString(),
            fontSize = 16.sp,
            color = if (stars >0) Color.White else Color.Black //Adjust text color for visibility
        )

    }
}

@Composable
fun DaysOfWeekTitle() {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
}

@Composable
fun DayOptionsDialog(
    selectedDay: LocalDate,
    selectedRateDay: RateDayEntity?,
    onDismiss: () -> Unit,
    onEditOrAdd: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Rating") },
        text = {
            Column {
                Text(text = selectedDay.format(dateFormatter))
                Spacer(modifier = Modifier.height(8.dp))
                if (selectedRateDay != null) {
                    Text(text = "This day has a rating of ${selectedRateDay.stars} stars.")
                } else {
                    Text(text = "This day has not been rated yet.")
                }
            }
        },

        confirmButton = {
            Button(onClick = onEditOrAdd) {
                Text(text = if (selectedRateDay != null) "Edit" else "Add")
            }
        },
        dismissButton = if (selectedRateDay != null) {
            {
                Button(onClick = onDelete) {
                    Text(text = "Delete")
                }
            }
        } else null
    )
}

@Composable
fun ViewStars(
    rating: Int,
    modifier: Modifier
) {

}

/**
 * Used for database testing and visibility of RatedDays table.
@Composable
fun ReadTableRatedDays(viewModel: RateMyDayViewModel) {
val ratedDays by viewModel.ratedDays.collectAsState()

Column (
modifier = Modifier
.fillMaxSize()
.padding(16.dp)
) {
Text("Rated Days")

LazyColumn {
items(ratedDays) { rateDay ->
Text("Date: ${rateDay.date} | Stars: ${rateDay.stars}")
}
}
}
}*/


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RatingLegend() {
    Column (
        Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        //horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RatingLegendItem(color = Color(0xFF3D348B), description = "1 Star - Very Bad")
        RatingLegendItem(color = Color(0xFF7678ED), description = "2 Stars - Bad")
        RatingLegendItem(color = Color(0xFFF7B801), description = "3 Stars - Okay")
        RatingLegendItem(color = Color(0xFFF18701), description = "4 Stars - Good")
        RatingLegendItem(color = Color(0xFFF35B04), description = "5 Stars - Excellent")
    }
}

@Composable
fun RatingLegendItem(color: Color, description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color = color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium.copy(color = color)
        )
    }
}

@Composable
fun StarRating(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "$i Stars",
                tint = if (i <= rating) Color(0xFFF7B801) else Color.Gray,
                modifier = Modifier
                    .size(64.dp)
                    .clickable { onRatingChange(i) }
                    .padding(horizontal = 4.dp)
            )
        }
    }
}
package com.example.rate_my_day.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
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
import com.example.rate_my_day.data.Preferences
import com.example.rate_my_day.data.db.RateDayEntity
import com.example.rate_my_day.ui.theme.LightColorScheme
import com.example.rate_my_day.ui.theme.star1
import com.example.rate_my_day.ui.theme.star2
import com.example.rate_my_day.ui.theme.star3
import com.example.rate_my_day.ui.theme.star4
import com.example.rate_my_day.ui.theme.star5
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun RateMyDayHeader(preferences: Preferences) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp) // Adjust height to your liking
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LightColorScheme.star1(preferences = preferences), // Bright gradient start color
                        LightColorScheme.star5(preferences = preferences)  // Gradient end color
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
fun BottomNavigationBar(navController: NavController, viewModel: RateMyDayViewModel) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val ratedDays by viewModel.ratedDays.collectAsState(emptyList())

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Screens.View.name,
            onClick = { navController.navigate(Screens.View.name) },
            icon = { Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Calendar") },
            label = { Text("View") }
        )
        NavigationBarItem(
            selected = currentRoute?.startsWith(Screens.Rate.name) == true,
            onClick = {
                val today = LocalDate.now()
                val todayInMillis = today.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000
                val todayRateDay = ratedDays.firstOrNull { it.date == todayInMillis }

                val dateArgument = today.toString()
                val starsArgument = todayRateDay?.stars ?: 0
                val commentArgument = todayRateDay?.comment ?: ""

                navController.navigate("${Screens.Rate.name}/$dateArgument/$starsArgument/$commentArgument") {
                    popUpTo(Screens.View.name) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(imageVector = Icons.Filled.Star, contentDescription = "Star") },
            label = { Text("Rate") }
        )
        NavigationBarItem(
            selected = currentRoute == Screens.Theme.name,
            onClick = { navController.navigate(Screens.Theme.name) },
            icon = { Icon(imageVector = Icons.Filled.Edit, contentDescription = "Theme") },
            label = { Text("Theme") }
        )
    }
}

@Composable
fun Day(day: CalendarDay, stars: Int, onDayClick: () -> Unit, preferences: Preferences) {
    val isToday = day.date == LocalDate.now()
    val isFutureDay = day.date.isAfter(LocalDate.now())

    //Determine the color based on the rating
    val ratingColor = when (stars) {
        5 -> LightColorScheme.star5(preferences = preferences) //Persimmon
        4 -> LightColorScheme.star4(preferences = preferences) //Tangerine
        3 -> LightColorScheme.star3(preferences = preferences) //Selective Yellow
        2 -> LightColorScheme.star2(preferences = preferences) //Medium Slate Blue
        1 -> LightColorScheme.star1(preferences = preferences) //Tekhelet
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clickable(onClick = onDayClick), // Handle day click
        contentAlignment = Alignment.Center
    ) {
        //Show the coloured circle if the date has a rating
        if (stars > 0) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color = ratingColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {}
        }
        //Add a circle for today's date
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isToday) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color = Color(0xFF574AE2), shape = CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Text(
            text = day.date.dayOfMonth.toString(),
            fontSize = 16.sp,
            color = when {
                isFutureDay -> Color.Gray
                stars > 0 -> Color.White
                else -> Color.Black
            }
        )

    }
}


@Composable
fun MonthHeader(
    month: YearMonth,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowLeft, // Updated Material 3 icon
            contentDescription = "Previous Month"
        )
        Text(
            text = "${
                month.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                )
            } ${month.year}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight, // Updated Material 3 icon
            contentDescription = "Next Month"
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
                fontWeight = FontWeight.Bold
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
    val isFutureDay = selectedDay.isAfter(LocalDate.now())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Rating") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = selectedDay.format(dateFormatter),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (isFutureDay) {
                    Text(
                        text = "Future days can't be rated.",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                } else if (selectedRateDay != null) {
                    ViewStars(
                        rating = selectedRateDay.stars,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                    selectedRateDay.comment?.let { comment ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = comment,
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    Text(
                        text = "This day has not been rated yet.",
                        textAlign = TextAlign.Center
                    )
                }
            }
        },

        confirmButton = {
            if (!isFutureDay) {
                Button(onClick = onEditOrAdd) {
                    Text(text = if (selectedRateDay != null) "Edit" else "Add")
                }
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
    Row(modifier = modifier) {
        repeat(rating) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Color(0xFFF7B801),
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

/** Used for database testing and visibility of RatedDays table.
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
Text("Date: ${rateDay.date} | Stars: ${rateDay.stars} | Comment: ${rateDay.comment}")
}
}
}
}
 */

@Composable
fun RatingLegend(preferences: Preferences) {
    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        //horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RatingLegendItem(
            color = LightColorScheme.star1(preferences = preferences),
            description = "1 Star - Very Bad"
        )
        RatingLegendItem(
            color = LightColorScheme.star2(preferences = preferences),
            description = "2 Stars - Bad"
        )
        RatingLegendItem(
            color = LightColorScheme.star3(preferences = preferences),
            description = "3 Stars - Okay"
        )
        RatingLegendItem(
            color = LightColorScheme.star4(preferences = preferences),
            description = "4 Stars - Good"
        )
        RatingLegendItem(
            color = LightColorScheme.star5(preferences = preferences),
            description = "5 Stars - Excellent"
        )
    }
}

@Composable
fun RatingLegendItem(color: Color, description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.5f),
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
            //style = MaterialTheme.typography.bodyMedium.copy(color = color)
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
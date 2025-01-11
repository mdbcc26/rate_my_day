package com.example.rate_my_day.ui

import com.example.rate_my_day.data.db.RateDayEntity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Text
import androidx.compose.material3.Button
//import androidx.compose.material3.Slider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

enum class Screens { View, Rate }

@Composable
fun RateMyDayApp(
    viewModel: CalendarViewModel
) {
    val navController = rememberNavController()
    val backgroundColor = Color(0xFFE9D6B8)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        innerPadding ->
        NavHost(
            navController,
            startDestination = Screens.View.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.View.name) {
                CalendarScreen(viewModel, navController)
            }
            composable(Screens.Rate.name) {
                RateDayFormScreen(
                    viewModel = viewModel,
                    initialDate = LocalDate.now(),
                    initialStars = 0,
                    navController = navController
                )
            }
            composable("${Screens.Rate.name}/{date}/{stars}") { backStackEntry ->
                val date = backStackEntry.arguments?.getString("date")?.let { LocalDate.parse(it) } ?: LocalDate.now()
                val stars = backStackEntry.arguments?.getString("stars")?.toIntOrNull() ?: 0

                RateDayFormScreen(
                    viewModel = viewModel,
                    initialDate = date,
                    initialStars = stars,
                    navController = navController
                    )
            }
        }
    }
}


@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    navController: NavController
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) }   // Show 12 Months before the current month
    val endMonth = remember { currentMonth.plusMonths(12) }         // Show 12 Months after the current month
    val daysOfWeek = remember { daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY) }

    val state = rememberCalendarState( //state loading
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
        )

    val ratedDays = viewModel.ratedDays.collectAsState().value  //Get the list of rated days
    val ratedDatesMap = ratedDays.associateBy { it.date }       //Create a map with date as key

    //Dialog Box state
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }
    var selectedRateDay by remember { mutableStateOf<RateDayEntity?>(null) }
    var isDialogVisible by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        RateMyDayHeader()
        Row {
            //Shows a calendar. Information is pulled from Room database and assigns a colour to the date relating to the amount of stars
            HorizontalCalendar(
                modifier = Modifier.weight(1f),
                state = state,
                dayContent = { day ->
                    val ratedDay = ratedDatesMap[day.date.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000] //Retrieve the rating (stars) for the current date
                    val stars = ratedDay?.stars ?: 0 // Default to 0 if no rating exists

                    Day(day = day, stars = stars, onDayClick = { // Pass both the day and the stars to the Day composable
                        selectedDay = day.date
                        selectedRateDay = ratedDatesMap[day.date.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000]
                        isDialogVisible = true
                    })
                },
                monthHeader = { month ->
                    Text(
                        text = "${month.yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.yearMonth.year}",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    DaysOfWeekTitle()
                }
            )

            if(isDialogVisible && selectedDay != null) {
                DayOptionsDialog(
                    selectedDay = selectedDay!!,
                    selectedRateDay = selectedRateDay,
                    onDismiss = { isDialogVisible = false },
                    onEditOrAdd = {
                        val dateArgument = selectedDay?.toString() ?: LocalDate.now().toString() // Convert Local Date (selected or not) to String
                        val starsArgument = selectedRateDay?.stars ?: 0
                        navController.navigate("${Screens.Rate.name}/$dateArgument/$starsArgument") // Redirect to RateDayFormScreen with date as part of the route
                        isDialogVisible = false
                    },
                    onDelete = {
                        selectedDay?.let { viewModel.deleteRatedDayByDate(it.toEpochMillis()) }
                        isDialogVisible = false
                    }
                )
            }
        }
        Row { RatingLegend() }
        //Row { ReadTableRatedDays(viewModel) }
    }
}
fun LocalDate.toEpochMillis(): Long {
    return this.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Day Options") },
        text = {
            Column {
                Text(text = "Date: $selectedDay")
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
                Text(text = if (selectedRateDay != null) "Edit Rating" else "Add Rating")
            }
        },
        dismissButton = if (selectedRateDay != null) {
            {
                Button(onClick = onDelete) {
                    Text(text = "Delete Rating")
                }
            }
        } else null
    )
}
/**
 * Used for database testing and visibility of RatedDays table.
    @Composable
fun ReadTableRatedDays(viewModel: CalendarViewModel) {
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
    FlowRow (
        Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
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
fun RateDayFormScreen(
    viewModel: CalendarViewModel,
    initialDate: LocalDate,
    initialStars: Int,
    navController: NavController
) {
    var stars by remember { mutableIntStateOf(initialStars) }
    var selectedDate by remember { mutableStateOf(initialDate) }
    val context = LocalContext.current

    val currentDate = LocalDate.now()
    var errorMessage by remember { mutableStateOf("") }

    val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        RateMyDayHeader()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text("Click below to choose date!")
            Button(onClick = {
                val datePickerDialog = android.app.DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            val pickedDate = LocalDate.of(year, month + 1, dayOfMonth)
                            if (pickedDate.isAfter(currentDate)) {
                                errorMessage = "You cannot select a future date."
                            } else {
                                selectedDate = pickedDate
                                errorMessage = "" //Clear error message if valid
                            }
                        },
                        selectedDate.year,
                        selectedDate.monthValue - 1,
                        selectedDate.dayOfMonth
                    )
                    datePickerDialog.show()
                }
            )
            { Text(
                selectedDate.format(dateFormatter)
                )
            }

           //5 point range slider of Stars
            Text("Rating: $stars Stars")
            StarRating(
                rating = stars,
                onRatingChange = { stars = it}
            )
            /*Slider(
                value = stars.toFloat(),
                onValueChange = { stars = it.toInt() },
                valueRange = 1f..5f,
                steps = 3,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            */

            // Save Button
            Button(
                onClick = {
                    when {
                        stars !in 1..5 -> {
                            errorMessage = "Stars cannot be less than 1 or greater than 5."
                        }
                        selectedDate.isAfter(currentDate) -> {
                            errorMessage = "Cannot save a future date."
                        }
                        else -> {
                            val dateInMillis = selectedDate.atStartOfDay()
                                .toEpochSecond(ZoneOffset.UTC) * 1000 // Convert LocalDate to milliseconds
                            val rateDayEntity = RateDayEntity(date = dateInMillis, stars = stars)
                            viewModel.saveRatedDay(rateDayEntity)
                            navController.popBackStack() // Redirect 'back' after saving rating
                            errorMessage = "" // Clear error message on successful save
                        }
                    }
                }
            ) {
                Text("Save")
            }

            // Error Message for Save Button
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
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
    }
}

package com.example.rate_my_day.ui

import com.example.rate_my_day.data.db.RateDayEntity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rate_my_day.data.Preferences
import com.example.rate_my_day.utils.toEpochMillis
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


enum class Screens { View, Rate, Theme }

val themes = List(100) { "Option ${it + 1}" }



@Composable
fun RateMyDayApp(
    viewModel: RateMyDayViewModel,
    preferences: Preferences
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
            startDestination = Screens.Rate.name,
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
            composable(Screens.Theme.name) {
                ThemeScreen(viewModel, navController, preferences = preferences)
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
    viewModel: RateMyDayViewModel,
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
    }
}

@Composable
fun RateDayFormScreen(
    viewModel: RateMyDayViewModel,
    initialDate: LocalDate,
    initialStars: Int,
    navController: NavController
) {
    var stars by remember { mutableIntStateOf(initialStars) }
    val selectedDate by remember { mutableStateOf(initialDate) }

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
            Text(
                selectedDate.format(dateFormatter)
            )

            RatingLegend()

            //5 point range slider of Stars
            Text("Rating: $stars Stars")
            StarRating(
                rating = stars,
                onRatingChange = { stars = it}
            )

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
                            navController.navigate(Screens.View.name) // Redirect to 'View' screen after saving rating
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
fun ThemeScreen(viewModel: RateMyDayViewModel, navController: NavController, preferences : Preferences) {

    val key = remember { mutableStateOf("") }

    val value = remember { mutableStateOf("") }

    val storedInfo = remember { mutableStateOf("") }

    var itemPosition by rememberSaveable() {
        mutableIntStateOf(0)
    }
    var expanded by remember { mutableStateOf(false) }

    val menuItemData = themes

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Button(onClick = { expanded = !expanded }) {
            Text(key.value)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            menuItemData.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {

                    }
                )
            }
        }
    }

}
package com.example.rate_my_day.ui

import androidx.compose.foundation.background
import com.example.rate_my_day.data.db.RateDayEntity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rate_my_day.data.Preferences
import com.example.rate_my_day.ui.theme.LightColorScheme
import com.example.rate_my_day.ui.theme.star1
import com.example.rate_my_day.ui.theme.star2
import com.example.rate_my_day.ui.theme.star3
import com.example.rate_my_day.ui.theme.star4
import com.example.rate_my_day.ui.theme.star5
import com.example.rate_my_day.utils.toEpochMillis
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.round

enum class Screens { View, Rate, Theme }

val themes = listOf(
    "Legacy",
    "Red",
    "Green",
    "Blue",
    "Yellow",
    "Orchard",
    "Rimmy Tim",
    "Fanta",
    "OFISF",
    "Jesus"
)


@Composable
fun RateMyDayApp(
    viewModel: RateMyDayViewModel,
    preferences: Preferences
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController, viewModel, preferences) }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screens.Rate.name, //startDestination,
            modifier = Modifier.padding(innerPadding)

        ) {
            composable(Screens.View.name) {
                CalendarScreen(viewModel, navController, preferences)
            }
            composable(Screens.Rate.name) {
                RateDayFormScreen(
                    viewModel = viewModel,
                    initialDate = LocalDate.now(),
                    initialStars = 0,
                    initialComment = "",
                    navController = navController,
                    preferences
                )
            }
            composable("${Screens.Rate.name}/{date}/{stars}/{comment}") { backStackEntry ->
                val date = backStackEntry.arguments?.getString("date")?.let { LocalDate.parse(it) }
                    ?: LocalDate.now()
                val stars = backStackEntry.arguments?.getString("stars")?.toIntOrNull() ?: 0
                val comment = backStackEntry.arguments?.getString("comment") ?: ""

                RateDayFormScreen(
                    viewModel = viewModel,
                    initialDate = date,
                    initialStars = stars,
                    initialComment = comment,
                    navController = navController,
                    preferences = preferences
                )
            }
            composable(Screens.Theme.name) {
                ThemeScreen(preferences = preferences)
            }
        }
    }
}


@Composable
fun CalendarScreen(
    viewModel: RateMyDayViewModel,
    navController: NavController,
    preferences: Preferences
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

    Column {
        RateMyDayHeader(preferences = preferences)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row {
                //Shows a calendar. Information is pulled from Room database and assigns a colour to the date relating to the amount of stars
                HorizontalCalendar(
                    modifier = Modifier.weight(1f),
                    state = state,
                    dayContent = { day ->
                        val ratedDay = ratedDatesMap[day.date.atStartOfDay()
                            .toEpochSecond(ZoneOffset.UTC) * 1000] //Retrieve the rating (stars) for the current date
                        val stars = ratedDay?.stars ?: 0 // Default to 0 if no rating exists

                        Day(
                            day = day,
                            stars = stars,
                            onDayClick = { // Pass both the day and the stars to the Day composable
                                selectedDay = day.date
                                selectedRateDay = ratedDatesMap[day.date.atStartOfDay()
                                    .toEpochSecond(ZoneOffset.UTC) * 1000]
                                isDialogVisible = true
                            },
                            preferences = preferences
                        )
                    },
                    monthHeader = { month ->
                        MonthHeader(month = month.yearMonth, state = state)

                        DaysOfWeekTitle()

                    }
                )

                if (isDialogVisible && selectedDay != null) {
                    DayOptionsDialog(
                        selectedDay = selectedDay!!,
                        selectedRateDay = selectedRateDay,
                        onDismiss = { isDialogVisible = false },
                        onEditOrAdd = {
                            val dateArgument = selectedDay?.toString() ?: LocalDate.now()
                                .toString() // Convert Local Date (selected or not) to String
                            val starsArgument = selectedRateDay?.stars ?: 0
                            val commentArgument = selectedRateDay?.comment ?: ""
                            navController.navigate("${Screens.Rate.name}/$dateArgument/$starsArgument/$commentArgument") // Redirect to RateDayFormScreen with date as part of the route
                            isDialogVisible = false
                        },
                        onDelete = {
                            selectedDay?.let { viewModel.deleteRatedDayByDate(it.toEpochMillis()) }
                            isDialogVisible = false
                        }
                    )
                }
            }
            Row { RatingLegend(preferences = preferences) }
        }
    }
}

@Composable
fun RateDayFormScreen(
    viewModel: RateMyDayViewModel,
    initialDate: LocalDate,
    initialStars: Int,
    initialComment: String,
    navController: NavController,
    preferences: Preferences
) {
    var stars by remember { mutableIntStateOf(initialStars) }
    val selectedDate by remember { mutableStateOf(initialDate) }
    var comment by remember { mutableStateOf(initialComment) }
    val maxCommentLength = 100

    val currentDate = LocalDate.now()
    var errorMessage by remember { mutableStateOf("") }

    val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RateMyDayHeader(preferences = preferences)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = selectedDate.format(dateFormatter),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            RatingLegend(preferences = preferences)

            //5 point range slider of Stars
            Text("Rating: $stars Stars")
            StarRating(
                rating = stars,
                onRatingChange = { stars = it }
            )

            // Comment Input Field
            OutlinedTextField(
                value = comment,
                onValueChange = {
                    if (it.length <= maxCommentLength) comment = it
                },
                label = { Text("Comment") },
                placeholder = { Text("How was your day?") },
                modifier = Modifier.fillMaxWidth(),
                //maxLines = 3
            )
            // Max character tracker
            Text(
                text = "${comment.length} / $maxCommentLength",
                style = MaterialTheme.typography.bodySmall,
                color = if (comment.length == maxCommentLength) Color.Red else Color.Gray
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
                            val rateDayEntity = RateDayEntity(
                                date = dateInMillis,
                                stars = stars,
                                comment = comment.takeIf { it.isNotBlank() }
                            )
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
fun ThemeScreen(
    preferences: Preferences
) {


    val scope = rememberCoroutineScope()
    val value by preferences.getString.collectAsState("")
    val mode by preferences.getBoolean.collectAsState(false)

    var expanded by remember { mutableStateOf(false) }


    val menuItemData = themes



    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                modifier = Modifier
                    .padding(4.dp),
                text = "Dark Mode"
            )
            Switch(

                checked = mode,
                onCheckedChange = {

                    scope.launch {
                        preferences.saveBoolean(!mode)
                    }
                },
                thumbContent = if (mode) {
                    {
                        /*  Icon(
                              imageVector = Icons.Filled.Moon,
                              contentDescription = null,
                              modifier = Modifier.size(SwitchDefaults.IconSize),
                          )

                         */
                    }
                } else {
                    null
                }
            )
            Text(
                modifier = Modifier
                    .padding(4.dp),
                text = "Theme"
            )
            Box(
                modifier = Modifier,
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { expanded = !expanded },
                )
                {
                    Text(text = value)
                    if (!expanded) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "",
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp,
                            contentDescription = "",
                        )
                    }



                    DropdownMenu(
                        modifier = Modifier
                            .fillMaxWidth(0.5f),
                        expanded = expanded,
                        onDismissRequest = { expanded = false },


                        ) {
                        menuItemData.forEachIndexed { _, option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    scope.launch {
                                        preferences.saveString(option)
                                    }
                                    expanded = false
                                }

                            )
                        }
                    }
                }
            }
        }
    }
}



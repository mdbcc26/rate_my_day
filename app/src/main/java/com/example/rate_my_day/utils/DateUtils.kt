package com.example.rate_my_day.utils

import java.time.LocalDate
import java.time.ZoneOffset

fun LocalDate.toEpochMillis(): Long {
    return this.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}
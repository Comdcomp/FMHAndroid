package ru.netology.fmhandroid.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import dagger.hilt.android.qualifiers.ActivityContext
import retrofit2.Response
import ru.netology.fmhandroid.R
import ru.netology.fmhandroid.dto.ClaimComment
import ru.netology.fmhandroid.dto.ClaimWithCreatorAndExecutor
import ru.netology.fmhandroid.dto.User
import ru.netology.fmhandroid.exceptions.ApiException
import ru.netology.fmhandroid.exceptions.ServerException
import ru.netology.fmhandroid.exceptions.UnknownException
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import ru.netology.fmhandroid.dto.Claim as Claim

object Utils {

    object Empty {
        val emptyClaimComment = ClaimComment(
            id = null,
            claimId = null,
            description = "",
            creatorId = null,
            createDate = null,
        )

        val emptyClaim = ClaimWithCreatorAndExecutor(
            claim = Claim(
                id = null,
                title = null,
                description = null,
                creatorId = null,
                executorId = null,
                createDate = null,
                planExecuteDate = null,
                factExecuteDate = null,
                status = null,
                deleted = false
            ),
            executor = User(
                id = null,
                login = null,
                password = null,
                firstName = null,
                lastName = null,
                middleName = null,
                phoneNumber = null,
                email = null,
                deleted = false,
            ),
            creator = User(
                id = null,
                login = null,
                password = null,
                firstName = null,
                lastName = null,
                middleName = null,
                phoneNumber = null,
                email = null,
                deleted = false,
            )
        )
    }

    fun convertNewsCategory(category: String): Int {
        return when (category) {
            "Объявление" -> 1
            "День рождения" -> 2
            "Зарплата" -> 3
            "Профсоюз" -> 4
            "Праздник" -> 5
            "Массаж" -> 6
            "Благодарность" -> 7
            "Нужна помощь" -> 8
            else -> 0
        }
    }

    fun updateDateLabel(calendar: Calendar, editText: EditText) {
        val format = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        editText.setText(simpleDateFormat.format(calendar.time))
    }

    fun updateTimeLabel(calendar: Calendar, editText: EditText) {
        val format = "HH:mm"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        editText.setText(simpleDateFormat.format(calendar.time))
    }

    //Save date and time from pickers, and convert it to String in fragment

    fun saveDateTime(date: String, time: String): Long {
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val localDate = LocalDate.parse(date, dateFormatter)
        val localTime = LocalTime.parse(time, timeFormatter)
        return LocalDateTime.of(localDate, localTime)
            .toEpochSecond(ZoneId.of("Europe/Moscow").rules.getOffset(Instant.now()))
    }

    fun fromLongToLocalDateTime(value: Long): LocalDateTime {
        val instant = Instant.ofEpochSecond(value)
        return instant.atZone(ZoneId.of("Europe/Moscow"))
            .toLocalDateTime()
    }

    fun showDate(date: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(date),
            ZoneId.systemDefault()
        )
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "dd.MM.yyy",
            Locale.getDefault()
        )
        return formatter.format(localDateTime)
    }

    fun showTime(date: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(date),
            ZoneId.systemDefault()
        )
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "HH:mm",
            Locale.getDefault()
        )
        return formatter.format(localDateTime)
    }

    fun showDateTimeInOne(dateTime: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(dateTime),
            ZoneId.systemDefault()
        )
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "HH:mm dd.MM.yyyy",
            Locale.getDefault()
        )
        return formatter.format(localDateTime)
    }

    //-----------------------------------------------------------------------------------------------

    fun convertDate(dateTime: String): String {

        val localDateTime = LocalDateTime.parse(dateTime.toString())
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "dd.MM.yyy",
            Locale.getDefault()
        )
        return formatter.format(localDateTime)
    }

    fun convertTime(dateTime: Long): String {

        val localDateTime = LocalDateTime.parse(dateTime.toString())
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "HH:mm",
            Locale.getDefault()
        )
        return formatter.format(localDateTime)
    }

    suspend fun <T, R> makeRequest(
        request: suspend () -> Response<T>,
        onSuccess: suspend (body: T) -> R
    ): R {
        try {
            val response = request()
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body =
                response.body() ?: throw ApiException(response.code(), response.message())
            return onSuccess(body)
        } catch (e: IOException) {
            throw ServerException
        } catch (e: Exception) {
            e.printStackTrace()
            throw UnknownException
        }
    }

    fun shortUserNameGenerator(lastName: String, firstName: String, middleName: String): String {
        return "$lastName ${firstName.first().uppercase()}. ${middleName.first().uppercase()}."
    }

    fun fullUserNameGenerator(lastName: String, firstName: String, middleName: String): String {
        return "$lastName $firstName $middleName"
    }

    fun hideKeyboard(view: View) {
        val imm =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
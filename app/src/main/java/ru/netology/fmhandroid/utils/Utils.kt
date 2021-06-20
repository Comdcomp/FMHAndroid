package ru.netology.fmhandroid.utils

import ru.netology.fmhandroid.dto.Note
import ru.netology.fmhandroid.dto.Patient
import ru.netology.fmhandroid.dto.PatientStatusEnum
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Utils {
    val emptyPatient = Patient(
        id = 0,
        firstName = "",
        lastName = "",
        middleName = "",
        birthDate = "",
        currentAdmissionId = 0,
        deleted = false,
        status = PatientStatusEnum.EXPECTED,
        shortPatientName = ""
    )

    val emptyNote = Note(
        id = 0,
        patientId = null,
        description = "",
        creatorId = null,
        executorId = null,
        createDate = null,
        planeExecuteDate = null,
        factExecuteDate = null,
        noteStatus = null,
        comment = null,
        deleted = false,
        shortExecutorName = "",
        shortPatientName = ""
    )

    fun convertDate(dateTime: LocalDateTime): String {

        val localDateTime = LocalDateTime.parse(dateTime.toString())
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "dd MMMM yyyy HH:mm",
            Locale.getDefault()
        )
        return formatter.format(localDateTime)
    }
}
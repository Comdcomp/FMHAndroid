package ru.netology.fmhandroid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.fmhandroid.dto.Patient
import ru.netology.fmhandroid.dto.PatientStatusEnum

@Entity(tableName = "PatientEntity")
data class PatientEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "firstName")
    val firstName: String,
    @ColumnInfo(name = "lastName")
    val lastName: String,
    @ColumnInfo(name = "middleName")
    val middleName: String,
    @ColumnInfo(name = "birthDate")
    val birthDate: String,
    @ColumnInfo(name = "currentAdmissionId")
    val currentAdmissionId: Int,
    @ColumnInfo(name = "deleted")
    val deleted: Boolean,
    @ColumnInfo(name = "status")
    val status: PatientStatusEnum,

    ) {
    fun toDto() = Patient(
        id = id,
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        birthDate = birthDate,
        currentAdmissionId = currentAdmissionId,
        deleted = deleted,
        status = status
    )
}

fun List<PatientEntity>.toDto(): List<Patient> = map(PatientEntity::toDto)
fun List<Patient>.toListEntity(): List<PatientEntity> = map(Patient::toEntity)
fun Patient.toEntity(): PatientEntity = PatientEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    birthDate = birthDate,
    currentAdmissionId = currentAdmissionId,
    deleted = deleted,
    status = status
)

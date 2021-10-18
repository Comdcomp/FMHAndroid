package ru.netology.fmhandroid.model

import ru.netology.fmhandroid.dto.ClaimWithCreatorAndExecutor
import ru.netology.fmhandroid.utils.Utils

data class ClaimStateModel(
    val claim: ClaimWithCreatorAndExecutor = Utils.Empty.emptyClaim,
    val loading: Boolean = false,
    val error: Boolean = false
)
package ru.netology.fmhandroid.repository.claimRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.netology.fmhandroid.dto.*
import ru.netology.fmhandroid.model.ClaimStateModel

interface ClaimRepository {
    val data: Flow<List<ClaimWithCreatorAndExecutor>>
    val dataOpenInProgress: Flow<List<ClaimWithCreatorAndExecutor>>
    val dataComments: Flow<List<ClaimCommentWithCreator>>
//    val dataClaim: StateFlow<ClaimWithCreatorAndExecutor>
    suspend fun getAllClaims(): List<Claim>
    suspend fun editClaim(editedClaim: Claim): Claim
    suspend fun saveClaim(claim: Claim): Claim
    fun getClaimById(id: Int): ClaimWithCreatorAndExecutor
    suspend fun getAllCommentsForClaim(id: Int): List<ClaimComment>
    suspend fun saveClaimComment(claimId: Int, comment: ClaimComment): ClaimComment
    suspend fun changeClaimStatus(
        claimId: Int,
        newStatus: Claim.Status,
        executorId: Int?,
        claimComment: ClaimComment
    ): Claim

    suspend fun changeClaimComment(comment: ClaimComment): ClaimComment
    suspend fun getClaimCommentById(id: Int): ClaimComment
    suspend fun getAllClaimsWithOpenAndInProgressStatus(): List<Claim>
}
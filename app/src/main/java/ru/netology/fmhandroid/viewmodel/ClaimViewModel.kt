package ru.netology.fmhandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.netology.fmhandroid.dto.*
import ru.netology.fmhandroid.model.ClaimStateModel
import ru.netology.fmhandroid.repository.claimRepository.ClaimRepository
import ru.netology.fmhandroid.utils.Events
import ru.netology.fmhandroid.utils.Utils
import javax.inject.Inject

@HiltViewModel
class ClaimViewModel @Inject constructor(
    private val claimRepository: ClaimRepository
) : ViewModel() {

    lateinit var commentsData: Flow<List<ClaimCommentWithCreator>>

    private val _state = MutableLiveData(
        ClaimStateModel(
            loading = true,
            claim = Utils.Empty.emptyClaim
        )
    )

    val stateClaim: LiveData<ClaimStateModel>
        get() = _state

    val claimCreatedEvent = Events()
    val claimCommentCreatedEvent = Events()
    val claimCommentsLoadedEvent = Events()
    val claimCommentUpdatedEvent = Events()
    val claimStatusChangedEvent = Events()
    val claimUpdatedEvent = Events()
    val claimCommentsLoadExceptionEvent = Events()
    val claimCommentCreateExceptionEvent = Events()
    val updateClaimCommentExceptionEvent = Events()
    val claimStatusChangeExceptionEvent = Events()
    val loadClaimExceptionEvent = Events()
    val createClaimExceptionEvent = Events()
    val claimUpdateExceptionEvent = Events()
    val claimLoadedEvent = Events()

    val data: Flow<List<ClaimWithCreatorAndExecutor>>
        get() = claimRepository.data

    val dataOpenInProgress: Flow<List<ClaimWithCreatorAndExecutor>>
        get() = claimRepository.dataOpenInProgress

    init {
        viewModelScope.launch {
            claimRepository.getAllClaims()
        }
    }

    fun save(claim: Claim) {
        viewModelScope.launch {
            try {
                claimRepository.saveClaim(claim)
                Events.produceEvents(claimCreatedEvent)
            } catch (e: Exception) {
                e.printStackTrace()
                Events.produceEvents(createClaimExceptionEvent)
            }
        }
    }

    fun createClaimComment(claimComment: ClaimComment) {
        viewModelScope.launch {
            try {
                claimComment.claimId?.let { claimRepository.saveClaimComment(it, claimComment) }
                Events.produceEvents(claimCommentCreatedEvent)
            } catch (e: Exception) {
                e.printStackTrace()
                Events.produceEvents(claimCommentCreateExceptionEvent)
            }
        }
    }

    fun updateClaimComment(comment: ClaimComment) {
        viewModelScope.launch {
            try {
                claimRepository.changeClaimComment(comment)
                Events.produceEvents(claimCommentUpdatedEvent)
            } catch (e: Exception) {
                e.printStackTrace()
                Events.produceEvents(updateClaimCommentExceptionEvent)
            }
        }
    }

    fun updateClaim(updatedClaim: Claim) {
        viewModelScope.launch {
            try {
                claimRepository.editClaim(updatedClaim)
                getClaimById(updatedClaim.id!!)
                Events.produceEvents(claimUpdatedEvent)
            } catch (e: Exception) {
                e.printStackTrace()
                Events.produceEvents(claimUpdateExceptionEvent)
            }
        }
    }

    fun getAllClaimComments(id: Int) {
        viewModelScope.launch {
            try {
                claimRepository.getAllCommentsForClaim(id)
                commentsData = claimRepository.dataComments
                Events.produceEvents(claimCommentsLoadedEvent)
            } catch (e: Exception) {
                e.printStackTrace()
                Events.produceEvents(claimCommentsLoadExceptionEvent)
            }
        }
    }

    fun getClaimById(claimId: Int) = viewModelScope.launch {
        try {
            _state.value = ClaimStateModel(loading = true)
            _state.value = ClaimStateModel(claimRepository.getClaimById(claimId))
        } catch (e: Exception) {
            _state.value = ClaimStateModel(error = true, claim = Utils.Empty.emptyClaim)
        }
    }

    fun changeClaimStatus(
        claimId: Int,
        newClaimStatus: Claim.Status,
        executorId: Int?,
        claimComment: ClaimComment
    ) =
        viewModelScope.launch {
            try {
                claimRepository.changeClaimStatus(
                    claimId,
                    newClaimStatus,
                    executorId,
                    claimComment
                )

                if (!claimComment.description.isNullOrBlank()) {
                    commentsData = claimRepository.dataComments
                }
                getClaimById(claimId)
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = ClaimStateModel(error = true)
            }
        }

}
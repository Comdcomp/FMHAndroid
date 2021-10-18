package ru.netology.fmhandroid.utils

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

class Events {

    companion object {
        private val mutableEvents = MutableSharedFlow<Events>()
        val events = mutableEvents.asSharedFlow()

        suspend fun produceEvents(event: Events) {
            mutableEvents.emit(event)
        }
    }
}
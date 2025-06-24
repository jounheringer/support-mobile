package com.reringuy.support.helper

import androidx.lifecycle.ViewModel
import com.reringuy.support.BuildConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<State: Reducer.ViewState, Event: Reducer.ViewEvent, Effect: Reducer.ViewEffect>(
    initialState: State,
    private val reducer: Reducer<State, Event, Effect>
) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state get() = _state.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event get() = _event.asSharedFlow()

    private val _effect = Channel<Effect>(capacity = Channel.CONFLATED)
    val effect get() = _effect.receiveAsFlow()

    private val timeCapsule: TimeCapsule<State> = TimeTravelCapsule {
        _state.tryEmit(it)
    }

    init {
        timeCapsule.addState(initialState)
    }

    fun sendEffect(effect: Effect) {
        _effect.trySend(effect)
    }

    fun sendEvent(event: Event) {
        val (newState, _) = reducer.reduce(_state.value, event)

        val success = _state.tryEmit(newState)

        if (BuildConfig.DEBUG && success) {
            timeCapsule.addState(newState)
        }
    }

    fun sendEventForEffect(event: Event) {
        val (newState, effect) = reducer.reduce(_state.value, event)

        val success = _state.tryEmit(newState)

        if (BuildConfig.DEBUG && success) {
            timeCapsule.addState(newState)
        }

        effect?.let {
            sendEffect(it)
        }
    }
}
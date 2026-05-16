package com.aurabeat.core

sealed class UiEvent {
    object Idle : UiEvent()
    data class ShowMessage(val message: String) : UiEvent()
}

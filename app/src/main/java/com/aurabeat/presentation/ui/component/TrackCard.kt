package com.aurabeat.presentation.ui.component

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TrackCard(title: String, artist: String) {
    Card {
        Text(text = "$title - $artist")
    }
}

package com.aurabeat.presentation.ui.component

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AuraBeatNavigationBar() {
    NavigationBar {
        NavigationBarItem(selected = true, onClick = {}, label = { Text("Home") }, icon = {})
        NavigationBarItem(selected = false, onClick = {}, label = { Text("Search") }, icon = {})
        NavigationBarItem(selected = false, onClick = {}, label = { Text("Profile") }, icon = {})
    }
}

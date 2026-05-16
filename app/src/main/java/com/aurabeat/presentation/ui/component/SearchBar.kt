package com.aurabeat.presentation.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AuraBeatSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = "Search music...",
        leadingIcon = Icons.Rounded.Search,
        modifier = modifier
    )
}

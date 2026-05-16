package com.aurabeat.presentation.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import com.aurabeat.presentation.ui.navigation.AppDestination
import com.aurabeat.presentation.ui.navigation.BottomNavItem
import com.aurabeat.presentation.ui.navigation.bottomNavItems
import com.aurabeat.presentation.ui.theme.AppElevation
import com.aurabeat.presentation.ui.theme.AppRadius
import com.aurabeat.presentation.ui.theme.AppSpacing

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
    items: List<BottomNavItem> = bottomNavItems
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.sm)
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = AppElevation.navigation,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppRadius.xl))
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.destination.route
                val iconScale by animateFloatAsState(
                    targetValue = if (selected) 1.12f else 1f,
                    label = "bottom_nav_icon_scale"
                )

                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(item.destination) },
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.destination.label,
                            modifier = Modifier.scale(iconScale)
                        )
                    },
                    label = { Text(item.destination.label) },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                    )
                )
            }
        }
    }
}

package com.aurabeat.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aurabeat.presentation.ui.model.SearchCategoryItem
import com.aurabeat.presentation.ui.theme.AppColors
import com.aurabeat.presentation.ui.theme.AppRadius
import com.aurabeat.presentation.ui.theme.AppSpacing

@Composable
fun CategoryCard(
    category: SearchCategoryItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp)
            .clip(RoundedCornerShape(AppRadius.lg))
            .background(Brush.linearGradient(category.colors))
            .padding(AppSpacing.lg),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(
            text = category.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AppColors.ArtworkContent
        )
    }
}

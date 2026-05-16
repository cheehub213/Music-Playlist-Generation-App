package com.aurabeat.presentation.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.aurabeat.presentation.ui.theme.AppColors
import com.aurabeat.presentation.ui.theme.AppIconSize
import com.aurabeat.presentation.ui.theme.AppRadius
import com.aurabeat.presentation.ui.theme.AppSpacing
import com.aurabeat.presentation.viewmodel.ArtistDetails

@Composable
fun ArtistHeader(
    artist: ArtistDetails,
    collapsedFraction: Float,
    isFollowing: Boolean,
    onFollowClick: () -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val followColor by animateColorAsState(
        targetValue = if (isFollowing) {
            MaterialTheme.colorScheme.surfaceContainerHigh
        } else {
            MaterialTheme.colorScheme.primary
        },
        label = "artist_follow_button"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.lg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppIconSize.artwork * 4)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(artist.bannerColor),
                            MaterialTheme.colorScheme.background
                        )
                    ),
                    MaterialTheme.shapes.extraLarge
                )
                .padding(AppSpacing.xl),
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                modifier = Modifier
                    .size(AppIconSize.artwork * 2)
                    .background(Color(artist.bannerColor).copy(alpha = 0.72f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = artist.name.take(1),
                    style = MaterialTheme.typography.headlineLarge,
                    color = AppColors.ArtworkContent,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Text(
            text = artist.name,
            style = if (collapsedFraction > 0.55f) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "${artist.monthlyListeners} monthly listeners",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Button(
                onClick = onFollowClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = followColor,
                    contentColor = if (isFollowing) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(if (isFollowing) "Following" else "Follow")
            }
            IconButton(
                onClick = onShuffleClick,
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
            ) {
                Icon(Icons.Rounded.Shuffle, contentDescription = "Shuffle")
            }
            IconButton(
                onClick = onPlayClick,
                modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

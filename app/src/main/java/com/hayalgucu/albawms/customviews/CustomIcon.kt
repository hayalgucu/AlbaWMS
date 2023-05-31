package com.hayalgucu.albawms.customviews

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.hayalgucu.albawms.ui.theme.Primary100
import com.hayalgucu.albawms.ui.theme.Primary500
import com.hayalgucu.albawms.util.iconSize

@Composable
fun CustomIcon(icon: ImageVector, cd: String, size: Dp = iconSize, enabled: Boolean = true) {
    Icon(
        icon,
        contentDescription = cd,
        Modifier.size(size),
        tint = if (enabled) Primary500 else Primary100
    )
}
package com.glitchcode.flowery.core.theme.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val FloweryIcons.Warning: ImageVector
    get() {
        if (_Warning != null) {
            return _Warning!!
        }
        _Warning = ImageVector.Builder(
            name = "Warning",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveToRelative(22.242f, 5.272f)
                lineToRelative(-3.515f, -3.515f)
                curveToRelative(-1.133f, -1.133f, -2.64f, -1.757f, -4.242f, -1.757f)
                horizontalLineToRelative(-4.971f)
                curveToRelative(-1.603f, 0f, -3.109f, 0.624f, -4.242f, 1.757f)
                lineToRelative(-3.515f, 3.514f)
                curveToRelative(-1.134f, 1.133f, -1.758f, 2.64f, -1.758f, 4.243f)
                verticalLineToRelative(4.971f)
                curveToRelative(0f, 1.603f, 0.624f, 3.11f, 1.758f, 4.243f)
                lineToRelative(3.515f, 3.515f)
                curveToRelative(1.133f, 1.133f, 2.64f, 1.757f, 4.242f, 1.757f)
                horizontalLineToRelative(4.971f)
                curveToRelative(1.603f, 0f, 3.109f, -0.624f, 4.242f, -1.757f)
                lineToRelative(3.515f, -3.514f)
                curveToRelative(1.134f, -1.133f, 1.758f, -2.64f, 1.758f, -4.243f)
                verticalLineToRelative(-4.971f)
                curveToRelative(0f, -1.603f, -0.624f, -3.11f, -1.758f, -4.243f)
                close()
                moveTo(11f, 6f)
                curveToRelative(0f, -0.552f, 0.447f, -1f, 1f, -1f)
                reflectiveCurveToRelative(1f, 0.448f, 1f, 1f)
                verticalLineToRelative(8f)
                curveToRelative(0f, 0.552f, -0.447f, 1f, -1f, 1f)
                reflectiveCurveToRelative(-1f, -0.448f, -1f, -1f)
                lineTo(11f, 6f)
                close()
                moveTo(12f, 19f)
                curveToRelative(-0.828f, 0f, -1.5f, -0.672f, -1.5f, -1.5f)
                reflectiveCurveToRelative(0.672f, -1.5f, 1.5f, -1.5f)
                reflectiveCurveToRelative(1.5f, 0.672f, 1.5f, 1.5f)
                reflectiveCurveToRelative(-0.672f, 1.5f, -1.5f, 1.5f)
                close()
            }
        }.build()

        return _Warning!!
    }

@Suppress("ObjectPropertyName")
private var _Warning: ImageVector? = null

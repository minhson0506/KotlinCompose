package com.example.helloworld2.ui.theme

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = CutCornerShape(
        bottomStart = ZeroCornerSize,
        bottomEnd = ZeroCornerSize,
        topEnd = ZeroCornerSize,
        topStart = CornerSize(4.dp)
    ),
    large = RoundedCornerShape(0.dp)
)
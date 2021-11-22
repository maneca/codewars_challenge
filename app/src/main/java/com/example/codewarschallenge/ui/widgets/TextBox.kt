package com.example.codewarschallenge.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun TextBox(elements: List<String>, color: Color, paddingStart: Dp = 0.dp) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = paddingStart)
            .wrapContentHeight(),
        mainAxisSpacing = 5.dp,
        crossAxisSpacing = 5.dp,
    ) {
        elements.forEach {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(5.dp))
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(color = Color.White, text = it, modifier = Modifier.padding(6.dp))
            }
        }
    }
}
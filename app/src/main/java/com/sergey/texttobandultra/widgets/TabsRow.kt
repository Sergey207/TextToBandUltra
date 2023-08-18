package com.sergey.texttobandultra.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.sergey.texttobandultra.tabs

@Composable
fun TabsRow(
    modifier: Modifier,
    currentIndex: MutableState<Int>
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        state = rememberLazyListState(0)
    ) {
            items(tabs.size)
            {
                Tab(tabs[it], it, currentIndex)
            }
    }
}
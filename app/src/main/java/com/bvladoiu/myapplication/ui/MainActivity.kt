@file:OptIn(ExperimentalFoundationApi::class)

package com.bvladoiu.myapplication.ui

import ScaleAndAlphaArgs
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bvladoiu.myapplication.model.Files
import com.bvladoiu.myapplication.ui.theme.CenterTopAppBar
import com.bvladoiu.myapplication.ui.theme.MyApplicationTheme
import scaleAndAlpha

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                Modifier.safeContentPadding(),
                topBar = {
                    CenterTopAppBar(
                        title = {
                            Text(
                                textAlign = TextAlign.Center,
                                text = "Select and Merge"
                            )
                        },
                        actions = {
                            TextButton(onClick = {/* Do Something*/ }) {
                                Text(text = "Merge", color = Color.White)
                            }
                        }
                    )
                })
            { padding ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Spacer(Modifier.height(16.dp))
                    Grid()
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Grid() {
    val data = remember { Files.getFiles().toMutableStateList() }
    val columns = 3
    val state = androidx.compose.foundation.lazy.grid.rememberLazyGridState()
    LazyVerticalGrid(
        state = state,
        columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items = data,
            itemContent = { item ->
                this@LazyVerticalGrid.fileGridContent(files = data, columns = columns, state = state)

                val isSelected = rememberSaveable { mutableStateOf(false) }
                LazyColumn(
                    modifier = Modifier.animateContentSize { initialValue, targetValue -> }
                ) {

                }
                Box(
                    modifier = Modifier
                        //.aspectRatio(2 / 3F)
                        .requiredWidth(100.dp)
                        .requiredHeight(150.dp)
                        .clickable {
                            isSelected.value = !isSelected.value
                            data.addAll(listOf(data.size + 3, data.size + 1, data.size + 2))
                            data.removeAt(0)
                        }
                        .background(if (isSelected.value) Color.DarkGray else Color.LightGray)

                ) {
                    Text(
                        text = "Item $item",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(alignment = Alignment.Center),
                        color = if (isSelected.value) Color.White else Color.Black
                    )
                    Text(
                        text = "Size $item",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(alignment = Alignment.BottomCenter),
                        color = if (isSelected.value) Color.White else Color.Black
                    )
                }
            }
        )
    }
}


private fun LazyGridScope.fileGridContent(files: List<Int>, columns: Int, state: LazyGridState) {
    items(files.count()) { index ->
        //val (delay, easing) = state.calc(index, columns)
        val animation = tween<Float>(durationMillis = 500)
        val args = ScaleAndAlphaArgs(fromScale = 2f, toScale = 1f, fromAlpha = 0f, toAlpha = 1f)
        val (scale, alpha) = scaleAndAlpha(args = args, animation = animation)
        val file = files[index]
    }
}

@Composable
private fun LazyListState.calculateDelayAndEasing(index: Int, columnCount: Int): Pair<Int, Easing> {
    val row = index / columnCount
    val column = index % columnCount
    val firstVisibleRow = firstVisibleItemIndex
    val visibleRows = layoutInfo.visibleItemsInfo.count()
    val scrollingToBottom = firstVisibleRow < row
    val isFirstLoad = visibleRows == 0
    val rowDelay = 200 * when {
        isFirstLoad -> row // initial load
        scrollingToBottom -> visibleRows + firstVisibleRow - row // scrolling to bottom
        else -> 1 // scrolling to top
    }
    val scrollDirectionMultiplier = if (scrollingToBottom || isFirstLoad) 1 else -1
    val columnDelay = column * 150 * scrollDirectionMultiplier
    val easing =
        if (scrollingToBottom || isFirstLoad) LinearOutSlowInEasing else FastOutSlowInEasing
    return rowDelay + columnDelay to easing
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Column(Modifier.padding(16.dp)) {
            Spacer(Modifier.height(16.dp))
            Grid()
        }
    }
}
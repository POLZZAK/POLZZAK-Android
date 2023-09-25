package com.polzzak_android.presentation.feature.myPage.point.history

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polzzak_android.R
import com.polzzak_android.presentation.common.compose.Gray400
import com.polzzak_android.presentation.common.compose.PolzzakTheme
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.myPage.components.PointHistoryListItem
import com.polzzak_android.presentation.feature.myPage.model.PointHistoryItemModel
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

@Composable
fun PointHistoryScreen(
    screenStateFlow: StateFlow<ModelState<Unit>>,
    historyListFlow: StateFlow<List<PointHistoryItemModel>>,
    onNextList: () -> Unit,
    onError: (Exception) -> Unit
) {
    val screenState by screenStateFlow.collectAsState()
    val historyList by historyListFlow.collectAsState()

    // screenState를 직접 사용하면 Error일 때
    // 보고있던 리스트가 사라지거나, 화면이 다시 그려지면서 스크롤이 상단으로 올라감.
    // 해당 현상들을 방지하기 위해 로딩 상태인지를 따로 체크하여 사용.
    val isLoading by remember {
        derivedStateOf { screenState is ModelState.Loading }
    }

    Crossfade(
        targetState = isLoading,
        animationSpec = tween(200),
        label = "PointHistoryScreen change animation"
    ) { loadingState ->
        when (loadingState) {
            true -> {}
            false -> {
                if (historyList.isEmpty()) {
                    EmptyHistoryText()

                } else {
                    PointHistoryScreen(
                        historyList = historyList,
                        onNextList = onNextList
                    )
                }
            }
        }
    }

    LaunchedEffect(screenState) {
        if (screenState is ModelState.Error) {
            onError((screenState as ModelState.Error<Unit>).exception)
        }
    }
}

@Composable
private fun PointHistoryScreen(
    historyList: List<PointHistoryItemModel>,
    onNextList: () -> Unit
) {
    val lazyListState = rememberLazyListState()

    // 전달된 리스트의 마지막 데이터가 화면에 보였는지 여부
    val isLastVisible by remember {
        derivedStateOf {
            // 상단에 들어간 빈 item 때문에 인덱스 값이 한개씩 밀림
            val lastIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index

            // 새로 들어오는 리스트의 사이즈를 가져와서 비교해야
            // 리스트 화면이 바로 업데이트 됨
            lastIndex == historyList.size
        }
    }

    LaunchedEffect(isLastVisible) {
        if (isLastVisible) {
            onNextList()
        }
    }

    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }

        itemsIndexed(
            items = historyList,
            key = { index, _ -> index }
        ) { index, item ->
            PointHistoryListItem(
                title = item.title,
                date = item.createdDate,
                increasedPoint = item.increasedPoint,
                remainingPoint = item.remainingPoint
            )

            if (index == historyList.lastIndex) {
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
}

@Preview
@Composable
private fun PointHistoryScreenPreview() {
    PointHistoryScreen(emptyList(), {})
}

@Composable
private fun EmptyHistoryText() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    Text(
        text = stringResource(id = R.string.point_history_empty),
        textAlign = TextAlign.Center,
        style = PolzzakTheme.typography.regular20, 
        color = Gray400
    )
}
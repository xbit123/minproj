package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Content2()
            }
        }
    }
}

@Composable
fun Content2() {
    val webView = remember { mutableStateOf<WebView?>(null) }
//                val scrollableState = rememberScrollableState(consumeScrollDelta = { it })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(null) {
                detectDragGestures { change, dragAmount ->
                    Log.d("draggg", "${dragAmount.x}, ${dragAmount.y}")
                    Log.d("draggg", "${webView.value}")
                    webView.value?.scrollBy(
                        dragAmount.x.toInt() * 10,
                        dragAmount.y.toInt() * 10
                    )
                }
            }
            .verticalScroll(rememberScrollState())
    ) {
        val state = rememberWebViewState("https://youtube.com")
        WebView(
            state = state,
            onCreated = {
                it.settings.javaScriptEnabled = true
                it.isNestedScrollingEnabled = true
                webView.value = it
//                            CoroutineScope(Dispatchers.Main).launch {
//                                while (true) {
//                                    delay(1000)
//                                    it.scrollBy(500, 500)
//                                }
//                            }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Content1() {
    val cs = rememberCoroutineScope()
    val ss =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)
    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetContentColor = MaterialTheme.colors.background,
        scrimColor = MaterialTheme.colors.secondary,
        sheetContent = {
            val state = rememberWebViewState("https://wikipedia.org")
            WebView(
                modifier = Modifier.pointerInput(null) {
                    while (true) {
                        awaitPointerEventScope {
                            awaitPointerEvent(pass = PointerEventPass.Main).changes.forEach {
                                Log.d("pepega", it.toString())
                                it.consume()
                            }
                        }
                    }
                },
                state = state,
                onCreated = { it.settings.javaScriptEnabled = true }
            )
        },
        content = {
            Box(Modifier.fillMaxSize()) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        cs.launch {
                            ss.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    }) {
                    Text("Open")
                }
            }
        },
        sheetState = ss
    )
}

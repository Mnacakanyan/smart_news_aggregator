package com.news.smartnewsaggregator.ui.features.detail

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun DetailScreen(url: String) {

    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }


    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                            error = null
                        }
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }
                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            webResourceError: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, webResourceError)
                            if (request?.isForMainFrame == true) {
                                isLoading = false
                                error = "Error loading page: ${webResourceError?.description}"
                            }
                        }
                    }
                    loadUrl(url)
                }
            },
            update = {
                it.alpha = if(error == null) 1f else 0f
            }
        )
        if (isLoading) {
            CircularProgressIndicator()
        }
        error?.let {
            Text(
                text = it,
                textAlign = TextAlign.Center
            )
        }
    }
}

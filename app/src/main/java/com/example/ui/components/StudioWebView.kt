package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

private const val DESKTOP_USER_AGENT =
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun StudioWebView(
    url: String,
    isActive: Boolean,
    isDesktopMode: Boolean,
    onStateUpdate: (title: String?, progress: Int, canBack: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    webViewInstance: (WebView?) -> Unit = {}
) {
    val context = LocalContext.current
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var filePathCallback by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }

    // File chooser for uploads (prompt media, code, datasets)
    val fileChooserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uriList = mutableListOf<Uri>()
        result.data?.data?.let { uriList.add(it) }
        result.data?.clipData?.let { clip ->
            for (i in 0 until clip.itemCount) {
                uriList.add(clip.getItemAt(i).uri)
            }
        }
        filePathCallback?.onReceiveValue(if (uriList.isNotEmpty()) uriList.toTypedArray() else null)
        filePathCallback = null
    }

    // Hardware back press: active only when this tab is selected and can go back
    val canGoBack = webViewRef?.canGoBack() == true
    BackHandler(enabled = isActive && canGoBack) {
        webViewRef?.goBack()
    }

    // React to Desktop Mode changes
    LaunchedEffect(isDesktopMode) {
        webViewRef?.let { wv ->
            wv.settings.userAgentString = if (isDesktopMode) DESKTOP_USER_AGENT else null
            wv.settings.useWideViewPort = isDesktopMode
            wv.settings.loadWithOverviewMode = isDesktopMode
            wv.reload()
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                val cookieManager = CookieManager.getInstance()
                cookieManager.setAcceptCookie(true)
                cookieManager.setAcceptThirdPartyCookies(this, true)

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    builtInZoomControls = true
                    displayZoomControls = false
                    setSupportZoom(true)
                    allowFileAccess = true
                    setSupportMultipleWindows(false)
                    cacheMode = WebSettings.LOAD_DEFAULT
                    mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                    userAgentString = if (isDesktopMode) DESKTOP_USER_AGENT else null
                    useWideViewPort = isDesktopMode
                    loadWithOverviewMode = isDesktopMode
                }

                setDownloadListener { downloadUrl, _, _, _, _ ->
                    try {
                        val downloadIntent = Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl))
                        context.startActivity(downloadIntent)
                    } catch (e: Exception) {
                        // ignore or handle
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        onStateUpdate(view?.title, newProgress, view?.canGoBack() == true)
                    }

                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        super.onReceivedTitle(view, title)
                        onStateUpdate(title, view?.progress ?: 100, view?.canGoBack() == true)
                    }

                    override fun onShowFileChooser(
                        webView: WebView?,
                        filePathCallbackParam: ValueCallback<Array<Uri>>?,
                        fileChooserParams: FileChooserParams?
                    ): Boolean {
                        filePathCallback?.onReceiveValue(null)
                        filePathCallback = filePathCallbackParam

                        val intent = fileChooserParams?.createIntent() ?: Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "*/*"
                            addCategory(Intent.CATEGORY_OPENABLE)
                        }

                        return try {
                            fileChooserLauncher.launch(intent)
                            true
                        } catch (e: Exception) {
                            filePathCallback = null
                            false
                        }
                    }
                }

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        onStateUpdate(view?.title, 10, view?.canGoBack() == true)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onStateUpdate(view?.title, 100, view?.canGoBack() == true)
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val target = request?.url?.toString() ?: return false

                        if (target.contains("aistudio.google.com") ||
                            target.contains("accounts.google.com") ||
                            target.contains("ai.google.dev") ||
                            target.contains("google.com/auth") ||
                            target.contains("github.com")
                        ) {
                            return false
                        }

                        if (target.startsWith("mailto:") || target.startsWith("tel:")) {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(target))
                                context.startActivity(intent)
                                return true
                            } catch (e: Exception) {
                                return true
                            }
                        }

                        return false
                    }
                }

                loadUrl(url)
                webViewRef = this
                webViewInstance(this)
            }
        },
        update = { wv ->
            webViewRef = wv
            webViewInstance(wv)
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            webViewRef?.stopLoading()
            webViewInstance(null)
        }
    }
}

package com.rickroll.template.android.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.C
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerRoute(
    viewModel: PlayerViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PlayerScreen(
        state = state,
        onLogoutClick = { viewModel.onEvent(PlayerEvent.LogoutClicked) },
    )
}

@Composable
private fun PlayerScreen(
    state: PlayerState,
    onLogoutClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        if (state.mediaUrl.isNotBlank()) {
            VideoPlayer(
                mediaUrl = state.mediaUrl,
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
            )
        }

        state.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp),
            )
        }

        Text(
            text = state.title.ifBlank { "RickRoll Player" },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .safeDrawingPadding()
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
        )

        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(text = "Logout")
        }
    }
}

@Composable
private fun VideoPlayer(
    mediaUrl: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val exoPlayer = remember(mediaUrl) {
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                15_000,
                50_000,
                250,
                750,
            )
            .build()

        ExoPlayer.Builder(context).setLoadControl(loadControl).build().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                    .build(),
                true,
            )
            setMediaItem(MediaItem.fromUri(mediaUrl))
            playWhenReady = true
            prepare()
        }
    }

    DisposableEffect(lifecycleOwner, exoPlayer) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> exoPlayer.playWhenReady = true
                Lifecycle.Event.ON_STOP -> exoPlayer.pause()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                controllerAutoShow = true
                keepScreenOn = true
            }
        },
        update = { view ->
            view.player = exoPlayer
            view.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            view.useController = true
        },
    )
}

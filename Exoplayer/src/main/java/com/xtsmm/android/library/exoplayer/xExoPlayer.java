package com.xtsmm.android.library.exoplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import static com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection.*;

public class xExoPlayer {

    Activity activity;
    public static final int MIN_BUFFER_DURATION = 2000;
    public static final int MAX_BUFFER_DURATION = 5000;
    public static final int MIN_PLAYBACK_START_BUFFER = 1500;
    public static final int MIN_PLAYBACK_RESUME_BUFFER = 2000;
    ExoPlayer player;
    public xExoPlayer(Activity activity){
        this.activity=activity;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    @SuppressLint("SetJavaScriptEnabled")
    public FrameLayout play(String url, FrameLayout playerFrame,Boolean showControl,Boolean repeat,String userAgent,String headerKey,String headerValue){

        int currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = activity.getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoFactory = new AdaptiveTrackSelection.Factory();
        TrackSelector trackSelector = new DefaultTrackSelector(videoFactory);

        LoadControl loadControl = new DefaultLoadControl.Builder()
                .setAllocator(new DefaultAllocator(true, 16))
                .setBufferDurationsMs(MIN_BUFFER_DURATION,
                        MAX_BUFFER_DURATION,
                        MIN_PLAYBACK_START_BUFFER,
                        MIN_PLAYBACK_RESUME_BUFFER)
                .setTargetBufferBytes(-1)
                .setPrioritizeTimeOverSizeThresholds(true).createDefaultLoadControl();

        // Create Player
        player = ExoPlayerFactory.newSimpleInstance(activity, trackSelector, loadControl);
        PlayerView playerView = new PlayerView(activity);


        try{
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            if(userAgent==null){userAgent="xExoplayer";}
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory(
                    userAgent, null,
                    DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    1000,
                    true);
            if(headerKey==null || headerValue==null){}
            else {
                dataSourceFactory.getDefaultRequestProperties().set(headerKey, headerValue);
            }
            HlsMediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));
            player.setPlayWhenReady(true);
            player.prepare(mediaSource);
            player.prepare(mediaSource, true, false);
            if(repeat) {
                player.setRepeatMode(player.REPEAT_MODE_ONE);
            }
            playerView.setPlayer(player);
            player.addListener(new ExoPlayer.EventListener() {

                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    player.retry();

                }

            });
        }catch (Exception e){
            player.retry();
        }

        playerView.setUseController(showControl);
        playerFrame.addView(playerView);

        return playerFrame;
    }
}

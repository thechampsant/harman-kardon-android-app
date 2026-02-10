package com.ariston.training_module.modules.training_module.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.view_models.DocViewerViewModel;
import com.ariston.training_module.utility.TrainingConstants;
import com.ariston.training_module.utility.services.UserDetailsService;
import com.ariston.training_module.utility.widgets.ExtendedTimeBar;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

public class VideoPlayerActivity extends AppCompatActivity implements VideoRendererEventListener {
    private static final String TAG = "MainActivity";
    private PlayerView simpleExoPlayerView;
    private ImageView auiodio;
    private SimpleExoPlayer player;
    private ExtendedTimeBar timeBar;
    private boolean isBound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Uri mp4VideoUri = Uri.parse(getIntent().getStringExtra(TrainingConstants.MEDIA_URL)); //ABC NEWS

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); //test
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        // 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        DefaultHttpDataSourceFactory dataSource = new DefaultHttpDataSourceFactory(
                Util.getUserAgent(this, "harman"));
        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSource)
                .createMediaSource(mp4VideoUri, null, null);
        simpleExoPlayerView = new PlayerView(this);
        simpleExoPlayerView = findViewById(R.id.player_view);
        auiodio=findViewById(R.id.auiodio);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        simpleExoPlayerView.setShowBuffering(true);
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        simpleExoPlayerView.requestFocus();
        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);
        if(mp4VideoUri.toString().contains("mp3"))
        {
            auiodio.setVisibility(View.VISIBLE);
        }
        initViews();
        // Measures bandwidth during playback. Can be null if not required.
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeter);
        // This is the MediaSource representing the media to be played.
//        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(liveStreamUri);

        //// II. ADJUST HERE:

        ////        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeterA);
        ////Produces Extractor instances for parsing the media data.
        //        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        //This is the MediaSource representing the media to be played:
        //FOR SD CARD SOURCE:
        //        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri, dataSourceFactory, extractorsFactory, null, null);

        //FOR LIVESTREAM LINK:
        MediaSource videoSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, 1, null, null);
        final LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);
        // Prepare the player with the source.
        player.prepare(videoSource);

        player.addListener(new ExoPlayer.EventListener() {


            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    mViewModel.trainMatCompleted(boundService.getUserBundle().getString(TrainingConstants.USER_ID), getIntent().getLongExtra(TrainingConstants.MAT_ID, 0));
                    simpleExoPlayerView.setKeepScreenOn(false);
                } else { // STATE_IDLE, STATE_ENDED
                    // This prevents the screen from getting dim/lock
                    simpleExoPlayerView.setKeepScreenOn(true);
                }


            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                player.stop();
                player.prepare(loopingSource);
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        //run file/link when ready to play.
        player.setVideoDebugListener(this);
        player.prepare(mediaSource);
        mViewModel = ViewModelProviders.of(this).get(DocViewerViewModel.class);
        initService();
        observeVM();
    }

    private void observeVM() {
        mViewModel.getTrainingMatLiveData().observe(this, baseResponse -> {
            if (baseResponse.getmStatus()) {
                Toast.makeText(boundService, "Material Completed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initService() {
        Intent intent = new Intent(this, UserDetailsService.class);
        startService(intent);
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);
    }

    private DocViewerViewModel mViewModel;
    private UserDetailsService boundService;
    private ServiceConnection boundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UserDetailsService.MyBinder binderBridge = (UserDetailsService.MyBinder) service;
            boundService = binderBridge.getService();
            isBound = true;


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };


    private void initViews() {
        timeBar = findViewById(com.google.android.exoplayer2.R.id.exo_progress);
        timeBar.setForceDisabled(true);


    }


    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }
//-------------------------------------------------------ANDROID LIFECYCLE---------------------------------------------------------------------------------------------

    @Override
    protected void onStop() {
        super.onStop();
        player.setPlayWhenReady(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
        if (isBound) {
            unbindService(boundServiceConnection);
            isBound = false;
        }
        mViewModel.disposeApis();
    }
}
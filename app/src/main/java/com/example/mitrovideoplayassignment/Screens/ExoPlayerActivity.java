package com.example.mitrovideoplayassignment.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import com.example.mitrovideoplayassignment.Model.VideoDetailsModel;
import com.example.mitrovideoplayassignment.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.ArrayList;

public class ExoPlayerActivity extends AppCompatActivity {
    private SimpleExoPlayer videoPlayer = null;
    PlayerView exoPlayerView;

    FrameLayout mute_btn;
    private int  position;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    ArrayList<VideoDetailsModel> videoDetailsModelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        exoPlayerView = (PlayerView) findViewById(R.id.exoplayerView);
        mute_btn = (FrameLayout) findViewById(R.id.mute_btn);
        videoDetailsModelArrayList = new ArrayList<>();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            position = extras.getInt("position");
        }
         videoDetailsModelArrayList =  getIntent().getExtras().getParcelableArrayList("VideoArrayList");


        mute_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                float currentvolume =  videoPlayer.getAudioComponent().getVolume();
                if (currentvolume == 0f) {
                    videoPlayer.getAudioComponent().setVolume(1f);
                }else {
                    videoPlayer.getAudioComponent().setVolume(0f);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();

    }


    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.setPlayWhenReady(false);
        videoPlayer.getPlaybackState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // if (Util.SDK_INT >= 24) {
        releasePlayer();
        //}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();

    }



    private void initializePlayer() {

        videoPlayer = new SimpleExoPlayer.Builder(this).build();
        exoPlayerView.setPlayer(videoPlayer);
        MediaItem mediaItem = MediaItem.fromUri( videoDetailsModelArrayList.get(position).getVideoUri().toString());
        videoPlayer.setMediaItem(mediaItem);
        //check if last item in list then don't add the next play item to playlist
        if(position != (videoDetailsModelArrayList.size()-1))
        {
            MediaItem secondMediaItem = MediaItem.fromUri(videoDetailsModelArrayList.get(++position).getVideoUri().toString());
            videoPlayer.addMediaItem(secondMediaItem);
        }
        videoPlayer.getPlaybackState();
        videoPlayer.setPlayWhenReady(playWhenReady);
        videoPlayer.seekTo(currentWindow, playbackPosition);
        videoPlayer.prepare();
        videoPlayer.play();


    }



    private void releasePlayer() {
        if (videoPlayer != null) {
            playWhenReady = videoPlayer.getPlayWhenReady();
            playbackPosition = videoPlayer.getCurrentPosition();
            currentWindow = videoPlayer.getCurrentWindowIndex();
            videoPlayer.setPlayWhenReady(false);
            videoPlayer.release();
            videoPlayer = null;
        }
    }
}
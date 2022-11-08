package com.bawp.playvideoexop;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExoVideoPlayer {
    private final PlayerView playerVw;
    private SimpleExoPlayer exoPlayer;
    UtilCountdown countdown;
    Activity act;

    // e.g videoUrl: "https://res.cloudinary.com/fiddelize/video/upload/v1656390694/marica-ad_yhhbmi.mp4"
    public ExoVideoPlayer(final Context context, final TextView countdownTxtVw, String videoUrl) {
        act = (Activity) context;
        playerVw = act.findViewById(R.id.playerView);

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        playerVw.setPlayer(exoPlayer);
        playerVw.setKeepScreenOn(true); // prevent device to turn the screen off when user is watching but inactive
        exoPlayer.setPlayWhenReady(true); // Make the ExoPlayer play when its data source is prepared

        if(videoUrl == null || videoUrl.equals("")) {
            Toast.makeText(context, "Vídeo não pode ser executado porque expirou ou inválido.", Toast.LENGTH_LONG).show(); // throw new RuntimeException("Missing a valid video url");
            return;
        }

        DataSource.Factory datasourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context,  act.getString(R.string.app_name)));

        MediaSource videoSource = new ExtractorMediaSource.Factory(datasourceFactory)
                .createMediaSource(Uri.parse(videoUrl));

        exoPlayer.prepare(videoSource);

        exoPlayer.addListener(new Player.DefaultEventListener() {
                                  @Override
                                  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                                      super.onPlayerStateChanged(playWhenReady, playbackState);
                                      // playbackState
                                      // 3 - starts playing
                                      // 4 - stops playing
                                      if (playWhenReady && playbackState == 3) {
                                          int durationVideo = getVideoDurationSeconds(exoPlayer);
                                          handleCountDownOnView(countdownTxtVw, durationVideo);
                                      }
                                  }
                              }
        );
    }

    public boolean isVideoLoading() {
        return exoPlayer.isLoading();
    }

    public void destroyMediaPlayer() {
        playerVw.setPlayer(null);
        exoPlayer.release();
        exoPlayer = null;
        countdown.cancel();
    };

    // HELPERS
    private void handleCountDownOnView(TextView txtVw, int countdownSec) {
        countdown = new UtilCountdown(txtVw, countdownSec);
//        countdown.start();
    }

    private int getVideoDurationSeconds(SimpleExoPlayer exoPlayer) {
        int timeMs=(int) exoPlayer.getDuration();
        return timeMs / 1000;
    }

}

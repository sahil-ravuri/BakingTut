package com.sahil.bakingtut;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.sahil.bakingtut.Model.Step;

import java.util.List;

public class StepDetailFragment extends Fragment {
    Button prev;
    Context context;
    Button next;
    ImageView imageView;
    TextView desc;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private long playerStopPosition;
    private boolean stopPlay;
    private long playbackPosition;

    private int currentWindow;

    private boolean playWhenReady = true;
    private String video_url;
    private List<Step> step_list;
    private int current_position;
    private int total_items = 0;

    public StepDetailFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_detail, container, false);
        if(getArguments()!=null){
            step_list= (List<Step>) getArguments().getSerializable(Constants.STEP_LIST_ACTIVITY_EXTRA_KEY);
            current_position=getArguments().getInt(Constants.POSITION_KEY,0);
        }

        desc = v.findViewById(R.id.description);
        prev = v.findViewById(R.id.prev_button);
        next = v.findViewById(R.id.next_button);
        imageView = v.findViewById(R.id.thumbnail_image);
        playerView = v.findViewById(R.id.video_view);
        playbackPosition = C.TIME_UNSET;
        imageView.setVisibility(View.GONE);
        if(savedInstanceState!=null)
        {
            playWhenReady = savedInstanceState.getBoolean(Constants.PLAY_WHEN_READY);
            current_position = savedInstanceState.getInt(Constants.CURRENT_POS_SAVE_INSTANCE);
            playbackPosition = savedInstanceState.getLong(Constants.PLAY_BACK_POS_SAVE_INSTANCE, C.TIME_UNSET);

        }
        Toast.makeText(getActivity(),"Position is "+current_position, Toast.LENGTH_SHORT).show();
        total_items = step_list.size();
        video_url = step_list.get(current_position).getVideoURL();
        desc.setText(step_list.get(current_position).getDescription());
        hideUnhideExo();
        if(current_position == 0)
        {
            prev.setVisibility(View.INVISIBLE);
        }
        if(current_position == total_items-1)
        {
            next.setVisibility(View.INVISIBLE);
        }

        next.setOnClickListener(v12 -> {
            releasePlayer();
            prev.setVisibility(View.VISIBLE);
            current_position++;
            desc.setText(step_list.get(current_position).getDescription());
            video_url = step_list.get(current_position).getVideoURL();
            if(current_position==total_items-1)
            {
                next.setVisibility(View.INVISIBLE);
            }
            hideUnhideExo();
            Toast.makeText(getActivity(),video_url, Toast.LENGTH_LONG).show();
            initializePlayer();
            player.seekTo(0);
        });

        prev.setOnClickListener(v1 -> {
            releasePlayer();
            next.setVisibility(View.VISIBLE);
            current_position--;
            desc.setText(step_list.get(current_position).getDescription());
            if(current_position == 0)
            {
                prev.setVisibility(View.INVISIBLE);
            }
            video_url = step_list.get(current_position).getVideoURL();
            hideUnhideExo();
            Toast.makeText(getActivity(),video_url,Toast.LENGTH_LONG).show();
            initializePlayer();
            player.seekTo(0);
        });

        return v;
    }
    private void initializePlayer()
    {
        DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(getActivity());
        DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector();
        DefaultLoadControl defaultLoadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(defaultRenderersFactory,defaultTrackSelector,defaultLoadControl);

/*        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(StepDetailFragment.this);*/
        playerView.setPlayer(player);

        Uri uri = Uri.parse(video_url);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);

        player.setPlayWhenReady(playWhenReady);
        if(playbackPosition!=0&&!stopPlay)
        {
            player.seekTo(playbackPosition);
        }
        else {
            player.seekTo(playerStopPosition);
        }
    }
    private MediaSource buildMediaSource(Uri uri)
    {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(Constants.BAKING_APP_AGENT)).createMediaSource(uri);
    }
    @Override
    public void onStart()
    {
        super.onStart();
        if (Util.SDK_INT > 23)
        {
            initializePlayer();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if ((Util.SDK_INT <= 23 || player == null))
        {
            initializePlayer();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (player != null) {
                playerStopPosition = player.getCurrentPosition();
                stopPlay = true;
                releasePlayer();
            }

        }
    }
    private void releasePlayer()
    {
        if (player != null)
        {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    public void hideUnhideExo()
    {
        if(TextUtils.isEmpty(video_url))
        {
            playerView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(step_list.get(current_position).getThumbnailURL()))
            {
                Glide.with(this).load(step_list.get(current_position).getThumbnailURL()).into(imageView);
            }

        }
        else
        {
            playerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);;
        outState.putInt(Constants.CURRENT_POS_SAVE_INSTANCE,current_position);
        outState.putLong(Constants.PLAY_BACK_POS_SAVE_INSTANCE,player.getCurrentPosition());
        outState.putBoolean(Constants.PLAY_WHEN_READY,player.getPlayWhenReady());
    }
}

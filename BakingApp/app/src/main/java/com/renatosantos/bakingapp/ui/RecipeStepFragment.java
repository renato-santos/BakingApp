package com.renatosantos.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.renatosantos.bakingapp.R;
import com.renatosantos.bakingapp.model.Recipe;
import com.renatosantos.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class RecipeStepFragment extends Fragment {

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mExoPlayerView;

    @Nullable
    @BindView(R.id.tv_description)
    TextView mDescriptionTextView;

    @Nullable
    @BindView(R.id.step_toolbar)
    Toolbar mStepToolbar;

    @BindView(R.id.image_no_video)
    ImageView mImageNoVideo;

    @Nullable
    @BindView(R.id.button_next)
    Button mNextButton;

    @Nullable
    @BindView(R.id.button_prev)
    Button mPreviousButton;

    private SimpleExoPlayer mExoPlayer;
    private StepListener stepListener;
    private List<Step> mRecipeSteps;
    private Recipe mRecipe;
    boolean isNextButtonEnabled = true;
    boolean isPreviousButtonEnabled = true;
    int stepIndex = 0;
    long playerPosition = 0;

    private static final String EXTRA_STEP_INDEX = "step_index";
    private static final String EXTRA_RECIPE = "recipe_item";


    public RecipeStepFragment() {
    }

    public interface StepListener {
        void onNext();
        void onPrevious();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        ButterKnife.bind(this, rootView);


        if (savedInstanceState != null) {
            stepIndex = savedInstanceState.getInt(EXTRA_STEP_INDEX);
            mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
        }

        populateUI(stepIndex);

        return rootView;
    }

    private void populateUI(int stepIndex){

        releasePlayer();

        mRecipeSteps = mRecipe.getSteps();

        Step step = mRecipeSteps.get(stepIndex);


        if(mStepToolbar != null) {
            mStepToolbar.setTitle(step.getShortDescription());
            mDescriptionTextView.setText(step.getDescription());

            mPreviousButton.setEnabled(stepIndex > 0);
            mNextButton.setEnabled(stepIndex < mRecipeSteps.size() - 1);
        }

        initializePlayer(step.getVideoURL());
    }

    private void initializePlayer(String videoURL) {

        if (mExoPlayer == null) {

            if(!videoURL.isEmpty()){

                Uri videoUri = Uri.parse(videoURL);

                mExoPlayerView.setVisibility(View.VISIBLE);
                mImageNoVideo.setVisibility(View.GONE);
                // Create an instance of the ExoPlayer.
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
                mExoPlayerView.setPlayer(mExoPlayer);
                // Prepare the MediaSource.
                String userAgent = Util.getUserAgent(getContext(), "BakingApp");
                MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                        getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);

                mExoPlayer.seekTo(playerPosition);


            } else {
                mExoPlayerView.setVisibility(View.GONE);
                mImageNoVideo.setVisibility(View.VISIBLE);
            }


        }
    }

    private void releasePlayer() {

        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void closeOnError() {
        getActivity().finish();
        Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    public void setRecipe(Recipe recipe){
        mRecipe = recipe;
    }



    public void setStepIndex (int index){
        stepIndex = index;
    }



    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer != null){
            //playerPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.setPlayWhenReady(false);
            Log.d("DEBUG", "PlayerPosition="+playerPosition);
        } else {
            playerPosition = 0;
        }

        //releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_STEP_INDEX, stepIndex);
        outState.putParcelable(EXTRA_RECIPE, mRecipe);
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Optional
    @OnClick(R.id.button_prev)
    public void previousStep() {
        stepIndex--;
        populateUI(stepIndex);
    }

    @Optional
    @OnClick(R.id.button_next)
    public void nextStep() {
        stepIndex++;
        populateUI(stepIndex);
    }
}

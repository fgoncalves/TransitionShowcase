package com.fred.transitionshowcase;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final int MAIN_VIEW = 0x01;
    private static final int FADED_VIEW = 0x02;
    private static final int SCALED_VIEW = 0x03;

    @Bind(R.id.container)
    FrameLayout container;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private LayoutInflater layoutInflater;
    private int currentView = MAIN_VIEW;
    private View mainView;
    private View androidIcon;
    private SmallAndroidFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        layoutInflater = getLayoutInflater();
        mainView = layoutInflater.inflate(R.layout.content_main, null);
        addView(mainView);
        mainView.findViewById(R.id.view_normal_animation_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewNormalAnimationButtonClicked();
            }
        });
        mainView.findViewById(R.id.view_resize_animation_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewResizeAnimationButtonClicked();
            }
        });
        mainView.findViewById(R.id.activity_animation_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActivityAnimationButtonClicked();
            }
        });
        mainView.findViewById(R.id.dialog_fragment_animation_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogFragmentButtonClicked();
            }
        });

        androidIcon = mainView.findViewById(R.id.android_icon);
        fragment = new SmallAndroidFragment();

        configureActivitiesTransitions();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void configureActivitiesTransitions() {
        Transition exit = new ChangeBounds();
        getWindow().setExitTransition(exit);

        Transition reenter = new Slide();
        getWindow().setReenterTransition(reenter);
    }

    /**
     * Add the given view to the main container
     *
     * @param view The view to add
     */
    private void addView(View view) {
        container.removeAllViews();
        container.addView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onViewNormalAnimationButtonClicked() {
        View view = layoutInflater.inflate(R.layout.big_android, null);
        addViewWithTransition(view);
        currentView = FADED_VIEW;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onDialogFragmentButtonClicked() {
        container.removeAllViews();
        getFragmentManager().beginTransaction()
                .replace(container.getId(), fragment)
                .addToBackStack(null)
                .commit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onActivityAnimationButtonClicked() {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, androidIcon, "androidIcon");
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent, options.toBundle());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onViewResizeAnimationButtonClicked() {
        View view = layoutInflater.inflate(R.layout.big_android, null);
        Transition transition = TransitionInflater
                .from(this)
                .inflateTransition(R.transition.scale_transition);
        addViewWithTransition(view, transition);
        currentView = SCALED_VIEW;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void addViewWithTransition(View view) {
        // The cast to ViewGroup is need because the constructor without the ViewGroup doesn't exist
        // in api 19. This makes this code target api 19. However, in api 21 this is deprecated
        // ¯\_(ツ)_/¯
        Scene scene = new Scene(container, (ViewGroup) view);
        TransitionManager.go(scene);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void addViewWithTransition(View view, Transition transition) {
        // The cast to ViewGroup is need because the constructor without the ViewGroup doesn't exist
        // in api 19. This makes this code target api 19. However, in api 21 this is deprecated
        // ¯\_(ツ)_/¯
        Scene scene = new Scene(container, (ViewGroup) view);
        TransitionManager.go(scene, transition);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onBackPressed() {
        switch (currentView) {
            case FADED_VIEW: {
                addViewWithTransition(mainView);
                currentView = MAIN_VIEW;
                break;
            }
            case SCALED_VIEW: {
                Transition transition = TransitionInflater
                        .from(this)
                        .inflateTransition(R.transition.scale_transition);
                addViewWithTransition(mainView, transition);
                currentView = MAIN_VIEW;
                break;
            }
            default:
                super.onBackPressed();
        }
    }
}

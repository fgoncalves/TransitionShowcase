package com.fred.transitionshowcase;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.transition.Transition;

public class SecondActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_android);
        configureActivitiesTransitions();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void configureActivitiesTransitions() {
        Transition enterTransition = new ChangeBounds();
        getWindow().setEnterTransition(enterTransition);

        Transition returnTransition = new Slide();
        getWindow().setReturnTransition(returnTransition);
    }
}

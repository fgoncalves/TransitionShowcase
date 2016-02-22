package com.fred.transitionshowcase;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmallAndroidFragment extends Fragment {
    @Bind(R.id.android_icon)
    View androidIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.small_android, null);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.transition)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onStartTransition() {
        BigAndroidFragment fragment = new BigAndroidFragment();
        fragment.setSharedElementEnterTransition(new ChangeBounds());
        getFragmentManager().beginTransaction()
                .addSharedElement(androidIcon, "androidIcon")
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}

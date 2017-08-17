package ru.littlebrains.tabatatimer.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ru.littlebrains.tabatatimer.R;

/**
 * Created by Evgeny on 05.01.2016.
 */
public class TabataFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setTitle(R.string.tabata);

        if (rootView != null) return rootView;

        rootView = (FrameLayout) inflater.inflate(R.layout.fragment_tabata, container, false);

        return rootView;
    }
}

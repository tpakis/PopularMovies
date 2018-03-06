package com.popularmovies.aithanasakis.popularmovies.ui.details;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popularmovies.aithanasakis.popularmovies.R;

/**
 * Created by 3piCerberus on 06/03/2018.
 */

public class DetailsFragment extends Fragment {


    public DetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View viewgroup = inflater.inflate(R.layout.details_fragment,container,false);

        return viewgroup;
    }
}

package com.bli.spotifystreamer.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseDialogFragment extends DialogFragment{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        if (savedInstanceState == null || savedInstanceState.isEmpty())
            savedInstanceState = WorkaroundSavedState.savedInstanceState;

        setRetainInstance(true);
        Log.d("TAG", "saved instance state oncreate: "
                + WorkaroundSavedState.savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if (savedInstanceState == null || savedInstanceState.isEmpty())
            savedInstanceState = WorkaroundSavedState.savedInstanceState;
        Log.d("TAG", "saved instance state oncretaedialog: "
                + WorkaroundSavedState.savedInstanceState);

        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null || savedInstanceState.isEmpty())
            savedInstanceState = WorkaroundSavedState.savedInstanceState;

        Log.d("TAG", "saved instance state oncretaeview: "
                + WorkaroundSavedState.savedInstanceState);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() // necessary for restoring the dialog
    {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);

        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        // ...

        super.onSaveInstanceState(outState);
        WorkaroundSavedState.savedInstanceState = outState;
        Log.d("TAG", "saved instance state onsaveins: "
                + WorkaroundSavedState.savedInstanceState);

    }

    @Override
    public void onDestroy()
    {
        WorkaroundSavedState.savedInstanceState = null;
        super.onDestroy();
    }

    public static final class WorkaroundSavedState {
        public static Bundle savedInstanceState;
    }
}

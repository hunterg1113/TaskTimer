package com.example.huntergreer.tasktimer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Hunter on 3/2/2018.
 */

public class AppDialog extends AppCompatDialogFragment {
    private static final String TAG = "AppDialog";

    public static final String DIALOG_ID = "id";
    public static final String DIALOG_MESSAGE = "message";
    public static final String DIALOG_POSITIVE_RID = "positive_rid";
    public static final String DIALOG_NEGATIVE_RID = "negative_rid";

    private DialogEvents mListener;

    interface DialogEvents {
        void onPositiveDialogResult(int dialogId, Bundle args);

        void onNegativeDialogResult(int dialogId, Bundle args);

        void onDialogCancelled(int dialogId);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: entering");
        super.onAttach(context);
        Activity activity = getActivity();
        if (!(activity instanceof DialogEvents)) {
            throw new ClassCastException(context.toString() + " must implement AppDialog.DialogEvents interface");
        }
        mListener = (DialogEvents) activity;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: entering");
        super.onDetach();
        mListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Bundle args = getArguments();
        final int dialogId;
        String message;
        int positiveStringId;
        int negativeStringId;

        if (args != null) {
            dialogId = args.getInt(DIALOG_ID);
            message = args.getString(DIALOG_MESSAGE);

            if (dialogId == 0 || message == null) {
                throw new IllegalArgumentException("DIALOG_ID and/or DIALOG_MESSAGE not present in bundle");
            }

            positiveStringId = args.getInt(DIALOG_POSITIVE_RID);
            if (positiveStringId == 0) {
                positiveStringId = R.string.ok;
            }
            negativeStringId = args.getInt(DIALOG_NEGATIVE_RID);
            if (negativeStringId == 0) {
                negativeStringId = R.string.cancel;
            }
        } else {
            throw new IllegalArgumentException("Must pass DIALOG_ID and DIALOG_MESSAGE in the bundle");
        }

        builder.setMessage(message).
                setPositiveButton(positiveStringId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null) mListener.onPositiveDialogResult(dialogId, args);
                    }
                }).
                setNegativeButton(negativeStringId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null) mListener.onNegativeDialogResult(dialogId, args);
                    }
                });
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mListener != null) {
            int dialogId = getArguments().getInt(DIALOG_ID);
            mListener.onDialogCancelled(dialogId);
        }
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        Log.d(TAG, "onInflate: called");
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d(TAG, "onHiddenChanged: called");
        super.onHiddenChanged(hidden);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: called");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: called");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Log.d(TAG, "onViewStateRestored: called");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: called");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: called");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: called");
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: called");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: called");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: called");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        super.onDestroy();
    }
}

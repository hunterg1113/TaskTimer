package com.example.huntergreer.tasktimer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class AddEditActivity extends AppCompatActivity implements AddEditActivityFragment.OnSaveClicked, AppDialog.DialogEvents {
    private static final String TAG = "AddEditActivity";
    public static final int DIALOG_ID_CANCEL_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getSupportFragmentManager().findFragmentById(R.id.add_edit_frame_layout) == null) {
            AddEditActivityFragment fragment = new AddEditActivityFragment();

            Bundle args = getIntent().getExtras();
            fragment.setArguments(args);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.add_edit_frame_layout, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AddEditActivityFragment fragment = (AddEditActivityFragment) getSupportFragmentManager().findFragmentById(R.id.add_edit_frame_layout);
                if (fragment.canClose()) {
                    return super.onOptionsItemSelected(item);
                } else {
                    showConfirmationDialog();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveClicked() {
        finish();
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        finish();
    }

    @Override
    public void onDialogCancelled(int dialogId) {
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditActivityFragment fragment = (AddEditActivityFragment) fragmentManager.findFragmentById(R.id.add_edit_frame_layout);
        if (fragment.canClose()) {
            super.onBackPressed();
        } else {
            showConfirmationDialog();
        }
    }

    public void showConfirmationDialog() {
        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_CANCEL_EDIT);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.cancelEditDiag_message));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.cancelEditDiag_positive_caption);
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.cancelEditDiag_negative_caption);

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);
    }
}

package com.example.huntergreer.tasktimer;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.OnTaskClickListener,
        AddEditActivityFragment.OnSaveClicked, AppDialog.DialogEvents {

    private static final String TAG = "MainActivity";

    //    Whether or not activity is in 2-pane mode, i.e. running in
    //    landscape on a tablet
    private boolean mTwoPane = false;

    public static final int DIALOG_ID_DELETE = 1;
    public static final int DIALOG_ID_CANCEL_EDIT = 2;

    private AlertDialog mDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.task_details_container) != null) mTwoPane = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menumain_addTask:
                taskEditRequest(null);
                break;
            case R.id.menumain_showDurations:
                break;
            case R.id.menumain_settings:
                break;
            case R.id.menumain_showAbout:
                showAboutDialog();
                break;
            case R.id.menumain_generate:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    public void showAboutDialog() {
        @SuppressLint("InflateParams") View messageView = getLayoutInflater().inflate(R.layout.about, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);

        builder.setView(messageView);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });

        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);

        TextView tv = (TextView) messageView.findViewById(R.id.about_version);
        tv.setText("v" + BuildConfig.VERSION_NAME);

        TextView aboutUrl = (TextView) messageView.findViewById(R.id.about_url);
        if (aboutUrl != null) {
            aboutUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    String s = ((TextView) v).getText().toString();
                    browserIntent.setData(Uri.parse(s));
                    try {
                        startActivity(browserIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "No browser application found or URL is invalid", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        mDialog.show();
    }

    /*
        @SuppressLint("SetTextI18n")
        public void showAboutDialog() {
            @SuppressLint("InflateParams") View messageView = getLayoutInflater().inflate(R.layout.about, null, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);

            builder.setView(messageView);

            mDialog = builder.create();
            mDialog.setCanceledOnTouchOutside(true);

            messageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: isShowing() = " + mDialog.isShowing());
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                }
            });

            TextView tv = (TextView) messageView.findViewById(R.id.about_version);
            tv.setText("v" + BuildConfig.VERSION_NAME);

            mDialog.show();
        }

    */
    private void taskEditRequest(Task task) {
        if (mTwoPane) {
            AddEditActivityFragment fragment = new AddEditActivityFragment();

            Bundle args = new Bundle();
            args.putSerializable(Task.class.getSimpleName(), task);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.task_details_container, fragment).commit();
        } else {
            Intent detailIntent = new Intent(this, AddEditActivity.class);
            if (task != null) { // editing a task
                detailIntent.putExtra(Task.class.getSimpleName(), task);
                startActivity(detailIntent);
            } else { // adding a new task
                startActivity(detailIntent);
            }
        }
    }

    @Override
    public void onEditClick(Task task) {
        taskEditRequest(task);
    }

    @Override
    public void onDeleteClick(Task task) {
        AppDialog appDialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_DELETE);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.deldiag_message, task.getId(), task.getName()));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.deldiag_positive_caption);
        args.putLong("TaskId", task.getId());

        appDialog.setArguments(args);
        appDialog.show(getSupportFragmentManager(), null);

    }

    @Override
    public void onSaveClicked() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: called");
        switch (dialogId) {
            case DIALOG_ID_DELETE:
                long taskId = args.getLong("TaskId");
                if (BuildConfig.DEBUG && taskId == 0) throw new AssertionError("Task id is zero");
                getContentResolver().delete(TasksContract.buildTaskUri(taskId), null, null);
                break;
            case DIALOG_ID_CANCEL_EDIT:
                //no action required
                break;
        }
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: called");
        switch (dialogId) {
            case DIALOG_ID_DELETE:
                //no action required
                break;
            case DIALOG_ID_CANCEL_EDIT:
                finish();
                break;
        }
    }

    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: called");
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditActivityFragment fragment = (AddEditActivityFragment) fragmentManager.findFragmentById(R.id.task_details_container);
        if (fragment == null || fragment.canClose()) {
            super.onBackPressed();
        } else {
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

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        Log.d(TAG, "onAttachFragment: onAttachFragment called, fragment is " + fragment.toString());
        super.onAttachFragment(fragment);
    }
}

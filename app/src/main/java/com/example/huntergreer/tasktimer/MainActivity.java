package com.example.huntergreer.tasktimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //    Whether or not activity is in 2-pane mode, i.e. running in
    //    landscape on a tablet
    private boolean mTwoPane = false;

    private static final String ADD_EDIT_FRAGMENT = "AddEditFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, ": starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                break;
            case R.id.menumain_generate:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void taskEditRequest(Task task) {
        if (mTwoPane) {
            Log.d(TAG, "taskEditRequest: in 2-pane mode tablet");
        } else {
            Log.d(TAG, "taskEditRequest: in single-pane mode tablet");

            // In single-pane mode, start the detail activity for the selected item Id
            Intent detailIntent = new Intent(this, AddEditActivity.class);
            if (task != null) { // editing a task
                detailIntent.putExtra(Task.class.getSimpleName(), task);
                startActivity(detailIntent);
            } else { // adding a new task
                startActivity(detailIntent);
            }
        }
    }
}

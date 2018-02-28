package com.example.huntergreer.tasktimer;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Hunter on 2/27/2018.
 */

class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.TaskViewHolder> {
    private static final String TAG = "CursorRecyclerViewAdapt";

    private Cursor mCursor;

    public CursorRecyclerViewAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_items, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        if (mCursor == null || mCursor.getCount() == 0) {
            holder.name.setText(R.string.instructions_heading);
            holder.description.setText(R.string.instructions);
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        } else {
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("Couldn't move cursor to position " + position);
            }
            holder.name.setText(mCursor.getString(mCursor.getColumnIndex(TasksContract.Columns.TASKS_NAME)));
            holder.description.setText(mCursor.getString(mCursor.getColumnIndex(TasksContract.Columns.TASKS_DESCRIPTION)));
            holder.editButton.setVisibility(View.VISIBLE);       // TODO add onClickListener
            holder.deleteButton.setVisibility(View.VISIBLE);     // TODO add onClickListener
        }
    }


    @Override
    public int getItemCount() {
        if (mCursor == null || mCursor.getCount() == 0) {
            return 1;
        } else {
            return mCursor.getCount();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor
     * The returning Cursor is <em>not</em> closed.
     *
     * @param newCursor The new Cursor to be used
     * @return Returns the previously set Cursor, or null if there wasn't one
     * If the given new Cursor is the same instance as the previously set Cursor,
     * null is also returned.
     */
    Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }

        final Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
            //notify observers of new cursor
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView name = null;
        TextView description = null;
        ImageButton editButton = null;
        ImageButton deleteButton = null;

        public TaskViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tli_name);
            description = (TextView) itemView.findViewById(R.id.tli_description);
            editButton = (ImageButton) itemView.findViewById(R.id.tli_edit);
            deleteButton = (ImageButton) itemView.findViewById(R.id.tli_delete);
        }
    }
}

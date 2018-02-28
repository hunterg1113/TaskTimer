package com.example.huntergreer.tasktimer;

import java.io.Serializable;

/**
 * Created by Hunter on 2/26/2018.
 */

class Task implements Serializable {
    public static final long serialVersionUID = 20180226L;

    private long m_Id;
    private final String mName;
    private final String mDescription;
    private final int mSortOrder;

    public Task(long id, String name, String description, int sortOrder) {
        m_Id = id;
        mName = name;
        mDescription = description;
        mSortOrder = sortOrder;
    }

    public long getId() {
        return m_Id;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getSortOrder() {
        return mSortOrder;
    }

    public void setId(long id) {
        m_Id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "m_Id=" + m_Id +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mSortOrder='" + mSortOrder + '\'' +
                '}';
    }
}

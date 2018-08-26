package com.zn.google_android_dev_exam_practice.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

/**
 * ViewModels live beyond the activity and fragment lifecycle, so it can serve as an effective
 * holder for your data that would be presented to the users. Interact with your repository
 * exclusively through the ViewModel. Don't reference your repository outside of the ViewModel
 * to guarantee that your data live across configuration changes, and to ensure that the ViewModel
 * is singularly responsible for interacting with the repository
 * <p>
 * Use LiveData so the data in the ViewModel are automatically updated when the database changes
 * <p>
 * The AndroidViewModel is used if you need to reference the Application context. It is bad practice
 * to pass in and store references to Activity, Fragment, or View instsances as these can be
 * destroyed and recreated many times, leading to references that point to the destroyed objects.
 * This poses risks for memory leaks, so use AndroidViewModel so you can reference your
 * Application's context
 */
public class TaskViewModel extends AndroidViewModel {
    private TaskRepository mRepository;
    private LiveData<PagedList<Task>> mAllTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TaskRepository(application);
        mAllTasks = mRepository.getAllTasks();
    }

    public LiveData<PagedList<Task>> getAllTasks() {
        return mAllTasks;
    }

    public void insert(Task task) {
        mRepository.insert(task);
    }

    public void update(Task task) {
        mRepository.update(task);
    }

    public void delete(long id) {
        mRepository.delete(id);
    }
}

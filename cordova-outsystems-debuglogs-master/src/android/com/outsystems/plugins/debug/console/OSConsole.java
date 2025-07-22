
package com.outsystems.plugins.debug.console;

import android.app.Activity;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class OSConsole implements OSConsoleFragment.OSUiConsoleInterface, OSConsoleCommands {
    private OSConsoleFragment mConsoleFragment;
    private View mContainer;
    private ArrayList<String> mEntries;
    private FragmentManager mFragmentManager;

    public OSConsole(Activity paramActivity, View container) {
        this.mContainer = container;
        if (paramActivity instanceof FragmentActivity) {
            this.mFragmentManager = ((FragmentActivity) paramActivity).getSupportFragmentManager();
        } else {
            throw new IllegalArgumentException("Activity must be a FragmentActivity for AndroidX fragments");
        }
        this.mEntries = new ArrayList<>();
    }

    public void clear() {
        if (this.mConsoleFragment != null) {
            this.mConsoleFragment.clear();
        }
        this.mEntries.clear();
    }

    public void close() {
        if ((this.mContainer.getVisibility() == View.VISIBLE) && (this.mFragmentManager != null)) {
            FragmentTransaction localFragmentTransaction = this.mFragmentManager.beginTransaction();
            localFragmentTransaction.remove(this.mConsoleFragment);
            localFragmentTransaction.commit();

            this.mContainer.setVisibility(View.GONE);
        }
    }

    public OSConsoleCommands getConsoleInterface() {
        return this;
    }

    public void log(String paramString) {
        if ((this.mConsoleFragment != null) && (this.mConsoleFragment.isResumed())) {
            this.mConsoleFragment.log(paramString);
        }
        this.mEntries.add(paramString);
    }

    public void onClear() {
        this.mEntries.clear();
    }

    public void onClose() {
        close();
    }

    public void onReadyToReceiveData() {
        Iterator localIterator = this.mEntries.iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            this.mConsoleFragment.log(str);
        }
    }

    public void open() {
        if (this.mConsoleFragment == null) {
            this.mConsoleFragment = OSConsoleFragment.newInstance();
        }
        this.mConsoleFragment.setOsUiConsoleListener(this);
        if (this.mContainer.getVisibility() != View.VISIBLE) {
            FragmentTransaction localFragmentTransaction = this.mFragmentManager.beginTransaction();
            localFragmentTransaction.add(this.mContainer.getId(), this.mConsoleFragment);
            localFragmentTransaction.commit();
            this.mContainer.setVisibility(View.VISIBLE);
        }
    }
}
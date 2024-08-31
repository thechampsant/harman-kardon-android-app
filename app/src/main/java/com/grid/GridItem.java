package com.grid;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.util.concurrent.Callable;

public class GridItem<T> {

    private String Title;
    private int IconResourceID;
    private Drawable IconResource;
    private boolean IsIconResourceExists = false;
    private boolean IsIconExists = false;
    private int AppIndex = -1;
    @SuppressWarnings("rawtypes")
    private Class activity;
    private int InitialNav = -1;
    private boolean finishcurrentactivity = false;
    private boolean IsCallable = false;
    private boolean IsApplication = false;
    public Callable<T> func;
    private Bundle bundle;


    @SuppressWarnings("rawtypes")
    public GridItem setAsApplication() {
        IsApplication = true;
        return this;
    }

    public boolean isApplication() {
        return IsApplication;
    }

    @SuppressWarnings("rawtypes")
    public GridItem setAppIndex(int index) {
        AppIndex = index;
        return this;
    }

    public int getAppIndex() {
        return AppIndex;
    }

    @SuppressWarnings("rawtypes")
    public GridItem navigate(int tabindex) {
        this.InitialNav = tabindex;
        return this;
    }

    public int getInitialNav() {
        return this.InitialNav;
    }

    @SuppressWarnings("rawtypes")
    public GridItem finish() {
        finishcurrentactivity = true;
        return this;
    }

    public boolean isfinishable() {
        return this.finishcurrentactivity;
    }

    @SuppressWarnings("rawtypes")
    public GridItem Call(Callable<T> func) {

        IsCallable = true;
        this.func = func;
        return this;
    }

    public boolean IsMethodCallable() {
        return this.IsCallable;
    }

    @SuppressWarnings("rawtypes")
    public GridItem setItem(String Title, Class activity) {
        this.Title = Title;
        this.activity = activity;
        this.IsIconExists = false;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public GridItem setItem(String Title, Class activity, int IconResourceID) {
        this.Title = Title;
        this.activity = activity;
        this.IsIconExists = true;
        this.IconResourceID = IconResourceID;
        return this;
    }

    public GridItem setItem(String Title, Class activity, int IconResourceID, Bundle bundle) {
        this.Title = Title;
        this.activity = activity;
        this.IsIconExists = true;
        this.IconResourceID = IconResourceID;
        this.bundle = bundle;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public GridItem setItem(String Title, Class activity, Drawable IconResource) {
        this.Title = Title;
        this.activity = activity;
        this.IconResource = IconResource;
        this.IsIconResourceExists = true;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public GridItem setItem(String Title, Class activity, int IconResourceID,
                            boolean showIcon) {
        this.Title = Title;
        this.activity = activity;
        this.IsIconExists = showIcon;
        this.IconResourceID = IconResourceID;
        return this;
    }

    public boolean IsShowIcon() {
        return this.IsIconExists;
    }

    public boolean IsShowIconDrawable() {
        return this.IsIconResourceExists;
    }

    public String getTitle() {
        return this.Title;
    }

    public int getIcon() {
        return this.IconResourceID;
    }

    public Drawable getIconDrawable() {
        return this.IconResource;
    }

    public boolean IsActivityExists() {
        if (getActivityClass() == null)
            return false;
        else
            return true;
    }

    @SuppressWarnings("rawtypes")
    public Class getActivityClass() {
        return this.activity;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}

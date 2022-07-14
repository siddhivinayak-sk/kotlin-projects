package com.sk.ktl.basic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyMeeting {
    private String title;

    public void addTitle(@NotNull String title) {
        this.title = title;
    }

    public @Nullable String meetingTitle() {
        return title;
    }
}

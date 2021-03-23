package com.example.myfirstandroid.Utils;

public class TimeCount {
    private long time;
    private static class Singleton{
        public static TimeCount instance = new TimeCount();
    }

    public static TimeCount getInstance(){
        return Singleton.instance;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

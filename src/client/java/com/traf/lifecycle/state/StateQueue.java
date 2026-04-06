package com.traf.lifecycle.state;
import java.util.ArrayList;
import java.util.List;


/*
* If you attend to do something during the next game tick, add it to the queue
* and it will perform everything
* */
public class StateQueue {
    private static List<Runnable> actions;
    private final static int MAX_CAPACITY = 20;

    public static void setup() {
        actions = new ArrayList<Runnable>();
    }

    public static void add(Runnable r){
        if(actions.size() + 1 >= MAX_CAPACITY) return;
        actions.add(r);
    }

    public void flush() {
        actions.forEach(Runnable::run);
        actions.clear();
    }

}

package com.codev.capturalo.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by junior on 24/03/16.
 */
public class NotificationsID {

    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}

package com.nonameyet.ui.life;

public interface StatusSubject {

    void attachListener(StatusListener listener);

    void detachListener(StatusListener listener);

    void notify(StatusListener.StatusEvent event);
}

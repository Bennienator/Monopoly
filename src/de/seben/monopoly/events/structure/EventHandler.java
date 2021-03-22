package de.seben.monopoly.events.structure;

import de.seben.monopoly.main.Monopoly;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventHandler implements Comparable<EventHandler>{

    private final EventListener listener;
    private final Method method;
    private final Event annotation;

    public EventHandler(EventListener listener, Method method, Event annotation) {
        this.listener = listener;
        this.method = method;
        this.annotation = annotation;
    }

    public Event getAnnotation() {
        return annotation;
    }

    public Method getMethod() {
        return method;
    }
    public EventListener getListener() {
        return listener;
    }

    public void execute(IEvent event) {
        try {
            method.invoke(listener, event);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Monopoly.debug("Exception when performing EventHandler " + this.listener.getClass().getSimpleName() + " for event " + event.getClass().getSimpleName());
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return "EventHandler " + this.listener.getClass().getSimpleName() + ": " + method.getName() + "";
    }

    public int getPriority() {
        return annotation.priority();
    }

    @Override
    public int compareTo(EventHandler other) {
        int annotation = this.annotation.priority() - other.annotation.priority();
        if (annotation == 0)
            annotation = this.listener.hashCode() - other.listener.hashCode();
        return annotation == 0 ? this.hashCode() - other.hashCode() : annotation;
    }

}
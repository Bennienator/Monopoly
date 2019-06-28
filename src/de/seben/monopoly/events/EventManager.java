package de.seben.monopoly.events;

import java.util.*;
import java.lang.reflect.Method;
import java.lang.Class;
import java.lang.annotation.Annotation;

import de.seben.monopoly.main.Monopoly;

public class EventManager {

    public EventManager() {
        Monopoly.debug("Created instance");
        this.bindings = new HashMap<Class<? extends IEvent>, Collection<EventHandler>>();
        this.registeredListeners = new HashSet<EventListener>();
    }

    public static final int PRE = -1;
    public static final int ALL = 0;
    public static final int POST = 1;

    private Map<Class<? extends IEvent>, Collection<EventHandler>> bindings;
    private Set<EventListener> registeredListeners;

    public List<EventHandler> getListenersFor(Class<? extends IEvent> clazz) {
        if (!this.bindings.containsKey(clazz))
            return new ArrayList<EventHandler>(); // No handlers so we return an empty list
        return new ArrayList<EventHandler>(this.bindings.get(clazz));
    }

    public <T extends IEvent> T executeEvent(T event, int i) {
        Collection<EventHandler> handlers = this.bindings.get(event.getClass());
        if (handlers == null) {
            Monopoly.debug("Event " + event.getClass().getSimpleName() + " has no handlers.");
            return event;
        }
        Monopoly.debug("Event " + event.getClass().getSimpleName() + " was fired! Handler: " + handlers.iterator().next().getListener().getClass().getSimpleName());
        for (EventHandler handler : handlers) {
            // Basic support for multi-stage events. More can be added later by specifying exactly which priority to be executed - executeEventPre(event, lessThanPriority) for example
            if (i == PRE && handler.getPriority() >= 0)
                continue;
            if (i == POST && handler.getPriority() < 0)
                continue;
            handler.execute(event);
        }
        return event;
    }

    public <T extends IEvent> T executeEvent(T event) {
        return this.executeEvent(event, ALL);
    }

    public void registerListener(EventListener listener) {

        if (registeredListeners.contains(listener)) {
            return;
        }

        Method[] methods = listener.getClass().getDeclaredMethods();
        this.registeredListeners.add(listener);
        for (Method method : methods) {
            Event annotation = method.getAnnotation(Event.class);
            if (annotation == null){
                continue;
            }

            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1){
                continue;
            }

            Class<?> param = params[0];

            if (!method.getReturnType().equals(void.class)) {
                continue;
            }

            if (IEvent.class.isAssignableFrom(param)) {
                @SuppressWarnings("unchecked") // Java just doesn't understand that this actually is a safe cast because of the above if-statement
                Class<? extends IEvent> realParam = (Class<? extends IEvent>) param;

                if (!this.bindings.containsKey(realParam)) {
                    this.bindings.put(realParam, new TreeSet<EventHandler>());
                }
                Collection<EventHandler> eventHandlersForEvent = this.bindings.get(realParam);
                Monopoly.debug("Registered listener '" + method.getName() + "' for event '" + realParam.getSimpleName() + "'");
                eventHandlersForEvent.add(createEventHandler(listener, method, annotation));
            }
        }
    }

    private EventHandler createEventHandler(final EventListener listener, final Method method, final Event annotation) {
        return new EventHandler(listener, method, annotation);
    }

    public void clearListeners() {
        this.bindings.clear();
        this.registeredListeners.clear();
    }

    public void removeListener(EventListener listener) {
        for (Map.Entry<Class<? extends IEvent>, Collection<EventHandler>> ee : bindings.entrySet()) {
            Iterator<EventHandler> it = ee.getValue().iterator();
            while (it.hasNext()) {
                EventHandler curr = it.next();
                if (curr.getListener() == listener)
                    it.remove();
            }
        }
        this.registeredListeners.remove(listener);
    }
    public Map<Class<? extends IEvent>, Collection<EventHandler>> getBindings() {
        return new HashMap<Class<? extends IEvent>, Collection<EventHandler>>(bindings);
    }
    public Set<EventListener> getRegisteredListeners() {
        return new HashSet<EventListener>(registeredListeners);
    }
}
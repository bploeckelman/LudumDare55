package lando.systems.ld55.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventManager {
    private static final EventManager instance = new EventManager();

    private final Map<String, Set<EventListener>> listeners = new HashMap<>();

    public static EventManager get() {
        return instance;
    }

    public void subscribe(String type, EventListener listener) {
        Set<EventListener> eventListeners = listeners.get(type);
        if (eventListeners == null) {
            eventListeners = new HashSet<>();
            listeners.put(type, eventListeners);
        }
        eventListeners.add(listener);
    }

    public void unsubscribe(String eventType, EventListener listener) {
        Set<EventListener> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    public void dispatch(String eventType, Object... data) {
        Set<EventListener> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            for (EventListener listener : eventListeners) {
                listener.onEvent(eventType, data);
            }
        }
    }

    public interface EventListener {
        void onEvent(String eventType, Object... data);
    }
}

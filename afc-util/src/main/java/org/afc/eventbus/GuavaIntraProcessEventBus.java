package org.afc.eventbus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.eventbus.EventBus;

public class GuavaIntraProcessEventBus implements IntraProcessEventBus<Object, Object> {

	private Map<String, EventBus> eventBuses;
	
	public GuavaIntraProcessEventBus() {
		eventBuses = new ConcurrentHashMap<>();
	}
	
	@Override
	public Registration<Object> register(String topic, Object listener) {
		EventBus eventBus = ensureEventBus(topic);
		eventBus.register(listener);
		return new Registration<Object>(topic, listener);
	}

	@Override
	public void unregister(Registration<Object> registration) {
		EventBus eventBus = ensureEventBus(registration.getTopic());
		eventBus.unregister(registration.getListener());
	}
	
	@Override
	public void unregister(String topic, Object listener) {
		EventBus eventBus = ensureEventBus(topic);
		eventBus.unregister(listener);
	}

	@Override
	public void post(String topic, Object event) {
		EventBus eventBus = ensureEventBus(topic);
		eventBus.post(event);
	}

	private EventBus ensureEventBus(String topic) {
		synchronized(eventBuses) {
			EventBus eventBus = eventBuses.get(topic);
			if (eventBus == null) {
				eventBus = new EventBus();
				eventBuses.put(topic, eventBus);
			}
			return eventBus;
		}
	}
}

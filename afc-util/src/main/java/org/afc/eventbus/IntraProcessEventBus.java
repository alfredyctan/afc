package org.afc.eventbus;

public interface IntraProcessEventBus<L, E> {

	public class Registration<L> {
		
		private String topic;

		private L listener;

		public Registration() {}
		
		public Registration(String topic, L listener) {
			this.topic = topic;
			this.listener = listener;
		}

		public String getTopic() {
			return topic;
		}

		public L getListener() {
			return listener;
		}
	}
	
	public Registration<L> register(String topic, L listener);
	
	public void unregister(Registration<L> registration);

	public void unregister(String topic, L listener);

	public void post(String topic, E event);
	
}

package org.afc.outbound;

public interface OutboundPublisher<T> {

	public void publish(T message);

}

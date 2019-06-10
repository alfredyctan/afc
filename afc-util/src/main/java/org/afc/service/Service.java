package org.afc.service;

public interface Service<T, R> {

	default public void init() {
	};

	default public void start() {
	};

	default public R serve(T request) {
		return null;
	};

	default public void stop() {
	};

	default public void dispose() {
	};
}

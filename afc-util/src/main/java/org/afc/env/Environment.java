package org.afc.env;


public class Environment {

	public static final String SERVICE = "sys.service";

	public static final String ENV = "sys.env";

	public static final String REGION = "sys.region";

	public static final String CLUSTER = "sys.cluster";

	public static final String INSTANCE = "sys.instance";

	public static void set(String service, String env, String region, String cluster, String instance) {
		System.setProperty(SERVICE, service);
		System.setProperty(ENV, env);
		System.setProperty(REGION, region);
		System.setProperty(CLUSTER, cluster);
		System.setProperty(INSTANCE, instance);
	}
	
	public static String service() {
		return System.getProperty(SERVICE);
	}

	public static String env() {
		return System.getProperty(ENV);
	}

	public static String region() {
		return System.getProperty(REGION);
	}

	public static String cluster() {
		return System.getProperty(CLUSTER);
	}

	public static String instance() {
		return System.getProperty(INSTANCE);
	}
}

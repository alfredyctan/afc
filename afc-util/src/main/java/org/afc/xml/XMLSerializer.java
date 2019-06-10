package org.afc.xml;

public interface XMLSerializer<O, X> {

	public X serialize(O object);

	public <S> S deserialize(X xml, Class<S> clazz);

}

package org.afc.xml;

import java.util.Collection;

import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.mapper.Mapper;

public class XStreamCollectionConverter extends CollectionConverter {

	public XStreamCollectionConverter(Mapper mapper) {
		super(mapper, null);
	}

	@Override
	public boolean canConvert(Class type) {
		return Collection.class.isAssignableFrom(type);
	}
}

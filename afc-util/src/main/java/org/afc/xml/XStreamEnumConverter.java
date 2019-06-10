package org.afc.xml;

import java.util.Map;
import java.util.function.Function;

import org.afc.util.EnumUtil;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class XStreamEnumConverter<E> implements Converter {

	private Class<E> clazz;
	
	private Map<String, E> maps;

	private Function<E, String> keyMapper;
	
	public XStreamEnumConverter(Class<E> clazz, Function<E, String> keyMapper) {
		this.clazz = clazz;
		this.keyMapper = keyMapper;
		this.maps = EnumUtil.mapper(clazz.getEnumConstants(), keyMapper);
	}
	
	
	@Override
	public boolean canConvert(Class type) {
		return clazz.equals(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		writer.setValue(keyMapper.apply((E)source));
		
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		return maps.get(reader.getValue());
	}

}

package org.afc.xml;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.afc.util.ClasspathUtil;
import org.afc.util.ObjectUtil;
import org.afc.util.StringUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class XStreamStringXMLSerializer<O> implements XMLSerializer<O, String> {

	private static final Logger logger = LoggerFactory.getLogger(XStreamStringXMLSerializer.class);

	private final ThreadLocal<XStream> _xstream = new ThreadLocal<XStream>() {
		@Override
		protected XStream initialValue() {
			XStream xstream;
			if (StringUtil.hasValue(namespace) && StringUtil.hasValue(namespaceUrl)) {
				StaxDriver staxDriver = new StaxDriver();
				QNameMap qnameMap = new QNameMap();
				qnameMap.setDefaultPrefix(namespace);
				qnameMap.setDefaultNamespace(namespaceUrl);
				staxDriver.setQnameMap(qnameMap);
				xstream = new XStream(staxDriver);
			} else {
				xstream = new XStream();
			}

			xstream.registerConverter(new XStreamCollectionConverter(xstream.getMapper()), 10);
			xstream.aliasSystemAttribute(null, "class");
			xstream.allowTypesByWildcard(packages);
			xstream.autodetectAnnotations(true);
			enums.forEach(e -> {
				xstream.registerConverter(e);
			});
			omits.forEach((c, o) -> {
				for (String f : o) {
					xstream.omitField(c, f);
				}
			});
			aliases.forEach((a, c) -> {
				logger.info("register alias [{}]:[{}]", a, c);
				xstream.alias(a, c);
			});
			classes.forEach(c -> {
				logger.info("process xstream annotation [{}]", c.getName());
				xstream.processAnnotations(c);
			});
			return xstream;
		};
	};

	@Setter
	private boolean formatted;

	@Setter
	private String namespace;

	@Setter
	private String namespaceUrl;

	private String[] packages;

	private String[] patterns;

	private List<Class<?>> classes;

	private Map<String, Class<?>> aliases;

	private Map<Class<?>, String[]> omits;

	private List<XStreamEnumConverter<?>> enums;

	public XStreamStringXMLSerializer(String... packages) {
		this.formatted = true;
		this.packages = packages;
		this.patterns = new String[packages.length];
		this.classes = new LinkedList<>();
		this.aliases = new ConcurrentHashMap<>();
		this.omits = new ConcurrentHashMap<>();
		this.enums = new LinkedList<>();
		for (int i = 0; i < packages.length; i++) {
			this.classes.addAll(ClasspathUtil.findClasses(packages[i]));
			this.patterns[i] = packages[i] + ".*";
		}
	}

	@Override
	public String serialize(O object) {
		if (formatted) {
			return _xstream.get().toXML(object);
		} else {
			StringWriter writer = new StringWriter();
			_xstream.get().marshal(object, new CompactWriter(writer));
			return writer.toString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S deserialize(String xml, Class<S> clazz) {
		return (S)_xstream.get().fromXML(xml, ObjectUtil.newInstance(clazz));
	}

	public XStreamStringXMLSerializer<O> alias(String alias, Class<?> clazz) {
		aliases.put(alias, clazz);
		return this;
	}

	public XStreamStringXMLSerializer<O> omit(Class<?> clazz, String... omit) {
		omits.put(clazz, omit);
		return this;
	}

	public <E> XStreamStringXMLSerializer<O> enums(Class<E> clazz, Function<E, String> keyMapper) {
		enums.add(new XStreamEnumConverter<>(clazz, keyMapper));
		return this;
	}
}

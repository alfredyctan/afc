package org.afc.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.afc.AFCException;
import org.afc.util.ObjectUtil;
import org.afc.util.StringUtil;

import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class JAXBStringXMLSerializer<J> implements XMLSerializer<J, String> {

	private static final char DELIMITER = ':';

    private static final String JAXB_ENCODING = Marshaller.JAXB_ENCODING;
    		
    private static final String JAXB_FRAGMENT = Marshaller.JAXB_FRAGMENT;
    		
	private static final String JAXB_FORMATTED_OUTPUT = Marshaller.JAXB_FORMATTED_OUTPUT;

	private static final String JAXB_NAMESPACE_PREFIX_MAPPER = "com.sun.xml.bind.namespacePrefixMapper";
	
	private static final String JAXB_XMLDECLARATION = "com.sun.xml.bind.xmlDeclaration";

	private final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

	private final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
	
	private final ThreadLocal<Marshaller> _marshaller = new ThreadLocal<Marshaller>() {

		@Override
		protected Marshaller initialValue() {
			try {
				Marshaller marshaller = _jaxbContext.get().createMarshaller();
				marshaller.setProperty(JAXB_FORMATTED_OUTPUT, formatted);
				if (encoding != null) {
					marshaller.setProperty(JAXB_ENCODING, encoding);
				}
				marshaller.setProperty(JAXB_FRAGMENT, standalone);
				marshaller.setProperty(JAXB_XMLDECLARATION,  xmlDeclaration);
				if (prefixMapper != null) {
					marshaller.setProperty(JAXB_NAMESPACE_PREFIX_MAPPER, prefixMapper);
				}
				return marshaller;
			} catch (JAXBException e) {
				throw new AFCException("unable to create JAXBContext", e);
			}
		}
	};

	private final ThreadLocal<Unmarshaller> _unmarshaller = new ThreadLocal<Unmarshaller>() {
		@Override
		protected Unmarshaller initialValue() {
			try {
				Unmarshaller unmarshaller = _jaxbContext.get().createUnmarshaller();
				return unmarshaller;
			} catch (JAXBException e) {
				throw new AFCException("unable to create JAXBContext", e);
			}
		}
	};

	private final ThreadLocal<JAXBContext> _jaxbContext = new ThreadLocal<JAXBContext>() {
		@Override
		protected JAXBContext initialValue() {
			try {
				return JAXBContext.newInstance(contextPath);
			} catch (JAXBException e) {
				throw new AFCException("unable to create JAXBContext", e);
			}
		}
	};

	private String contextPath;

	private String namespaceURI;
	
	@Setter
	private boolean formatted;
	
	@Setter
	private boolean xmlDeclaration;

	@Setter
	private String encoding;

	@Setter
	private boolean standalone;
	
	@Setter
	private MappedNamespacePrefixMapper prefixMapper;
	
	@Setter
	private NamespaceContext nsContext;

	public JAXBStringXMLSerializer(String... contextPaths) {
		this.contextPath = StringUtil.createKey(DELIMITER, (Object[])contextPaths);
		this.xmlDeclaration = true;
		this.encoding = "UTF-8";
		this.standalone = true;
	}

	public JAXBStringXMLSerializer<J> setNamespaceURI(String namespaceURI) {
		this.namespaceURI = namespaceURI;
		this.xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
		return this;
	}
	
	@Override
	public String serialize(J jaxb) {
		try {
			Marshaller marshaller = _marshaller.get();
			StringWriter out = new StringWriter();
			if (nsContext != null) {
				XMLStreamWriter xmlWriter = xmlOutputFactory.createXMLStreamWriter(out);
				xmlWriter.setNamespaceContext(nsContext);
				marshaller.marshal(createRootElement(jaxb), xmlWriter);
			} else {
				marshaller.marshal(createRootElement(jaxb), out);
			}
			return out.toString();
		} catch (JAXBException | XMLStreamException | FactoryConfigurationError e) {
			throw new AFCException("failed to marshall", e);
		}
	}

	@Override
	public <S> S deserialize(String xml, Class<S> clazz) {
		try {
			Unmarshaller unmarshaller = _unmarshaller.get();
			if (namespaceURI != null) {
				XMLStreamReader xmlReader = xmlInputFactory.createXMLStreamReader(new StringReader(xml));
				DefaultNamespaceXMLReader reader = new DefaultNamespaceXMLReader(namespaceURI, xmlReader);
				return (clazz != null) ? 
					ObjectUtil.<JAXBElement<S>>cast(unmarshaller.unmarshal(reader, clazz)).getValue() :
					ObjectUtil.<JAXBElement<S>>cast(unmarshaller.unmarshal(reader)).getValue();
			} else {
				XMLStreamReader xmlReader = xmlInputFactory.createXMLStreamReader(new StringReader(xml));
				return (clazz != null) ? 
					ObjectUtil.<JAXBElement<S>>cast(unmarshaller.unmarshal(xmlReader, clazz)).getValue() :
					ObjectUtil.<JAXBElement<S>>cast(unmarshaller.unmarshal(xmlReader)).getValue();
			}
		} catch (JAXBException | XMLStreamException | FactoryConfigurationError e) {
			throw new AFCException("failed to marshall", e);
		}
	}

	private JAXBElement<J> createRootElement(J jaxbObject) {
		QName qName = new QName(namespaceURI, jaxbObject.getClass().getSimpleName());
		JAXBElement<J> rootElement = new JAXBElement<>(qName, ObjectUtil.cast(jaxbObject.getClass()), jaxbObject);
		return rootElement;
	}
}

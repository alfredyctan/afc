package org.afc.xml;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

public class DefaultNamespaceXMLReader extends StreamReaderDelegate {
	
	private String namespaceURI; 
	
	private int prefixIndex = -2;
	
	public DefaultNamespaceXMLReader(String namespaceURI, XMLStreamReader reader) {
		super(reader);
		this.namespaceURI = namespaceURI;
	}

	@Override
	public String getNamespaceURI() {
		return namespaceURI;
	}

	@Override
	public String getLocalName() {
		if (prefixIndex < 0) {
			prefixIndex = super.getLocalName().indexOf(':') + 1; 
		}
		return super.getLocalName().substring(prefixIndex);
	}
}

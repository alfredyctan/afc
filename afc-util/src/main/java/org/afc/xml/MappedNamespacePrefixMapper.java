package org.afc.xml;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class MappedNamespacePrefixMapper extends NamespacePrefixMapper {

	private Map<String, String> prefixes;
	
	public MappedNamespacePrefixMapper() {
		this.prefixes = new ConcurrentHashMap<>();
	}
	
	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
		return prefixes.get(namespaceUri);
	}
	
	public MappedNamespacePrefixMapper map(String namespaceUri, String prefix) {
		prefixes.put(namespaceUri, prefix);
		return this;
	}
}

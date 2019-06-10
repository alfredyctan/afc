package org.afc.xml;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.NamespaceContext;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class MappedNamespaceContext implements NamespaceContext {

	private Map<String, String> prefixes;

	public MappedNamespaceContext() {
		this.prefixes = new ConcurrentHashMap<>();
	}

	@Override
	public String getNamespaceURI(String prefix) {
		Optional<Entry<String, String>> find = prefixes.entrySet().stream().filter(
			e -> e.getValue().equals(prefix)
		).findFirst();
		if (find.isPresent()) {
			return find.get().getKey();
		} else {
			return null;
		}
	}

	@Override
	public String getPrefix(String namespaceURI) {
		return prefixes.get(namespaceURI);
	}

	@Override
	public Iterator<String> getPrefixes(String namespaceURI) {
		return prefixes.entrySet().stream().filter(e -> e.getKey().equals(namespaceURI)).map(e -> e.getValue()).iterator();
	}

	public MappedNamespaceContext map(String namespaceUri, String prefix) {
		prefixes.put(namespaceUri, prefix);
		return this;
	}
}

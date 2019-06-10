package org.afc.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import org.afc.AFCException;
import org.afc.util.FileUtil;

public class DefaultXSDValidator implements XSDValidator {

	private String[] xsdPaths;
	
	private SchemaFactory factory;
	
	private Schema schema;
	
	private Validator validator;

	public DefaultXSDValidator(String... xsdPaths) {
		try {
			this.xsdPaths = xsdPaths;
			this.factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			String[] basePaths = Arrays.stream(xsdPaths).map(xsdPath -> {
				int index = Math.max(xsdPath.lastIndexOf('/'), xsdPath.lastIndexOf('\\'));
				return index == -1 ? "" : xsdPath.substring(0, index + 1);
			}).collect(Collectors.toList()).toArray(new String[0]);
			this.factory.setResourceResolver(new XSDResourceResolver(basePaths));

			Source[] sources = Arrays.stream(xsdPaths)
			.map(p -> new StreamSource(FileUtil.resolveResourceAsStream(p)))
			.collect(Collectors.toList()).toArray(new Source[xsdPaths.length]);
			this.schema = factory.newSchema(sources);
			
			this.validator = schema.newValidator();
		} catch (SAXException e) {
			throw new AFCException(e.getMessage());
		}
	}

	@Override
	public void validate(String xml) {
		try {
			validator.validate(new StreamSource(new StringReader(xml)));
		} catch (SAXException | IOException e) {
			throw new AFCException(e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		return Arrays.toString(xsdPaths);
	}
}

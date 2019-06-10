package org.afc.xml;

import javax.xml.bind.annotation.XmlSchema;

import org.afc.util.ObjectUtil;

public class JAXBUtil {

	public static String namespace(Class<?> objectFactory) {
		return ObjectUtil.searchAnnotation(objectFactory, XmlSchema.class)[0].namespace();
	}
}

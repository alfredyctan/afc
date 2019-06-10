package org.afc.xml;

import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class XSDInput implements LSInput {

	protected String publicId = null;
	
	protected String systemId = null;
	
	protected String baseURI = null;

	protected InputStream byteStream = null;
	
	protected Reader characterStream = null;
	
	protected String stringData = null;

	protected String encoding = null;

	protected boolean certifiedText = false;

	@Override
	public boolean getCertifiedText() {
		return certifiedText;
	}
}
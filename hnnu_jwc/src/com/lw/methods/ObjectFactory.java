package com.lw.methods;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.lw.methods package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _RequestRESResponse_QNAME = new QName(
			"http://methods.lw.com/", "requestRESResponse");
	private final static QName _RequestRES_QNAME = new QName(
			"http://methods.lw.com/", "requestRES");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.lw.methods
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link RequestRESResponse }
	 * 
	 */
	public RequestRESResponse createRequestRESResponse() {
		return new RequestRESResponse();
	}

	/**
	 * Create an instance of {@link RequestRES }
	 * 
	 */
	public RequestRES createRequestRES() {
		return new RequestRES();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RequestRESResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://methods.lw.com/", name = "requestRESResponse")
	public JAXBElement<RequestRESResponse> createRequestRESResponse(
			RequestRESResponse value) {
		return new JAXBElement<RequestRESResponse>(_RequestRESResponse_QNAME,
				RequestRESResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link RequestRES }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://methods.lw.com/", name = "requestRES")
	public JAXBElement<RequestRES> createRequestRES(RequestRES value) {
		return new JAXBElement<RequestRES>(_RequestRES_QNAME, RequestRES.class,
				null, value);
	}

}

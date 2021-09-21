
package com.spts.lms.studentms.sap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Return" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="TMessage" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZmessageLogTt"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "_return",
    "tMessage"
})
@XmlRootElement(name = "ZchangeStMobileEmailResponse")
public class ZchangeStMobileEmailResponse {

    @XmlElement(name = "Return", required = true)
    protected String _return;
    @XmlElement(name = "TMessage", required = true)
    protected ZmessageLogTt tMessage;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturn(String value) {
        this._return = value;
    }

    /**
     * Gets the value of the tMessage property.
     * 
     * @return
     *     possible object is
     *     {@link ZmessageLogTt }
     *     
     */
    public ZmessageLogTt getTMessage() {
        return tMessage;
    }

    /**
     * Sets the value of the tMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZmessageLogTt }
     *     
     */
    public void setTMessage(ZmessageLogTt value) {
        this.tMessage = value;
    }

}

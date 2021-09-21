
package com.spts.lms.sap.attendance;

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
 *         &lt;element name="ItAttSt" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZattStPortalTt"/>
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
    "itAttSt"
})
@XmlRootElement(name = "ZattStPortalUpdate")
public class ZattStPortalUpdate {

    @XmlElement(name = "ItAttSt", required = true)
    protected ZattStPortalTt itAttSt;

    /**
     * Gets the value of the itAttSt property.
     * 
     * @return
     *     possible object is
     *     {@link ZattStPortalTt }
     *     
     */
    public ZattStPortalTt getItAttSt() {
        return itAttSt;
    }

    /**
     * Sets the value of the itAttSt property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZattStPortalTt }
     *     
     */
    public void setItAttSt(ZattStPortalTt value) {
        this.itAttSt = value;
    }

}

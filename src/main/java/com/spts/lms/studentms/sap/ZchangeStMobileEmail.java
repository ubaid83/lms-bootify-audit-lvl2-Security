
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
 *         &lt;element name="EmailId" type="{urn:sap-com:document:sap:rfc:functions}char241" minOccurs="0"/>
 *         &lt;element name="MobileNo" type="{urn:sap-com:document:sap:rfc:functions}char30" minOccurs="0"/>
 *         &lt;element name="Photo" type="{urn:sap-com:document:sap:soap:functions:mc-style}string" minOccurs="0"/>
 *         &lt;element name="Stnum" type="{urn:sap-com:document:sap:soap:functions:mc-style}char12"/>
 *         &lt;element name="Stobjid" type="{urn:sap-com:document:sap:soap:functions:mc-style}char100"/>
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
    "emailId",
    "mobileNo",
    "photo",
    "stnum",
    "stobjid"
})
@XmlRootElement(name = "ZchangeStMobileEmail")
public class ZchangeStMobileEmail {

    @XmlElement(name = "EmailId")
    protected String emailId;
    @XmlElement(name = "MobileNo")
    protected String mobileNo;
    @XmlElement(name = "Photo")
    protected String photo;
    @XmlElement(name = "Stnum", required = true)
    protected String stnum;
    @XmlElement(name = "Stobjid", required = true)
    protected String stobjid;

    /**
     * Gets the value of the emailId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * Sets the value of the emailId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailId(String value) {
        this.emailId = value;
    }

    /**
     * Gets the value of the mobileNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the value of the mobileNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileNo(String value) {
        this.mobileNo = value;
    }

    /**
     * Gets the value of the photo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets the value of the photo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhoto(String value) {
        this.photo = value;
    }

    /**
     * Gets the value of the stnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStnum() {
        return stnum;
    }

    /**
     * Sets the value of the stnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStnum(String value) {
        this.stnum = value;
    }

    /**
     * Gets the value of the stobjid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStobjid() {
        return stobjid;
    }

    /**
     * Sets the value of the stobjid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStobjid(String value) {
        this.stobjid = value;
    }

}

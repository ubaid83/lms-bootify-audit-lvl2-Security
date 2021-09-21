
package com.spts.lms.sap.facultyWorkload;

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
 *         &lt;element name="IvDate" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="IvEventid" type="{urn:sap-com:document:sap:soap:functions:mc-style}numeric8"/>
 *         &lt;element name="IvFacultyid" type="{urn:sap-com:document:sap:soap:functions:mc-style}numeric8"/>
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
    "ivDate",
    "ivEventid",
    "ivFacultyid"
})
@XmlRootElement(name = "ZfacultyWorkload")
public class ZfacultyWorkload {

    @XmlElement(name = "IvDate", required = true)
    protected String ivDate;
    @XmlElement(name = "IvEventid", required = true)
    protected String ivEventid;
    @XmlElement(name = "IvFacultyid", required = true)
    protected String ivFacultyid;

    /**
     * Gets the value of the ivDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIvDate() {
        return ivDate;
    }

    /**
     * Sets the value of the ivDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIvDate(String value) {
        this.ivDate = value;
    }

    /**
     * Gets the value of the ivEventid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIvEventid() {
        return ivEventid;
    }

    /**
     * Sets the value of the ivEventid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIvEventid(String value) {
        this.ivEventid = value;
    }

    /**
     * Gets the value of the ivFacultyid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIvFacultyid() {
        return ivFacultyid;
    }

    /**
     * Sets the value of the ivFacultyid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIvFacultyid(String value) {
        this.ivFacultyid = value;
    }

}

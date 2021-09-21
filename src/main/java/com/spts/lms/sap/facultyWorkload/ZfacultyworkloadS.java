
package com.spts.lms.sap.facultyWorkload;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ZfacultyworkloadS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZfacultyworkloadS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EDate" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="EObjid" type="{urn:sap-com:document:sap:rfc:functions}numeric8"/>
 *         &lt;element name="PhObjid" type="{urn:sap-com:document:sap:rfc:functions}numeric8"/>
 *         &lt;element name="AllottedLecture" type="{urn:sap-com:document:sap:rfc:functions}decimal5.2"/>
 *         &lt;element name="ConductedLecture" type="{urn:sap-com:document:sap:rfc:functions}decimal5.2"/>
 *         &lt;element name="LastUpdateDate" type="{urn:sap-com:document:sap:rfc:functions}date10"/>
 *         &lt;element name="LastUpdateTime" type="{urn:sap-com:document:sap:rfc:functions}time"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZfacultyworkloadS", propOrder = {
    "eDate",
    "eObjid",
    "phObjid",
    "allottedLecture",
    "conductedLecture",
    "lastUpdateDate",
    "lastUpdateTime"
})
public class ZfacultyworkloadS {

    @XmlElement(name = "EDate", required = true)
    protected String eDate;
    @XmlElement(name = "EObjid", required = true)
    protected String eObjid;
    @XmlElement(name = "PhObjid", required = true)
    protected String phObjid;
    @XmlElement(name = "AllottedLecture", required = true)
    protected BigDecimal allottedLecture;
    @XmlElement(name = "ConductedLecture", required = true)
    protected BigDecimal conductedLecture;
    @XmlElement(name = "LastUpdateDate", required = true)
    protected String lastUpdateDate;
    @XmlElement(name = "LastUpdateTime", required = true)
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar lastUpdateTime;

    /**
     * Gets the value of the eDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEDate() {
        return eDate;
    }

    /**
     * Sets the value of the eDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEDate(String value) {
        this.eDate = value;
    }

    /**
     * Gets the value of the eObjid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEObjid() {
        return eObjid;
    }

    /**
     * Sets the value of the eObjid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEObjid(String value) {
        this.eObjid = value;
    }

    /**
     * Gets the value of the phObjid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhObjid() {
        return phObjid;
    }

    /**
     * Sets the value of the phObjid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhObjid(String value) {
        this.phObjid = value;
    }

    /**
     * Gets the value of the allottedLecture property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAllottedLecture() {
        return allottedLecture;
    }

    /**
     * Sets the value of the allottedLecture property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAllottedLecture(BigDecimal value) {
        this.allottedLecture = value;
    }

    /**
     * Gets the value of the conductedLecture property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getConductedLecture() {
        return conductedLecture;
    }

    /**
     * Sets the value of the conductedLecture property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setConductedLecture(BigDecimal value) {
        this.conductedLecture = value;
    }

    /**
     * Gets the value of the lastUpdateDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * Sets the value of the lastUpdateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastUpdateDate(String value) {
        this.lastUpdateDate = value;
    }

    /**
     * Gets the value of the lastUpdateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * Sets the value of the lastUpdateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastUpdateTime(XMLGregorianCalendar value) {
        this.lastUpdateTime = value;
    }

}

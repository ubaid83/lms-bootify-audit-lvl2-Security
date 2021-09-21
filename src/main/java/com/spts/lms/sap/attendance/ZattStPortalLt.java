
package com.spts.lms.sap.attendance;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ZattStPortalLt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZattStPortalLt">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Mandt" type="{urn:sap-com:document:sap:soap:functions:mc-style}clnt3"/>
 *         &lt;element name="Orgunit" type="{urn:sap-com:document:sap:soap:functions:mc-style}char12"/>
 *         &lt;element name="ProgId" type="{urn:sap-com:document:sap:soap:functions:mc-style}char30"/>
 *         &lt;element name="Ayear" type="{urn:sap-com:document:sap:soap:functions:mc-style}numeric4"/>
 *         &lt;element name="Asession" type="{urn:sap-com:document:sap:soap:functions:mc-style}char20"/>
 *         &lt;element name="EventId" type="{urn:sap-com:document:sap:soap:functions:mc-style}numeric8"/>
 *         &lt;element name="EventDate" type="{urn:sap-com:document:sap:soap:functions:mc-style}date10"/>
 *         &lt;element name="StartTime" type="{urn:sap-com:document:sap:soap:functions:mc-style}time"/>
 *         &lt;element name="EndTime" type="{urn:sap-com:document:sap:soap:functions:mc-style}time"/>
 *         &lt;element name="FacultyId" type="{urn:sap-com:document:sap:soap:functions:mc-style}char60"/>
 *         &lt;element name="StObjid" type="{urn:sap-com:document:sap:soap:functions:mc-style}numeric8"/>
 *         &lt;element name="LogDate" type="{urn:sap-com:document:sap:soap:functions:mc-style}date10"/>
 *         &lt;element name="LogTime" type="{urn:sap-com:document:sap:soap:functions:mc-style}time"/>
 *         &lt;element name="StNumber" type="{urn:sap-com:document:sap:soap:functions:mc-style}char12"/>
 *         &lt;element name="Flag" type="{urn:sap-com:document:sap:soap:functions:mc-style}char1"/>
 *         &lt;element name="UpdtFacFlag" type="{urn:sap-com:document:sap:soap:functions:mc-style}char1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZattStPortalLt", propOrder = {
    "mandt",
    "orgunit",
    "progId",
    "ayear",
    "asession",
    "eventId",
    "eventDate",
    "startTime",
    "endTime",
    "facultyId",
    "stObjid",
    "logDate",
    "logTime",
    "stNumber",
    "flag",
    "updtFacFlag"
})
public class ZattStPortalLt {

    @XmlElement(name = "Mandt", required = true)
    protected String mandt;
    @XmlElement(name = "Orgunit", required = true)
    protected String orgunit;
    @XmlElement(name = "ProgId", required = true)
    protected String progId;
    @XmlElement(name = "Ayear", required = true)
    protected String ayear;
    @XmlElement(name = "Asession", required = true)
    protected String asession;
    @XmlElement(name = "EventId", required = true)
    protected String eventId;
    @XmlElement(name = "EventDate", required = true)
    protected String eventDate;
    @XmlElement(name = "StartTime", required = true)
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar startTime;
    @XmlElement(name = "EndTime", required = true)
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar endTime;
    @XmlElement(name = "FacultyId", required = true)
    protected String facultyId;
    @XmlElement(name = "StObjid", required = true)
    protected String stObjid;
    @XmlElement(name = "LogDate", required = true)
    protected String logDate;
    @XmlElement(name = "LogTime", required = true)
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar logTime;
    @XmlElement(name = "StNumber", required = true)
    protected String stNumber;
    @XmlElement(name = "Flag", required = true)
    protected String flag;
    @XmlElement(name = "UpdtFacFlag", required = true)
    protected String updtFacFlag;

    /**
     * Gets the value of the mandt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMandt() {
        return mandt;
    }

    /**
     * Sets the value of the mandt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMandt(String value) {
        this.mandt = value;
    }

    /**
     * Gets the value of the orgunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgunit() {
        return orgunit;
    }

    /**
     * Sets the value of the orgunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgunit(String value) {
        this.orgunit = value;
    }

    /**
     * Gets the value of the progId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProgId() {
        return progId;
    }

    /**
     * Sets the value of the progId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProgId(String value) {
        this.progId = value;
    }

    /**
     * Gets the value of the ayear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAyear() {
        return ayear;
    }

    /**
     * Sets the value of the ayear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAyear(String value) {
        this.ayear = value;
    }

    /**
     * Gets the value of the asession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsession() {
        return asession;
    }

    /**
     * Sets the value of the asession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsession(String value) {
        this.asession = value;
    }

    /**
     * Gets the value of the eventId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Sets the value of the eventId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventId(String value) {
        this.eventId = value;
    }

    /**
     * Gets the value of the eventDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * Sets the value of the eventDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventDate(String value) {
        this.eventDate = value;
    }

    /**
     * Gets the value of the startTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartTime(XMLGregorianCalendar value) {
        this.startTime = value;
    }

    /**
     * Gets the value of the endTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndTime() {
        return endTime;
    }

    /**
     * Sets the value of the endTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndTime(XMLGregorianCalendar value) {
        this.endTime = value;
    }

    /**
     * Gets the value of the facultyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFacultyId() {
        return facultyId;
    }

    /**
     * Sets the value of the facultyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFacultyId(String value) {
        this.facultyId = value;
    }

    /**
     * Gets the value of the stObjid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStObjid() {
        return stObjid;
    }

    /**
     * Sets the value of the stObjid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStObjid(String value) {
        this.stObjid = value;
    }

    /**
     * Gets the value of the logDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogDate() {
        return logDate;
    }

    /**
     * Sets the value of the logDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogDate(String value) {
        this.logDate = value;
    }

    /**
     * Gets the value of the logTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLogTime() {
        return logTime;
    }

    /**
     * Sets the value of the logTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLogTime(XMLGregorianCalendar value) {
        this.logTime = value;
    }

    /**
     * Gets the value of the stNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStNumber() {
        return stNumber;
    }

    /**
     * Sets the value of the stNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStNumber(String value) {
        this.stNumber = value;
    }

    /**
     * Gets the value of the flag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlag() {
        return flag;
    }

    /**
     * Sets the value of the flag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlag(String value) {
        this.flag = value;
    }

    /**
     * Gets the value of the updtFacFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdtFacFlag() {
        return updtFacFlag;
    }

    /**
     * Sets the value of the updtFacFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdtFacFlag(String value) {
        this.updtFacFlag = value;
    }

}

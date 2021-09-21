
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
 *         &lt;element name="EtFacultyworkload" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZfacultyworkloadTt"/>
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
    "etFacultyworkload"
})
@XmlRootElement(name = "ZfacultyWorkloadResponse")
public class ZfacultyWorkloadResponse {

    @XmlElement(name = "EtFacultyworkload", required = true)
    protected ZfacultyworkloadTt etFacultyworkload;

    /**
     * Gets the value of the etFacultyworkload property.
     * 
     * @return
     *     possible object is
     *     {@link ZfacultyworkloadTt }
     *     
     */
    public ZfacultyworkloadTt getEtFacultyworkload() {
        return etFacultyworkload;
    }

    /**
     * Sets the value of the etFacultyworkload property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZfacultyworkloadTt }
     *     
     */
    public void setEtFacultyworkload(ZfacultyworkloadTt value) {
        this.etFacultyworkload = value;
    }

}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.14 at 07:35:12 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for docType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="docType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="diskDoc" type="{}diskDocType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "docType", propOrder = {
    "diskDoc"
})
public class DocType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected DiskDocType diskDoc;

    /**
     * Gets the value of the diskDoc property.
     * 
     * @return
     *     possible object is
     *     {@link DiskDocType }
     *     
     */
    public DiskDocType getDiskDoc() {
        return diskDoc;
    }

    /**
     * Sets the value of the diskDoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiskDocType }
     *     
     */
    public void setDiskDoc(DiskDocType value) {
        this.diskDoc = value;
    }

}

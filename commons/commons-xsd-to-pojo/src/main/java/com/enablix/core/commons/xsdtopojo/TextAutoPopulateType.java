//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.09 at 03:09:46 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for textAutoPopulateType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="textAutoPopulateType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="refContentItem" type="{}refContentItemType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "textAutoPopulateType", propOrder = {
    "refContentItem"
})
public class TextAutoPopulateType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected RefContentItemType refContentItem;

    /**
     * Gets the value of the refContentItem property.
     * 
     * @return
     *     possible object is
     *     {@link RefContentItemType }
     *     
     */
    public RefContentItemType getRefContentItem() {
        return refContentItem;
    }

    /**
     * Sets the value of the refContentItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link RefContentItemType }
     *     
     */
    public void setRefContentItem(RefContentItemType value) {
        this.refContentItem = value;
    }

}
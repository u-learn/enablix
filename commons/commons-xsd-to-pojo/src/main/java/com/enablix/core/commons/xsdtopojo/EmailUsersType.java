//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.17 at 02:51:36 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for emailUsersType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="emailUsersType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="filterTags" type="{}filterTagsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "emailUsersType", propOrder = {
    "filterTags"
})
public class EmailUsersType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected FilterTagsType filterTags;

    /**
     * Gets the value of the filterTags property.
     * 
     * @return
     *     possible object is
     *     {@link FilterTagsType }
     *     
     */
    public FilterTagsType getFilterTags() {
        return filterTags;
    }

    /**
     * Sets the value of the filterTags property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterTagsType }
     *     
     */
    public void setFilterTags(FilterTagsType value) {
        this.filterTags = value;
    }

}

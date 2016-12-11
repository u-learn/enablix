//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.06 at 07:02:49 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for entityContentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="entityContentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contentTypes" type="{}candidateContainersType" minOccurs="0"/>
 *         &lt;element name="filterTags" type="{}filterTagsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "entityContentType", propOrder = {
    "contentTypes",
    "filterTags"
})
public class EntityContentType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected CandidateContainersType contentTypes;
    protected FilterTagsType filterTags;

    /**
     * Gets the value of the contentTypes property.
     * 
     * @return
     *     possible object is
     *     {@link CandidateContainersType }
     *     
     */
    public CandidateContainersType getContentTypes() {
        return contentTypes;
    }

    /**
     * Sets the value of the contentTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link CandidateContainersType }
     *     
     */
    public void setContentTypes(CandidateContainersType value) {
        this.contentTypes = value;
    }

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

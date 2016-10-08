//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.10.08 at 05:24:49 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pathItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pathItemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="filterCriteria" type="{}filterCriteriaType"/>
 *         &lt;choice>
 *           &lt;element name="relatedUser" type="{}relatedUserType"/>
 *           &lt;element name="pathItem" type="{}pathItemType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="qualifiedId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pathItemType", propOrder = {
    "filterCriteria",
    "relatedUser",
    "pathItem"
})
public class PathItemType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected FilterCriteriaType filterCriteria;
    protected RelatedUserType relatedUser;
    protected PathItemType pathItem;
    @XmlAttribute(name = "qualifiedId")
    protected String qualifiedId;

    /**
     * Gets the value of the filterCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link FilterCriteriaType }
     *     
     */
    public FilterCriteriaType getFilterCriteria() {
        return filterCriteria;
    }

    /**
     * Sets the value of the filterCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterCriteriaType }
     *     
     */
    public void setFilterCriteria(FilterCriteriaType value) {
        this.filterCriteria = value;
    }

    /**
     * Gets the value of the relatedUser property.
     * 
     * @return
     *     possible object is
     *     {@link RelatedUserType }
     *     
     */
    public RelatedUserType getRelatedUser() {
        return relatedUser;
    }

    /**
     * Sets the value of the relatedUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link RelatedUserType }
     *     
     */
    public void setRelatedUser(RelatedUserType value) {
        this.relatedUser = value;
    }

    /**
     * Gets the value of the pathItem property.
     * 
     * @return
     *     possible object is
     *     {@link PathItemType }
     *     
     */
    public PathItemType getPathItem() {
        return pathItem;
    }

    /**
     * Sets the value of the pathItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link PathItemType }
     *     
     */
    public void setPathItem(PathItemType value) {
        this.pathItem = value;
    }

    /**
     * Gets the value of the qualifiedId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQualifiedId() {
        return qualifiedId;
    }

    /**
     * Sets the value of the qualifiedId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQualifiedId(String value) {
        this.qualifiedId = value;
    }

}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.20 at 02:25:58 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for relatedItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="relatedItemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="filterCriteria" type="{}filterCriteriaType" minOccurs="0"/>
 *         &lt;element name="matchCriteria" type="{}matchCriteriaType" minOccurs="0"/>
 *         &lt;element name="recordAsRelated" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="tags" type="{}tagsType" minOccurs="0"/>
 *         &lt;element name="relatedItem" type="{}relatedItemType" minOccurs="0"/>
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
@XmlType(name = "relatedItemType", propOrder = {
    "filterCriteria",
    "matchCriteria",
    "recordAsRelated",
    "tags",
    "relatedItem"
})
public class RelatedItemType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected FilterCriteriaType filterCriteria;
    protected MatchCriteriaType matchCriteria;
    @XmlElement(defaultValue = "true")
    protected boolean recordAsRelated;
    protected TagsType tags;
    protected RelatedItemType relatedItem;
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
     * Gets the value of the matchCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link MatchCriteriaType }
     *     
     */
    public MatchCriteriaType getMatchCriteria() {
        return matchCriteria;
    }

    /**
     * Sets the value of the matchCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link MatchCriteriaType }
     *     
     */
    public void setMatchCriteria(MatchCriteriaType value) {
        this.matchCriteria = value;
    }

    /**
     * Gets the value of the recordAsRelated property.
     * 
     */
    public boolean isRecordAsRelated() {
        return recordAsRelated;
    }

    /**
     * Sets the value of the recordAsRelated property.
     * 
     */
    public void setRecordAsRelated(boolean value) {
        this.recordAsRelated = value;
    }

    /**
     * Gets the value of the tags property.
     * 
     * @return
     *     possible object is
     *     {@link TagsType }
     *     
     */
    public TagsType getTags() {
        return tags;
    }

    /**
     * Sets the value of the tags property.
     * 
     * @param value
     *     allowed object is
     *     {@link TagsType }
     *     
     */
    public void setTags(TagsType value) {
        this.tags = value;
    }

    /**
     * Gets the value of the relatedItem property.
     * 
     * @return
     *     possible object is
     *     {@link RelatedItemType }
     *     
     */
    public RelatedItemType getRelatedItem() {
        return relatedItem;
    }

    /**
     * Sets the value of the relatedItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link RelatedItemType }
     *     
     */
    public void setRelatedItem(RelatedItemType value) {
        this.relatedItem = value;
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
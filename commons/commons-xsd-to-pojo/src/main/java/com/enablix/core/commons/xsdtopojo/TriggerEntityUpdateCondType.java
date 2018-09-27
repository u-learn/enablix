//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.09.27 at 02:36:10 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for triggerEntityUpdateCondType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="triggerEntityUpdateCondType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attributeUpdate" type="{}attributeUpdateType" minOccurs="0"/>
 *         &lt;element name="entityMatch" type="{}filterCriteriaType" minOccurs="0"/>
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
@XmlType(name = "triggerEntityUpdateCondType", propOrder = {
    "attributeUpdate",
    "entityMatch"
})
public class TriggerEntityUpdateCondType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected AttributeUpdateType attributeUpdate;
    protected FilterCriteriaType entityMatch;
    @XmlAttribute(name = "qualifiedId")
    protected String qualifiedId;

    /**
     * Gets the value of the attributeUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeUpdateType }
     *     
     */
    public AttributeUpdateType getAttributeUpdate() {
        return attributeUpdate;
    }

    /**
     * Sets the value of the attributeUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeUpdateType }
     *     
     */
    public void setAttributeUpdate(AttributeUpdateType value) {
        this.attributeUpdate = value;
    }

    /**
     * Gets the value of the entityMatch property.
     * 
     * @return
     *     possible object is
     *     {@link FilterCriteriaType }
     *     
     */
    public FilterCriteriaType getEntityMatch() {
        return entityMatch;
    }

    /**
     * Sets the value of the entityMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterCriteriaType }
     *     
     */
    public void setEntityMatch(FilterCriteriaType value) {
        this.entityMatch = value;
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

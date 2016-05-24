//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.24 at 10:08:55 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for itemCorrelationRuleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="itemCorrelationRuleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="triggerItem" type="{}triggerItemType"/>
 *         &lt;element name="relatedItems" type="{}relatedItemsType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemCorrelationRuleType", propOrder = {
    "triggerItem",
    "relatedItems"
})
public class ItemCorrelationRuleType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected TriggerItemType triggerItem;
    @XmlElement(required = true)
    protected RelatedItemsType relatedItems;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the triggerItem property.
     * 
     * @return
     *     possible object is
     *     {@link TriggerItemType }
     *     
     */
    public TriggerItemType getTriggerItem() {
        return triggerItem;
    }

    /**
     * Sets the value of the triggerItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link TriggerItemType }
     *     
     */
    public void setTriggerItem(TriggerItemType value) {
        this.triggerItem = value;
    }

    /**
     * Gets the value of the relatedItems property.
     * 
     * @return
     *     possible object is
     *     {@link RelatedItemsType }
     *     
     */
    public RelatedItemsType getRelatedItems() {
        return relatedItems;
    }

    /**
     * Sets the value of the relatedItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link RelatedItemsType }
     *     
     */
    public void setRelatedItems(RelatedItemsType value) {
        this.relatedItems = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}

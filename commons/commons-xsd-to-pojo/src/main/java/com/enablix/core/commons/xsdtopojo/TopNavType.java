//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.15 at 04:59:14 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for topNavType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="topNavType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="itemContainers" type="{}itemContainersType"/>
 *         &lt;element name="enclosures" type="{}portalTopNavEnclosuresType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "topNavType", propOrder = {
    "itemContainers",
    "enclosures"
})
public class TopNavType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected ItemContainersType itemContainers;
    protected PortalTopNavEnclosuresType enclosures;

    /**
     * Gets the value of the itemContainers property.
     * 
     * @return
     *     possible object is
     *     {@link ItemContainersType }
     *     
     */
    public ItemContainersType getItemContainers() {
        return itemContainers;
    }

    /**
     * Sets the value of the itemContainers property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemContainersType }
     *     
     */
    public void setItemContainers(ItemContainersType value) {
        this.itemContainers = value;
    }

    /**
     * Gets the value of the enclosures property.
     * 
     * @return
     *     possible object is
     *     {@link PortalTopNavEnclosuresType }
     *     
     */
    public PortalTopNavEnclosuresType getEnclosures() {
        return enclosures;
    }

    /**
     * Sets the value of the enclosures property.
     * 
     * @param value
     *     allowed object is
     *     {@link PortalTopNavEnclosuresType }
     *     
     */
    public void setEnclosures(PortalTopNavEnclosuresType value) {
        this.enclosures = value;
    }

}

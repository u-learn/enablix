//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.09 at 10:35:31 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="dataDefinition" type="{}dataDefinitionType"/>
 *         &lt;element name="uiDefinition" type="{}uiDefinitionType"/>
 *         &lt;element name="portalUIDefinition" type="{}portalUIDefType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dataDefinition",
    "uiDefinition",
    "portalUIDefinition"
})
@XmlRootElement(name = "contentTemplate")
public class ContentTemplate
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected DataDefinitionType dataDefinition;
    @XmlElement(required = true)
    protected UiDefinitionType uiDefinition;
    protected PortalUIDefType portalUIDefinition;
    @XmlAttribute(name = "id", required = true)
    protected String id;

    /**
     * Gets the value of the dataDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link DataDefinitionType }
     *     
     */
    public DataDefinitionType getDataDefinition() {
        return dataDefinition;
    }

    /**
     * Sets the value of the dataDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataDefinitionType }
     *     
     */
    public void setDataDefinition(DataDefinitionType value) {
        this.dataDefinition = value;
    }

    /**
     * Gets the value of the uiDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link UiDefinitionType }
     *     
     */
    public UiDefinitionType getUiDefinition() {
        return uiDefinition;
    }

    /**
     * Sets the value of the uiDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link UiDefinitionType }
     *     
     */
    public void setUiDefinition(UiDefinitionType value) {
        this.uiDefinition = value;
    }

    /**
     * Gets the value of the portalUIDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link PortalUIDefType }
     *     
     */
    public PortalUIDefType getPortalUIDefinition() {
        return portalUIDefinition;
    }

    /**
     * Sets the value of the portalUIDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link PortalUIDefType }
     *     
     */
    public void setPortalUIDefinition(PortalUIDefType value) {
        this.portalUIDefinition = value;
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

}

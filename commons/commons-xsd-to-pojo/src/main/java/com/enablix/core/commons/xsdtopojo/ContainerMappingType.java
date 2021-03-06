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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for containerMappingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="containerMappingType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="itemMapping" type="{}contentItemMappingType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="qualifiedId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "containerMappingType", propOrder = {
    "itemMapping"
})
public class ContainerMappingType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected ContentItemMappingType itemMapping;
    @XmlAttribute(name = "qualifiedId")
    protected String qualifiedId;
    @XmlAttribute(name = "id")
    protected String id;

    /**
     * Gets the value of the itemMapping property.
     * 
     * @return
     *     possible object is
     *     {@link ContentItemMappingType }
     *     
     */
    public ContentItemMappingType getItemMapping() {
        return itemMapping;
    }

    /**
     * Sets the value of the itemMapping property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentItemMappingType }
     *     
     */
    public void setItemMapping(ContentItemMappingType value) {
        this.itemMapping = value;
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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.10 at 03:12:36 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for textUIDefType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="textUIDefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="autoPopulate" type="{}textAutoPopulateType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" type="{}textUIClassType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "textUIDefType", propOrder = {
    "autoPopulate"
})
public class TextUIDefType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected TextAutoPopulateType autoPopulate;
    @XmlAttribute(name = "type")
    protected TextUIClassType type;

    /**
     * Gets the value of the autoPopulate property.
     * 
     * @return
     *     possible object is
     *     {@link TextAutoPopulateType }
     *     
     */
    public TextAutoPopulateType getAutoPopulate() {
        return autoPopulate;
    }

    /**
     * Sets the value of the autoPopulate property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextAutoPopulateType }
     *     
     */
    public void setAutoPopulate(TextAutoPopulateType value) {
        this.autoPopulate = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link TextUIClassType }
     *     
     */
    public TextUIClassType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextUIClassType }
     *     
     */
    public void setType(TextUIClassType value) {
        this.type = value;
    }

}

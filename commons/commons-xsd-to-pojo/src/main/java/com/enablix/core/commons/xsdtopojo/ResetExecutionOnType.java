//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.07.27 at 12:21:54 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resetExecutionOnType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resetExecutionOnType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="triggerEntityUpdate" type="{}triggerEntityUpdateCondType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resetExecutionOnType", propOrder = {
    "triggerEntityUpdate"
})
public class ResetExecutionOnType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected TriggerEntityUpdateCondType triggerEntityUpdate;

    /**
     * Gets the value of the triggerEntityUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link TriggerEntityUpdateCondType }
     *     
     */
    public TriggerEntityUpdateCondType getTriggerEntityUpdate() {
        return triggerEntityUpdate;
    }

    /**
     * Sets the value of the triggerEntityUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link TriggerEntityUpdateCondType }
     *     
     */
    public void setTriggerEntityUpdate(TriggerEntityUpdateCondType value) {
        this.triggerEntityUpdate = value;
    }

}

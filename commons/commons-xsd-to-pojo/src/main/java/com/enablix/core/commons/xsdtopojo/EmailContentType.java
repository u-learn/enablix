//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.21 at 11:26:28 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for emailContentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="emailContentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="triggerEntity" type="{}emailContentTriggerEntityType" minOccurs="0"/>
 *         &lt;element name="correlatedEntities" type="{}correlatedEntitiesType" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "emailContentType", propOrder = {

})
public class EmailContentType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected EmailContentTriggerEntityType triggerEntity;
    protected CorrelatedEntitiesType correlatedEntities;

    /**
     * Gets the value of the triggerEntity property.
     * 
     * @return
     *     possible object is
     *     {@link EmailContentTriggerEntityType }
     *     
     */
    public EmailContentTriggerEntityType getTriggerEntity() {
        return triggerEntity;
    }

    /**
     * Sets the value of the triggerEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailContentTriggerEntityType }
     *     
     */
    public void setTriggerEntity(EmailContentTriggerEntityType value) {
        this.triggerEntity = value;
    }

    /**
     * Gets the value of the correlatedEntities property.
     * 
     * @return
     *     possible object is
     *     {@link CorrelatedEntitiesType }
     *     
     */
    public CorrelatedEntitiesType getCorrelatedEntities() {
        return correlatedEntities;
    }

    /**
     * Sets the value of the correlatedEntities property.
     * 
     * @param value
     *     allowed object is
     *     {@link CorrelatedEntitiesType }
     *     
     */
    public void setCorrelatedEntities(CorrelatedEntitiesType value) {
        this.correlatedEntities = value;
    }

}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.29 at 06:17:46 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for emailRecepientType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="emailRecepientType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="correlatedUsers" type="{}emailCorrelatedUsersType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "emailRecepientType", propOrder = {
    "correlatedUsers"
})
public class EmailRecepientType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected EmailCorrelatedUsersType correlatedUsers;

    /**
     * Gets the value of the correlatedUsers property.
     * 
     * @return
     *     possible object is
     *     {@link EmailCorrelatedUsersType }
     *     
     */
    public EmailCorrelatedUsersType getCorrelatedUsers() {
        return correlatedUsers;
    }

    /**
     * Sets the value of the correlatedUsers property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailCorrelatedUsersType }
     *     
     */
    public void setCorrelatedUsers(EmailCorrelatedUsersType value) {
        this.correlatedUsers = value;
    }

}
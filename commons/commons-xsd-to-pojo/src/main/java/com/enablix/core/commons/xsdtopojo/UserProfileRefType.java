//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.22 at 02:21:49 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for userProfileRefType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="userProfileRefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="containerQId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="emailAttributeId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userProfileRefType", propOrder = {
    "containerQId",
    "emailAttributeId"
})
public class UserProfileRefType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String containerQId;
    @XmlElement(required = true)
    protected String emailAttributeId;

    /**
     * Gets the value of the containerQId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContainerQId() {
        return containerQId;
    }

    /**
     * Sets the value of the containerQId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContainerQId(String value) {
        this.containerQId = value;
    }

    /**
     * Gets the value of the emailAttributeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAttributeId() {
        return emailAttributeId;
    }

    /**
     * Sets the value of the emailAttributeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAttributeId(String value) {
        this.emailAttributeId = value;
    }

}

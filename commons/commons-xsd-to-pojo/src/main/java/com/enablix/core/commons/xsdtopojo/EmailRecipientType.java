//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.27 at 11:36:24 AM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for emailRecipientType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="emailRecipientType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="correlatedUsers" type="{}emailCorrelatedUsersType" minOccurs="0"/>
 *         &lt;element name="allUsers" type="{}emailAllUsersType" minOccurs="0"/>
 *         &lt;element name="userGroups" type="{}emailUserGroupsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "emailRecipientType", propOrder = {
    "correlatedUsers",
    "allUsers",
    "userGroups"
})
public class EmailRecipientType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected EmailCorrelatedUsersType correlatedUsers;
    protected EmailAllUsersType allUsers;
    protected EmailUserGroupsType userGroups;

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

    /**
     * Gets the value of the allUsers property.
     * 
     * @return
     *     possible object is
     *     {@link EmailAllUsersType }
     *     
     */
    public EmailAllUsersType getAllUsers() {
        return allUsers;
    }

    /**
     * Sets the value of the allUsers property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailAllUsersType }
     *     
     */
    public void setAllUsers(EmailAllUsersType value) {
        this.allUsers = value;
    }

    /**
     * Gets the value of the userGroups property.
     * 
     * @return
     *     possible object is
     *     {@link EmailUserGroupsType }
     *     
     */
    public EmailUserGroupsType getUserGroups() {
        return userGroups;
    }

    /**
     * Sets the value of the userGroups property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailUserGroupsType }
     *     
     */
    public void setUserGroups(EmailUserGroupsType value) {
        this.userGroups = value;
    }

}

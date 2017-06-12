//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.09 at 03:09:46 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for userGroupType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="userGroupType">
 *   &lt;complexContent>
 *     &lt;extension base="{}describedType">
 *       &lt;sequence>
 *         &lt;element name="userSet" type="{}userSetType" minOccurs="0"/>
 *         &lt;element name="referenceUserSet" type="{}referenceUserSetType" minOccurs="0"/>
 *         &lt;element name="filteredUserSet" type="{}filteredUserSetType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userGroupType", propOrder = {
    "userSet",
    "referenceUserSet",
    "filteredUserSet"
})
public class UserGroupType
    extends DescribedType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected UserSetType userSet;
    protected ReferenceUserSetType referenceUserSet;
    protected FilteredUserSetType filteredUserSet;

    /**
     * Gets the value of the userSet property.
     * 
     * @return
     *     possible object is
     *     {@link UserSetType }
     *     
     */
    public UserSetType getUserSet() {
        return userSet;
    }

    /**
     * Sets the value of the userSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserSetType }
     *     
     */
    public void setUserSet(UserSetType value) {
        this.userSet = value;
    }

    /**
     * Gets the value of the referenceUserSet property.
     * 
     * @return
     *     possible object is
     *     {@link ReferenceUserSetType }
     *     
     */
    public ReferenceUserSetType getReferenceUserSet() {
        return referenceUserSet;
    }

    /**
     * Sets the value of the referenceUserSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferenceUserSetType }
     *     
     */
    public void setReferenceUserSet(ReferenceUserSetType value) {
        this.referenceUserSet = value;
    }

    /**
     * Gets the value of the filteredUserSet property.
     * 
     * @return
     *     possible object is
     *     {@link FilteredUserSetType }
     *     
     */
    public FilteredUserSetType getFilteredUserSet() {
        return filteredUserSet;
    }

    /**
     * Sets the value of the filteredUserSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilteredUserSetType }
     *     
     */
    public void setFilteredUserSet(FilteredUserSetType value) {
        this.filteredUserSet = value;
    }

}

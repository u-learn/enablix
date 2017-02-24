//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.21 at 05:24:49 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dataDefinitionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dataDefinitionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="container" type="{}containerType" maxOccurs="unbounded"/>
 *         &lt;element name="userProfileRef" type="{}userProfileRefType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataDefinitionType", propOrder = {
    "container",
    "userProfileRef"
})
public class DataDefinitionType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected List<ContainerType> container;
    protected UserProfileRefType userProfileRef;

    /**
     * Gets the value of the container property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the container property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContainer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContainerType }
     * 
     * 
     */
    public List<ContainerType> getContainer() {
        if (container == null) {
            container = new ArrayList<ContainerType>();
        }
        return this.container;
    }

    /**
     * Gets the value of the userProfileRef property.
     * 
     * @return
     *     possible object is
     *     {@link UserProfileRefType }
     *     
     */
    public UserProfileRefType getUserProfileRef() {
        return userProfileRef;
    }

    /**
     * Sets the value of the userProfileRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserProfileRefType }
     *     
     */
    public void setUserProfileRef(UserProfileRefType value) {
        this.userProfileRef = value;
    }

}

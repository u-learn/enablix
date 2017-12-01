//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.12.01 at 06:08:59 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for containerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="containerType">
 *   &lt;complexContent>
 *     &lt;extension base="{}baseContainerType">
 *       &lt;sequence>
 *         &lt;element name="container" type="{}containerType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="referenceable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="refData" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="single" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="linkContainerQId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="linkContentItemId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="businessCategory" type="{}containerBusinessCategoryType" />
 *       &lt;attribute name="color" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "containerType", propOrder = {
    "container"
})
public class ContainerType
    extends BaseContainerType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected List<ContainerType> container;
    @XmlAttribute(name = "referenceable")
    protected Boolean referenceable;
    @XmlAttribute(name = "refData")
    protected Boolean refData;
    @XmlAttribute(name = "single")
    protected Boolean single;
    @XmlAttribute(name = "linkContainerQId")
    protected String linkContainerQId;
    @XmlAttribute(name = "linkContentItemId")
    protected String linkContentItemId;
    @XmlAttribute(name = "businessCategory")
    protected ContainerBusinessCategoryType businessCategory;
    @XmlAttribute(name = "color")
    protected String color;

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
     * Gets the value of the referenceable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isReferenceable() {
        if (referenceable == null) {
            return true;
        } else {
            return referenceable;
        }
    }

    /**
     * Sets the value of the referenceable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReferenceable(Boolean value) {
        this.referenceable = value;
    }

    /**
     * Gets the value of the refData property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isRefData() {
        if (refData == null) {
            return false;
        } else {
            return refData;
        }
    }

    /**
     * Sets the value of the refData property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRefData(Boolean value) {
        this.refData = value;
    }

    /**
     * Gets the value of the single property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSingle() {
        if (single == null) {
            return false;
        } else {
            return single;
        }
    }

    /**
     * Sets the value of the single property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSingle(Boolean value) {
        this.single = value;
    }

    /**
     * Gets the value of the linkContainerQId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkContainerQId() {
        return linkContainerQId;
    }

    /**
     * Sets the value of the linkContainerQId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkContainerQId(String value) {
        this.linkContainerQId = value;
    }

    /**
     * Gets the value of the linkContentItemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkContentItemId() {
        return linkContentItemId;
    }

    /**
     * Sets the value of the linkContentItemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkContentItemId(String value) {
        this.linkContentItemId = value;
    }

    /**
     * Gets the value of the businessCategory property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerBusinessCategoryType }
     *     
     */
    public ContainerBusinessCategoryType getBusinessCategory() {
        return businessCategory;
    }

    /**
     * Sets the value of the businessCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerBusinessCategoryType }
     *     
     */
    public void setBusinessCategory(ContainerBusinessCategoryType value) {
        this.businessCategory = value;
    }

    /**
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColor(String value) {
        this.color = value;
    }

}

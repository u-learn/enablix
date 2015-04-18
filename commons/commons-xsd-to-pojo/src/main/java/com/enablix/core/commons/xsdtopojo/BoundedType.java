//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.18 at 06:49:09 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for boundedType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="boundedType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="fixedList" type="{}boundedFixedListType"/>
 *         &lt;element name="refList" type="{}boundedRefListType"/>
 *       &lt;/choice>
 *       &lt;attribute name="multivalued" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "boundedType", propOrder = {
    "fixedList",
    "refList"
})
public class BoundedType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected BoundedFixedListType fixedList;
    protected BoundedRefListType refList;
    @XmlAttribute(name = "multivalued")
    protected Boolean multivalued;

    /**
     * Gets the value of the fixedList property.
     * 
     * @return
     *     possible object is
     *     {@link BoundedFixedListType }
     *     
     */
    public BoundedFixedListType getFixedList() {
        return fixedList;
    }

    /**
     * Sets the value of the fixedList property.
     * 
     * @param value
     *     allowed object is
     *     {@link BoundedFixedListType }
     *     
     */
    public void setFixedList(BoundedFixedListType value) {
        this.fixedList = value;
    }

    /**
     * Gets the value of the refList property.
     * 
     * @return
     *     possible object is
     *     {@link BoundedRefListType }
     *     
     */
    public BoundedRefListType getRefList() {
        return refList;
    }

    /**
     * Sets the value of the refList property.
     * 
     * @param value
     *     allowed object is
     *     {@link BoundedRefListType }
     *     
     */
    public void setRefList(BoundedRefListType value) {
        this.refList = value;
    }

    /**
     * Gets the value of the multivalued property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMultivalued() {
        return multivalued;
    }

    /**
     * Sets the value of the multivalued property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMultivalued(Boolean value) {
        this.multivalued = value;
    }

}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.09.27 at 02:36:10 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for filterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="filterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attributeId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="operator" type="{}filterOperatorType" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="constant" type="{}filterConstantType"/>
 *           &lt;element name="parentAttribute" type="{}filterParentAttributeType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "filterType", propOrder = {
    "attributeId",
    "operator",
    "constant",
    "parentAttribute"
})
@XmlSeeAlso({
    MatchType.class
})
public class FilterType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String attributeId;
    @XmlElement(defaultValue = "MATCH")
    protected FilterOperatorType operator;
    protected FilterConstantType constant;
    protected FilterParentAttributeType parentAttribute;

    /**
     * Gets the value of the attributeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttributeId() {
        return attributeId;
    }

    /**
     * Sets the value of the attributeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttributeId(String value) {
        this.attributeId = value;
    }

    /**
     * Gets the value of the operator property.
     * 
     * @return
     *     possible object is
     *     {@link FilterOperatorType }
     *     
     */
    public FilterOperatorType getOperator() {
        return operator;
    }

    /**
     * Sets the value of the operator property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterOperatorType }
     *     
     */
    public void setOperator(FilterOperatorType value) {
        this.operator = value;
    }

    /**
     * Gets the value of the constant property.
     * 
     * @return
     *     possible object is
     *     {@link FilterConstantType }
     *     
     */
    public FilterConstantType getConstant() {
        return constant;
    }

    /**
     * Sets the value of the constant property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterConstantType }
     *     
     */
    public void setConstant(FilterConstantType value) {
        this.constant = value;
    }

    /**
     * Gets the value of the parentAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link FilterParentAttributeType }
     *     
     */
    public FilterParentAttributeType getParentAttribute() {
        return parentAttribute;
    }

    /**
     * Sets the value of the parentAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterParentAttributeType }
     *     
     */
    public void setParentAttribute(FilterParentAttributeType value) {
        this.parentAttribute = value;
    }

}

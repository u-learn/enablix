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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for conditionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="conditionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="andCondition" type="{}andConditionType"/>
 *         &lt;element name="orCondition" type="{}orConditionType"/>
 *         &lt;element name="basicCondition" type="{}basicConditionType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conditionType", propOrder = {
    "andCondition",
    "orCondition",
    "basicCondition"
})
@XmlSeeAlso({
    FocusItemMatchType.class,
    FilteredUserSetType.class,
    ExecConditionType.class
})
public class ConditionType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected AndConditionType andCondition;
    protected OrConditionType orCondition;
    protected BasicConditionType basicCondition;

    /**
     * Gets the value of the andCondition property.
     * 
     * @return
     *     possible object is
     *     {@link AndConditionType }
     *     
     */
    public AndConditionType getAndCondition() {
        return andCondition;
    }

    /**
     * Sets the value of the andCondition property.
     * 
     * @param value
     *     allowed object is
     *     {@link AndConditionType }
     *     
     */
    public void setAndCondition(AndConditionType value) {
        this.andCondition = value;
    }

    /**
     * Gets the value of the orCondition property.
     * 
     * @return
     *     possible object is
     *     {@link OrConditionType }
     *     
     */
    public OrConditionType getOrCondition() {
        return orCondition;
    }

    /**
     * Sets the value of the orCondition property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrConditionType }
     *     
     */
    public void setOrCondition(OrConditionType value) {
        this.orCondition = value;
    }

    /**
     * Gets the value of the basicCondition property.
     * 
     * @return
     *     possible object is
     *     {@link BasicConditionType }
     *     
     */
    public BasicConditionType getBasicCondition() {
        return basicCondition;
    }

    /**
     * Sets the value of the basicCondition property.
     * 
     * @param value
     *     allowed object is
     *     {@link BasicConditionType }
     *     
     */
    public void setBasicCondition(BasicConditionType value) {
        this.basicCondition = value;
    }

}

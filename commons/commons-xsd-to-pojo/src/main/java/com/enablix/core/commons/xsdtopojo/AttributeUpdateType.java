//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.27 at 11:36:24 AM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for attributeUpdateType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="attributeUpdateType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="on" type="{}attributeCheckType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="except" type="{}attributeCheckType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attributeUpdateType", propOrder = {
    "on",
    "except"
})
public class AttributeUpdateType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected List<AttributeCheckType> on;
    protected List<AttributeCheckType> except;

    /**
     * Gets the value of the on property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the on property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeCheckType }
     * 
     * 
     */
    public List<AttributeCheckType> getOn() {
        if (on == null) {
            on = new ArrayList<AttributeCheckType>();
        }
        return this.on;
    }

    /**
     * Gets the value of the except property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the except property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExcept().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeCheckType }
     * 
     * 
     */
    public List<AttributeCheckType> getExcept() {
        if (except == null) {
            except = new ArrayList<AttributeCheckType>();
        }
        return this.except;
    }

}

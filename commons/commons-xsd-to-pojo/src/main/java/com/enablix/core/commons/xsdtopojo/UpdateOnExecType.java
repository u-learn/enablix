//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.05 at 06:09:53 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateOnExecType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateOnExecType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contentGroups" type="{}updateOnContentGroupsType" minOccurs="0"/>
 *         &lt;element name="focusItem" type="{}focusItemRefType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateOnExecType", propOrder = {
    "contentGroups",
    "focusItem"
})
public class UpdateOnExecType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected UpdateOnContentGroupsType contentGroups;
    protected FocusItemRefType focusItem;

    /**
     * Gets the value of the contentGroups property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateOnContentGroupsType }
     *     
     */
    public UpdateOnContentGroupsType getContentGroups() {
        return contentGroups;
    }

    /**
     * Sets the value of the contentGroups property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateOnContentGroupsType }
     *     
     */
    public void setContentGroups(UpdateOnContentGroupsType value) {
        this.contentGroups = value;
    }

    /**
     * Gets the value of the focusItem property.
     * 
     * @return
     *     possible object is
     *     {@link FocusItemRefType }
     *     
     */
    public FocusItemRefType getFocusItem() {
        return focusItem;
    }

    /**
     * Sets the value of the focusItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link FocusItemRefType }
     *     
     */
    public void setFocusItem(FocusItemRefType value) {
        this.focusItem = value;
    }

}

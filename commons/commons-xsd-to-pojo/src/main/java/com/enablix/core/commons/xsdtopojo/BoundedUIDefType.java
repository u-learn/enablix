//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.14 at 07:35:12 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for boundedUIDefType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="boundedUIDefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="dropdown" type="{}dropdownType"/>
 *         &lt;element name="radioButton" type="{}radioButtonType"/>
 *         &lt;element name="checkbox" type="{}checkboxType"/>
 *         &lt;element name="autoSuggest" type="{}autoSuggestType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "boundedUIDefType", propOrder = {
    "dropdown",
    "radioButton",
    "checkbox",
    "autoSuggest"
})
public class BoundedUIDefType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected DropdownType dropdown;
    protected RadioButtonType radioButton;
    protected CheckboxType checkbox;
    protected AutoSuggestType autoSuggest;

    /**
     * Gets the value of the dropdown property.
     * 
     * @return
     *     possible object is
     *     {@link DropdownType }
     *     
     */
    public DropdownType getDropdown() {
        return dropdown;
    }

    /**
     * Sets the value of the dropdown property.
     * 
     * @param value
     *     allowed object is
     *     {@link DropdownType }
     *     
     */
    public void setDropdown(DropdownType value) {
        this.dropdown = value;
    }

    /**
     * Gets the value of the radioButton property.
     * 
     * @return
     *     possible object is
     *     {@link RadioButtonType }
     *     
     */
    public RadioButtonType getRadioButton() {
        return radioButton;
    }

    /**
     * Sets the value of the radioButton property.
     * 
     * @param value
     *     allowed object is
     *     {@link RadioButtonType }
     *     
     */
    public void setRadioButton(RadioButtonType value) {
        this.radioButton = value;
    }

    /**
     * Gets the value of the checkbox property.
     * 
     * @return
     *     possible object is
     *     {@link CheckboxType }
     *     
     */
    public CheckboxType getCheckbox() {
        return checkbox;
    }

    /**
     * Sets the value of the checkbox property.
     * 
     * @param value
     *     allowed object is
     *     {@link CheckboxType }
     *     
     */
    public void setCheckbox(CheckboxType value) {
        this.checkbox = value;
    }

    /**
     * Gets the value of the autoSuggest property.
     * 
     * @return
     *     possible object is
     *     {@link AutoSuggestType }
     *     
     */
    public AutoSuggestType getAutoSuggest() {
        return autoSuggest;
    }

    /**
     * Sets the value of the autoSuggest property.
     * 
     * @param value
     *     allowed object is
     *     {@link AutoSuggestType }
     *     
     */
    public void setAutoSuggest(AutoSuggestType value) {
        this.autoSuggest = value;
    }

}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.09 at 03:09:46 PM IST 
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
 * <p>Java class for playScopeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="playScopeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="focusItemMatch" type="{}focusItemMatchType"/>
 *         &lt;element name="focusItemRecord" type="{}focusItemRecordType" maxOccurs="unbounded"/>
 *       &lt;/choice>
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "playScopeType", propOrder = {
    "focusItemMatch",
    "focusItemRecord"
})
public class PlayScopeType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected FocusItemMatchType focusItemMatch;
    protected List<FocusItemRecordType> focusItemRecord;
    @XmlAttribute(name = "label")
    protected String label;

    /**
     * Gets the value of the focusItemMatch property.
     * 
     * @return
     *     possible object is
     *     {@link FocusItemMatchType }
     *     
     */
    public FocusItemMatchType getFocusItemMatch() {
        return focusItemMatch;
    }

    /**
     * Sets the value of the focusItemMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link FocusItemMatchType }
     *     
     */
    public void setFocusItemMatch(FocusItemMatchType value) {
        this.focusItemMatch = value;
    }

    /**
     * Gets the value of the focusItemRecord property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the focusItemRecord property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFocusItemRecord().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FocusItemRecordType }
     * 
     * 
     */
    public List<FocusItemRecordType> getFocusItemRecord() {
        if (focusItemRecord == null) {
            focusItemRecord = new ArrayList<FocusItemRecordType>();
        }
        return this.focusItemRecord;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

}

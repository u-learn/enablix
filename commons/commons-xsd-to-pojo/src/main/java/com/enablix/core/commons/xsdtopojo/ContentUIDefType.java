//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.12.01 at 06:08:59 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for contentUIDefType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="contentUIDefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="container" type="{}containerUIDefType"/>
 *         &lt;element name="text" type="{}textUIDefType"/>
 *         &lt;element name="bounded" type="{}boundedUIDefType"/>
 *         &lt;element name="doc" type="{}docUIDefType"/>
 *         &lt;element name="dateTime" type="{}dateTimeUIDefType"/>
 *         &lt;element name="numeric" type="{}numericUIDefType"/>
 *       &lt;/choice>
 *       &lt;attribute name="qualifiedId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contentUIDefType", propOrder = {
    "container",
    "text",
    "bounded",
    "doc",
    "dateTime",
    "numeric"
})
public class ContentUIDefType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected ContainerUIDefType container;
    protected TextUIDefType text;
    protected BoundedUIDefType bounded;
    protected DocUIDefType doc;
    protected DateTimeUIDefType dateTime;
    protected NumericUIDefType numeric;
    @XmlAttribute(name = "qualifiedId", required = true)
    protected String qualifiedId;

    /**
     * Gets the value of the container property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerUIDefType }
     *     
     */
    public ContainerUIDefType getContainer() {
        return container;
    }

    /**
     * Sets the value of the container property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerUIDefType }
     *     
     */
    public void setContainer(ContainerUIDefType value) {
        this.container = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link TextUIDefType }
     *     
     */
    public TextUIDefType getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextUIDefType }
     *     
     */
    public void setText(TextUIDefType value) {
        this.text = value;
    }

    /**
     * Gets the value of the bounded property.
     * 
     * @return
     *     possible object is
     *     {@link BoundedUIDefType }
     *     
     */
    public BoundedUIDefType getBounded() {
        return bounded;
    }

    /**
     * Sets the value of the bounded property.
     * 
     * @param value
     *     allowed object is
     *     {@link BoundedUIDefType }
     *     
     */
    public void setBounded(BoundedUIDefType value) {
        this.bounded = value;
    }

    /**
     * Gets the value of the doc property.
     * 
     * @return
     *     possible object is
     *     {@link DocUIDefType }
     *     
     */
    public DocUIDefType getDoc() {
        return doc;
    }

    /**
     * Sets the value of the doc property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocUIDefType }
     *     
     */
    public void setDoc(DocUIDefType value) {
        this.doc = value;
    }

    /**
     * Gets the value of the dateTime property.
     * 
     * @return
     *     possible object is
     *     {@link DateTimeUIDefType }
     *     
     */
    public DateTimeUIDefType getDateTime() {
        return dateTime;
    }

    /**
     * Sets the value of the dateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTimeUIDefType }
     *     
     */
    public void setDateTime(DateTimeUIDefType value) {
        this.dateTime = value;
    }

    /**
     * Gets the value of the numeric property.
     * 
     * @return
     *     possible object is
     *     {@link NumericUIDefType }
     *     
     */
    public NumericUIDefType getNumeric() {
        return numeric;
    }

    /**
     * Sets the value of the numeric property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumericUIDefType }
     *     
     */
    public void setNumeric(NumericUIDefType value) {
        this.numeric = value;
    }

    /**
     * Gets the value of the qualifiedId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQualifiedId() {
        return qualifiedId;
    }

    /**
     * Sets the value of the qualifiedId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQualifiedId(String value) {
        this.qualifiedId = value;
    }

}

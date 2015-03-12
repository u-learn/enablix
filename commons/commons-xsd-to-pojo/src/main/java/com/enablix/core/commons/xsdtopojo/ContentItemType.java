//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.13 at 01:13:01 AM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for contentItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="contentItemType">
 *   &lt;complexContent>
 *     &lt;extension base="{}baseContentType">
 *       &lt;choice minOccurs="0">
 *         &lt;element name="text" type="{}textType"/>
 *         &lt;element name="bounded" type="{}boundedType"/>
 *         &lt;element name="doc" type="{}docType"/>
 *         &lt;element name="dateTime" type="{}dateTimeType"/>
 *         &lt;element name="numeric" type="{}numericType"/>
 *       &lt;/choice>
 *       &lt;attribute name="type" type="{}contentItemClassType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contentItemType", propOrder = {
    "text",
    "bounded",
    "doc",
    "dateTime",
    "numeric"
})
public class ContentItemType
    extends BaseContentType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected TextType text;
    protected BoundedType bounded;
    protected DocType doc;
    protected DateTimeType dateTime;
    protected NumericType numeric;
    @XmlAttribute(name = "type")
    protected ContentItemClassType type;

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setText(TextType value) {
        this.text = value;
    }

    /**
     * Gets the value of the bounded property.
     * 
     * @return
     *     possible object is
     *     {@link BoundedType }
     *     
     */
    public BoundedType getBounded() {
        return bounded;
    }

    /**
     * Sets the value of the bounded property.
     * 
     * @param value
     *     allowed object is
     *     {@link BoundedType }
     *     
     */
    public void setBounded(BoundedType value) {
        this.bounded = value;
    }

    /**
     * Gets the value of the doc property.
     * 
     * @return
     *     possible object is
     *     {@link DocType }
     *     
     */
    public DocType getDoc() {
        return doc;
    }

    /**
     * Sets the value of the doc property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocType }
     *     
     */
    public void setDoc(DocType value) {
        this.doc = value;
    }

    /**
     * Gets the value of the dateTime property.
     * 
     * @return
     *     possible object is
     *     {@link DateTimeType }
     *     
     */
    public DateTimeType getDateTime() {
        return dateTime;
    }

    /**
     * Sets the value of the dateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTimeType }
     *     
     */
    public void setDateTime(DateTimeType value) {
        this.dateTime = value;
    }

    /**
     * Gets the value of the numeric property.
     * 
     * @return
     *     possible object is
     *     {@link NumericType }
     *     
     */
    public NumericType getNumeric() {
        return numeric;
    }

    /**
     * Sets the value of the numeric property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumericType }
     *     
     */
    public void setNumeric(NumericType value) {
        this.numeric = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link ContentItemClassType }
     *     
     */
    public ContentItemClassType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentItemClassType }
     *     
     */
    public void setType(ContentItemClassType value) {
        this.type = value;
    }

}

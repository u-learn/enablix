//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.23 at 12:07:24 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for contentGroupType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="contentGroupType">
 *   &lt;complexContent>
 *     &lt;extension base="{}describedType">
 *       &lt;sequence>
 *         &lt;element name="focusItemCorrelatedContent" type="{}focusItemCorrelatedContentType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="contentSet" type="{}contentSetType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contentGroupType", propOrder = {
    "focusItemCorrelatedContent",
    "contentSet"
})
public class ContentGroupType
    extends DescribedType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected List<FocusItemCorrelatedContentType> focusItemCorrelatedContent;
    protected ContentSetType contentSet;

    /**
     * Gets the value of the focusItemCorrelatedContent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the focusItemCorrelatedContent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFocusItemCorrelatedContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FocusItemCorrelatedContentType }
     * 
     * 
     */
    public List<FocusItemCorrelatedContentType> getFocusItemCorrelatedContent() {
        if (focusItemCorrelatedContent == null) {
            focusItemCorrelatedContent = new ArrayList<FocusItemCorrelatedContentType>();
        }
        return this.focusItemCorrelatedContent;
    }

    /**
     * Gets the value of the contentSet property.
     * 
     * @return
     *     possible object is
     *     {@link ContentSetType }
     *     
     */
    public ContentSetType getContentSet() {
        return contentSet;
    }

    /**
     * Sets the value of the contentSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentSetType }
     *     
     */
    public void setContentSet(ContentSetType value) {
        this.contentSet = value;
    }

}

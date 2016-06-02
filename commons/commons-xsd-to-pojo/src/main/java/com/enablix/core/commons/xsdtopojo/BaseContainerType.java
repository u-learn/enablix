//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.01 at 10:42:00 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for baseContainerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="baseContainerType">
 *   &lt;complexContent>
 *     &lt;extension base="{}baseContentType">
 *       &lt;sequence>
 *         &lt;element name="contentItem" type="{}contentItemType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseContainerType", propOrder = {
    "contentItem"
})
@XmlSeeAlso({
    ContainerType.class
})
public class BaseContainerType
    extends BaseContentType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected List<ContentItemType> contentItem;

    /**
     * Gets the value of the contentItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contentItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContentItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContentItemType }
     * 
     * 
     */
    public List<ContentItemType> getContentItem() {
        if (contentItem == null) {
            contentItem = new ArrayList<ContentItemType>();
        }
        return this.contentItem;
    }

}

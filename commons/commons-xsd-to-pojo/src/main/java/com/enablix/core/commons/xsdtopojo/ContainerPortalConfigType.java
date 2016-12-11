//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.06 at 07:02:49 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for containerPortalConfigType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="containerPortalConfigType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="headingContentItem" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="condensedView" type="{}portalContentCondensedViewType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "containerPortalConfigType", propOrder = {
    "headingContentItem",
    "condensedView"
})
public class ContainerPortalConfigType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected ContainerPortalConfigType.HeadingContentItem headingContentItem;
    protected PortalContentCondensedViewType condensedView;

    /**
     * Gets the value of the headingContentItem property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerPortalConfigType.HeadingContentItem }
     *     
     */
    public ContainerPortalConfigType.HeadingContentItem getHeadingContentItem() {
        return headingContentItem;
    }

    /**
     * Sets the value of the headingContentItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerPortalConfigType.HeadingContentItem }
     *     
     */
    public void setHeadingContentItem(ContainerPortalConfigType.HeadingContentItem value) {
        this.headingContentItem = value;
    }

    /**
     * Gets the value of the condensedView property.
     * 
     * @return
     *     possible object is
     *     {@link PortalContentCondensedViewType }
     *     
     */
    public PortalContentCondensedViewType getCondensedView() {
        return condensedView;
    }

    /**
     * Sets the value of the condensedView property.
     * 
     * @param value
     *     allowed object is
     *     {@link PortalContentCondensedViewType }
     *     
     */
    public void setCondensedView(PortalContentCondensedViewType value) {
        this.condensedView = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class HeadingContentItem
        implements Serializable
    {

        private final static long serialVersionUID = 1L;
        @XmlAttribute(name = "id", required = true)
        protected String id;

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

    }

}

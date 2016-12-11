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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for contentContainerMappingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="contentContainerMappingType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sourceId" type="{}sourceIdType"/>
 *         &lt;element name="parent" type="{}parentContainerMappingType" minOccurs="0"/>
 *         &lt;element name="createdBy" type="{}sytemUserMappingType" minOccurs="0"/>
 *         &lt;element name="modifiedBy" type="{}sytemUserMappingType" minOccurs="0"/>
 *         &lt;element name="contentItemMappings" type="{}contentItemMappingsType"/>
 *         &lt;element name="containerMappings" type="{}containerMappingsType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="qualifiedId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contentContainerMappingType", propOrder = {
    "sourceId",
    "parent",
    "createdBy",
    "modifiedBy",
    "contentItemMappings",
    "containerMappings"
})
public class ContentContainerMappingType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected SourceIdType sourceId;
    protected ParentContainerMappingType parent;
    protected SytemUserMappingType createdBy;
    protected SytemUserMappingType modifiedBy;
    @XmlElement(required = true)
    protected ContentItemMappingsType contentItemMappings;
    protected ContainerMappingsType containerMappings;
    @XmlAttribute(name = "qualifiedId", required = true)
    protected String qualifiedId;

    /**
     * Gets the value of the sourceId property.
     * 
     * @return
     *     possible object is
     *     {@link SourceIdType }
     *     
     */
    public SourceIdType getSourceId() {
        return sourceId;
    }

    /**
     * Sets the value of the sourceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceIdType }
     *     
     */
    public void setSourceId(SourceIdType value) {
        this.sourceId = value;
    }

    /**
     * Gets the value of the parent property.
     * 
     * @return
     *     possible object is
     *     {@link ParentContainerMappingType }
     *     
     */
    public ParentContainerMappingType getParent() {
        return parent;
    }

    /**
     * Sets the value of the parent property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParentContainerMappingType }
     *     
     */
    public void setParent(ParentContainerMappingType value) {
        this.parent = value;
    }

    /**
     * Gets the value of the createdBy property.
     * 
     * @return
     *     possible object is
     *     {@link SytemUserMappingType }
     *     
     */
    public SytemUserMappingType getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link SytemUserMappingType }
     *     
     */
    public void setCreatedBy(SytemUserMappingType value) {
        this.createdBy = value;
    }

    /**
     * Gets the value of the modifiedBy property.
     * 
     * @return
     *     possible object is
     *     {@link SytemUserMappingType }
     *     
     */
    public SytemUserMappingType getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Sets the value of the modifiedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link SytemUserMappingType }
     *     
     */
    public void setModifiedBy(SytemUserMappingType value) {
        this.modifiedBy = value;
    }

    /**
     * Gets the value of the contentItemMappings property.
     * 
     * @return
     *     possible object is
     *     {@link ContentItemMappingsType }
     *     
     */
    public ContentItemMappingsType getContentItemMappings() {
        return contentItemMappings;
    }

    /**
     * Sets the value of the contentItemMappings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentItemMappingsType }
     *     
     */
    public void setContentItemMappings(ContentItemMappingsType value) {
        this.contentItemMappings = value;
    }

    /**
     * Gets the value of the containerMappings property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerMappingsType }
     *     
     */
    public ContainerMappingsType getContainerMappings() {
        return containerMappings;
    }

    /**
     * Sets the value of the containerMappings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerMappingsType }
     *     
     */
    public void setContainerMappings(ContainerMappingsType value) {
        this.containerMappings = value;
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

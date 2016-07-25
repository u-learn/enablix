//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.25 at 11:21:01 AM IST 
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
 * <p>Java class for containerUIDefType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="containerUIDefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="enclosure" type="{}childEnclosureType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listViewConfig" type="{}containerListViewConfigType" minOccurs="0"/>
 *         &lt;element name="portalConfig" type="{}containerPortalConfigType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="labelQualifiedId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "containerUIDefType", propOrder = {
    "enclosure",
    "listViewConfig",
    "portalConfig"
})
public class ContainerUIDefType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected List<ChildEnclosureType> enclosure;
    protected ContainerListViewConfigType listViewConfig;
    protected ContainerPortalConfigType portalConfig;
    @XmlAttribute(name = "labelQualifiedId")
    protected String labelQualifiedId;

    /**
     * Gets the value of the enclosure property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enclosure property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnclosure().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ChildEnclosureType }
     * 
     * 
     */
    public List<ChildEnclosureType> getEnclosure() {
        if (enclosure == null) {
            enclosure = new ArrayList<ChildEnclosureType>();
        }
        return this.enclosure;
    }

    /**
     * Gets the value of the listViewConfig property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerListViewConfigType }
     *     
     */
    public ContainerListViewConfigType getListViewConfig() {
        return listViewConfig;
    }

    /**
     * Sets the value of the listViewConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerListViewConfigType }
     *     
     */
    public void setListViewConfig(ContainerListViewConfigType value) {
        this.listViewConfig = value;
    }

    /**
     * Gets the value of the portalConfig property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerPortalConfigType }
     *     
     */
    public ContainerPortalConfigType getPortalConfig() {
        return portalConfig;
    }

    /**
     * Sets the value of the portalConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerPortalConfigType }
     *     
     */
    public void setPortalConfig(ContainerPortalConfigType value) {
        this.portalConfig = value;
    }

    /**
     * Gets the value of the labelQualifiedId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabelQualifiedId() {
        return labelQualifiedId;
    }

    /**
     * Sets the value of the labelQualifiedId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabelQualifiedId(String value) {
        this.labelQualifiedId = value;
    }

}

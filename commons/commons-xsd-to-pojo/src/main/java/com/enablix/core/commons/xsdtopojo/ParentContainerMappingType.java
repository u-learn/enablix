//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.09.27 at 02:36:10 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for parentContainerMappingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="parentContainerMappingType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="containerMapping" type="{}containerMappingType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parentContainerMappingType", propOrder = {
    "containerMapping"
})
public class ParentContainerMappingType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected ContainerMappingType containerMapping;

    /**
     * Gets the value of the containerMapping property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerMappingType }
     *     
     */
    public ContainerMappingType getContainerMapping() {
        return containerMapping;
    }

    /**
     * Sets the value of the containerMapping property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerMappingType }
     *     
     */
    public void setContainerMapping(ContainerMappingType value) {
        this.containerMapping = value;
    }

}

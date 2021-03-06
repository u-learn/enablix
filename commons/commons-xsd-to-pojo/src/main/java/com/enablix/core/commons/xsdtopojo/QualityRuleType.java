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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for qualityRuleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="qualityRuleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="exclude" type="{}containerRefListType" minOccurs="0"/>
 *           &lt;element name="include" type="{}containerRefListType" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element name="configParams" type="{}paramSetType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "qualityRuleType", propOrder = {
    "exclude",
    "include",
    "configParams"
})
public class QualityRuleType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected ContainerRefListType exclude;
    protected ContainerRefListType include;
    protected ParamSetType configParams;
    @XmlAttribute(name = "id")
    protected String id;

    /**
     * Gets the value of the exclude property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerRefListType }
     *     
     */
    public ContainerRefListType getExclude() {
        return exclude;
    }

    /**
     * Sets the value of the exclude property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerRefListType }
     *     
     */
    public void setExclude(ContainerRefListType value) {
        this.exclude = value;
    }

    /**
     * Gets the value of the include property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerRefListType }
     *     
     */
    public ContainerRefListType getInclude() {
        return include;
    }

    /**
     * Sets the value of the include property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerRefListType }
     *     
     */
    public void setInclude(ContainerRefListType value) {
        this.include = value;
    }

    /**
     * Gets the value of the configParams property.
     * 
     * @return
     *     possible object is
     *     {@link ParamSetType }
     *     
     */
    public ParamSetType getConfigParams() {
        return configParams;
    }

    /**
     * Sets the value of the configParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParamSetType }
     *     
     */
    public void setConfigParams(ParamSetType value) {
        this.configParams = value;
    }

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

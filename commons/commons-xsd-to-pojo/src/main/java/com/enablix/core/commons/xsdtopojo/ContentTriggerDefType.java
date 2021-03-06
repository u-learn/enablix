//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.09.27 at 02:36:10 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for contentTriggerDefType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="contentTriggerDefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="candidateContainers" type="{}candidateContainersType" maxOccurs="unbounded"/>
 *         &lt;element name="lifecycle" type="{}lifecycleType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" use="required" type="{}triggerTypeEnum" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contentTriggerDefType", propOrder = {
    "candidateContainers",
    "lifecycle"
})
public class ContentTriggerDefType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected List<CandidateContainersType> candidateContainers;
    @XmlElement(required = true)
    protected LifecycleType lifecycle;
    @XmlAttribute(name = "type", required = true)
    protected TriggerTypeEnum type;
    @XmlAttribute(name = "id", required = true)
    protected String id;

    /**
     * Gets the value of the candidateContainers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the candidateContainers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCandidateContainers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CandidateContainersType }
     * 
     * 
     */
    public List<CandidateContainersType> getCandidateContainers() {
        if (candidateContainers == null) {
            candidateContainers = new ArrayList<CandidateContainersType>();
        }
        return this.candidateContainers;
    }

    /**
     * Gets the value of the lifecycle property.
     * 
     * @return
     *     possible object is
     *     {@link LifecycleType }
     *     
     */
    public LifecycleType getLifecycle() {
        return lifecycle;
    }

    /**
     * Sets the value of the lifecycle property.
     * 
     * @param value
     *     allowed object is
     *     {@link LifecycleType }
     *     
     */
    public void setLifecycle(LifecycleType value) {
        this.lifecycle = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link TriggerTypeEnum }
     *     
     */
    public TriggerTypeEnum getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link TriggerTypeEnum }
     *     
     */
    public void setType(TriggerTypeEnum value) {
        this.type = value;
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

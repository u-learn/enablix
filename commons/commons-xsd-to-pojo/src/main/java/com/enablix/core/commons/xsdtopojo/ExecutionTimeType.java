//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.10.08 at 05:24:49 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for executionTimeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="executionTimeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="after" type="{}laterExecutionTimeType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" type="{}execTimeType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "executionTimeType", propOrder = {
    "after"
})
public class ExecutionTimeType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected LaterExecutionTimeType after;
    @XmlAttribute(name = "type")
    protected ExecTimeType type;

    /**
     * Gets the value of the after property.
     * 
     * @return
     *     possible object is
     *     {@link LaterExecutionTimeType }
     *     
     */
    public LaterExecutionTimeType getAfter() {
        return after;
    }

    /**
     * Sets the value of the after property.
     * 
     * @param value
     *     allowed object is
     *     {@link LaterExecutionTimeType }
     *     
     */
    public void setAfter(LaterExecutionTimeType value) {
        this.after = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link ExecTimeType }
     *     
     */
    public ExecTimeType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExecTimeType }
     *     
     */
    public void setType(ExecTimeType value) {
        this.type = value;
    }

}

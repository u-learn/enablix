//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.15 at 04:59:14 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for qualityRuleConfigType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="qualityRuleConfigType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="qualityRules" type="{}qualityRulesType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "qualityRuleConfigType", propOrder = {
    "qualityRules"
})
public class QualityRuleConfigType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected QualityRulesType qualityRules;

    /**
     * Gets the value of the qualityRules property.
     * 
     * @return
     *     possible object is
     *     {@link QualityRulesType }
     *     
     */
    public QualityRulesType getQualityRules() {
        return qualityRules;
    }

    /**
     * Sets the value of the qualityRules property.
     * 
     * @param value
     *     allowed object is
     *     {@link QualityRulesType }
     *     
     */
    public void setQualityRules(QualityRulesType value) {
        this.qualityRules = value;
    }

}
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.05 at 06:09:53 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for execTimeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="execTimeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NOW"/>
 *     &lt;enumeration value="LATER"/>
 *     &lt;enumeration value="RECURRING"/>
 *     &lt;enumeration value="ON_CONTENT_UPDATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "execTimeType")
@XmlEnum
public enum ExecTimeType {

    NOW,
    LATER,
    RECURRING,
    ON_CONTENT_UPDATE;

    public String value() {
        return name();
    }

    public static ExecTimeType fromValue(String v) {
        return valueOf(v);
    }

}

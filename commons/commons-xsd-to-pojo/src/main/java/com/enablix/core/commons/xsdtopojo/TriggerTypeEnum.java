//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.26 at 02:33:21 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for triggerTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="triggerTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CONTENT_ADD"/>
 *     &lt;enumeration value="CONTENT_UPDATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "triggerTypeEnum")
@XmlEnum
public enum TriggerTypeEnum {

    CONTENT_ADD,
    CONTENT_UPDATE;

    public String value() {
        return name();
    }

    public static TriggerTypeEnum fromValue(String v) {
        return valueOf(v);
    }

}

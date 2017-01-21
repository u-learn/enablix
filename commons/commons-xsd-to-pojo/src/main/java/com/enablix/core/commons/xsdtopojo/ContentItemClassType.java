//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.21 at 02:07:53 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for contentItemClassType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="contentItemClassType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="text"/>
 *     &lt;enumeration value="bounded"/>
 *     &lt;enumeration value="doc"/>
 *     &lt;enumeration value="dateTime"/>
 *     &lt;enumeration value="numeric"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "contentItemClassType")
@XmlEnum
public enum ContentItemClassType {

    @XmlEnumValue("text")
    TEXT("text"),
    @XmlEnumValue("bounded")
    BOUNDED("bounded"),
    @XmlEnumValue("doc")
    DOC("doc"),
    @XmlEnumValue("dateTime")
    DATE_TIME("dateTime"),
    @XmlEnumValue("numeric")
    NUMERIC("numeric");
    private final String value;

    ContentItemClassType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ContentItemClassType fromValue(String v) {
        for (ContentItemClassType c: ContentItemClassType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

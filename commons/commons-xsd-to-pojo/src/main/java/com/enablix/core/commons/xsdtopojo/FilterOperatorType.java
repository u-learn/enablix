//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.07 at 12:17:09 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for filterOperatorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="filterOperatorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MATCH"/>
 *     &lt;enumeration value="NOT_MATCH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "filterOperatorType")
@XmlEnum
public enum FilterOperatorType {

    MATCH,
    NOT_MATCH;

    public String value() {
        return name();
    }

    public static FilterOperatorType fromValue(String v) {
        return valueOf(v);
    }

}

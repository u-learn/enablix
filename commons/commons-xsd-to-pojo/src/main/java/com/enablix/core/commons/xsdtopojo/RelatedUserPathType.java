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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for relatedUserPathType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="relatedUserPathType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="relatedUser" type="{}relatedUserType"/>
 *           &lt;element name="pathItem" type="{}pathItemType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relatedUserPathType", propOrder = {
    "relatedUser",
    "pathItem"
})
public class RelatedUserPathType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected RelatedUserType relatedUser;
    protected PathItemType pathItem;

    /**
     * Gets the value of the relatedUser property.
     * 
     * @return
     *     possible object is
     *     {@link RelatedUserType }
     *     
     */
    public RelatedUserType getRelatedUser() {
        return relatedUser;
    }

    /**
     * Sets the value of the relatedUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link RelatedUserType }
     *     
     */
    public void setRelatedUser(RelatedUserType value) {
        this.relatedUser = value;
    }

    /**
     * Gets the value of the pathItem property.
     * 
     * @return
     *     possible object is
     *     {@link PathItemType }
     *     
     */
    public PathItemType getPathItem() {
        return pathItem;
    }

    /**
     * Sets the value of the pathItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link PathItemType }
     *     
     */
    public void setPathItem(PathItemType value) {
        this.pathItem = value;
    }

}

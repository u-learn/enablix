//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.26 at 02:33:21 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{}describedAndVersionedType">
 *       &lt;sequence>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="focusItems" type="{}focusItemsType"/>
 *         &lt;element name="scope" type="{}playScopeType" minOccurs="0"/>
 *         &lt;element name="userGroups" type="{}userGroupsType" minOccurs="0"/>
 *         &lt;element name="contentGroups" type="{}contentGroupsType" minOccurs="0"/>
 *         &lt;element name="execution" type="{}playExecutionType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="prototype" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="executable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="prototypeId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "title",
    "focusItems",
    "scope",
    "userGroups",
    "contentGroups",
    "execution"
})
@XmlRootElement(name = "playTemplate")
public class PlayTemplate
    extends DescribedAndVersionedType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected String title;
    @XmlElement(required = true)
    protected FocusItemsType focusItems;
    protected PlayScopeType scope;
    protected UserGroupsType userGroups;
    protected ContentGroupsType contentGroups;
    protected PlayExecutionType execution;
    @XmlAttribute(name = "prototype")
    protected Boolean prototype;
    @XmlAttribute(name = "executable")
    protected Boolean executable;
    @XmlAttribute(name = "prototypeId")
    protected String prototypeId;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the focusItems property.
     * 
     * @return
     *     possible object is
     *     {@link FocusItemsType }
     *     
     */
    public FocusItemsType getFocusItems() {
        return focusItems;
    }

    /**
     * Sets the value of the focusItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link FocusItemsType }
     *     
     */
    public void setFocusItems(FocusItemsType value) {
        this.focusItems = value;
    }

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link PlayScopeType }
     *     
     */
    public PlayScopeType getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlayScopeType }
     *     
     */
    public void setScope(PlayScopeType value) {
        this.scope = value;
    }

    /**
     * Gets the value of the userGroups property.
     * 
     * @return
     *     possible object is
     *     {@link UserGroupsType }
     *     
     */
    public UserGroupsType getUserGroups() {
        return userGroups;
    }

    /**
     * Sets the value of the userGroups property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserGroupsType }
     *     
     */
    public void setUserGroups(UserGroupsType value) {
        this.userGroups = value;
    }

    /**
     * Gets the value of the contentGroups property.
     * 
     * @return
     *     possible object is
     *     {@link ContentGroupsType }
     *     
     */
    public ContentGroupsType getContentGroups() {
        return contentGroups;
    }

    /**
     * Sets the value of the contentGroups property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentGroupsType }
     *     
     */
    public void setContentGroups(ContentGroupsType value) {
        this.contentGroups = value;
    }

    /**
     * Gets the value of the execution property.
     * 
     * @return
     *     possible object is
     *     {@link PlayExecutionType }
     *     
     */
    public PlayExecutionType getExecution() {
        return execution;
    }

    /**
     * Sets the value of the execution property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlayExecutionType }
     *     
     */
    public void setExecution(PlayExecutionType value) {
        this.execution = value;
    }

    /**
     * Gets the value of the prototype property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isPrototype() {
        if (prototype == null) {
            return true;
        } else {
            return prototype;
        }
    }

    /**
     * Sets the value of the prototype property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPrototype(Boolean value) {
        this.prototype = value;
    }

    /**
     * Gets the value of the executable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isExecutable() {
        if (executable == null) {
            return false;
        } else {
            return executable;
        }
    }

    /**
     * Sets the value of the executable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExecutable(Boolean value) {
        this.executable = value;
    }

    /**
     * Gets the value of the prototypeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrototypeId() {
        return prototypeId;
    }

    /**
     * Sets the value of the prototypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrototypeId(String value) {
        this.prototypeId = value;
    }

}

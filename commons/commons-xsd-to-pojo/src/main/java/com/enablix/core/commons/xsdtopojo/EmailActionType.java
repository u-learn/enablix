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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for emailActionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="emailActionType">
 *   &lt;complexContent>
 *     &lt;extension base="{}actionType">
 *       &lt;sequence>
 *         &lt;element name="emailTemplate" type="{}emailTemplateType"/>
 *         &lt;element name="emailContent" type="{}emailContentType"/>
 *         &lt;element name="recipient" type="{}emailRecipientType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "emailActionType", propOrder = {
    "emailTemplate",
    "emailContent",
    "recipient"
})
public class EmailActionType
    extends ActionType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected EmailTemplateType emailTemplate;
    @XmlElement(required = true)
    protected EmailContentType emailContent;
    @XmlElement(required = true)
    protected EmailRecipientType recipient;

    /**
     * Gets the value of the emailTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link EmailTemplateType }
     *     
     */
    public EmailTemplateType getEmailTemplate() {
        return emailTemplate;
    }

    /**
     * Sets the value of the emailTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailTemplateType }
     *     
     */
    public void setEmailTemplate(EmailTemplateType value) {
        this.emailTemplate = value;
    }

    /**
     * Gets the value of the emailContent property.
     * 
     * @return
     *     possible object is
     *     {@link EmailContentType }
     *     
     */
    public EmailContentType getEmailContent() {
        return emailContent;
    }

    /**
     * Sets the value of the emailContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailContentType }
     *     
     */
    public void setEmailContent(EmailContentType value) {
        this.emailContent = value;
    }

    /**
     * Gets the value of the recipient property.
     * 
     * @return
     *     possible object is
     *     {@link EmailRecipientType }
     *     
     */
    public EmailRecipientType getRecipient() {
        return recipient;
    }

    /**
     * Sets the value of the recipient property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailRecipientType }
     *     
     */
    public void setRecipient(EmailRecipientType value) {
        this.recipient = value;
    }

}

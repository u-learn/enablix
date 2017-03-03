//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.02 at 04:35:33 PM IST 
//


package com.enablix.core.commons.xsdtopojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for actionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="actionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="correlation" type="{}correlationActionType" minOccurs="0"/>
 *         &lt;element name="email" type="{}emailActionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reevaluateCheckpoint" type="{}reevaluatePendingCheckpointType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "actionsType", propOrder = {
    "correlation",
    "email",
    "reevaluateCheckpoint"
})
public class ActionsType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected CorrelationActionType correlation;
    protected List<EmailActionType> email;
    protected ReevaluatePendingCheckpointType reevaluateCheckpoint;

    /**
     * Gets the value of the correlation property.
     * 
     * @return
     *     possible object is
     *     {@link CorrelationActionType }
     *     
     */
    public CorrelationActionType getCorrelation() {
        return correlation;
    }

    /**
     * Sets the value of the correlation property.
     * 
     * @param value
     *     allowed object is
     *     {@link CorrelationActionType }
     *     
     */
    public void setCorrelation(CorrelationActionType value) {
        this.correlation = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the email property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEmail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EmailActionType }
     * 
     * 
     */
    public List<EmailActionType> getEmail() {
        if (email == null) {
            email = new ArrayList<EmailActionType>();
        }
        return this.email;
    }

    /**
     * Gets the value of the reevaluateCheckpoint property.
     * 
     * @return
     *     possible object is
     *     {@link ReevaluatePendingCheckpointType }
     *     
     */
    public ReevaluatePendingCheckpointType getReevaluateCheckpoint() {
        return reevaluateCheckpoint;
    }

    /**
     * Sets the value of the reevaluateCheckpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReevaluatePendingCheckpointType }
     *     
     */
    public void setReevaluateCheckpoint(ReevaluatePendingCheckpointType value) {
        this.reevaluateCheckpoint = value;
    }

}

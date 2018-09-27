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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for checkpointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="checkpointType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="execCondition" type="{}execConditionType" minOccurs="0"/>
 *         &lt;element name="executionTime" type="{}executionTimeType"/>
 *         &lt;element name="actions" type="{}actionsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "checkpointType", propOrder = {
    "execCondition",
    "executionTime",
    "actions"
})
public class CheckpointType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected ExecConditionType execCondition;
    @XmlElement(required = true)
    protected ExecutionTimeType executionTime;
    @XmlElement(required = true)
    protected ActionsType actions;

    /**
     * Gets the value of the execCondition property.
     * 
     * @return
     *     possible object is
     *     {@link ExecConditionType }
     *     
     */
    public ExecConditionType getExecCondition() {
        return execCondition;
    }

    /**
     * Sets the value of the execCondition property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExecConditionType }
     *     
     */
    public void setExecCondition(ExecConditionType value) {
        this.execCondition = value;
    }

    /**
     * Gets the value of the executionTime property.
     * 
     * @return
     *     possible object is
     *     {@link ExecutionTimeType }
     *     
     */
    public ExecutionTimeType getExecutionTime() {
        return executionTime;
    }

    /**
     * Sets the value of the executionTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExecutionTimeType }
     *     
     */
    public void setExecutionTime(ExecutionTimeType value) {
        this.executionTime = value;
    }

    /**
     * Gets the value of the actions property.
     * 
     * @return
     *     possible object is
     *     {@link ActionsType }
     *     
     */
    public ActionsType getActions() {
        return actions;
    }

    /**
     * Sets the value of the actions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActionsType }
     *     
     */
    public void setActions(ActionsType value) {
        this.actions = value;
    }

}

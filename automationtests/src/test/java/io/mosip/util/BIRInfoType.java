//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.06 at 02:49:01 PM IST 
//


package io.mosip.util;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;



/**
 * <p>Java class for BIRInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BIRInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Creator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Index" type="{http://docs.oasis-open.org/bias/ns/biaspatronformat-1.0/}UUIDType" minOccurs="0"/&gt;
 *         &lt;element name="Payload" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="Integrity" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="CreationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="NotValidBefore" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="NotValidAfter" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BIRInfoType", propOrder = {
    "creator",
    "index",
    "payload",
    "integrity",
    "creationDate",
    "notValidBefore",
    "notValidAfter"
})
public class BIRInfoType {

    @XmlElement(name = "Creator")
    protected String creator;
    @XmlElement(name = "Index")
    protected String index;
    @XmlElement(name = "Payload")
    protected byte[] payload;
    @XmlElement(name = "Integrity")
    protected boolean integrity;
    @XmlElement(name = "CreationDate")
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(DateAdapter.class)
    protected LocalDateTime creationDate;
    @XmlElement(name = "NotValidBefore")
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(DateAdapter.class)
    protected LocalDateTime notValidBefore;
    @XmlElement(name = "NotValidAfter")
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(DateAdapter.class)
    protected LocalDateTime notValidAfter;

    /**
     * Gets the value of the creator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Sets the value of the creator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreator(String value) {
        this.creator = value;
    }

    /**
     * Gets the value of the index property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndex(String value) {
        this.index = value;
    }

    /**
     * Gets the value of the payload property.
     * 
     * @return payload
     *     possible object is
     *     byte[]
     */
    public byte[] getPayload() {
        return payload;
    }

    /**
     * Sets the value of the payload property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPayload(byte[] value) {
        this.payload = value;
    }

    /**
     * Gets the value of the integrity property.
     * 
     *  @return boolean
     */
    public boolean isIntegrity() {
        return integrity;
    }

    /**
     * Sets the value of the integrity property.
     * 
     *  @param value boolean
     */
    public void setIntegrity(boolean value) {
        this.integrity = value;
    }

    /**
     * Gets the value of the creationDate property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the value of the creationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setCreationDate(LocalDateTime value) {
        this.creationDate = value;
    }

    /**
     * Gets the value of the notValidBefore property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public LocalDateTime getNotValidBefore() {
        return notValidBefore;
    }

    /**
     * Sets the value of the notValidBefore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setNotValidBefore(LocalDateTime value) {
        this.notValidBefore = value;
    }

    /**
     * Gets the value of the notValidAfter property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public LocalDateTime getNotValidAfter() {
        return notValidAfter;
    }

    /**
     * Sets the value of the notValidAfter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setNotValidAfter(LocalDateTime value) {
        this.notValidAfter = value;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		result = prime * result + (integrity ? 1231 : 1237);
		result = prime * result + ((notValidAfter == null) ? 0 : notValidAfter.hashCode());
		result = prime * result + ((notValidBefore == null) ? 0 : notValidBefore.hashCode());
		result = prime * result + Arrays.hashCode(payload);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BIRInfoType other = (BIRInfoType) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		if (integrity != other.integrity)
			return false;
		if (notValidAfter == null) {
			if (other.notValidAfter != null)
				return false;
		} else if (!notValidAfter.equals(other.notValidAfter))
			return false;
		if (notValidBefore == null) {
			if (other.notValidBefore != null)
				return false;
		} else if (!notValidBefore.equals(other.notValidBefore))
			return false;
		if (!Arrays.equals(payload, other.payload))
			return false;
		return true;
	}

}

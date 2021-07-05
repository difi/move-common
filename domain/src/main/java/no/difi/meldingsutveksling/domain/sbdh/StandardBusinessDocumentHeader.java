//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.25 at 12:23:12 PM CET 
//


package no.difi.meldingsutveksling.domain.sbdh;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import no.difi.meldingsutveksling.validation.group.ValidationGroups;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.ConvertGroup;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


/**
 * Java class for StandardBusinessDocumentHeader complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>{@code
 * <complexType name="StandardBusinessDocumentHeader">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="HeaderVersion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="Sender" type="{http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader}Partner" maxOccurs="unbounded"/>
 *         <element name="Receiver" type="{http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader}Partner" maxOccurs="unbounded"/>
 *         <element name="DocumentIdentification" type="{http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader}DocumentIdentification"/>
 *         <element name="Manifest" type="{http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader}Manifest" minOccurs="0"/>
 *         <element name="BusinessScope" type="{http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader}BusinessScope" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StandardBusinessDocumentHeader", propOrder = {
        "headerVersion",
        "sender",
        "receiver",
        "documentIdentification",
        "manifest",
        "businessScope"
})
@Data
public class StandardBusinessDocumentHeader {

    @XmlElement(name = "HeaderVersion", required = true)
    @NotNull
    private String headerVersion;

    @XmlElement(name = "Sender", required = true)
    @Size(max = 1)
    @Valid
    @ConvertGroup(to = ValidationGroups.Partner.Sender.class)
    private Set<@Valid Sender> sender;

    @XmlElement(name = "Receiver", required = true)
    @NotEmpty
    @Size(min = 1, max = 1)
    @Valid
    @ConvertGroup(to = ValidationGroups.Partner.Receiver.class)
    private Set<@Valid Receiver> receiver;

    @XmlElement(name = "DocumentIdentification", required = true)
    @NotNull
    @Valid
    private DocumentIdentification documentIdentification;

    @XmlElement(name = "Manifest")
    @Valid
    private Manifest manifest;

    @XmlElement(name = "BusinessScope")
    @NotNull
    @Valid
    private BusinessScope businessScope;

    public void setSender(Set<Sender> sender) {
        this.sender = sender;
    }

    public Set<Sender> getSender() {
        if (sender == null) {
            sender = new HashSet<>();
        }
        return this.sender;
    }

    public StandardBusinessDocumentHeader addSender(Sender partner) {
        getSender().add(partner);
        return this;
    }

    public Set<Receiver> getReceiver() {
        if (receiver == null) {
            receiver = new HashSet<>();
        }
        return this.receiver;
    }

    public StandardBusinessDocumentHeader addReceiver(Receiver partner) {
        getReceiver().add(partner);
        return this;
    }

    @JsonIgnore
    Optional<Sender> getFirstSender() {
        if (sender == null) {
            return Optional.empty();
        }
        return sender.stream().findFirst();
    }

    @JsonIgnore
    Optional<Receiver> getFirstReceiver() {
        if (receiver == null) {
            return Optional.empty();
        }
        return receiver.stream().findFirst();
    }
}

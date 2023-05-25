package org.example.demo.preservingstate.data;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Contact extends AbstractPersistable<Long> {

    @Column(length = PersonName.MAX_LENGTH)
    @Convert(converter = PersonNameAttributeConverter.class)
    private PersonName firstName;

    @Column(length = PersonName.MAX_LENGTH)
    @Convert(converter = PersonNameAttributeConverter.class)
    private PersonName lastName;

    @Column(length = PhoneNumber.MAX_LENGTH)
    @Convert(converter = PhoneNumberAttributeConverter.class)
    private PhoneNumber phoneNumber;


    @Column(length = EmailAddress.MAX_LENGTH)
    @Convert(converter = EmailAddressAttributeConverter.class)
    private EmailAddress emailAddress;

    protected Contact() {
    }

    public Contact(PersonName firstName, PersonName lastName, PhoneNumber phoneNumber, EmailAddress emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public PersonName getFirstName() {
        return firstName;
    }

    public PersonName getLastName() {
        return lastName;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }
}

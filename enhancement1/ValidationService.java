package com.contactservice.service;

import com.contactservice.model.Contact;
import com.contactservice.util.Logger;

/**
 * Service for validating Contact objects.
 * Centralizes all validation logic in one place.
 */
public class ValidationService {

    /**
     * Validates a complete Contact object
     * @param contact The contact to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validateContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }

        validateContactId(contact.getContactId());
        validateFirstName(contact.getFirstName());
        validateLastName(contact.getLastName());
        validatePhone(contact.getPhone());
        validateAddress(contact.getAddress());

        Logger.log("Contact validation passed: " + contact.getContactId());
    }

    /**
     * Validates contact ID
     */
    private void validateContactId(String contactId) {
        if (contactId == null || contactId.isEmpty()) {
            throw new IllegalArgumentException(
                    "Contact ID cannot be null or empty");
        }
        if (contactId.length() > 10) {
            throw new IllegalArgumentException(
                    "Contact ID cannot exceed 10 characters");
        }
    }

    /**
     * Validates first name
     */
    private void validateFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException(
                    "First name cannot be null or empty");
        }
        if (firstName.length() > 10) {
            throw new IllegalArgumentException(
                    "First name cannot exceed 10 characters");
        }
    }

    /**
     * Validates last name
     */
    private void validateLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException(
                    "Last name cannot be null or empty");
        }
        if (lastName.length() > 10) {
            throw new IllegalArgumentException(
                    "Last name cannot exceed 10 characters");
        }
    }

    /**
     * Validates phone number
     */
    private void validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException(
                    "Phone cannot be null or empty");
        }
        if (phone.length() != 10) {
            throw new IllegalArgumentException(
                    "Phone must be exactly 10 digits");
        }
        if (!phone.matches("\\d{10}")) {
            throw new IllegalArgumentException(
                    "Phone must contain only digits");
        }
    }

    /**
     * Validates address
     */
    private void validateAddress(String address) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException(
                    "Address cannot be null or empty");
        }
        if (address.length() > 30) {
            throw new IllegalArgumentException(
                    "Address cannot exceed 30 characters");
        }
    }
}
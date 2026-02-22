package com.contactservice.controller;

import com.contactservice.model.Contact;
import com.contactservice.service.ContactService;
import com.contactservice.util.Logger;
import java.util.List;

/**
 * Controller layer for handling user operations.
 * Provides the interface between user and service layer.
 *
 * Updated for Category 3: Added constructor for dependency injection
 * to support database-backed service.
 */
public class ContactController {

    private ContactService service;

    /**
     * Default constructor - creates service with in-memory repository
     * Used for Categories 1 & 2
     */
    public ContactController() {
        this.service = new ContactService();
        Logger.log("ContactController initialized with default service");
    }

    /**
     * NEW - Constructor for dependency injection
     * Allows specifying which service instance to use
     *
     * Used for Category 3 to inject service with database repository
     *
     * @param service The service instance to use
     */
    public ContactController(ContactService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.service = service;
        Logger.log("ContactController initialized with provided service");
    }

    /**
     * Creates a new contact
     *
     * @param id Contact ID
     * @param firstName First name
     * @param lastName Last name
     * @param phone Phone number (10 digits)
     * @param address Address (max 30 characters)
     */
    public void createContact(String id, String firstName, String lastName,
                              String phone, String address) {
        Logger.log("CREATE request - Contact ID: " + id);

        try {
            Contact contact = new Contact(id, firstName, lastName, phone, address);
            service.addContact(contact);
            Logger.log("CREATE successful - Contact ID: " + id);
        } catch (Exception e) {
            Logger.error("CREATE failed - Contact ID: " + id, e);
            throw e;
        }
    }

    /**
     * Updates an existing contact
     *
     * @param id Contact ID
     * @param firstName First name
     * @param lastName Last name
     * @param phone Phone number (10 digits)
     * @param address Address (max 30 characters)
     */
    public void updateContact(String id, String firstName, String lastName,
                              String phone, String address) {
        Logger.log("UPDATE request - Contact ID: " + id);

        try {
            Contact contact = new Contact(id, firstName, lastName, phone, address);
            service.updateContact(contact);
            Logger.log("UPDATE successful - Contact ID: " + id);
        } catch (Exception e) {
            Logger.error("UPDATE failed - Contact ID: " + id, e);
            throw e;
        }
    }

    /**
     * Deletes a contact
     *
     * @param id Contact ID to delete
     */
    public void deleteContact(String id) {
        Logger.log("DELETE request - Contact ID: " + id);

        try {
            boolean deleted = service.deleteContact(id);
            if (deleted) {
                Logger.log("DELETE successful - Contact ID: " + id);
            } else {
                Logger.log("DELETE failed - Contact not found: " + id);
            }
        } catch (Exception e) {
            Logger.error("DELETE failed - Contact ID: " + id, e);
            throw e;
        }
    }

    /**
     * Retrieves a contact by ID
     *
     * @param id Contact ID to retrieve
     * @return Contact if found, null otherwise
     */
    public Contact getContact(String id) {
        Logger.log("GET request - Contact ID: " + id);

        try {
            Contact contact = service.getContact(id);
            if (contact != null) {
                Logger.log("GET successful - Contact ID: " + id);
            } else {
                Logger.log("GET failed - Contact not found: " + id);
            }
            return contact;
        } catch (Exception e) {
            Logger.error("GET failed - Contact ID: " + id, e);
            throw e;
        }
    }

    /**
     * Lists all contacts
     *
     * @return List of all contacts
     */
    public List<Contact> listAllContacts() {
        Logger.log("LIST ALL request");

        try {
            List<Contact> contacts = service.getAllContacts();
            Logger.log("LIST ALL successful - Count: " + contacts.size());
            return contacts;
        } catch (Exception e) {
            Logger.error("LIST ALL failed", e);
            throw e;
        }
    }

    /**
     * Checks if a contact exists
     *
     * @param id Contact ID to check
     * @return true if exists, false otherwise
     */
    public boolean checkContactExists(String id) {
        Logger.log("EXISTS check - Contact ID: " + id);

        try {
            boolean exists = service.contactExists(id);
            Logger.log("EXISTS result for " + id + ": " + exists);
            return exists;
        } catch (Exception e) {
            Logger.error("EXISTS check failed - Contact ID: " + id, e);
            return false;
        }
    }

    /**
     * Gets the total count of contacts
     *
     * @return Number of contacts
     */
    public int getContactCount() {
        Logger.log("COUNT request");

        try {
            int count = service.getContactCount();
            Logger.log("COUNT result: " + count);
            return count;
        } catch (Exception e) {
            Logger.error("COUNT failed", e);
            return 0;
        }
    }
}

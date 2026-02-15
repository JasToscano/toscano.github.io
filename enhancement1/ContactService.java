package com.contactservice.service;

import com.contactservice.model.Contact;
import com.contactservice.repository.ContactRepository;
import com.contactservice.repository.InMemoryContactRepository;
import com.contactservice.util.Logger;
import java.util.List;

/**
 * Service layer for Contact operations.
 * Handles business logic and coordinates between controller and repository.
 *
 * Updated for Category 3: Added constructor for dependency injection
 * to support database repository implementation.
 */
public class ContactService {

    private ContactRepository repository;
    private ValidationService validator;

    /**
     * Default constructor - uses in-memory repository
     * Used for Categories 1 & 2 (HashMap, TreeMap, LinkedHashMap)
     */
    public ContactService() {
        this.repository = InMemoryContactRepository.getInstance();
        this.validator = new ValidationService();
        Logger.log("ContactService initialized with in-memory repository");
    }

    /**
     * NEW - Constructor for dependency injection
     * Allows specifying which repository implementation to use
     *
     * Used for Category 3 to inject DatabaseContactRepository
     *
     * @param repository The repository implementation to use
     */
    public ContactService(ContactRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        this.repository = repository;
        this.validator = new ValidationService();
        Logger.log("ContactService initialized with provided repository: "
                + repository.getClass().getSimpleName());
    }

    /**
     * Adds a new contact
     *
     * @param contact Contact to add
     * @throws IllegalArgumentException if contact is invalid or already exists
     */
    public void addContact(Contact contact) {
        Logger.log("Attempting to add contact: " + contact.getContactId());

        // Validate the contact
        validator.validateContact(contact);

        // Check for duplicates
        if (repository.exists(contact.getContactId())) {
            throw new IllegalArgumentException(
                    "Contact already exists with ID: " + contact.getContactId());
        }

        // Save to repository
        repository.save(contact);
        Logger.log("Contact added successfully: " + contact.getContactId());
    }

    /**
     * Updates an existing contact
     *
     * @param contact Contact with updated information
     * @throws IllegalArgumentException if contact is invalid or doesn't exist
     */
    public void updateContact(Contact contact) {
        Logger.log("Attempting to update contact: " + contact.getContactId());

        // Validate the contact
        validator.validateContact(contact);

        // Check if contact exists
        if (!repository.exists(contact.getContactId())) {
            throw new IllegalArgumentException(
                    "Contact not found with ID: " + contact.getContactId());
        }

        // Update in repository
        repository.save(contact);
        Logger.log("Contact updated successfully: " + contact.getContactId());
    }

    /**
     * Deletes a contact by ID
     *
     * @param contactId ID of contact to delete
     * @return true if contact was deleted, false if not found
     */
    public boolean deleteContact(String contactId) {
        Logger.log("Attempting to delete contact: " + contactId);

        if (contactId == null || contactId.isEmpty()) {
            throw new IllegalArgumentException("Contact ID cannot be null or empty");
        }

        boolean deleted = repository.delete(contactId);

        if (deleted) {
            Logger.log("Contact deleted successfully: " + contactId);
        } else {
            Logger.log("Contact not found for deletion: " + contactId);
        }

        return deleted;
    }

    /**
     * Retrieves a contact by ID
     *
     * @param contactId ID of contact to retrieve
     * @return Contact if found, null otherwise
     */
    public Contact getContact(String contactId) {
        Logger.log("Retrieving contact: " + contactId);

        if (contactId == null || contactId.isEmpty()) {
            throw new IllegalArgumentException("Contact ID cannot be null or empty");
        }

        Contact contact = repository.findById(contactId);

        if (contact != null) {
            Logger.log("Contact found: " + contactId);
        } else {
            Logger.log("Contact not found: " + contactId);
        }

        return contact;
    }

    /**
     * Retrieves all contacts
     *
     * @return List of all contacts
     */
    public List<Contact> getAllContacts() {
        Logger.log("Retrieving all contacts");
        List<Contact> contacts = repository.findAll();
        Logger.log("Retrieved " + contacts.size() + " contacts");
        return contacts;
    }

    /**
     * Checks if a contact exists
     *
     * @param contactId ID to check
     * @return true if contact exists, false otherwise
     */
    public boolean contactExists(String contactId) {
        if (contactId == null || contactId.isEmpty()) {
            return false;
        }
        return repository.exists(contactId);
    }

    /**
     * Gets the count of contacts in the repository
     *
     * @return Number of contacts
     */
    public int getContactCount() {
        List<Contact> contacts = repository.findAll();
        return contacts.size();
    }
}

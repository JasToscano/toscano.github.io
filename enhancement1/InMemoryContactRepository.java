package com.contactservice.repository;

import com.contactservice.model.Contact;
import com.contactservice.util.Logger;
import java.util.*;

/**
 * Enhanced in-memory implementation with multiple data structures.
 * Category 2 Enhancement: Adds TreeMap for sorted access and LinkedHashMap for caching.
 *
 * Data Structures Used:
 * - HashMap: O(1) lookup by ID (from Category 1)
 * - TreeMap: O(log n) sorted access by last name
 * - LinkedHashMap: O(1) LRU cache with automatic eviction
 */
public class InMemoryContactRepository implements ContactRepository {

    private static InMemoryContactRepository instance;

    // Primary storage: HashMap for O(1) lookup by ID
    private Map<String, Contact> contactsById;

    // NEW: TreeMap for sorted access by last name
    private TreeMap<String, Contact> contactsByName;

    // NEW: LRU Cache using LinkedHashMap (keeps last 50 accessed contacts)
    private LinkedHashMap<String, Contact> recentContactsCache;

    private static final int CACHE_SIZE = 50;

    private InMemoryContactRepository() {
        this.contactsById = new HashMap<>();

        // Initialize TreeMap for automatic sorting by name
        this.contactsByName = new TreeMap<>();

        // Initialize LRU cache with automatic eviction
        this.recentContactsCache = new LinkedHashMap<String, Contact>(
                CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Contact> eldest) {
                return size() > CACHE_SIZE;
            }
        };

        Logger.log("Repository initialized with HashMap, TreeMap, and LRU Cache");
    }

    public static InMemoryContactRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryContactRepository();
        }
        return instance;
    }

    @Override
    public void save(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }

        String id = contact.getContactId();
        String nameKey = contact.getLastName() + ", " + contact.getFirstName();
        boolean isNew = !contactsById.containsKey(id);

        // Save to all data structures
        contactsById.put(id, contact);
        contactsByName.put(nameKey, contact);

        // Invalidate cache entry if updating
        if (!isNew) {
            recentContactsCache.remove(id);
        }

        Logger.log((isNew ? "Created" : "Updated") + " contact: " + id);
    }

    @Override
    public Contact findById(String contactId) {
        if (contactId == null) {
            return null;
        }

        // Check cache first (O(1))
        if (recentContactsCache.containsKey(contactId)) {
            Logger.log("CACHE HIT: " + contactId);
            return recentContactsCache.get(contactId);
        }

        // Cache miss - get from main storage (O(1))
        Logger.log("CACHE MISS: " + contactId);
        Contact contact = contactsById.get(contactId);

        // Add to cache for future access
        if (contact != null) {
            recentContactsCache.put(contactId, contact);
            Logger.log("Added to cache: " + contactId);
        }

        return contact;
    }

    @Override
    public List<Contact> findAll() {
        Logger.log("Retrieving all contacts. Count: " + contactsById.size());
        return new ArrayList<>(contactsById.values());
    }

    /**
     * NEW: Get all contacts sorted by last name, then first name.
     * Uses TreeMap which maintains sorted order automatically.
     * Time complexity: O(n) to create list from sorted map
     */
    public List<Contact> findAllSorted() {
        Logger.log("Retrieving contacts in sorted order");
        return new ArrayList<>(contactsByName.values());
    }

    /**
     * NEW: Find contacts whose last names fall within a specified range.
     * Time complexity: O(log n + k) where k is number of results
     *
     * @param startName Starting last name (inclusive)
     * @param endName Ending last name (inclusive)
     * @return List of contacts in the specified range
     */
    public List<Contact> findInNameRange(String startName, String endName) {
        Logger.log("Range query: " + startName + " to " + endName);

        // Use TreeMap's subMap for efficient range query
        Map<String, Contact> rangeMap = contactsByName.subMap(
                startName, true,  // inclusive start
                endName + Character.MAX_VALUE, true  // inclusive end (add max char to include all)
        );

        return new ArrayList<>(rangeMap.values());
    }

    @Override
    public boolean delete(String contactId) {
        if (contactId == null) {
            return false;
        }

        // Get contact before deletion for name key
        Contact contact = contactsById.remove(contactId);

        if (contact != null) {
            // Remove from all data structures
            String nameKey = contact.getLastName() + ", " + contact.getFirstName();
            contactsByName.remove(nameKey);
            recentContactsCache.remove(contactId);

            Logger.log("Deleted contact: " + contactId);
            return true;
        } else {
            Logger.log("Contact not found for deletion: " + contactId);
            return false;
        }
    }

    @Override
    public boolean exists(String contactId) {
        if (contactId == null) {
            return false;
        }
        return contactsById.containsKey(contactId);
    }

    /**
     * Clears all contacts from all data structures (useful for testing)
     */
    public void clear() {
        contactsById.clear();
        contactsByName.clear();
        recentContactsCache.clear();
        Logger.log("All contacts cleared from all data structures");
    }

    /**
     * Gets the current number of contacts
     */
    public int size() {
        return contactsById.size();
    }

    /**
     * NEW: Get cache statistics for performance analysis
     */
    public String getCacheStats() {
        return String.format("Cache size: %d/%d contacts",
                recentContactsCache.size(), CACHE_SIZE);
    }
}

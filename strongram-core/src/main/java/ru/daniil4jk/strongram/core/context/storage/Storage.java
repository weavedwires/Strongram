package ru.daniil4jk.strongram.core.context.storage;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

/**
 * An interface defining a generic key-value storage mechanism for managing objects in a context.
 * Implementations of this interface provide methods to store, retrieve, and manage objects
 * using string-based keys. The storage supports individual objects as well as collections,
 * with type safety through the use of generics and {@link Class} parameters.
 */
public interface Storage {

    /**
     * Adds an object to the storage with the specified key.
     * If the key already exists, the behavior depends on the implementation—
     * it may add to a collection or replace the existing value.
     *
     * @param key    the key with which the specified object is to be associated
     * @param object the object to be stored
     * @param <T>    the type of the object
     * @throws ClassCastException if the object cannot be cast to the expected type during storage or retrieval in some implementations
     */
    <T> void add(String key, T object);

    /**
     * Sets an object in the storage, associating it with the specified key.
     * If the key already exists, the previous value is replaced.
     *
     * @param key    the key with which the specified object is to be associated
     * @param object the object to be stored
     * @param <T>    the type of the object
     * @throws ClassCastException if the object cannot be cast to the expected type during storage or retrieval in some implementations
     */
    <T> void set(String key, T object);

    /**
     * Retrieves the object associated with the specified key.
     * The caller is responsible for ensuring the correct type is used.
     *
     * @param key the key whose associated object is to be returned
     * @param <T> the expected type of the returned object
     * @return the object associated with the specified key, or {@code null} if not found
     * @throws ClassCastException if the stored object is not assignable to the requested type
     */
    <T> T get(String key);

    /**
     * Retrieves the object associated with the specified key and ensures it is of the given type.
     * This method provides type safety by using the provided {@link Class} object.
     *
     * @param classOfReturnValue the class object representing the expected type of the returned value
     * @param key                the key whose associated object is to be returned
     * @param <T>                the type of the object to be returned
     * @return a non-null collection of objects associated with the key
     * @throws ClassCastException if the stored object is not an instance of the specified class
     */
    <T> T get(Class<T> classOfReturnValue, String key);

    /**
     * Retrieves a collection of objects associated with the specified key.
     * The caller is responsible for ensuring that the elements are of the correct type.
     *
     * @param key the key whose associated collection is to be returned
     * @param <T> the expected type of the elements in the returned collection
     * @return the collection associated with the specified key, or {@code null} if not found
     * @throws ClassCastException if the stored value is not a collection or if elements are not compatible with the expected type
     */
    <T> @NotNull Collection<T> getCollection(String key);

    /**
     * Retrieves a collection of objects associated with the specified key,
     * ensuring that each element in the collection is of the specified type.
     *
     * @param classOfReturnEntryValue the class object representing the expected type of the elements in the collection
     * @param key                     the key whose associated collection is to be returned
     * @param <T>                     the type of the elements in the returned collection
     * @return a non-null collection of objects associated with the key
     * @throws ClassCastException if the stored value is not a collection or if any element is not an instance of the specified class
     */
    <T> @NotNull Collection<T> getCollection(Class<T> classOfReturnEntryValue, String key);

    /**
     * Retrieves a map of all key-value pairs currently stored.
     * The returned map may be a snapshot or a live view, depending on the implementation.
     *
     * @return a map containing all stored keys and their associated values
     */
    Map<String, Object> getAll();
}
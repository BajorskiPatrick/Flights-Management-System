package lot.dao;

import lot.exceptions.dao.DatabaseActionException;

import java.util.List;

/**
 * Generic Data Access Object (DAO) interface that defines common CRUD operations.
 *
 * @param <T> the type of entity this DAO handles
 */
public interface GenericDao<T> {
    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity to find
     * @return the found entity
     * @throws DatabaseActionException if a database error occurs
     */
    T findById(int id) throws DatabaseActionException;

    /**
     * Retrieves all entities of type T.
     *
     * @return a list of all entities
     * @throws DatabaseActionException if a database error occurs
     */
    List<T> findAll() throws DatabaseActionException;

    /**
     * Retrieves all entities IDs.
     *
     * @return a list of all entities IDs
     * @throws DatabaseActionException if a database error occurs
     */
    List<Integer> findAllId() throws DatabaseActionException;

    /**
     * Saves a new entity to the database.
     *
     * @param t the entity to save
     * @return the generated ID of the saved entity
     * @throws DatabaseActionException if a database error occurs
     */
    int save(T t) throws DatabaseActionException;

    /**
     * Updates an existing entity in the database.
     *
     * @param t the entity to update
     * @throws DatabaseActionException if a database error occurs
     */
    void update(T t) throws DatabaseActionException;

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     * @throws DatabaseActionException if a database error occurs
     */
    void delete(int id) throws DatabaseActionException;

    /**
     * Checks if an entity with provided ID exists in the database
     *
     * @param id the ID to check
     * @return true if entity with id exists, false otherwise
     * @throws DatabaseActionException if a database error occurs
     */
    Boolean existsById(int id) throws DatabaseActionException;
}

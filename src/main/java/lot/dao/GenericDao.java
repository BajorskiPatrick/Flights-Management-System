package lot.dao;

import lot.exceptions.dao.DatabaseActionException;

import java.util.List;

public interface GenericDao<T> {
    T findById(int id) throws DatabaseActionException;
    List<T> findAll() throws DatabaseActionException;
    void save(T t) throws DatabaseActionException;
    void update(T t) throws DatabaseActionException;
    void delete(int id) throws DatabaseActionException;
    Boolean existsById(int id) throws DatabaseActionException;
}


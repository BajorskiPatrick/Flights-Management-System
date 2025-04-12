package lot.dao;

import java.util.List;

public interface GenericDao<T> {
    T findById(int id);
    List<T> findAll();
    void save(T t);
    void update(T t);
    void delete(T t);
}


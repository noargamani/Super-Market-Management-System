package DataAcessLayer.DAO;

import DataAcessLayer.DTO.DTO;

import java.sql.SQLException;

public interface DAO {
    void insert(Object d) throws SQLException;
    void delete(DTO d);
    void update(DTO d);
    DTO get(Object primary);

}

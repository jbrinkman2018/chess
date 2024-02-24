package services;
import dataAccess.DataAccessException;
import model.*;

public interface Service {
    public String register(User user) throws DataAccessException;
}

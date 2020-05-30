package ro.ubb.catalog.core.service;

import org.springframework.stereotype.Service;
import ro.ubb.catalog.core.model.User;
@Service
public interface UserService {
  User getUserByUserName(String userName);
}

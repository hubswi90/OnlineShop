package pl.hubswi90.spring.OnlineShop.service;

import pl.hubswi90.spring.OnlineShop.domain.User;

public interface UserService {
    public User findUserByEmail(String email);
    public void saveUser(User user);

    public User findUserById(long id);
    public boolean updateUser(User user);

    public User findUserByQuery(long id);
}

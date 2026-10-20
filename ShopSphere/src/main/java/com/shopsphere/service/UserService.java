package com.shopsphere.service;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.dao.UserDAO;
import com.shopsphere.dao.WishlistDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.User;
import com.shopsphere.model.WishlistItem;

import java.util.List;

/**
 * Service Layer for User authentication, profile and addresses.
 */
public class UserService {

    private final UserDAO userDAO = new UserDAO();
    private final AddressDAO addressDAO = new AddressDAO();
    private final WishlistDAO wishlistDAO = new WishlistDAO();

    public User authenticate(String email, String password) {
        return userDAO.authenticate(email, password);
    }

    public User login(String email, String password) {
        return authenticate(email, password);
    }

    public boolean register(String name, String email, String password, String mobile) {
        if (userDAO.findByEmail(email) != null) {
            return false; // Email already in use
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setMobile(mobile);
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");
        return userDAO.create(user);
    }

    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }

    public boolean updateProfile(User user) {
        return userDAO.update(user);
    }

    public boolean updatePassword(int userId, String newPassword) {
        return userDAO.updatePassword(userId, newPassword);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public boolean updateUserStatus(int userId, String status) {
        return userDAO.updateStatus(userId, status);
    }

    public int getCustomerCount() {
        return userDAO.countUsers();
    }

    public List<Address> getUserAddresses(int userId) {
        return addressDAO.findByUserId(userId);
    }

    public Address getAddressById(int addressId) {
        return addressDAO.findById(addressId);
    }

    public boolean addAddress(Address address) {
        return addressDAO.create(address);
    }

    public boolean deleteAddress(int addressId, int userId) {
        return addressDAO.delete(addressId, userId);
    }

    public List<WishlistItem> getUserWishlist(int userId) {
        return wishlistDAO.getWishlistByUser(userId);
    }

    public boolean addToWishlist(int userId, int productId) {
        return wishlistDAO.addToWishlist(userId, productId);
    }

    public boolean removeFromWishlist(int userId, int productId) {
        return wishlistDAO.removeFromWishlist(userId, productId);
    }

    public boolean isInWishlist(int userId, int productId) {
        return wishlistDAO.isInWishlist(userId, productId);
    }
}

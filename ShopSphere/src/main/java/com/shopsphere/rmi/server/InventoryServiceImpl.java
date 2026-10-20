package com.shopsphere.rmi.server;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import com.shopsphere.rmi.remote.InventoryService;
import com.shopsphere.util.DBConnection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Remote Implementation of InventoryService.
 * Extends UnicastRemoteObject to enable remote method invocations.
 */
public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(InventoryServiceImpl.class.getName());

    private final ProductDAO productDAO;

    public InventoryServiceImpl() throws RemoteException {
        super();
        this.productDAO = new ProductDAO();
    }

    @Override
    public Product getProduct(int id) throws RemoteException {
        LOGGER.info("RMI Call: getProduct(" + id + ")");
        return productDAO.findById(id);
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        LOGGER.info("RMI Call: getStock(" + productId + ")");
        return productDAO.getStock(productId);
    }

    @Override
    public boolean updateStock(int productId, int quantity) throws RemoteException {
        LOGGER.info("RMI Call: updateStock(" + productId + ", " + quantity + ")");
        return productDAO.updateStock(productId, quantity);
    }

    @Override
    public List<Product> getAllProducts() throws RemoteException {
        LOGGER.info("RMI Call: getAllProducts()");
        return productDAO.findAll();
    }

    @Override
    public String getServerStatus() throws RemoteException {
        return "ShopSphere RMI Inventory Service running at " + new Date() +
               " | Database: " + DBConnection.getActiveDatabaseInfo();
    }
}

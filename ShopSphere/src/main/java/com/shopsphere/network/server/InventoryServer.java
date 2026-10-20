package com.shopsphere.network.server;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import com.shopsphere.network.protocol.NetworkMessage;
import com.shopsphere.util.DBConnection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Multi-threaded Java Socket Inventory Server.
 * Implements RTU Syllabus: java.net.ServerSocket, Socket, Multi-threading, Object Serialization.
 */
public class InventoryServer {
    private static final Logger LOGGER = Logger.getLogger(InventoryServer.class.getName());
    public static final int PORT = 8088;

    private final int port;
    private volatile boolean running = true;
    private final ProductDAO productDAO = new ProductDAO();

    public InventoryServer(int port) {
        this.port = port;
    }

    public void start() {
        LOGGER.info("Starting ShopSphere Inventory Server on port " + port + "...");
        LOGGER.info("Connected to database: " + DBConnection.getActiveDatabaseInfo());

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LOGGER.info("Inventory Server is listening on port " + port + ". Ready for client connections.");

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    LOGGER.info("Accepted connection from " + clientSocket.getRemoteSocketAddress());
                    new Thread(new ClientHandler(clientSocket)).start();
                } catch (IOException e) {
                    if (!running) break;
                    LOGGER.log(Level.WARNING, "Error accepting client connection", e);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not start server on port " + port, e);
        }
    }

    public void stop() {
        this.running = false;
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                while (!socket.isClosed()) {
                    Object received = in.readObject();
                    if (!(received instanceof NetworkMessage)) {
                        continue;
                    }

                    NetworkMessage request = (NetworkMessage) received;
                    NetworkMessage response = processRequest(request);
                    out.writeObject(response);
                    out.flush();
                }
            } catch (java.io.EOFException e) {
                // Client disconnected normally
            } catch (Exception e) {
                LOGGER.log(Level.INFO, "Client disconnected: " + socket.getRemoteSocketAddress());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }

        private NetworkMessage processRequest(NetworkMessage request) {
            NetworkMessage response = new NetworkMessage(NetworkMessage.Type.RESPONSE_SUCCESS);

            switch (request.getType()) {
                case PING:
                    response.setMessage("PONG: ShopSphere Server Active");
                    break;

                case GET_ALL:
                    List<Product> allProducts = productDAO.findAll();
                    response.setPayload(allProducts);
                    response.setMessage("Retrieved " + allProducts.size() + " products.");
                    break;

                case GET_PRODUCT:
                    Product p = productDAO.findById(request.getProductId());
                    if (p != null) {
                        response.setPayload(p);
                        response.setMessage("Product found.");
                    } else {
                        response.setType(NetworkMessage.Type.RESPONSE_ERROR);
                        response.setMessage("Product ID " + request.getProductId() + " not found.");
                    }
                    break;

                case GET_STOCK:
                    int stock = productDAO.getStock(request.getProductId());
                    response.setQuantity(stock);
                    response.setMessage("Stock level: " + stock);
                    break;

                case UPDATE_STOCK:
                    boolean updated = productDAO.updateStock(request.getProductId(), request.getQuantity());
                    if (updated) {
                        response.setMessage("Stock updated by " + request.getQuantity() + " units.");
                    } else {
                        response.setType(NetworkMessage.Type.RESPONSE_ERROR);
                        response.setMessage("Failed to update stock (insufficient inventory or invalid ID).");
                    }
                    break;

                case SEARCH:
                    List<Product> searchResults = productDAO.search(request.getQuery(), null, null, null, null);
                    response.setPayload(searchResults);
                    response.setMessage("Found " + searchResults.size() + " matching products.");
                    break;

                default:
                    response.setType(NetworkMessage.Type.RESPONSE_ERROR);
                    response.setMessage("Unknown command type: " + request.getType());
            }
            return response;
        }
    }

    public static void main(String[] args) {
        new InventoryServer(PORT).start();
    }
}

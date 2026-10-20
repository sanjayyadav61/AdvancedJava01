package com.shopsphere.network.protocol;

import java.io.Serializable;

/**
 * Message protocol envelope transferred over ObjectOutputStream and ObjectInputStream.
 * Demonstrates RTU Syllabus: Object Serialization over java.net Sockets.
 */
public class NetworkMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Type {
        GET_ALL,
        GET_PRODUCT,
        GET_STOCK,
        UPDATE_STOCK,
        SEARCH,
        PING,
        RESPONSE_SUCCESS,
        RESPONSE_ERROR
    }

    private Type type;
    private String query;
    private int productId;
    private int quantity;
    private String message;
    private Object payload; // e.g. Product or List<Product>

    public NetworkMessage() {}

    public NetworkMessage(Type type) {
        this.type = type;
    }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getPayload() { return payload; }
    public void setPayload(Object payload) { this.payload = payload; }

    @Override
    public String toString() {
        return "NetworkMessage{" +
                "type=" + type +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                '}';
    }
}

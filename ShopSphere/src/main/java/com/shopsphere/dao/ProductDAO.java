package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Product entity.
 * Used by Servlets, Swing Admin, Sockets, and RMI inventory modules.
 */
public class ProductDAO {

    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id ORDER BY p.product_id ASC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return list;
    }

    public List<Product> findActive() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.status = TRUE ORDER BY p.product_id ASC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return list;
    }

    public List<Product> findFeatured(int limit) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.status = TRUE ORDER BY p.discount DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    public Product findById(int id) {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id WHERE p.product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return extractProductFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public List<Product> findByCategory(int categoryId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.status = TRUE AND p.category_id = ? ORDER BY p.product_id ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    public List<Product> search(String keyword) {
        return search(keyword, null, null, null, "newest");
    }

    /**
     * Search and Filter query using PreparedStatement parameters.
     */
    public List<Product> search(String keyword, Integer categoryId, Double minPrice, Double maxPrice, String sortBy) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT p.*, c.name AS category_name FROM products p ")
                .append("LEFT JOIN categories c ON p.category_id = c.category_id WHERE p.status = TRUE ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.brand) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String term = "%" + keyword.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (categoryId != null && categoryId > 0) {
            sql.append("AND p.category_id = ? ");
            params.add(categoryId);
        }

        if (minPrice != null && minPrice >= 0) {
            sql.append("AND p.price >= ? ");
            params.add(minPrice);
        }

        if (maxPrice != null && maxPrice > 0) {
            sql.append("AND p.price <= ? ");
            params.add(maxPrice);
        }

        if ("price_asc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.price ASC");
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.price DESC");
        } else if ("discount".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.discount DESC");
        } else {
            sql.append("ORDER BY p.product_id ASC");
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    public boolean create(Product product) {
        String sql = "INSERT INTO products (category_id, name, brand, description, price, discount, stock, image_url, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setDouble(5, product.getPrice());
            ps.setDouble(6, product.getDiscount());
            ps.setInt(7, product.getStock());
            ps.setString(8, product.getImageUrl());
            ps.setBoolean(9, product.isStatus());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    product.setProductId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    public boolean update(Product product) {
        String sql = "UPDATE products SET category_id = ?, name = ?, brand = ?, description = ?, " +
                     "price = ?, discount = ?, stock = ?, image_url = ?, status = ? WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setDouble(5, product.getPrice());
            ps.setDouble(6, product.getDiscount());
            ps.setInt(7, product.getStock());
            ps.setString(8, product.getImageUrl());
            ps.setBoolean(9, product.isStatus());
            ps.setInt(10, product.getProductId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, null);
        }
        return false;
    }

    public boolean updateStock(int productId, int quantityChange) {
        String sql = "UPDATE products SET stock = stock + ? WHERE product_id = ? AND (stock + ?) >= 0";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, quantityChange);
            ps.setInt(2, productId);
            ps.setInt(3, quantityChange);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, null);
        }
        return false;
    }

    public boolean setStock(int productId, int newStock) {
        String sql = "UPDATE products SET stock = ? WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, null);
        }
        return false;
    }

    public int getStock(int productId) {
        String sql = "SELECT stock FROM products WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, null);
        }
        return false;
    }

    public List<Product> getLowStockProducts(int threshold) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.stock <= ? ORDER BY p.stock ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, threshold);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    public int countProducts() {
        String sql = "SELECT COUNT(*) FROM products";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return 0;
    }

    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setDiscount(rs.getDouble("discount"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setStatus(rs.getBoolean("status"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            p.setCategoryName(rs.getString("category_name"));
        } catch (SQLException ignored) {}
        return p;
    }
}

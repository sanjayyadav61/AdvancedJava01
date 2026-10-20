package com.shopsphere.model;

import java.io.Serializable;

/**
 * Category model for catalog organization.
 */
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private int categoryId;
    private String name;
    private String description;
    private String iconClass;
    private boolean status;

    public Category() {}

    public Category(int categoryId, String name, String description, String iconClass) {
        this(categoryId, name, description, iconClass, true);
    }

    public Category(int categoryId, String name, String description, String iconClass, boolean status) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.iconClass = iconClass;
        this.status = status;
    }

    public String getImageUrl() { return iconClass; }
    public void setImageUrl(String imageUrl) { this.iconClass = imageUrl; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconClass() { return iconClass; }
    public void setIconClass(String iconClass) { this.iconClass = iconClass; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    @Override
    public String toString() {
        return name;
    }
}

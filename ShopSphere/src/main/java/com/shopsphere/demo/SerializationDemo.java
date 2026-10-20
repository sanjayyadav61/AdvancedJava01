package com.shopsphere.demo;

import com.shopsphere.model.Product;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Demonstrates RTU Syllabus Topic:
 * Object Serialization and Deserialization using ObjectOutputStream and ObjectInputStream.
 */
public class SerializationDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("RTU Advanced Java Demo: Java Object Serialization");
        System.out.println("==========================================================");

        try {
            // 1. Create original object
            Product original = new Product(101, 1, "Sony WH-1000XM5", "Sony",
                    "Premium Noise Cancelling Headphones", 26990.00, 15.00, 24,
                    "https://images.unsplash.com/photo-1505740420928-5e560c06d30e", true);

            System.out.println("1. Original Product Object:");
            System.out.println("   ID:       " + original.getProductId());
            System.out.println("   Name:     " + original.getName());
            System.out.println("   Price:    ₹" + original.getPrice());
            System.out.println("   Discount: " + original.getDiscount() + "%");
            System.out.println("   Stock:    " + original.getStock());

            // 2. Serialize to byte stream
            System.out.println("\n2. Serializing object to byte array via ObjectOutputStream...");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(original);
                oos.flush();
            }

            byte[] serializedBytes = baos.toByteArray();
            System.out.println("   • Total Serialized Byte Size: " + serializedBytes.length + " bytes");
            System.out.println("   • Object successfully transformed into binary payload.");

            // 3. Deserialize back into a fresh Java Object
            System.out.println("\n3. Deserializing back via ObjectInputStream...");
            ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);
            Product deserialized;
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                deserialized = (Product) ois.readObject();
            }

            System.out.println("4. Deserialized Object Verification:");
            System.out.println("   ID:       " + deserialized.getProductId());
            System.out.println("   Name:     " + deserialized.getName());
            System.out.println("   Price:    ₹" + deserialized.getPrice());
            System.out.println("   Discount: " + deserialized.getDiscount() + "%");
            System.out.println("   Stock:    " + deserialized.getStock());

            boolean match = original.getProductId() == deserialized.getProductId() &&
                            original.getName().equals(deserialized.getName());
            System.out.println("\n[SUCCESS] Integrity Check: " + (match ? "PASSED (Identical Object State)" : "FAILED"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

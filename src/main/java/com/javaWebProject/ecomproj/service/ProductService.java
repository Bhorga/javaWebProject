package com.javaWebProject.ecomproj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.javaWebProject.ecomproj.model.Product;
import com.javaWebProject.ecomproj.repo.ProductRepository;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProduct(int id) {
        return repo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        if (imageFile.getSize() > 4 * 1024 * 1024) {
            throw new IllegalArgumentException("Image size exceeds 4 MB.");
        }

        // Populate product fields
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes()); // Store the image as binary data in the database

        return repo.save(product);
    }

    public Product updateProduct(int id, Product updatedProduct, MultipartFile imageFile) throws IOException {
        // Fetch the existing product
        Product existingProduct = repo.findById(id).orElse(null);
    
        if (existingProduct == null) {
            return null; // Return null if product is not found
        }
    
        // Update the fields of the existing product with the new values
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setBrand(updatedProduct.getBrand());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setReleaseDate(updatedProduct.getReleaseDate());
        existingProduct.setProductAvailable(updatedProduct.isProductAvailable());
        existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
    
        // If a new image is provided, update the image fields
        if (imageFile != null && !imageFile.isEmpty()) {
            existingProduct.setImageName(imageFile.getOriginalFilename());
            existingProduct.setImageType(imageFile.getContentType());
            existingProduct.setImageData(imageFile.getBytes());
        }
    
        // Save and return the updated product
        return repo.save(existingProduct);
    }
    

    public void deleteProduct(int id) {
        repo.deleteById(id);
    }


    // public List<Product> searchProducts(String keyword) {
    //     return repo.searchProducts(keyword);
    // }
}

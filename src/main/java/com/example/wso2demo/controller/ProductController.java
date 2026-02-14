package com.example.wso2demo.controller;

import com.example.wso2demo.model.ApiResponse;
import com.example.wso2demo.model.Product;
import com.example.wso2demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Product management APIs with support for headers and query parameters")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    @Operation(
        summary = "Get all products with optional filtering",
        description = "Retrieve products with optional filters: category, brand, minPrice, maxPrice, search. Supports custom headers."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts(
            @Parameter(description = "Filter by category") @RequestParam(required = false) String category,
            @Parameter(description = "Filter by brand") @RequestParam(required = false) String brand,
            @Parameter(description = "Minimum price") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Maximum price") @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Search in product name") @RequestParam(required = false) String search,
            @Parameter(description = "Client ID for tracking") @RequestHeader(value = "X-Client-ID", required = false) String clientId,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId,
            @Parameter(description = "API version") @RequestHeader(value = "X-API-Version", required = false, defaultValue = "1.0") String apiVersion
    ) {
        List<Product> products = productService.getFilteredProducts(category, brand, minPrice, maxPrice, search);
        
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(products.size()))
                .header("X-API-Version", apiVersion)
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(products, "Products retrieved successfully"));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    public ResponseEntity<ApiResponse<Product>> getProductById(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok()
                        .header("X-Request-ID", requestId != null ? requestId : "N/A")
                        .body(ApiResponse.success(product, "Product found")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Product not found with id: " + id)));
    }
    
    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get product by SKU", description = "Retrieve a product by its SKU code")
    public ResponseEntity<ApiResponse<Product>> getProductBySku(
            @Parameter(description = "Product SKU", required = true) @PathVariable String sku,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        return productService.getProductBySku(sku)
                .map(product -> ResponseEntity.ok()
                        .header("X-Request-ID", requestId != null ? requestId : "N/A")
                        .body(ApiResponse.success(product, "Product found")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Product not found with SKU: " + sku)));
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Retrieve all products in a specific category")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(
            @Parameter(description = "Category name", required = true) @PathVariable String category,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(products.size()))
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(products, "Products retrieved for category: " + category));
    }
    
    @GetMapping("/brand/{brand}")
    @Operation(summary = "Get products by brand", description = "Retrieve all products of a specific brand")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByBrand(
            @Parameter(description = "Brand name", required = true) @PathVariable String brand,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        List<Product> products = productService.getProductsByBrand(brand);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(products.size()))
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(products, "Products retrieved for brand: " + brand));
    }
    
    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Retrieve a list of all product categories")
    public ResponseEntity<ApiResponse<List<String>>> getAllCategories(
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        List<String> categories = productService.getAllCategories();
        return ResponseEntity.ok()
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(categories, "Categories retrieved successfully"));
    }
    
    @GetMapping("/brands")
    @Operation(summary = "Get all brands", description = "Retrieve a list of all product brands")
    public ResponseEntity<ApiResponse<List<String>>> getAllBrands(
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        List<String> brands = productService.getAllBrands();
        return ResponseEntity.ok()
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(brands, "Brands retrieved successfully"));
    }
    
    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products", description = "Retrieve products with stock below threshold")
    public ResponseEntity<ApiResponse<List<Product>>> getLowStockProducts(
            @Parameter(description = "Stock threshold (default: 10)") @RequestParam(defaultValue = "10") Integer threshold,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        List<Product> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(products.size()))
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(products, "Low stock products retrieved"));
    }
    
    @PostMapping
    @Operation(summary = "Create a new product", description = "Add a new product to the catalog")
    public ResponseEntity<ApiResponse<Product>> createProduct(
            @Valid @RequestBody Product product,
            @Parameter(description = "Client ID") @RequestHeader(value = "X-Client-ID", required = false) String clientId,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .header("Location", "/api/v1/products/" + createdProduct.getId())
                .body(ApiResponse.success(createdProduct, "Product created successfully"));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update an existing product by ID")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id,
            @Valid @RequestBody Product product,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        return productService.updateProduct(id, product)
                .map(updatedProduct -> ResponseEntity.ok()
                        .header("X-Request-ID", requestId != null ? requestId : "N/A")
                        .body(ApiResponse.success(updatedProduct, "Product updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Product not found with id: " + id)));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Remove a product from the catalog")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.ok()
                    .header("X-Request-ID", requestId != null ? requestId : "N/A")
                    .body(ApiResponse.success(null, "Product deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Product not found with id: " + id));
    }
}

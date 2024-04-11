package in.zeta.ecom.controller;

import in.zeta.ecom.entity.Product;
import in.zeta.ecom.entity.ProductResponse;
import in.zeta.ecom.services.OrderInfoService;
import in.zeta.ecom.services.ProductInfoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
public class ProductsController {
    private ProductInfoServices productInfoServices;

    private OrderInfoService orderInfoService;


    @Autowired
    public ProductsController(ProductInfoServices productInfoServices, OrderInfoService orderInfoService) {
        this.productInfoServices = productInfoServices;
        this.orderInfoService=orderInfoService;
    }

    @GetMapping("/products/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productInfoServices.getAllProducts());
    }

    @GetMapping("/products/product-category")
    public ResponseEntity<List<Product>> getAllProductsUnderProductCategory(@RequestParam("productCategoryId") String id) {
        return ResponseEntity.ok(productInfoServices.getAllProductsUnderProductCategoryId(id));
    }

    @GetMapping("/products/trending")
    public ResponseEntity<Map<String, Integer>> getAllTrendingProducts() {
        return ResponseEntity.ok(orderInfoService.getTrendingProducts());
    }

    @GetMapping("/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam("id") String id) {
        return ResponseEntity.ok(productInfoServices.getSpecificProduct(id));
    }


    @GetMapping("/products/low-stock")
    public ResponseEntity<List<Product>> getAllLowStockProducts(@RequestParam("amount") String num) {
        int amount = -1;
        if (num != null) amount = Integer.parseInt(num);
        return ResponseEntity.ok(productInfoServices.getLowStockProductsByNumber(amount));
    }

    @PostMapping("/products/new")
    public String addNewProduct(@RequestBody Map<String, String> request) {
        return productInfoServices.saveNewProduct(request);
    }

    @PutMapping("/products/update")
    public String updateNewProduct(@RequestBody Map<String, String> request) {
        return productInfoServices.saveNewProduct(request);
    }

    @DeleteMapping("/products/delete")
    public String deleteProduct(@RequestBody Map<String, String> request) {
        return productInfoServices.deleteProduct(request);
    }
}

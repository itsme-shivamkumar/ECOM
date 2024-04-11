package in.zeta.ecom.controller;

import in.zeta.ecom.entity.ProductCategoriesResponse;
import in.zeta.ecom.services.ProductInfoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CategoriesController {
    private ProductInfoServices productInfoServices;

    @Autowired
    public CategoriesController(ProductInfoServices productInfoServices) {
        this.productInfoServices = productInfoServices;
    }


    @GetMapping("/product-categories")
    public ResponseEntity<List<ProductCategoriesResponse>> getAllProductCategories() {
        return ResponseEntity.ok(productInfoServices.getAllProductCategories());
    }

    @PostMapping("/product-categories/new")
    public ResponseEntity<String> saveProductCategory(@RequestBody Map<String, String> requestMap) {
        return ResponseEntity.ok(productInfoServices.saveProductCategory(requestMap.get("productCategoryName")));
    }

    @PutMapping("/product-categories/update")
    public ResponseEntity<String> updateProductCategory(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(productInfoServices.updateProductCategory(request));
    }

    @DeleteMapping("/product-categories/delete")
    public ResponseEntity<String> deleteProductCategory(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(productInfoServices.deleteProductCategory(request));
    }
}

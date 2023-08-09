package com.ecommerce.micrommerce.web.controller;

import com.ecommerce.micrommerce.web.dao.ProductDao;
import com.ecommerce.micrommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.micrommerce.web.exceptions.ProduitIntrouvableException;
import com.ecommerce.micrommerce.web.model.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("API pour les opérations CRUD sur les produits.")
@RestController
public class ProductController {

	private final ProductDao productDao;

	public ProductController(ProductDao productDao) {
		this.productDao = productDao;
	}

	@ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
	@GetMapping(value = "/Produits/{id}")
	public Product afficherUnProduit(@PathVariable int id) {
		Product produit = productDao.findById(id);
		if (produit == null)
			throw new ProduitIntrouvableException(
					"Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");
		return produit;
	}

	// Récupérer la liste des produits
	@GetMapping("/Produits")
	public List<Product> listeProduits() {
		return productDao.findAll();
	}
	
	@PostMapping(value = "/Produits")
	public ResponseEntity<Product> ajouterProduit(@RequestBody @Valid Product product) {
		if (product.getPrix() <= 0) {
			throw new ProduitGratuitException("Ce produit a un prix null");
	}

		Product productAdded = productDao.save(product);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(productAdded.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping(value = "/Produits")
	public void updateProduit(@RequestBody Product product) {
		productDao.save(product);
	}
	
	@DeleteMapping(value = "/Produits/{id}")
	public void supprimerProduit(@PathVariable int id) {
		productDao.deleteById(id);
	}

	@GetMapping(value = "test/produits/{prixLimit}")
	public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
		return productDao.findByPrixGreaterThan(prixLimit);
	}


	@GetMapping("/AdminProduits")
	public Map<Product, Double> calculerMargeProduit() {
		List<Product> products = productDao.findAll();
		Map<Product, Double> marges = new HashMap<>();

		for (Product pr : products) {
			marges.put(pr, (double) (pr.getPrix() - pr.getPrixAchat()));
		}

		return marges;
	}

	@GetMapping("/AdminProduitsTrier")
	public List<Product> trierProduitsParOrdreAlphabetique() {
		return productDao.findNomOrderByAlphabetical();
	}
}

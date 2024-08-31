package app.core.services;

import android.annotation.SuppressLint;
import app.core.entitymodels.ProductCategory;

import java.util.HashMap;
import java.util.Map;
import linq.ArrayList;

public class ProductManager {

	ArrayList<ProductCategory> Products = null;
	@SuppressLint("UseSparseArrays")
	Map<Integer, ArrayList<ProductCategory>> map = null;

	public ProductManager(ArrayList<ProductCategory> Products) {
		setProductsData(Products);
	}

	public ProductManager() {
		Initilize();
	}

	public void refreshProducts() {
		this.Products = null;
		this.map = null;
	}

	@SuppressLint("UseSparseArrays")
	private void Initilize() {
		this.Products = new ArrayList<ProductCategory>();
		this.map = new HashMap<Integer, ArrayList<ProductCategory>>();
	}

	public ProductManager setProductsData(ArrayList<ProductCategory> Products) {
		this.Products = Products;
		return this;
	}

	private void addProducts(ArrayList<ProductCategory> products, int level) {
		if (!map.containsKey(level))
			map.put(level, products);
	}

	public ArrayList<ProductCategory> getProducts(int level) {
		if (map.containsKey(level))
			return map.get(level);
		else {
			this.switchLevel(level);
			return map.get(level);
		}
	}

	public ArrayList<ProductCategory> getProducts(int level, String ParentID) {
		if (map.containsKey(level)) {
			if (ParentID != null)
				return map.get(level).where("ParentID", ParentID);
			else
				return map.get(level);
		} else {
			this.switchLevel(level);
			if (ParentID != null)
				return map.get(level).where("ParentID", ParentID);
			else
				return map.get(level);
		}
	}

	private void switchLevel(int level) {
		switch (level) {
		case 1:
			this.setLevel_1();
			break;
		case 2:
			this.setLevel_2();
			break;
		case 3:
			this.setLevel_3();
			break;
		case 4:
			this.setLevel_4();
			break;
		case 5:
			this.setLevel_5();
			break;
		case 6:
			this.setLevel_6();
			break;
		}

	}

	private void setLevel_1() {
		if(map.containsKey(1))
			return;
		ProductCategory pc = this.Products.First();
		addProducts(this.Products.where("ParentID", pc.ID), 1);
	}

	private void setLevel_2() {
		setLevel_1();
		setLevelProducts(1, 2);
	}

	private void setLevel_3() {
		setLevel_2();
		setLevelProducts(2, 3);
	}

	private void setLevel_4() {
		setLevel_3();
		setLevelProducts(3, 4);
	}

	private void setLevel_5() {
		setLevel_4();
		setLevelProducts(4, 5);
	}

	private void setLevel_6() {
		setLevel_5();
		setLevelProducts(5, 6);
	}

	private void setLevelProducts(int previouslevel, int currentlevel) {
		ArrayList<ProductCategory> products = new ArrayList<ProductCategory>();
		for (ProductCategory pc : this.map.get(previouslevel)) {
			ArrayList<ProductCategory> hold = Products.where("ParentID", pc.ID);
			products.addAll(hold);
			Products.removeAll(hold);
		}
		addProducts(products, currentlevel);
	}

}

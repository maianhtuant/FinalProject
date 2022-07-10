package edu.sdccd.cisc191.c;

import java.io.Serializable;
import edu.sdccd.cisc191.c.Material;
public class MaterialDetail implements Serializable {
	private Material material;
	private double price;
	private String color;
	private int quality;
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public Material getMaterial() {
		return material;
	}
	public double getPrice() {
		return price;
	}
	public String getColor() {
		return color;
	}
	public int getQuality() {
		return quality;
	}
	public String getPriceS() {
		String r = String.format("%.1f", getPrice());
		return r;
	}
	
	public String getQualityS() {
		String r = String.format("%d", getQuality());
		return r;
	}
	
	public MaterialDetail() {
		// TODO Auto-generated constructor stub
	}
	public MaterialDetail(Material _m, double _price, String _color, int _quality) {
		// TODO Auto-generated constructor stub
		this.material = _m;
		this.price = _price;
		this.color = _color;
		this.quality = _quality;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String returnString = String.format(getMaterial().toString() + "\t Price: %1.2f - Color: %s - Quality: %d", getPrice(), getColor(), getQuality());
		
		return returnString;
	}
	
	
	public static void main(String[] args) {
		Material m = new Material("S1001", "trocar");
		MaterialDetail item = new MaterialDetail(m, 20.5, "yellow", 1000);
		System.out.println(item.getPriceS());
	}
	
}

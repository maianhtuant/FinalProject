package edu.sdccd.cisc191.c;

import java.io.Serializable;

import javax.persistence.*;


public class MaterialDetail extends Material implements Serializable {

	private double price;

	private String color;

	private int quality;
	
	
	public void setPrice(double price) {
		this.price = price;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void setQuality(int quality) {
		this.quality = quality;
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
	public MaterialDetail(String id, String name, double _price, String _color, int _quality) {
		// TODO Auto-generated constructor stub
		super(id, name);
		this.price = _price;
		this.color = _color;
		this.quality = _quality;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String returnString = String.format(getName() + "\t Price: %1.2f - Color: %s - Quality: %d", getPrice(), getColor(), getQuality());
		
		return returnString;
	}
	
	
	public static void main(String[] args) {
		MaterialDetail item = new MaterialDetail("S100", "Trocar", 20.5, "yellow", 1000);
		System.out.println(item.getPriceS());
	}
	
}

package edu.sdccd.cisc191.c;
import javax.persistence.*;

import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Material implements Serializable {
	@Id
	@Column(
			name = "ID"
			)
	private String id;
	@Column(
			name = "name",
			nullable = false,
			updatable = true
			)
	private String name;
	
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	public Material() {
		// TODO Auto-generated constructor stub
	}
	public Material(String _id, String _name) {
		// TODO Auto-generated constructor stub
		this.id = _id;
		this.name = _name;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String returnString = String.format("ID: %s - Name: %s", getId(), getName());
		return returnString;
	}
	
	public static void main(String[] args) {
		Material m = new Material("S1001", "trocar");
		System.out.println(m.toString());
	}
	
}

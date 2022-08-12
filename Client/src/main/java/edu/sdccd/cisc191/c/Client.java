package edu.sdccd.cisc191.c;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.Port;

public class Client {
	
	static int port = 4446;
	
	//method insertMaterial with parameters(Material, price, color, quality) for insert new marterial detail to Server.
	public ArrayList<MaterialDetail> insertMaterial(MaterialDetail materialDetail) throws IOException, ClassNotFoundException {
		Socket socket = new Socket("localhost", port);
		ArrayList<MaterialDetail> list = new ArrayList<MaterialDetail>();
		
		DataOutputStream dataToServer = new DataOutputStream(socket.getOutputStream());
		dataToServer.write(101);
		
		ObjectOutputStream objToServer = new ObjectOutputStream(socket.getOutputStream());
		try {
			objToServer.writeObject(materialDetail);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		socket.close();
		return list;
	}
	
	//Method getAllMaterial for get all material in Server.
	public ArrayList<MaterialDetail> getAllMaterial() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", port);
		ArrayList<MaterialDetail> list = new ArrayList<MaterialDetail>();
		DataOutputStream dataToServer = new DataOutputStream(socket.getOutputStream());
		dataToServer.write(102);
		ObjectInputStream objFromServer = new ObjectInputStream(socket.getInputStream());
		try {
			list = new ArrayList<MaterialDetail>();
			list = (ArrayList<MaterialDetail>) objFromServer.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket.close();
		return list;
	}
	
	//Method findMaterial return ArrayList MaterialDetail with parameter text that we can find by name or id.
	public ArrayList<MaterialDetail> findMaterial(String text) throws Exception {
		Socket socket = new Socket("localhost", port);
		
		ArrayList<MaterialDetail> returnList = new ArrayList<MaterialDetail>();
		DataOutputStream dataToServer = new DataOutputStream(socket.getOutputStream());
		
		dataToServer.write(103);
		ObjectOutputStream objToServer = new ObjectOutputStream(socket.getOutputStream());
		objToServer.writeUTF(text);
		objToServer.flush();
		ObjectInputStream objFromServer = new ObjectInputStream(socket.getInputStream());
		returnList = (ArrayList<MaterialDetail>) objFromServer.readObject();
		socket.close();
		return returnList;
		
	}
	
	//Method deleteItem return 0 or 1, 1 is successful and 0 is can't delete. With Parameter index
	public int deleteItem(String getID) throws Exception {
		Socket socket = new Socket("localhost", port);
		DataOutputStream dataToServer = new DataOutputStream(socket.getOutputStream());
		dataToServer.write(100);
		ObjectOutputStream objToServer = new ObjectOutputStream(socket.getOutputStream());
		objToServer.writeUTF(getID);
		objToServer.flush();
		DataInputStream dataFromServer = new DataInputStream(socket.getInputStream());
		int message = dataFromServer.read();
		
		socket.close();
		return message;
	}
	
	public int modifiedItem(MaterialDetail material) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", port);
		int r = 0;
		DataOutputStream dataToServer = new DataOutputStream(socket.getOutputStream());
		dataToServer.write(104);
		ObjectOutputStream objToServer = new ObjectOutputStream(socket.getOutputStream());
		objToServer.writeObject(material);
		
		DataInputStream dataFromServer = new DataInputStream(socket.getInputStream());
		r = dataFromServer.read();
		socket.close();
		
		return r;
	}
	
	
	
	public static void main(String[] args) throws Exception {
		Client client = new Client();
		Scanner scan = new Scanner(System.in);
		
		ArrayList<MaterialDetail> returnList = new ArrayList<MaterialDetail>();
		System.out.println("Input your request: (Write, ReadAll, Delete, Search, Edit or Stop)");
		String request = scan.nextLine();
		while(!request.equals("Stop")) {
			if(request.equals("Write")) {
				returnList = client.insertMaterial(client.writeMaterial());
				for(MaterialDetail c:returnList) {
					System.out.println(c.toString());
				}
			}else if(request.equals("ReadAll")) {
				returnList = client.getAllMaterial();
				for(MaterialDetail c:returnList) {
					System.out.println(c.toString());
				}
			}else if(request.equals("Delete")) {
				System.out.println("input id: ");
				String index = scan.next();
				int mess = 0;
				mess = client.deleteItem(index);
				if(mess == 1) {
					System.out.println("Delete Successful");
				}else {
					System.out.println("Error Input");
				}
			}else if(request.equals("Search")) {
				System.out.println("Input your value: ");
				String text = scan.nextLine();
				returnList = client.findMaterial(text);
				for(MaterialDetail c:returnList) {
					System.out.println(c.toString());
				}
			}else if(request.equals("Edit")) {
				int num = client.modifiedItem(client.writeMaterial());
				if(num==1) {
					System.out.println("Update successfully");
				}else {
					System.out.println("Check your fields");
				}
			}
			System.out.println("Input your next request: (Write, ReadAll, Delete, Search, or Stop)");
			request = scan.nextLine();
		}
		
	}
	
	
	//method writeMaterial for test insertMaterial, it will return MaterialDetail and insert this to insertMaterial parameter
	private MaterialDetail writeMaterial() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Input ID: ");
		String id = scanner.nextLine();
		System.out.println("Input Name: ");
		String name = scanner.nextLine();
		System.out.println("Input price: ");
		double price = Double.parseDouble(scanner.nextLine());
		System.out.println("Input Color: ");
		String color = scanner.nextLine();
		System.out.println("Input quality: ");
		int quality = Integer.parseInt(scanner.nextLine());
		
		MaterialDetail detail = new MaterialDetail(id, name, price, color, quality);
		return detail;
	}
}

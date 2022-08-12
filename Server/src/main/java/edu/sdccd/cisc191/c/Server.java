package edu.sdccd.cisc191.c;


import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;



import java.io.*;


/**
 * This program is a server that takes connection requests on
 * the port specified by the constant LISTENING_PORT.  When a
 * connection is opened, the program sends the current time to
 * the connected socket.  The program will continue to receive
 * and process connections until it is killed (by a CONTROL-C,
 * for example).  Note that this server processes each connection
 * as it is received, rather than creating a separate thread
 * to process the connection.
 */

public class Server extends Thread{
    
    static Socket s;
    static ArrayList<MaterialDetail> list = new ArrayList<MaterialDetail>();
    static final String DB_URL = "jdbc:sqlserver://TUANMAI-PC:1433;databaseName=MATERIALMANAGE;encrypt=true;trustServerCertificate=true";
    static final String USER = "sa";
    static final String PASS = "1234567";
    
    
    public static void main(String[] args) throws Exception {
    	Scanner scan = new Scanner(System.in);
    	System.out.println("Please input port: ");
    	int server_port = scan.nextInt();
        ServerSocket ss = new ServerSocket(server_port);
        while(true) {
        	s = ss.accept();
        	System.out.println("connected");

        	DataInputStream dataFromClient = new DataInputStream(s.getInputStream());
        	int readRequest = dataFromClient.read();
        	
        	if(readRequest == 101) {
        		addItem();
        	}else if(readRequest == 102){
        		getAllElement();
        	}else if(readRequest == 103) {
        		searchItem();
        	}else if(readRequest == 100) {
        		deleteItem();
        	}else if(readRequest == 104) {
        		modifiedItem();
        	}
        	
        	
        }
    }
    
    
    //addItem
    //@EventListener(ConnectDB.class)
    public static void addItem() throws Exception {
    	ObjectInputStream objFromClient = new ObjectInputStream(s.getInputStream());
    	MaterialDetail itemMaterial = (MaterialDetail) objFromClient.readObject();
    	//list.add(itemMaterial);
    	//System.out.println(list.get(0).toString());
    	//ObjectOutputStream objToClient = new ObjectOutputStream(s.getOutputStream());
    	//objToClient.writeObject(list);
    	
    	try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
    		
    		String sql = "INSERT INTO MATERIAL VALUES (?,?)";
    		PreparedStatement statement = conn.prepareStatement(sql);
    		statement.setString(1, itemMaterial.getId());
    		statement.setString(2, itemMaterial.getName());
    		statement.execute();
    		String sql2 = "INSERT INTO MATERIAL_DETAIL VALUES (?,?,?,?)";
    		PreparedStatement statement2 = conn.prepareStatement(sql2);
    		statement2.setString(1, itemMaterial.getId());
    		statement2.setString(2, itemMaterial.getColor());
    		statement2.setDouble(3, itemMaterial.getPrice());
    		statement2.setInt(4, itemMaterial.getQuality());
    		
    		statement2.execute();
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	
    	
    	objFromClient.close();
    	//objToClient.close();
    }
    
    public static void modifiedItem() throws Exception {
    	ObjectInputStream objFromClient = new ObjectInputStream(s.getInputStream());
    	MaterialDetail itemMaterial = (MaterialDetail) objFromClient.readObject();
    	//list.add(itemMaterial);
    	//System.out.println(list.get(0).toString());
    	//ObjectOutputStream objToClient = new ObjectOutputStream(s.getOutputStream());
    	//objToClient.writeObject(list);
    	DataOutputStream dataReturn = new DataOutputStream(s.getOutputStream());
    	try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
    		
    		String sql = "UPDATE MATERIAL SET NAME = ? WHERE ID = ?";
    		PreparedStatement statement = conn.prepareStatement(sql);
    		statement.setString(2, itemMaterial.getId());
    		statement.setString(1, itemMaterial.getName());
    		statement.execute();
    		String sql2 = "UPDATE MATERIAL_DETAIL SET COLOR = ?, PRICE = ?, QUALITY = ? WHERE ID = ?";
    		PreparedStatement statement2 = conn.prepareStatement(sql2);
    		statement2.setString(4, itemMaterial.getId());
    		statement2.setString(1, itemMaterial.getColor());
    		statement2.setDouble(2, itemMaterial.getPrice());
    		statement2.setInt(3, itemMaterial.getQuality());
    		statement2.execute();
    		dataReturn.write(1);
    	}catch(Exception e) {
    		dataReturn.write(0);
    		e.printStackTrace();
    	}
    	
    	
    	
    	objFromClient.close();
    	//objToClient.close();
    }
    
    
    
    
    //printAll
    public static void getAllElement() throws IOException {
    	ObjectOutputStream objToClient = new ObjectOutputStream(s.getOutputStream());
    	
    	ArrayList<MaterialDetail> returnList = new ArrayList<MaterialDetail>();
    	
    	try {
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM MATERIAL, MATERIAL_DETAIL WHERE MATERIAL.ID = MATERIAL_DETAIL.ID";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			while(rs.next()) {
				MaterialDetail m = new MaterialDetail();
				System.out.println(rs.getRow());
				m.setId(rs.getString(1));
				m.setName(rs.getString(2));
				m.setColor(rs.getString(5));
				m.setPrice(rs.getDouble(6));
				m.setQuality(rs.getInt(7));
				returnList.add(m);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	objToClient.writeObject(returnList);
    	objToClient.close();
    }
    
    //find item 
    public static void searchItem() throws IOException {
    	ObjectInputStream objFromClient = new ObjectInputStream(s.getInputStream());
    	String text = objFromClient.readUTF();
    	System.out.println(text);
    	ObjectOutputStream objToClient = new ObjectOutputStream(s.getOutputStream());
    	ArrayList<MaterialDetail> returnList = new ArrayList<MaterialDetail>();
    	try {
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM MATERIAL, MATERIAL_DETAIL WHERE MATERIAL.ID = MATERIAL_DETAIL.ID"+
								" AND (MATERIAL.ID = '" + text + "' OR  MATERIAL.NAME = '" + text +"')";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			while(rs.next()) {
				MaterialDetail m = new MaterialDetail();
				System.out.println(rs.getRow());
				m.setId(rs.getString(1));
				m.setName(rs.getString(2));
				m.setColor(rs.getString(5));
				m.setPrice(rs.getDouble(6));
				m.setQuality(rs.getInt(7));
				returnList.add(m);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	
    	objToClient.writeObject(returnList);
    	objToClient.flush();
    	objToClient.close();
    	objFromClient.close();
    }
    //deleteAtIndex
    public static void deleteItem() throws IOException {
    	ObjectInputStream objFromClient = new ObjectInputStream(s.getInputStream());
    	String id = objFromClient.readUTF();

    	DataOutputStream dataReturn = new DataOutputStream(s.getOutputStream());
    	try {
    		
    		
    			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
    			String query1 = "DELETE FROM MATERIAL_DETAIL WHERE MATERIAL_DETAIL.ID = '"+id+"'";
    			String query2 = "DELETE FROM MATERIAL WHERE MATERIAL.ID = '"+ id +"'";
    			
    			Statement statement = conn.createStatement();
    			statement.executeUpdate(query1);
    			statement.executeUpdate(query2);
    			
    		
    		dataReturn.write(1);
    		dataReturn.flush();
    		dataReturn.close();
    	}catch(Exception e) {
    		dataReturn.write(0);
    		dataReturn.flush();
    		dataReturn.close();
    	}
    	
    	objFromClient.close();
    }
    
    
   
    
    
    
    
    
} 

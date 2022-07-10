package edu.sdccd.cisc191.c;


import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import edu.sdccd.cisc191.c.Material;
import edu.sdccd.cisc191.c.MaterialDetail;



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
public class Server {
    
    static Socket s;
    static ArrayList<MaterialDetail> list = new ArrayList<MaterialDetail>();
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
        	}
        	
        	
        }
    }
    
    
    //addItem
    public static void addItem() throws ClassNotFoundException, IOException {
    	ObjectInputStream objFromClient = new ObjectInputStream(s.getInputStream());
    	MaterialDetail itemMaterial = (MaterialDetail) objFromClient.readObject();
    	list.add(itemMaterial);
    	System.out.println(list.get(0).toString());
    	ObjectOutputStream objToClient = new ObjectOutputStream(s.getOutputStream());
    	objToClient.writeObject(list);
    	objFromClient.close();
    	objToClient.close();
    }
    
    //printAll
    public static void getAllElement() throws IOException {
    	ObjectOutputStream objToClient = new ObjectOutputStream(s.getOutputStream());
    	ArrayList<MaterialDetail> returnList = new ArrayList<MaterialDetail>();
    	if(list.size()>0) {
    		for(MaterialDetail c:list) {
    				returnList.add(c);
    		}
    	}
    	objToClient.writeObject(returnList);
    	
    }
    
    //find item 
    public static void searchItem() throws IOException {
    	ObjectInputStream objFromClient = new ObjectInputStream(s.getInputStream());
    	String text = objFromClient.readUTF();
    	System.out.println(text);
    	ObjectOutputStream objToClient = new ObjectOutputStream(s.getOutputStream());
    	ArrayList<MaterialDetail> returnList = new ArrayList<MaterialDetail>();
    	if(list.size()>0) {
    		for(MaterialDetail c:list) {
    			if(c.getMaterial().getId().equals(text) || c.getMaterial().getName().equals(text)) {
    				returnList.add(c);
    			}
    		}
    	}
    	
    	objToClient.writeObject(returnList);
    	objToClient.flush();
    	objToClient.close();
    	objFromClient.close();
    }
    //deleteAtIndex
    public static void deleteItem() throws IOException {
    	ObjectInputStream objFromClient = new ObjectInputStream(s.getInputStream());
    	int index = objFromClient.read();

    	DataOutputStream dataReturn = new DataOutputStream(s.getOutputStream());
    	try {
    		list.remove(index);
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
    
    
   /* public ArrayList<MaterialDetail> listReturn(){
    	Material m1 = new Material("S100", "trocar");
    	MaterialDetail md1 = new MaterialDetail(m1, 12, "yellow", 1000);
    	Material m2 = new Material("S101", "trocar");
    	MaterialDetail md2 = new MaterialDetail(m2, 10, "Green", 1000);
    	Material m3 = new Material("S102", "trocar");
    	MaterialDetail md3 = new MaterialDetail(m3, 12, "Black", 1000);
    	Material m4 = new Material("S103", "trocar");
    	MaterialDetail md4 = new MaterialDetail(m4, 12, "Purple", 1000);
    	
    	
    	list.add(md1);
    	list.add(md2);
    	list.add(md3);
    	list.add(md4);
    	
    	return list;
    }
    */
    
    
    
} 

package Servers;
import java.net.MalformedURLException;
import java.rmi.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//import com.sun.corba.se.org.omg.CORBA.ORB;
//import com.sun.corba;

import Clients.Client;
import DCRS_CORBA.DCRSInterfaceHelper;

import java.io.*;
import java.net.*;
import java.util.*;
import org.omg.*;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
//import org.*;

public class COMPServer {
	static HashMap<String, Integer> semesterDetails;
	static HashMap<String,HashMap<String,Integer>> compData = new HashMap<String,HashMap<String,Integer>>();
	static String repEnroll="";
	static HashMap<String, Integer> valFetch;
	static HashMap<String, Integer> dropFetch;
	static String DataFetch = "";
	static String termList = "";
	static String response="";
	static String student="";
	static String course="";
	static Logger logger = Logger.getLogger(COMPServer.class.getName());
	static private FileHandler fh;
	static private SimpleFormatter sf;


	public static void startCOMPServer() throws  MalformedURLException,InvalidName, NotBoundException, SecurityException, IOException{
		
	    System.out.println("hello-1");
//	    ORB orb = ORB.init(args, null);
		try {
			 String path1="/Users/pulkitwadhwa/eclipse-workspace/RMI/src/Servers/COMP.log";
	    	
	        fh= new FileHandler(path1);
	        sf = new SimpleFormatter();
	            fh.setFormatter(sf);
	            logger.addHandler(fh);
			
			

//	        logger.info("Comp Server registered");
	        
	        inputTerms("fall");
	        inputTerms("summer");
	        inputTerms("winter");

		}
		catch(Exception e) {
			System.out.println("Exception in Comp Server: " + e);
		}
		Runnable task = () -> {
			receive();
		};
		
		Thread thread = new Thread(task);
		
		thread.start();
		
	}


	public static void inputTerms(String term) throws RemoteException{
		
		if(term == "fall") {
			semesterDetails = new HashMap<String, Integer>();
			semesterDetails.put("COMP6431", 10);
			semesterDetails.put("COMP6461", 10);
			semesterDetails.put("COMP6451", 10);
			semesterDetails.put("COMP6481", 10);
			compData.put(term,semesterDetails);

			
		}
		
		if(term == "summer") {
			semesterDetails = new HashMap<String, Integer>();
			semesterDetails.put("COMP6592", 10);
			semesterDetails.put("COMP6593", 10);
			semesterDetails.put("COMP6594", 10);
			semesterDetails.put("COMP6595", 10);
			compData.put(term,semesterDetails);
		}
		
		if(term == "winter") {
			semesterDetails = new HashMap<String, Integer>();
			semesterDetails.put("COMP6641", 10);
			semesterDetails.put("COMP6651", 10);
			semesterDetails.put("COMP6681", 10);
			compData.put(term,semesterDetails);	
			
			
		}
		
		logger.info(compData.toString());
	}
	private static String sendMessage(int serverPort,String msg) {
		DatagramSocket aSocket = null;

		try {

			aSocket = new DatagramSocket();
            String op = msg;
			byte[] message = op.getBytes();

			InetAddress aHost = InetAddress.getByName("localhost");

			DatagramPacket request = new DatagramPacket(message, message.length, aHost, serverPort);

			aSocket.send(request);

			System.out.println("Request message sent from the client to server with port number " + serverPort + " is: "

					+ new String(request.getData()));
			logger.info("Request message sent from the client to server with port number " + serverPort + " is: "

					+ new String(request.getData()));

			byte[] buffer = new byte[1000];

			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

			aSocket.receive(reply);
			DataFetch = new String(reply.getData());
			System.out.println("Reply received from the server with port number " + serverPort + " is: "

					+ new String(reply.getData()));
			logger.info("Request message sent from the client to server with port number " + serverPort + " is: "

					+ new String(request.getData()));

		} catch (SocketException e) {

			System.out.println("Socket: " + e.getMessage());

		} catch (IOException e) {

			e.printStackTrace();

			System.out.println("IO: " + e.getMessage());

		} finally {

			if (aSocket != null)

				aSocket.close();

		}
		return DataFetch;

	}

	private static void receive() {

		DatagramSocket aSocket = null;

		try {

			aSocket = new DatagramSocket(5555);

			System.out.println("Server 5555 Started............");


			logger.info("Server 5555 Started............");

			while (true) {
				byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);

				aSocket.receive(request);
				
				String datareceived=new String (request.getData());
				String indices[] = datareceived.trim().split(",");
//				System.out.println("index length--" + indices.length);
				if(indices.length<3) {
					termList = indices[0];
					System.out.println("ListedTerm  " + termList);
					logger.info("ListedTerm  " + termList);
				}
				else if(indices.length<5) {
					//studId = index[0].trim();
					course = indices[0].trim();
					termList = indices[1].trim();
					System.out.println("studentId" + student);
					System.out.println("courseId" + course);
					System.out.println("listedTerm" + termList);
					
					logger.info("courseId--" + course);
					logger.info("listedTerm--" + termList);
				}
				String operationName = indices[indices.length - 1].trim();
				
				System.out.println("operationName  " + operationName);
				logger.info("operationName  " + operationName);
				if(operationName.trim().equals("List")) {
					HashMap<String, Integer> fetchedDetail = compData.get(termList);
					response = fetchedDetail.toString().trim();
					logger.info(response);
				}
				else if(operationName.trim().equals("enroll")) {
					synchronized(compData) {
					if(compData.containsKey(termList)) {
						valFetch = compData.get(termList);
						if(valFetch.containsKey(course)) {
							System.out.println("capacity: "+valFetch.get(course));
							int total  = valFetch.get(course);
							if(total>0){
								total--;
								valFetch.put(course, total);
								compData.put(termList, valFetch);
								System.out.println("hashDataComp---"+compData);
								logger.info(compData.toString());
								response= "true";
							}
						}
					}
//					response= "false";
					}	
					
				}
				else if(operationName.trim().equals("drop")) {
//					response = getDropped(termList);
					synchronized(compData) {
					Set<String> keySet = compData.keySet();
					Iterator<String> keySetIterator = keySet.iterator();
					while (keySetIterator.hasNext()) {
					 String term = keySetIterator.next();
//					for(String term:compData.keySet()) {
//						System.out.println("soen---"+term);
						dropFetch  =compData.get(term);
						Set<String> keySet1 = dropFetch.keySet();
						Iterator<String> keySetIterator1 = keySet1.iterator();
						while (keySetIterator1.hasNext()) {
							 String subject = keySetIterator1.next();

//							for(String subject:dropFetch.keySet()) {
							if(subject.equals(termList)) {
								int total = dropFetch.get(subject);
								total++;
								dropFetch.put(subject, total);
								compData.put(term, dropFetch);
								logger.info("Successfulluy Dropped"+termList);
								response= "true";
							}
						}
					}

				   }
				}
				buffer = response.getBytes();

				DatagramPacket reply = new DatagramPacket(buffer, buffer.length, request.getAddress(),

						request.getPort());

				aSocket.send(reply);

			}

		} catch (SocketException e) {

			System.out.println("Socket: " + e.getMessage());

		} catch (IOException e) {

			System.out.println("IO: " + e.getMessage());

		} finally {

			if (aSocket != null)

				aSocket.close();

	}

	}

	public static String listCourseAvailability(String semester) {
		List<HashMap<String, Integer>> fetchedList = Arrays.asList(compData.get(semester));
		String availableCourses;
		availableCourses = "COMP" + fetchedList.toString();
		String sendMsgInse=sendMessage(6666,semester+","+"List");
		String sendMsgSoen=sendMessage(7777,semester+","+"List");
		availableCourses= availableCourses + "SOEN" +sendMsgSoen  + "INSE"+ sendMsgInse;
	    System.out.println("result comp " +availableCourses);
	    logger.info("result comp " +availableCourses);
	    return availableCourses;
}
	
	
	public static String getDetails(String courseId,String term) {
		String validCourse = courseId.substring(0,4);
		String message = courseId+","+term+","+"enroll";
		System.out.println("message  "+message);
		if( validCourse.equals("COMP")) {
			synchronized(compData) {
			if(compData.containsKey(term)) {
				valFetch = compData.get(term);
				if(valFetch.containsKey(courseId)) {
//					System.out.println("capacity: "+valFetch.get(courseId));
					int total  = valFetch.get(courseId);
					if(total>0){
						total--;
						valFetch.put(courseId, total);
						compData.put(term, valFetch);
						logger.info(compData.toString());
//						System.out.println("CompData  "+compData);
						return "true";
					}
				}
			 }
		   }
		}
		else if(validCourse.equals("INSE")) {
			repEnroll = sendMessage(6666,message);
			System.out.println("enrolRes ----" +repEnroll);
			return repEnroll.trim();
		}
		else if(validCourse.equals("SOEN")) {
			repEnroll = sendMessage(7777,message);
			System.out.println("enrolRes ----" +repEnroll);
			
			return repEnroll.trim();
		}
		
		
		return repEnroll;
	}
	
	public static String getDropDetails(String courseId) {
		String CourseDrop = "";
		String courseValid = courseId.substring(0,4);		
		String message = courseId+","+"drop";
		System.out.println("common---"+message);
		if(courseValid.equals("COMP")) {
			synchronized(compData) {
			Set<String> keySet = compData.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			while (keySetIterator.hasNext()) {
			 String term = keySetIterator.next();
//			for(String term:compData.keySet()) {
				System.out.println("s---"+term);
				dropFetch  =compData.get(term);
				Set<String> keySet1 = dropFetch.keySet();
				Iterator<String> keySetIterator1 = keySet1.iterator();
				while (keySetIterator1.hasNext()) {
				 String subject = keySetIterator1.next();

//				for(String subject:dropFetch.keySet()) {
					if(subject.equals(courseId)) {
						int total = dropFetch.get(subject);
						total++;
						dropFetch.put(subject, total);
						compData.put(term, dropFetch);
						logger.info(courseId+"is dropped");
						return "true";
					}
				}
			}
		 }
		}	
		else if(courseValid.equals("SOEN")) {
			CourseDrop = sendMessage(7777,message);
			System.out.println("dropC ----" +repEnroll);
			return CourseDrop.trim();
		}
		else if(courseValid.equals("INSE")) {
			CourseDrop = sendMessage(6666,message);
			System.out.println("dropC ----" +repEnroll);
			return CourseDrop.trim();
		}
		
		return "false";
	}

}

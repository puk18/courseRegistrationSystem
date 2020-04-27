package Servers;

import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;

import DCRS_CORBA.DCRSInterfaceHelper;

public class SOENServer {
	
	static HashMap<String, Integer> semesterDetails;
	static HashMap<String,HashMap<String,Integer>> soenData = new HashMap<String,HashMap<String,Integer>>();
	static String repEnroll="";
	static HashMap<String, Integer> valFetch;
	static HashMap<String, Integer> dropFetch;
	static String DataFetch = "";
	static String termList = "";
	static String response="";
	static String student="";
	static String course="";
//	static private FileHandler file;
//	static private SimpleFormatter formatter;
//
//	static Logger logger=Logger.getLogger(SOENServer.class.getName());
	
	static Logger logger = Logger.getLogger(INSEServer.class.getName());
	static private FileHandler fh;
	static private SimpleFormatter sf;




	public static void startSOENServer() throws RemoteException, MalformedURLException, NotBoundException, SecurityException, IOException{
		
		try {
String path1="/Users/pulkitwadhwa/eclipse-workspace/RMI/src/Servers/SOEN.log";
	    	
	        fh= new FileHandler(path1);
	        sf = new SimpleFormatter();
	            fh.setFormatter(sf);
	            logger.addHandler(fh);

//	        logger.info("SOEN Server registered");
	        inputTerms("fall");
	        inputTerms("summer");
	        inputTerms("winter");

	    
		}
		catch(Exception e) {
			System.out.println("Exception in Soen Server: " + e);
		}
		Runnable task = () -> {
			receive();
		};
		
		Thread thread = new Thread(task);
		
		thread.start();
		
	}


	public static void inputTerms(String term) throws RemoteException {
		
		if(term == "fall") {
			semesterDetails = new HashMap<String, Integer>();
			semesterDetails.put("SOEN6431", 10);
			semesterDetails.put("SOEN6461", 10);
			semesterDetails.put("SOEN6451", 10);
			semesterDetails.put("SOEN6481", 10);
			
			soenData.put(term,semesterDetails);
		}
		
		if(term == "summer") {
			semesterDetails = new HashMap<String, Integer>();
			semesterDetails.put("SOEN6592", 10);
			semesterDetails.put("SOEN6593", 10);
			semesterDetails.put("SOEN6594", 10);
			semesterDetails.put("SOEN6595", 10);
			soenData.put(term,semesterDetails);
		}
		
		if(term == "winter") {
			semesterDetails = new HashMap<String, Integer>();
			semesterDetails.put("SOEN6641", 10);
			semesterDetails.put("SOEN6651", 10);
			semesterDetails.put("SOEN6681", 10);
			soenData.put(term,semesterDetails);																																																																						
			
		}
		
	}
	private static String sendMessage(int serverPort,String msg) {
		DatagramSocket aSocket = null;

		try {

			aSocket = new DatagramSocket();
	        String op=msg;
	        
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

			aSocket = new DatagramSocket(7777);

			//byte[] buffer = new byte[1000];

			System.out.println("Server 7777 Started............");

			while (true) {
				byte[] buffer = new byte[1000];

				DatagramPacket request = new DatagramPacket(buffer, buffer.length);

				aSocket.receive(request);
				
				String datareceived=new String (request.getData());		    
			    
				
				String indices[] = datareceived.trim().split(",");
//				System.out.println("index length--" + indices.length);
				if(indices.length<3) {
					termList = indices[0].trim();
					System.out.println("liTerm--" + termList);
				}
				else if(indices.length<5) {
					course = indices[0].trim();
					termList = indices[1].trim();
					//System.out.println("studId--" + studId);
					System.out.println("studentId--" + student);

					System.out.println("courseId--" + course);
					System.out.println("Termlist--" + termList);
				}
				String operationName = indices[indices.length - 1];
				System.out.println("operationName " + operationName);
				if(operationName.equals("List")) {
					HashMap<String, Integer> fetchedDetail = soenData.get(termList);
					response = fetchedDetail.toString().trim();
					logger.info(response);
				}
				else if(operationName.equals("enroll")) {
//					response = getEnrollment(course,termList);
//					response = getEnrollment(course,termList);
					synchronized(soenData) {

					if(soenData.containsKey(termList)) {
						valFetch = soenData.get(termList);
						if(valFetch.containsKey(course)) {
							System.out.println("capacity: "+valFetch.get(course));
							int total  = valFetch.get(course);
							if(total>0){
								total--;
								valFetch.put(course, total);
								soenData.put(termList, valFetch);
								System.out.println("hashDataComp---"+soenData);
								logger.info(soenData.toString());
								response= "true";
							}
						}
					}
					}
//					response= "false";
				}
				else if(operationName.equals("drop")) {
//					response = getDropped(termList);
					synchronized(soenData) {

					Set<String> keySet = soenData.keySet();
					Iterator<String> keySetIterator = keySet.iterator();
					while (keySetIterator.hasNext()) {
					 String term = keySetIterator.next();
//					for(String term:compData.keySet()) {
//						System.out.println("soen---"+term);
						dropFetch  =soenData.get(term);
						Set<String> keySet1 = dropFetch.keySet();
						Iterator<String> keySetIterator1 = keySet1.iterator();
						while (keySetIterator1.hasNext()) {
							 String subject = keySetIterator1.next();

//							for(String subject:dropFetch.keySet()) {
							if(subject.equals(termList)) {
								int total = dropFetch.get(subject);
								total++;
								dropFetch.put(subject, total);
								soenData.put(term, dropFetch);
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
		String availableCourses="";
		HashMap<String, Integer> fetchedList = soenData.get(semester);
		availableCourses = "SOEN" + fetchedList.toString();
		String sendMsgInse=sendMessage(6666,semester+","+"List");
		String sendMsgComp=sendMessage(5555,semester+","+"List");
		availableCourses= availableCourses + "COMP" + sendMsgComp + "INSE"+ sendMsgInse;
	    System.out.println("result soen ----" +availableCourses);
	    logger.info("result SOEN ----" +availableCourses);
	    return availableCourses;
	}	
	public static String getDetails(String courseId, String semester) {
		String validCourse = courseId.substring(0,4);
		String message = courseId+","+semester+","+"enroll";
		System.out.println("message---"+message);
		 if(validCourse.equals("SOEN")) {
				synchronized(soenData) {

			if(soenData.containsKey(semester)) {
//				System.out.println("hasmap data: "+soenData.get(semester));
				valFetch = soenData.get(semester);
				if(valFetch.containsKey(courseId)) {
//					System.out.println("capacity: "+valFetch.get(courseId));
					int total  = valFetch.get(courseId);
					if(total>0){
						total--;
						valFetch.put(courseId, total);
						soenData.put(semester, valFetch);
						logger.info(soenData.toString());
//						System.out.println("SOEN Data---"+soenData);
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
		else if(validCourse.equals("COMP")) {
			repEnroll = sendMessage(5555,message);
			System.out.println("enrolRes ----" +repEnroll);
			return repEnroll.trim();
		}
		
		return repEnroll;
		}

	public static String getDropDetails(String courseId) {
		String courseValid = courseId.substring(0,4);
		String message = courseId+","+"drop";
		System.out.println("common---"+message);
		String CourseDrop = "";
		 if(courseValid.equals("SOEN")) {
				synchronized(soenData) {

			 Set<String> keySet = soenData.keySet();
				Iterator<String> keySetIterator = keySet.iterator();
				while (keySetIterator.hasNext()) {
				 String term = keySetIterator.next();
//				for(String term:compData.keySet()) {
					System.out.println("s---"+term);
					dropFetch  =soenData.get(term);
					Set<String> keySet1 = dropFetch.keySet();
					Iterator<String> keySetIterator1 = keySet1.iterator();
					while (keySetIterator1.hasNext()) {
					 String subject = keySetIterator1.next();

//			for(String term:soenData.keySet()) {
//				System.out.println("s---"+term);
//				dropFetch  =soenData.get(term);
//				for(String subject:dropFetch.keySet()) {
					if(subject.equals(courseId)) {
						int total = dropFetch.get(subject);
						total++;
						dropFetch.put(subject, total);
						soenData.put(term, dropFetch);
						logger.info(courseId+"is dropped");
						return "true";
					}
				}
			}
		  }
		}
		 else if(courseValid.equals("COMP")) {
			CourseDrop = sendMessage(5555,message);
			System.out.println("CourseDrop ----" +CourseDrop);
			return CourseDrop.trim();
		}
		else if(courseValid.equals("INSE")) {
			CourseDrop = sendMessage(6666,message);
			System.out.println("CourseDrop ----" +CourseDrop);
			return CourseDrop.trim();
		}
		
		return "false";
	}

	
}

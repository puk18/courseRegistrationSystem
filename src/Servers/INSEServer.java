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

import Clients.Client;
import DCRS_CORBA.DCRSInterfaceHelper;

public class INSEServer {
	
	static HashMap<String, Integer> semesterDetails;
	static HashMap<String,HashMap<String,Integer>> inseData = new HashMap<String,HashMap<String,Integer>>();
	static String repEnroll="";
	static HashMap<String, Integer> valFetch;
	static HashMap<String, Integer> dropFetch;
	static String DataFetch = "";
	static String termList = "";
	static String response="";
	static String student="";
	static String course="";
	

	static Logger logger = Logger.getLogger(INSEServer.class.getName());
	static private FileHandler fh;
	static private SimpleFormatter sf;

	public static void startINSEServer( ) throws RemoteException, MalformedURLException, NotBoundException, SecurityException, IOException{
		
		try {
			String path1="/Users/pulkitwadhwa/eclipse-workspace/RMI/src/Servers/INSE.log";
	    	
	        fh= new FileHandler(path1);
	        sf = new SimpleFormatter();
	            fh.setFormatter(sf);
	            logger.addHandler(fh);

//			int RMIPort=8760;
//			ServerStart.startRegistry(RMIPort);
//	        DCRSImpl inseImpl = new DCRSImpl();
//	        Naming.rebind("rmi://localhost:" + RMIPort + "/inseServer", inseImpl);
//	        System.out.println("INSE Server registered"); 
//	        logger.info("rmi://localhost:" + RMIPort + "/inseServer");
//	        logger.info("INSE Server registered");
	        
	        inputTerms("fall");
	        inputTerms("summer");
	        inputTerms("winter");
//		    Properties props = new Properties();
//	        props.put("org.omg.CORBA.ORBInitialPort", "1050");
//	        props.put("org.omg.CORBA.ORBInitialHost", "localhost");
//	        props.setProperty("org.omg.CORBA.ORBClass","org.jacorb.orb.ORB");

//	        String[] newArgs = new String[0];
//
//	        ORB orb = ORB.init(args, props);
////		    ORB orb = ORB.init(args, null);// create and initialize the ORB
//			 // get reference to rootpoa & activate the POAManager
//		    System.out.println("hello000");
//
//			    POA rootpoa = (POA)orb.resolve_initial_references("RootPOA");
//			    rootpoa.the_POAManager().activate();
//			 // create servant and register it with the ORB
//			    DCRSImpl DcrsImpl = new DCRSImpl();
//			    DCRSImpl.setORB(orb);
//			    System.out.println("hello");
//			 // get object reference from the servant
//			    org.omg.CORBA.Object ref = rootpoa.servant_to_reference(DcrsImpl);
//			    System.out.println("hello1");
//
//			    // and cast the reference to a CORBA reference
//			    DCRS_CORBA.DCRSInterface href = DCRSInterfaceHelper.narrow(ref);
//			    System.out.println("hello1.5");
//
//			 // get the root naming context
//			 // NameService invokes the transient name service
//			    org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
//			    System.out.println("hello2");

			// Use NamingContextExt, which is part of the
//			// Interoperable Naming Service (INS) specification.
//			    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
//			    System.out.println("hello3");
//
//			    String name = "InseServer";
//			    NameComponent path[] = ncRef.to_name( name );
//			    ncRef.rebind(path, href);
//			    System.out.println("CompServer ready and waiting ...");
//			    orb.run();
	    
		}
		catch(Exception e) {
			System.out.println("Exception in inse Server: " + e);
		}
	
		Runnable task = () -> {
			receive();
		};
		
		Thread thread = new Thread(task);
		
		thread.start();
		
	
	}
	


	public static void inputTerms(String term) throws RemoteException {
		
		
		if(term == "summer") {
			semesterDetails = new HashMap<String, Integer>();
			semesterDetails.put("INSE6592", 10);
			semesterDetails.put("INSE6593", 10);
			semesterDetails.put("INSE6594", 10);
			semesterDetails.put("INSE6595", 10);
			inseData.put(term,semesterDetails);
		}

		if(term == "fall") {
			semesterDetails = new HashMap<String, Integer>();
			semesterDetails.put("INSE6431", 10);
			semesterDetails.put("INSE6461", 10);
			semesterDetails.put("INSE6451", 10);
			semesterDetails.put("INSE6481", 10);
			inseData.put(term,semesterDetails);
		}
		
		if(term == "winter") {
			semesterDetails = new HashMap<String, Integer>();
			semesterDetails.put("INSE6641", 10);
			semesterDetails.put("INSE6651", 10);
			semesterDetails.put("INSE6681", 10);
			inseData.put(term,semesterDetails);																																																																				
			
		}
		logger.info(inseData.toString());
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

			aSocket = new DatagramSocket(6666);

			

			System.out.println("Server 6666 Started............");

			while (true) {
				byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);

				aSocket.receive(request);
				
				String datareceived=new String (request.getData());

				String indices[] = datareceived.split(",");
				
				System.out.println("index length--" + indices.length);
				if(indices.length<3) {
					termList = indices[0].trim();
					System.out.println("listedTerm--" + termList);
					logger.info("ListedTerm  " + termList);
					logger.info("courseId--" + course);
					logger.info("listedTerm--" + termList);
				}
				else if(indices.length<5) {
					//studId = index[0].trim();
					course = indices[0].trim();
					termList = indices[1].trim();
					System.out.println("studentId--" + student);
					System.out.println("courseId--" + course);
					System.out.println("liTerm--" + termList);
				}
				String operationName = indices[indices.length - 1].trim();
				System.out.println("methodName--" + operationName);
				if(operationName.equals("List")) {
					HashMap<String, Integer> fetchedDetail = inseData.get(termList);
					response = fetchedDetail.toString().trim();
					logger.info(response);
					
				}
				else if(operationName.equals("enroll")) {
//					response = getEnrollment(course,termList);
					synchronized(inseData) {

					if(inseData.containsKey(termList)) {
						valFetch = inseData.get(termList);
						if(valFetch.containsKey(course)) {
							System.out.println("capacity: "+valFetch.get(course));
							int total  = valFetch.get(course);
							if(total>0){
								total--;
								valFetch.put(course, total);
								inseData.put(termList, valFetch);
								System.out.println("hashDataComp---"+inseData);
								logger.info(inseData.toString());
								response= "true";
							}
						}
					}
//					response= "false";
					}
				}
				else if(operationName.equals("drop")) {
//					response = getDropped(termList);
					synchronized(inseData) {

					Set<String> keySet = inseData.keySet();
					Iterator<String> keySetIterator = keySet.iterator();
					while (keySetIterator.hasNext()) {
					 String term = keySetIterator.next();
//					for(String term:compData.keySet()) {
//						System.out.println("soen---"+term);
						dropFetch  =inseData.get(term);
						Set<String> keySet1 = dropFetch.keySet();
						Iterator<String> keySetIterator1 = keySet1.iterator();
						while (keySetIterator1.hasNext()) {
							 String subject = keySetIterator1.next();

//							for(String subject:dropFetch.keySet()) {
							if(subject.equals(termList)) {
								int total = dropFetch.get(subject);
								total++;
								dropFetch.put(subject, total);
								inseData.put(term, dropFetch);
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
		HashMap<String, Integer> fetchedList = inseData.get(semester);
		availableCourses = "INSE" + fetchedList.toString();
		String sendMsgComp=sendMessage(5555,semester+","+"List");
		String sendMsgSoen=sendMessage(7777,semester+","+"List");


		availableCourses= availableCourses + "COMP" + sendMsgComp + "SOEN"+ sendMsgSoen;
	    System.out.println("result inse ----" +availableCourses);
	    logger.info("result comp ----" +availableCourses);
	    return availableCourses;
	}
	
	public static String getDetails(String courseId, String semester) {
		String validCourse = courseId.substring(0,4);
		String message = courseId+","+semester+","+"enroll";
		System.out.println("common---"+message);
		if(validCourse.equals("INSE")) {
			synchronized(inseData) {

			if(inseData.containsKey(semester)) {
				
				System.out.println("hasmap data: "+inseData.get(semester));
				valFetch = inseData.get(semester);
				if(valFetch.containsKey(courseId)) {
					
					System.out.println("capacity: "+valFetch.get(courseId));
					int total  = valFetch.get(courseId);
					if(total>0){
						
						total--;
						valFetch.put(courseId, total);
						inseData.put(semester, valFetch);
						System.out.println("Data Inse---"+inseData);
						logger.info(inseData.toString());
						return "true";
					}
				}
			}
		}
		}
		else if(validCourse.equals("SOEN")) {
			repEnroll = sendMessage(7777,message);
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
		String CourseDrop = "";
		String courseValid = courseId.substring(0,4);
		String message = courseId+","+"drop";
		System.out.println("common---"+message);
		if(courseValid.equals("INSE")) {
			synchronized(inseData) {

			Set<String> keySet = inseData.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			while (keySetIterator.hasNext()) {
			 String term = keySetIterator.next();
//			for(String term:compData.keySet()) {
				System.out.println("s---"+term);
				dropFetch  =inseData.get(term);
				Set<String> keySet1 = dropFetch.keySet();
				Iterator<String> keySetIterator1 = keySet1.iterator();
				while (keySetIterator1.hasNext()) {
				 String subject = keySetIterator1.next();

//			for(String term:inseData.keySet()) {
//				System.out.println("s---"+term);
//				dropFetch  =inseData.get(term);
//				for(String subject:dropFetch.keySet()) {
					if(subject.equals(courseId)) {
						int total = dropFetch.get(subject);
						total++;
						dropFetch.put(subject, total);
						inseData.put(term, dropFetch);
						logger.info(courseId+"is dropped");

						return "true";
					}
				}
			}
		}
		}	
		else if(courseValid.equals("COMP")) {
			CourseDrop = sendMessage(5555,message);
			
			return CourseDrop.trim();
		}
		else if(courseValid.equals("SOEN")) {
			CourseDrop = sendMessage(7777,message);
			
			return CourseDrop.trim();
		}
		return "false";
	}
	
}
	
	
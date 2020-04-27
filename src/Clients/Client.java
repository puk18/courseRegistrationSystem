package Clients;
import java.rmi.*;
import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.Format;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import org.omg.CORBA.ORB;


public class Client {
	Scanner input=new Scanner(System.in);
	
	String registryURL=null;
	boolean valid=false;
	String inputID="";
	String studentId="";
	String serverName = null;
	int serverPort = 0;
	String ch="";
	String courseId=null;
	String oldcourseId=null;
	String semester=null;
	
	static Logger logger = Logger.getLogger(Client.class.getName());
	static private FileHandler fh;
	static private SimpleFormatter sf;
	static DCRS_CORBA.DCRSInterface server;
	
	String[] studentID={"COMPS1001","COMPS1002","COMPS1003","COMPS1004","COMPS1005","SOENS1001","SOENS1002","SOENS1003","SOENS1004","SOENS1005","INSES1001","INSES1002","INSES1003","INSES1004","INSES1005","INSES1006"};
	String[] advisorId={"COMPA1001","COMPA1002","COMPA1003","COMPA1004","COMPA1005","SOENA1001","SOENA1002","SOENA1003","SOENA1004","SOENA1005","INSEA1001","INSEA1002","INSEA1003","INSEA1004","INSEA1005","INSEA1006"};
	String getSchedule;
	String reply="";
	
	public static void main(String[] args) throws NotBoundException, SecurityException, IOException, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName, InvalidName {
		// TODO Auto-generated method stub
		
	    Client Sc=new Client();
		Sc.validUser(args);
	 
	
	}
	
	public void validUser(String[] args) throws NotBoundException, SecurityException, IOException, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName, InvalidName  {
		 
		
		System.out.println("Enter your Respective id");
		inputID=input.nextLine().trim().toUpperCase();
        String path="/Users/pulkitwadhwa/eclipse-workspace/"+inputID+".log";
    	
        fh= new FileHandler(path);
        sf = new SimpleFormatter();
            fh.setFormatter(sf);
            logger.addHandler(fh);
//		file=new FileHandler("/Users/pulkitwadhwa/eclipse-workspace/"+inputID+".log");
//		file.setFormatter(formatter);
//		logger.addHandler(file);
		logger.info("Entered ID"+inputID);
		ORB orb = ORB.init(args, null);
//		System.out.println("orb" + orb);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
//		System.out.println("objRef" + objRef);
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
//	    	System.out.println("ncRef" + ncRef);

		 if (isStudent(inputID) ||isAdvisor(inputID)){
			 if (inputID.substring(0, 4).equalsIgnoreCase("COMP")) {
//					serverName = "compServer";
//					serverPort = 8756;
//					registryURL = "rmi://localhost:" + serverPort + "/"+serverName; 
//					System.out.println(registryURL);
//			        server=(Servers.DCRSInterface)Naming.lookup(registryURL);
				 

				 server = (DCRS_CORBA.DCRSInterface) DCRS_CORBA.DCRSInterfaceHelper.narrow(ncRef.resolve_str("COMPServer"));
			 }
				else if (inputID.substring(0, 4).equalsIgnoreCase("SOEN")) {
//					serverName = "soenServer";
//					serverPort = 8758;
//
//					registryURL = "rmi://localhost:" + serverPort + "/"+serverName;  
//			        server=(Servers.DCRSInterface)Naming.lookup(registryURL);
					server = (DCRS_CORBA.DCRSInterface) DCRS_CORBA.DCRSInterfaceHelper.narrow(ncRef.resolve_str("SOENServer"));


				}
				else if (inputID.substring(0, 4).equalsIgnoreCase("INSE")) {
//					serverName = "inseServer";
//					serverPort = 8760;
//
//					registryURL = "rmi://localhost:" + serverPort + "/"+serverName;  
//			        server=(Servers.DCRSInterface)Naming.lookup(registryURL);
					server = (DCRS_CORBA.DCRSInterface) DCRS_CORBA.DCRSInterfaceHelper.narrow(ncRef.resolve_str("INSEServer"));


				}

		 }
			 
		 else {
			 	logger.info("Incorrect id "+inputID);
				System.out.println("incorrect id please enter again");
				validUser(args);
			}
	         

	         if (inputID.substring(4, 5).equalsIgnoreCase("S")) {
					
					System.out.println("welcome student");
					studentId=inputID;
					studentOperations(studentId);
					

				}
			else  if (inputID.substring(4, 5).equalsIgnoreCase("A")) {
					
					System.out.println("welcome advisor");
				while(true){
					System.out.println("Enter the operations you want to perform");
					System.out.println("1.Advisor operations");
					System.out.println("2.Student behalf operations");
					ch=input.nextLine();
					if (ch.equals("1")) {
						
						advisorOperations(inputID);
					}
			
					else if(ch.equals("2")) {

						while(true) {  

						System.out.println("Enter the student id for which you want to perform operation");
						studentId=input.next().trim().toUpperCase();
						if(isStudent(studentId)) {
							studentOperations(studentId);
							
						}
						else {
							System.out.println("incorrect id please enter again");

							continue;
					
				}
				break;		
			}	
			}
			else {
				System.out.println("Invalid entry Please enter between 1 and 2");
				continue;
			}
				break;	
			}
			}
		
	         
	         
	}
	public  boolean isStudent(String stId)
	{
		for(int i=0;i<studentID.length;i++)
		{
			if(studentID[i].equalsIgnoreCase(stId))
				return true;
		}
		return false;
	}
	public   boolean isAdvisor(String advisrId)
	{
		for(int i=0;i<advisorId.length;i++)
		{
			if(advisorId[i].equalsIgnoreCase(advisrId))
				return true;
		}
		return false;
	}
	public void studentOptions() {
		System.out.println("choose the option");
		System.out.println("1. enrolCourse");
		System.out.println("2. Get Class Schedule");
		System.out.println("3. Drop Course");
		System.out.println("4. Swap Course");

	}
	public  void advisorOptions() {
		System.out.println("choose the option");
		System.out.println("1. Add Course:");
		System.out.println("2. Remove Course");
		System.out.println("3. List Course Availability:");

	}
	
	public void studentOperations(String inputID) throws RemoteException,MalformedURLException, NotBoundException{
		
		
//       while(true) {  
        
        studentOptions();
        
		ch=input.next();
		
		switch(ch) {
		case "1":
			System.out.println("enrolling course");

			System.out.println("enter course Id");
			courseId=input.next().toUpperCase();
			System.out.println("enter Semester");
			semester=input.next().toLowerCase();
			boolean enrollSuccess=server.enrolCourse(inputID, courseId, semester);
			if(enrollSuccess) {
				logger.info("Enrollment Successful for"+inputID+"for course " +courseId);
				System.out.println("Enrollment Successful for"+inputID+"for course " +courseId);
				
			}
			else {
				logger.info("Enrollment unSuccessful for"+inputID+"for course " +courseId);
				System.out.println("Enrollment unSuccessful for"+inputID+"for course " +courseId);	
			}
			continueop(inputID);

			break;
		case "2":
			System.out.println("getting class schedule");
			getSchedule=server.getClassSchedule(inputID);
			if(getSchedule!=null) {
				logger.info("fetched schedule  "+getSchedule);
				
				System.out.printf("fetched schedule %s ",getSchedule);
				
			}
			else {
				logger.info("No schedule found for this Id");
				System.out.println("No schedule found for this Id");	
			}
			continueop(inputID);


			break;	
		case "3":
			System.out.println("Dropping course");

			System.out.println("enter course Id");
			courseId=input.next().toUpperCase();
			boolean dropSuccess=server.dropCourse(inputID, courseId);
			if(dropSuccess) {
				logger.info("Dropped Successfully"+courseId);
				System.out.printf("%s Dropped Successfully",courseId);
				
			}
			

			else {
				logger.info("Unable to Drop"+courseId);
				System.out.println("Unable to Drop"+courseId);	
			}
			continueop(inputID);

			break;	
			
		case "4":
			System.out.println("swapping course");

			System.out.println("enter old course Id");
			oldcourseId=input.next().toUpperCase();
		    System.out.println("enter the course Id for which you want to enroll");
			courseId=input.next().toUpperCase();

			boolean swapSuccess=server.swapCourse(inputID, oldcourseId, courseId);
			if(swapSuccess) {
				logger.info("Swapped Successfully"+courseId);
				System.out.printf("%s Swapped Successfully",courseId);
				
			}
			

			else {
				logger.info("Unable to swap"+courseId);
				System.out.println("Unable to swap"+courseId);	
			}
			continueop(inputID);

			break;	
			
			
		default:
			System.out.println("Invalid entry Please enter between 1 and 3");
			studentOperations(inputID);
//			continue;
		}
		
		

		
        
	}
	
	public void advisorOperations(String inputID) throws RemoteException,MalformedURLException, NotBoundException{
		String advisorDept = inputID.substring(0,4).trim().toUpperCase();

//		  while(true) {
	        	advisorOptions();
	        	
				ch=input.next();
	        	
		switch(ch) {
		case "1":
			System.out.println("Adding course");
			System.out.println("enter course Id");
			courseId=input.next().toUpperCase();
			System.out.println("enter Semester");
			semester=input.next().toLowerCase();
			System.out.println("enter capacity");
			String quantity=input.next();
			int capacity=Integer.parseInt(quantity);
			String course = courseId.substring(0, 4).trim().toUpperCase();
			boolean addCourse=false;
			if (advisorDept.equals(course)) {
			 addCourse=server.addCourse(courseId, semester,capacity);
			}
			
			if(addCourse) {
				logger.info("Successfully added"+courseId);
				System.out.printf("%s added Successfully",courseId);
				
			}
			else {
				logger.info("Course  cannot be added"+courseId);
				System.out.println("Course cannot be added");
			}
		
			continueop(inputID);
			break;
		case "2":
			System.out.println("Removing course");
			System.out.println("enter course Id");
			courseId=input.next().toUpperCase();
			System.out.println("enter Semester");
			semester=input.next().toLowerCase();
			boolean removeCourse=false;

			String courseremove = courseId.substring(0, 4).trim().toUpperCase();
			if (advisorDept.equals(courseremove)) {
			removeCourse=server.removeCourse(courseId, semester);
			}		
			if(removeCourse) {
				logger.info("Successfully removed"+courseId);
				System.out.printf("%s removed Successfully",courseId);
				
			}
			else {
				logger.info("unable t0 remove "+courseId);
				System.out.printf("unable to remove %s",courseId);	
			}
			continueop(inputID);

			break;	
		case "3":
			
			System.out.println("Listing course Availability");

			System.out.println("enter Semester");
			semester=input.next();
			String courseListing;
			if (inputID.substring(0, 4).equalsIgnoreCase("COMP")){
				courseListing=server.listCourseAvailability("Comp"+semester);
			}
			else if (inputID.substring(0, 4).equalsIgnoreCase("SOEN")){
				courseListing=server.listCourseAvailability("Soen"+semester);
			}
			else{
				courseListing=server.listCourseAvailability("Inse"+semester);
			}
			
			if(courseListing != null) {
				logger.info("course list "+courseListing);
				System.out.printf("course list %s",courseListing);
				
			}
			else {
				System.out.printf("unable to fetch course");	
			}
			continueop(inputID);

			break;		
		default:
			System.out.println("Invalid entry Please enter between 1 and 3");
			advisorOperations(inputID);
		}
	    
	}
	
	public  void continueop(String InputID) throws RemoteException,MalformedURLException, NotBoundException{
		
		System.out.println("\n if u want to continue");
		System.out.println("Press y for yes");
		System.out.println("press n for no");
		reply=input.next().trim();
		if (reply.equals("y")) {
			if (inputID.substring(4, 5).equalsIgnoreCase("S")) {
				studentOperations(inputID);
			}
			else {
				
				advisorOperations(inputID);
			}
		}
		else {
			
	    System.exit(0);
		}
	}
	
}

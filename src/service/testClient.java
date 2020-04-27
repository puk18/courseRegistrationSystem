package service;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class testClient extends Thread {
	String serverName;
	String studentID="";
	String courseID=null;
	String oldcourseID=null;
	String newcourseID=null;
	static DCRS_CORBA.DCRSInterface server;
	static String[] ref=null;
	String getSchedule;


	String semester=null;
	
	public testClient(String server,String studentId,String term,String oldCourseId,String newCourseId) {
		// TODO Auto-generated constructor stub
		serverName=server;
		studentID=studentId.toUpperCase();
		oldcourseID=oldCourseId.toUpperCase();
		newcourseID=newCourseId.toUpperCase();
		semester=term;
		
	}

	public static void main(String[] args) throws InvalidName {
		
		// TODO Auto-generated method stub
		ref=args;
		testClient d1 = new testClient("COMPServer", "COMPS1001","fall","comp6461","comp6451");
		Thread td1 = new Thread (d1);
		testClient d2 = new testClient("COMPServer", "COMPS1002","fall","comp6461","comp6451");
		Thread td2 = new Thread (d2);
		testClient d3 = new testClient("COMPServer", "COMPS1003","fall","comp6461","comp6451");
		Thread td3 = new Thread (d3);
		testClient d4 = new testClient("COMPServer", "COMPS1004","fall","comp6461","comp6451");
		Thread td4 = new Thread (d4);
		testClient d5 = new testClient("COMPServer", "COMPS1005","fall","comp6461","comp6451");
		Thread td5 = new Thread (d5);
		
		
//		testClient m1 = new testClient("SOENServer", "SOENS1001","fall","SOEN6461","SOEN6451");
//		Thread tm1 = new Thread (m1);
//		testClient m2 = new testClient("SOENServer", "SOENS1001","fall","SOEN6461","SOEN6451");
//		Thread tm2 = new Thread (m2);
//		testClient m3 = new testClient("SOENServer", "SOENS1001","fall","SOEN6461","SOEN6451");
//		Thread tm3 = new Thread (m3);
//		testClient m4 = new testClient("SOENServer", "SOENS1001","fall","SOEN6461","SOEN6451");
//		Thread tm4 = new Thread (m4);
//		testClient m5 = new testClient("SOENServer", "SOENS1001","fall","SOEN6461","SOEN6451");
//		Thread tm5 = new Thread (m5);
//		
		
//		testClient l1 = new testClient("INSEServer", "INSES1001","fall","INSE6461","INSE6451");
//		Thread tl1 = new Thread (l1);		
//		testClient l2 = new testClient("INSEServer", "INSES1002","fall","INSE6461","INSE6451");
//		Thread tl2 = new Thread (l2);
//		testClient l3 = new testClient("INSEServer", "INSES1003","fall","INSE6461","INSE6451");
//		Thread tl3 = new Thread (l3);
//		testClient l4 = new testClient("INSEServer", "INSES1004","fall","INSE6461","INSE6451");
//		Thread tl4 = new Thread (l4);
//		testClient l5 = new testClient("INSEServer", "INSES1005","fall","INSE6461","INSE6451");
//		Thread tl5 = new Thread (l5);
//		
		
		
		
		
		
		td1.start();
		td2.start();
		td3.start();
		td4.start();
		td5.start();
		
		
//		
//		tm1.start();
//		tm2.start();
//		tm3.start();
//		tm4.start();
//		tm5.start();
//		
		
		
		
//		tl1.start();
//		tl2.start();
//		tl3.start();
//		tl4.start();
//		tl5.start();
//		
	}

	public void run() {
		ORB orb = ORB.init(ref, null);
//		System.out.println("orb" + orb);
		org.omg.CORBA.Object objRef;
		try {
			objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			server = (DCRS_CORBA.DCRSInterface) DCRS_CORBA.DCRSInterfaceHelper.narrow(ncRef.resolve_str(serverName));
		} catch (InvalidName | NotFound | CannotProceed | org.omg.CosNaming.NamingContextPackage.InvalidName e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("objRef" + objRef);
		

		boolean enrollSuccess=server.enrolCourse(studentID, oldcourseID, semester);
		if(enrollSuccess) {
//			logger.info("Enrollment Successful for"+inputID+"for course " +courseId);
			System.out.println("Enrollment Successful for"+studentID+"for course " +oldcourseID+"\n");
			
		}
		else {
//			logger.info("Enrollment unSuccessful for"+inputID+"for course " +courseId);
			System.out.println("Enrollment unSuccessful for"+studentID+"for course " +oldcourseID+"\n");	
		}
		getSchedule=server.getClassSchedule(studentID);
		if(getSchedule!=null) {
//			logger.info("fetched schedule  "+getSchedule);
			
			System.out.printf("fetched schedule %s for student id %s",getSchedule,studentID);
			
		}
		else {
//			logger.info("No schedule found for this Id");
			System.out.println("No schedule found for this Id "+studentID+" \n");	
		}
		boolean swapSuccess=server.swapCourse(studentID, oldcourseID, newcourseID);
		if(swapSuccess) {
//			logger.info("Swapped Successfully"+courseId);
			System.out.printf("%s Swapped Successfully \n",oldcourseID);
			
		}
		

		else {
//			logger.info("Unable to swap"+oldcourseID);
			System.out.println("Unable to swap"+oldcourseID+"\n");	
		}

		boolean dropSuccess=server.dropCourse(studentID, newcourseID);
		if(dropSuccess) {
//			logger.info("Dropped Successfully"+courseId);
			System.out.printf("%s Dropped Successfully for student id %s \n",newcourseID,studentID);
			
		}
		

		else {
//			logger.info("Unable to Drop"+courseId);
			System.out.println("Unable to Drop"+oldcourseID+"for student id"+studentID+"\n");	
		}

	}
}

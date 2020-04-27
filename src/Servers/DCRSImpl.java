package Servers;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import org.omg.CORBA.ORB;
import DCRS_CORBA.DCRSInterfacePOA;
import Servers.COMPServer;
import  Servers.INSEServer;
import Servers.SOENServer;



public class DCRSImpl  extends DCRSInterfacePOA  {
	private static ORB orb;

	public static void setORB(ORB orb_val) {
		orb = orb_val;
	}
	

	static HashMap<String, HashMap<String,HashSet<String>>> studentDetail = new HashMap<String, HashMap<String,HashSet<String>>>();
	HashMap<String, HashSet<String>> studinDetail= new HashMap<String, HashSet<String>>();
	HashMap<String, Integer> fetchedDetails;
	HashMap<String, Integer> fetchComp;
	HashMap<String, Integer> fetchSoen;
	HashMap<String, Integer> fetchInse;
	HashMap<String,HashSet<String>> fetchStudentId;
	HashMap<String,HashSet<String>> fetchedSchedule;

	HashSet<String> courseList;
	HashSet<String> getHash;
	HashSet<String> coursefetched;
	HashSet<String> fallData;
	HashSet<String> summerData;
	HashSet<String> winterData;
	HashMap<String, HashMap<String, Integer>> serverData;
	Boolean checkTask=false;
	Boolean flag=false;
	String responseString="";
	Boolean checkEnroll = false;
	boolean responseBool = false;
	String response = null;

	int count=0;
	
	
	public DCRSImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean addCourse(String courseID, String semester,int capacity) {
		// TODO Auto-generated method stub
		String dept = courseID.substring(0, 4).toUpperCase();
		if(dept.equals("INSE")){
			if (INSEServer.inseData.get(semester).containsKey(courseID)) {
				return false;
			}
			else {	
			INSEServer.inseData.get(semester).put(courseID,capacity);
				return true;
		}
		}
		
		else if(dept.equals("COMP")){
			if(COMPServer.compData.get(semester).containsKey(courseID)) {
				return false;
			}
		else  {
				COMPServer.compData.get(semester).put(courseID,capacity);
				return true;
			}
			
		}
		else if(dept.equals("SOEN")){
			if(SOENServer.soenData.get(semester).containsKey(courseID)) {
				return false;
			}
			else  {
				SOENServer.soenData.get(semester).put(courseID,capacity);
				return true;
			}
			
		}
		
		return false;
	}

	@Override
	public synchronized boolean removeCourse(String courseID, String semester) {
		
		// TODO Auto-generated method stub
		String dept = courseID.substring(0, 4).toUpperCase().trim();
		if(dept.equals("INSE")){
			if(!INSEServer.inseData.get(semester).containsKey(courseID)) {
//				INSEServer.inseData.get(semester).remove(courseID);
				checkTask=false;
				return false;

			}
		else {
			INSEServer.inseData.get(semester).remove(courseID);
			student(courseID,semester); 
			return true;
			}
		}  
		else if(dept.equals("COMP")){
		
			if(!COMPServer.compData.get(semester).containsKey(courseID)) {
				return false;

			}
			else  {
				COMPServer.compData.get(semester).remove(courseID);
				System.out.println("remove"+courseID);
				student(courseID,semester); 
				return true;
			}
			
		}
		else if(dept.equals("SOEN")){
			if(!SOENServer.soenData.get(semester).containsKey(courseID)) {
				return false;
			}
			else  {
				SOENServer.soenData.get(semester).remove(courseID);
				student(courseID,semester); 
				return true;
			}
			
		}
	
		return false;
	}

	@Override
	public String listCourseAvailability(String semester) {
		String response=null;
		String term=semester.substring(4, semester.length());
		if (semester.trim().startsWith("Comp")){
			response=COMPServer.listCourseAvailability(term);
		}
		else if(semester.trim().startsWith("Soen")) {
			response=SOENServer.listCourseAvailability(term);			
		}
		else if (semester.trim().startsWith("Inse")){
			response=INSEServer.listCourseAvailability(term);			
		}
		else{
			System.out.println("wrong semester entered ");
		}


		return response;
	}

	@Override
	public synchronized boolean enrolCourse(String studentID, String courseID, String semester) {
		
		HashMap<String,HashSet<String>> addCourse = new HashMap<String,HashSet<String>>();
		String checkdept = studentID.substring(0,4);
		String checkCourse = courseID.substring(0,4);
		if(!studentDetail.containsKey(studentID)) {
			
			if(checkdept.equals("COMP")) {
				
				responseString= COMPServer.getDetails(courseID,semester);
//					System.out.println("responseString " +responseString);
					checkEnroll = Boolean.parseBoolean(responseString.trim());
//					System.out.println("checkEnroll" +checkEnroll);
					if(checkEnroll) {
						courseList = new HashSet<String>();
						courseList.add(courseID);
						addCourse.put(semester,courseList);
						studentDetail.put(studentID, addCourse);
//						System.out.println("studentDetail"+studentDetail);
						return true;
					}
			}
			else if(checkdept.equals("SOEN")) {
				
				responseString= SOENServer.getDetails(courseID,semester);
//				System.out.println("responseString " +responseString);
				checkEnroll = Boolean.parseBoolean(responseString.trim());
//				System.out.println("checkEnroll" +checkEnroll);
				if(checkEnroll) {
					courseList = new HashSet<String>();
					courseList.add(courseID);
					addCourse.put(semester,courseList);
					studentDetail.put(studentID, addCourse);
//					System.out.println("studentDetail"+studentDetail);
					return true;
				}
			}
			else if(checkdept.equals("INSE")) {
				
				responseString= INSEServer.getDetails(courseID,semester);
//				System.out.println("responseString " +responseString);
				checkEnroll = Boolean.parseBoolean(responseString.trim());
//				System.out.println("checkEnroll " +checkEnroll);
				if(checkEnroll) {
					courseList = new HashSet<String>();
					courseList.add(courseID);
					addCourse.put(semester,courseList);
					studentDetail.put(studentID, addCourse);
//					System.out.println("enroll---1"+studentDetail);
					return true;
				}
			}
			
		}
		
		else {
			addCourse = studentDetail.get(studentID);
			if(!addCourse.containsKey(semester)) {
				getHash = new HashSet<String>();
			}
			else{
				getHash = addCourse.get(semester);
//				System.out.println("getHash.size() "+getHash.size());
			}
		if(getHash.size()<3) {
			int countCheck = 0;

//				System.out.println("getHash.size()"+getHash.size());
				
				//System.out.println(addCourse.entrySet());
				
				studinDetail = studentDetail.get(studentID);
//				System.out.println("getStudentId---"+studinDetail);	
				for(String sem :studinDetail.keySet()) {
					
					HashSet<String> subject = studinDetail.get(sem);
					for(String course : subject) {
						if(course.equalsIgnoreCase(courseID)) {
							count = count+1;
							System.out.println("count"+count);
							break;	
						}
							
						}
				}
				
				if(count==0) {
					System.out.println("hello");
					Boolean checkEnrollElse=false;	
					System.out.println("in check--");
					if(checkdept.equals("COMP")) {
						if(!checkCourse.equals("COMP")) {
							System.out.println("hello1");

						for(String sem :studinDetail.keySet()) {
							
							HashSet<String> subjectList = studinDetail.get(sem);
							for(String course : subjectList) {
								String checkDep = course.substring(0,4);
								
								if(checkDep.equals("SOEN")||checkDep.equals("INSE")) {
									
									countCheck = countCheck+1;
									System.out.println("countCheck--"+countCheck);
									
								}
									
								}
						}
						}
							if(countCheck<2) {	
								System.out.println("hello3");

								responseString= COMPServer.getDetails(courseID,semester);
								System.out.println("responseString " +responseString);
								checkEnrollElse = Boolean.parseBoolean(responseString.trim());
								System.out.println("checkEnrollElse " +checkEnrollElse);
							}
						//checkTask = studentTask(studentId,courseId,semester,hashData);
					}
					else if(checkdept.equals("SOEN")) {
						if(!checkCourse.equals("SOEN")) {
							System.out.println("hello4");

						for(String sem :studinDetail.keySet()) {
							
							HashSet<String> subjectList = studinDetail.get(sem);
							for(String course : subjectList) {
								String checkDep = course.substring(0,4);
								
								if(checkDep.equals("COMP")||checkDep.equals("INSE")) {
									
									countCheck = countCheck+1;
									
									
								}
									
								}
						}
						}
						
						if(countCheck<2) {
							System.out.println("hello5");

							responseString= SOENServer.getDetails(courseID,semester);
//						System.out.println("responseString " +responseString);
						checkEnrollElse = Boolean.parseBoolean(responseString.trim());
//						System.out.println("checkEnroll " +checkEnroll);
						}
					}
					
					else if(checkdept.equals("INSE")) {
						if(!checkCourse.equals("INSE")) {

						for(String sem :studinDetail.keySet()) {
							
							HashSet<String> subjectList = studinDetail.get(sem);
							for(String course : subjectList) {
								String checkDep = course.substring(0,4);
								
								if(checkDep.equals("COMP")||checkDep.equals("SOEN")) {
									
									countCheck = countCheck+1;
//									System.out.println("countCheck"+countCheck);	
								}
									
								}
						}
						}
						
						if(countCheck<2) {
							responseString= INSEServer.getDetails(courseID,semester);
//						System.out.println("responseString " +responseString);
						checkEnrollElse = Boolean.parseBoolean(responseString.trim());
//						System.out.println("checkEnroll " +checkEnroll);
					}
				}
					if(checkEnrollElse) {
//						System.out.println("checkEnrollElse"+checkEnrollElse);
						getHash.add(courseID);
						addCourse.put(semester, getHash);
						
						studentDetail.put(studentID, addCourse);
						//capacity--;
//						System.out.println("studentDetail---"+studentDetail);
						//System.out.println("capacity---"+capacity);
						return true;
					}
				}
			
			}
		}
		
		return false;
	}
	
	

	
	@Override
	public String getClassSchedule(String studentID) {

		// TODO Auto-generated method stub
		
		System.out.println(studentDetail);
		if(studentDetail.get(studentID)!=null) {
			
			studinDetail = studentDetail.get(studentID);
			fallData = studinDetail.get("fall");
			winterData = studinDetail.get("winter");
			summerData = studinDetail.get("summer");
			System.out.println(studentID + " " + "is enrolled in below courses:");
			System.out.println("Fall:" + " " + fallData);
			System.out.println("Winter:" + " " + winterData);
			System.out.println("Summer:" + " " + summerData);
			fetchedSchedule = studentDetail.get(studentID);
			List<HashMap<String, HashSet<String>>> fetchedList = Arrays.asList(fetchedSchedule);

			return fetchedSchedule.toString();
		}
		else {
			return null;
		}
		
	}
		
		
		
	

	@Override
	public synchronized boolean dropCourse(String studentID, String courseID) {
		
		String student = studentID.substring(0,4).toUpperCase();
		if(studentDetail.get(studentID)==null){
			return false;

		}
		else  {
			fetchedSchedule = studentDetail.get(studentID);
			Set<String> keySet = fetchedSchedule.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			while (keySetIterator.hasNext()) {
			 String term = keySetIterator.next();

				HashSet<String> subject = fetchedSchedule.get(term);
				Iterator<String> itr = subject.iterator();
		        while(itr.hasNext()){
//				for(String course : subject) {
		        	String course = itr.next();
					if(course.equalsIgnoreCase(courseID)) {
						
						HashSet<String> hashsetDrop = fetchedSchedule.get(term);
						boolean flag = hashsetDrop.remove(course);
						
						System.out.println("courses left"+hashsetDrop);
						fetchedSchedule.put(term, hashsetDrop);
						studentDetail.put(studentID, fetchedSchedule);
						responseBool= true;
						break;
					}
						
					}
			}
			if(!responseBool){
				return false;
			}
			else  {	
				boolean operation=dropOperation(courseID,student);
				if(operation) {
					return true;
				}
				
			}
			
		}
		return false;
	}

	@Override
	public synchronized boolean swapCourse(String StudentID, String oldCourseID, String newCourseID) {
		boolean checkCourse=false;
		boolean enrollOperation=false;
		boolean dropOperation=false;
		String Semester="";
		if (studentDetail.containsKey(StudentID)) {
			studinDetail=studentDetail.get(StudentID);
			fallData = studinDetail.get("fall");
			winterData = studinDetail.get("winter");
			summerData = studinDetail.get("summer");
			if(fallData.contains(oldCourseID)) {
				Semester = "fall";
				checkCourse =true;}
			else if(winterData.contains(oldCourseID)) {
				Semester = "winter";
				checkCourse =true;}
			else if(summerData.contains(oldCourseID)) {
				Semester = "summer";
				checkCourse =true;}
			if(checkCourse) {
				dropOperation=dropCourse(StudentID,oldCourseID);
				if(dropOperation) {
					enrollOperation=enrolCourse(StudentID,newCourseID,Semester);
					
				    if(enrollOperation) {
				    	System.out.println("Swap done after drop and enroll");
				    	return true;
				    }	
				    else {
				    	System.out.println(" drop success and enroll unsuccesful");
				    	enrollOperation=enrolCourse(StudentID,oldCourseID,Semester);
				    }
				}
				else {
					System.out.println("unable to enroll");	
				}
			}
			else {
				System.out.println("Student not enrolled in-"+oldCourseID);
			}
			
			
		}
		else {
			System.out.println(StudentID + " " + "is not enrolled in any course.");
			
		}
		
		
		return false;
	}
	
		public void student(String courseID, String semester) {
			for ( String studentId:studentDetail.keySet()){
//				HashMap<String, HashSet<String>> studinDetail1= new HashMap<String, HashSet<String>>();

				studinDetail = studentDetail.get(studentId);
				HashSet<String> courseListing = studinDetail.get(semester);
				Iterator<String> itr = courseListing.iterator();
		        while(itr.hasNext()){
		        	String fetchedCourse = itr.next();
				
					if (fetchedCourse.equals(courseID)){

						courseListing.remove(fetchedCourse);

						studinDetail.put(semester, courseListing);
						studentDetail.put(studentId, studinDetail);
						System.out.println(studentDetail);
						
					}
				}
			}
		
			
		}
	
		public boolean dropOperation(String courseID,String student) {
//			String student = studentID.substring(0,4).toUpperCase();
			if(student.equals("INSE")) {
				response = INSEServer.getDropDetails(courseID);
				if(response.trim().equals("true")) {
					return true;
				}
			}
			else if(student.equals("COMP")) {
				response = COMPServer.getDropDetails(courseID);
				if(response.trim().equals("true")) {
					return true;
				}
			}
			else if(student.equals("SOEN")) {
				response = SOENServer.getDropDetails(courseID);
				if(response.trim().equals("true")) {
					return true;
				}
			}
			return false;
		}
}

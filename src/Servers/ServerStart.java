
package Servers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import org.omg.*;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import DCRS_CORBA.DCRSInterface;
import DCRS_CORBA.DCRSInterfaceHelper;



public class ServerStart {

	public static void main(String[] args)throws MalformedURLException, InvalidName, AdapterInactive, RemoteException, ServantNotActive, WrongPolicy, org.omg.CosNaming.NamingContextPackage.InvalidName, NotFound, CannotProceed,NotBoundException, SecurityException, IOException {
//		
//		COMPServer.startCOMPServer(args);
//		INSEServer.startINSEServer(args);
//		SOENServer.startSOENServer(args);
		
		ORB orb = ORB.init(args, null);
		
		POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		rootpoa.the_POAManager().activate();
		
		org.omg.CORBA.Object objRef =  orb.resolve_initial_references("NameService");
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	    
	    DCRSImpl implComp = new DCRSImpl();
	    implComp.setORB(orb);
	    org.omg.CORBA.Object objRefComp = rootpoa.servant_to_reference(implComp);
	    DCRSInterface objRefComp1 = DCRSInterfaceHelper.narrow(objRefComp);
		NameComponent nameobjectComp[] = ncRef.to_name("COMPServer");
		ncRef.rebind(nameobjectComp, objRefComp1);
		COMPServer.startCOMPServer();
			
		DCRSImpl implSoen = new DCRSImpl();
		implSoen.setORB(orb);   
		org.omg.CORBA.Object objRefSoen = rootpoa.servant_to_reference(implSoen);
		DCRSInterface objRefSoen1 = DCRSInterfaceHelper.narrow(objRefSoen);   
		NameComponent nameobjectSoen[] = ncRef.to_name("SOENServer");
		ncRef.rebind(nameobjectSoen, objRefSoen1);
		SOENServer.startSOENServer();
				
		DCRSImpl implInse = new DCRSImpl();
		implInse.setORB(orb);	    
		org.omg.CORBA.Object objRefInse = rootpoa.servant_to_reference(implInse);
		DCRSInterface objRefInse1 = DCRSInterfaceHelper.narrow(objRefInse);  
		NameComponent nameobject[] = ncRef.to_name("INSEServer");
		ncRef.rebind(nameobject, objRefInse1);
		INSEServer.startINSEServer();
		
		orb.run();
	}


	
	
	
	

//	public static void startRegistry(int rmiPort) throws RemoteException  {
//		// TODO Auto-generated method stub
//		try {
//			Registry registry=LocateRegistry.getRegistry(rmiPort);
//			registry.list();
//		}
//		catch(RemoteException e) {
//			
//			System.out.println("RMI registry cannot be located  at portnum:"+rmiPort);
//			Registry registry=LocateRegistry.createRegistry(rmiPort);
//			System.out.println("Rmi Registry created at port:"+rmiPort);
//			
//		}
//	}
	
}
	
	
	
	

	
	
	


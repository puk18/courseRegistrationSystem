package DCRS_CORBA;


/**
* DCRS_CORBA/_DCRSInterfaceStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/pulkitwadhwa/eclipse-workspace/RMI/src/CorbaInteface.idl
* Sunday, November 4, 2018 4:01:26 o'clock PM EST
*/

public class _DCRSInterfaceStub extends org.omg.CORBA.portable.ObjectImpl implements DCRS_CORBA.DCRSInterface
{

  public boolean addCourse (String courseID, String semester, int capacity)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("addCourse", true);
                $out.write_string (courseID);
                $out.write_string (semester);
                $out.write_long (capacity);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return addCourse (courseID, semester, capacity        );
            } finally {
                _releaseReply ($in);
            }
  } // addCourse

  public boolean removeCourse (String courseID, String semester)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("removeCourse", true);
                $out.write_string (courseID);
                $out.write_string (semester);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return removeCourse (courseID, semester        );
            } finally {
                _releaseReply ($in);
            }
  } // removeCourse

  public String listCourseAvailability (String semester)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("listCourseAvailability", true);
                $out.write_string (semester);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return listCourseAvailability (semester        );
            } finally {
                _releaseReply ($in);
            }
  } // listCourseAvailability

  public boolean enrolCourse (String studentID, String courseID, String semester)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("enrolCourse", true);
                $out.write_string (studentID);
                $out.write_string (courseID);
                $out.write_string (semester);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return enrolCourse (studentID, courseID, semester        );
            } finally {
                _releaseReply ($in);
            }
  } // enrolCourse

  public String getClassSchedule (String studentID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getClassSchedule", true);
                $out.write_string (studentID);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getClassSchedule (studentID        );
            } finally {
                _releaseReply ($in);
            }
  } // getClassSchedule

  public boolean dropCourse (String studentID, String courseID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("dropCourse", true);
                $out.write_string (studentID);
                $out.write_string (courseID);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return dropCourse (studentID, courseID        );
            } finally {
                _releaseReply ($in);
            }
  } // dropCourse

  public boolean swapCourse (String studentID, String oldcourseID, String newcourseID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("swapCourse", true);
                $out.write_string (studentID);
                $out.write_string (oldcourseID);
                $out.write_string (newcourseID);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return swapCourse (studentID, oldcourseID, newcourseID        );
            } finally {
                _releaseReply ($in);
            }
  } // swapCourse

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:DCRS_CORBA/DCRSInterface:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _DCRSInterfaceStub

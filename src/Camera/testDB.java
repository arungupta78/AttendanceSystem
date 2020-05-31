package Camera;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import javax.imageio.ImageIO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Arun
 */
public class testDB {
    Connection connection = null;
    Statement stt =null;
    PreparedStatement pstt = null;
    static String  course = null;
    static String  branch = null;
    static int  semester = 0;
    static int i=0;
    static String subject = null;
    static int LectureNo = 0;
    
    public void updateTeacherDB(String username, String password){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:TeachersDB.db");
            String sql = "update teachersID set password=? where username=?";
            pstt = connection.prepareStatement(sql);
            pstt.setString(1, password);
            pstt.setString(2, username);
            int a = pstt.executeUpdate();
            if(a>0){
            JOptionPane.showMessageDialog(null, "Password Updated Successfully !");
            }
            else{
                JOptionPane.showMessageDialog(null, "Update Unsuccessfull !",
                        "Error Occurred", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        
    }
    
    public boolean teacherIDExists(String username) throws ClassNotFoundException{
        try
	    {
	       Class.forName("org.sqlite.JDBC");
	       connection = DriverManager.getConnection("jdbc:sqlite:TeachersDB.db");	       
	       String sql = "SELECT * FROM  teachersID WHERE username = ?";
	       pstt = connection.prepareStatement(sql);
	       pstt.setString(1, username);
	       ResultSet rs = pstt.executeQuery();
	       if(rs.next()){
                   return true;
               } 
	    }
    catch(SQLException e)
    {
      JOptionPane.showMessageDialog(null, e);
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        JOptionPane.showMessageDialog(null, e);
      }
    }
        return false;
    }
    
    public void teacherDB(String username,String password) throws SQLException{
        try {
            Class.forName("org.sqlite.JDBC");
            connection= DriverManager.getConnection("jdbc:sqlite:TeachersDB.db");
            
            stt = connection.createStatement();
         
            if(stt.execute("create table if not exists teachersID (username String , password String )")){
//                  System.out.println("table created");
            }           
            pstt = connection.prepareStatement("insert into teachersID values(?,?)");
            pstt.setString(1, username);
            pstt.setString(2, password);
            pstt.executeUpdate();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    } 
    public void StudentDB(int RollNumber,String Name,String Birthday,String course ,String branch,int semester,String phonenumber,String Address,BufferedImage image) throws SQLException{
        try {
            Class.forName("org.sqlite.JDBC");
            connection= DriverManager.getConnection("jdbc:sqlite:"+course+branch+".db");
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try{
				ImageIO.write(image, "png", baos);
			}
			catch(IOException e){
				e.printStackTrace();
			}
			byte[] imageInByte = baos.toByteArray();
            Class.forName("org.sqlite.JDBC");
            connection= DriverManager.getConnection("jdbc:sqlite:"+course+branch+".db");
            
            stt = connection.createStatement();
         
            if(stt.execute("create table if not exists "+branch+semester+" (RollNumber int, Name String, Birthday String,"
                    + "course String,branch  String,semester int,phonenumber String,Address String,StudentFace blob)")){
//                  System.out.println("table created");
            }
            pstt = connection.prepareStatement("insert into "+branch+semester+" values(?,?,?,?,?,?,?,?,?)");
            pstt.setInt(1, RollNumber);
            pstt.setString(2, Name);
            pstt.setString(3, Birthday);
            pstt.setString(4,course);
            pstt.setString(5,branch);
            pstt.setInt(6, semester);
            pstt.setString(7, phonenumber);
            pstt.setString(8,Address);
            pstt.setBytes(9,imageInByte);
            int a = pstt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Student Registered Successfully !");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public boolean checkID(String username , String password) throws ClassNotFoundException{

	    try
	    {
	       Class.forName("org.sqlite.JDBC");
	       connection = DriverManager.getConnection("jdbc:sqlite:TeachersDB.db");
	       
	       String sql = "SELECT * FROM  teachersID WHERE username = ? AND password = ?";
	       pstt = connection.prepareStatement(sql);
	       pstt.setString(1, username);
	       pstt.setString(2, password);
	       ResultSet rs = pstt.executeQuery();
               
//                System.out.println(rs.next());
               return rs.next();
	       
	    }
    catch(SQLException e)
    {
      JOptionPane.showMessageDialog(null, e);
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        JOptionPane.showMessageDialog(null, e);
      }
    }
		return false;
}
  public boolean takeAttendence(int RollNo ,String Branch,String course,int sem) throws ClassNotFoundException{

	    try
	    {
	       Class.forName("org.sqlite.JDBC");
	       connection = DriverManager.getConnection("jdbc:sqlite:"+course+Branch+".db");
	       String sql = "SELECT RollNumber FROM  "+Branch+sem+" WHERE RollNumber = ?";
	       pstt = connection.prepareStatement(sql);
	       pstt.setInt(1, RollNo);
	       ResultSet rs = pstt.executeQuery();
	       while (rs.next()) {
	         // iterate through results
	    	   int no = rs.getInt("RollNumber");
	    	   if(no==RollNo){
	    		   return true;
	    	   }
	       }
	       
	       
	    }
    catch(SQLException e)
    {
      JOptionPane.showMessageDialog(null, e);
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        JOptionPane.showMessageDialog(null, e);
      }
    }
		return false;
  }
   public void DirLoader() throws ClassNotFoundException, SQLException, IOException{
		    try{
                        System.out.println(course+" "+branch+" "+semester);
		       Class.forName("org.sqlite.JDBC");
		       connection = DriverManager.getConnection("jdbc:sqlite:"+course+branch+".db");
		       String sql = "SELECT RollNumber,StudentFace FROM "+branch+semester;
	             stt= connection.createStatement();
	            ResultSet rs = stt.executeQuery(sql);
		       while (rs.next()) {
	                 int rollno = rs.getInt("RollNumber");
	                 InputStream input = rs.getBinaryStream("StudentFace");
	                 String filename = ".\\Faces\\"+rollno+"-Face.png";
	 	            File output = new File(filename);
		            String folder =".\\Faces";
		            File f = new File(folder);
		            f.mkdir();
                         FileOutputStream fos = new FileOutputStream(output);
	                 byte[] buffer = new byte[1024];
	                 while (input.read(buffer) > 0) {
	                     fos.write(buffer);
	                 }
		    	   }
		       }
		    catch(SQLException e){
	   JOptionPane.showMessageDialog(null, e);
	  }finally
	  {
	    try
	    {
	      if(connection != null)
	        connection.close();
	    }
	    catch(SQLException e)
	    {
	      JOptionPane.showMessageDialog(null, e);
	    }
	  }
	}

    public BufferedImage getImage(String course, String branch, int semester, int rollNumber) throws ClassNotFoundException, FileNotFoundException, IOException{
        BufferedImage bufferedImage = null;
        try{
                        
		       Class.forName("org.sqlite.JDBC");
		       connection = DriverManager.getConnection("jdbc:sqlite:"+course+branch+".db");
		       String sql = "SELECT StudentFace FROM "+branch+semester+" where RollNumber=?";
                       pstt= connection.prepareStatement(sql);
                       pstt.setInt(1, rollNumber);
                       ResultSet rs = pstt.executeQuery();
		       rs.next();
	                 InputStream input = rs.getBinaryStream("StudentFace");
	                 String filename = "Face.png";
	 	         File output = new File(filename);		        
                         FileOutputStream fos = new FileOutputStream(output);
	                 byte[] buffer = new byte[1024];
	                 while (input.read(buffer) > 0) {
	                     fos.write(buffer);
	                 }
                         bufferedImage = ImageIO.read(output);
		    	   }
                       
		       
		    catch(SQLException e){
	    JOptionPane.showMessageDialog(null, e);
	  }finally
	  {
	    try
	    {
	      if(connection != null)
	        connection.close();
	    }
	    catch(SQLException e)
	    {
	     JOptionPane.showMessageDialog(null, e);
	    }
	  }
        return bufferedImage;
    }
   
  public void setVar(String cs,String bh,int sem,String sub,int lh){
      course = cs;
      branch = bh;
      semester = sem;
      subject = sub;
      LectureNo=lh;
  }

  public void AttendenceTable(int RollNo) throws ClassNotFoundException{
	    try
	    {
	        DateFormat df = new SimpleDateFormat("dd_MM_yy");
	        Calendar calobj = Calendar.getInstance();
	        String date =df.format(calobj.getTime());
	        System.out.println(date);
              boolean a=false;
	       Class.forName("org.sqlite.JDBC");
	       connection = DriverManager.getConnection("jdbc:sqlite:"+course+branch+".db");
	       stt = connection.createStatement();
	       ResultSet rs1 = connection.getMetaData().getTables(null, null, branch+semester+subject, null);
	       while(rs1.next()){
	    	   String tName = rs1.getString("TABLE_NAME");
	            if (tName != null && tName.equals(branch+semester+subject)) {
	                a = true;
	                break;
	            }
	       }
	       if(!a){
		  stt.execute("create table "+branch+semester+subject+"(RollNumber int)");
	    	  ResultSet rs = stt.executeQuery("SELECT RollNumber FROM "+branch+semester);
	    	  while(rs.next()){
	    		  pstt = connection.prepareStatement("insert into "+branch+semester+subject+"(Rollnumber) values(?)");
	    		  pstt.setInt(1, rs.getInt("RollNumber"));
	    		  pstt.executeUpdate();
	    	  }
	       } 
	       String name = "date_"+date+"_L_"+LectureNo;
	       boolean check = false;
	       ResultSet resultSet = stt.executeQuery("SELECT * FROM "+branch+semester+subject);

	       ResultSetMetaData metadata = resultSet.getMetaData();
	       int columnCount = metadata.getColumnCount();
	       for (int i = 1; i <=columnCount; i++) {
	         String columnName = metadata.getColumnName(i);
	         if(name.equals(columnName)){
	        	 check=true;
	        	 System.out.println(columnName);
	         }
	       }
	       if(!check){
	    	   	stt.execute("alter table "+branch+semester+subject+" add column date_"+date+"_L_"+LectureNo+" int");
           		stt.execute("update "+branch+semester+subject+" set date_"+date+"_L_"+LectureNo+" = 0 ");
	       }
           pstt = connection.prepareStatement("update "+branch+semester+subject+" set date_"+date+"_L_"+LectureNo+" = 1 where RollNumber = ?");
	       pstt.setInt(1, RollNo);
	       pstt.executeUpdate();
	    }
	    catch(SQLException e)
	    {
	 		System.err.println(e.getMessage());
 		}
	    finally
 			{
	    try
   		{
	   	if(connection != null)
	   		connection.close();
   		}
   		catch(SQLException e)
   			{
	   			System.err.println(e);
   			}
 		}
   }
  
public void shiftInfo(String course, String branch) throws ClassNotFoundException, SQLException{
	   Class.forName("org.sqlite.JDBC");
       connection = DriverManager.getConnection("jdbc:sqlite:"+course+branch+".db");
       stt = connection.createStatement();
	   stt.execute("DROP TABLE IF EXISTS "+branch+8);
	   for(int i=7;i>=1;i--){
		   ResultSet rs1 = connection.getMetaData().getTables(null, null, branch+i, null);
	       while(rs1.next()){
	    	   String tName = rs1.getString("TABLE_NAME");
	            if (tName != null && tName.equals(branch+i)) {
	     		   stt.execute("ALTER TABLE  "+branch+i+" RENAME TO "+branch+(i+1));
	            }
	       }
	   }
	   stt.execute("DROP TABLE IF EXISTS "+branch+1);
       int a = branch.length()+1;
       ArrayList<String> listofTable = new ArrayList<String>();
	   DatabaseMetaData md = connection.getMetaData();
	   ResultSet rs = md.getTables(course+branch, null, "%", null);
	   while (rs.next()) {
	     String table = rs.getString(3);
	     if(table.length()>a){
	    	 listofTable.add(table);
	     }
	   }
	   String [] tables =listofTable.toArray(new String[listofTable.size()]);
	   for(int i=0;i<tables.length;i++){
		   stt.execute("DROP TABLE IF EXISTS "+tables[i]);
	   }
   }

}


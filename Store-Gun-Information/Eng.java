/*
 Author: Kun Huang
 */
import java.io.File;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;

public class Eng {

	public static void main(String[] args){
		Date date = new Date();
		PrintWriter pw;
		try{
			String inFileName = args[0];
			File inname = new File(args[0]);
			pw = new PrintWriter(args[1]);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pro?autoReconnect=true&useSSL=false" , "root" , "root");
			Statement myST = con.createStatement();
			String cre = "create table guninfo " + 
						 "(iid int auto_increment primary key, "+
						 "gunname varchar(40), " +
						 "price int, " +
						 "capacity int, " +
						 "typeofgun varchar(30), " +
						 "length varchar(10), " +
						 "weight varchar(10), " +
						 "quantity int, " +
						 "outofservice varchar(6), " +
						 "origincountry varchar(10), " +
						 "yearmade varchar(4));";
			myST.execute(cre);
			String sql = "load data local infile '" + inname
					+ "' into table guninfo columns terminated by ',' "
					+ " lines terminated by '\r\n' "
					+ "(gunname, price, capacity, typeofgun, length, weight, quantity, outofservice, origincountry,yearmade)";
			myST.execute(sql);
			
			ResultSet myRes = myST.executeQuery("select * from guninfo");
			pw.println("Gunname   Price   Capacity   TypeofGun   Length   Weight   Quantity   OutofService   OriginCountry   YearMade");
			while(myRes.next()){
				pw.println("Insert :" + myRes.getString("gunname") + 
						"  $" + myRes.getInt("price") +
						"  " + myRes.getInt("capacity") +
						"  " + myRes.getString("typeofgun") + 
						"  " + myRes.getString("length") +
						"  " + myRes.getString("weight") +
						"  " + myRes.getInt("quantity") + 
						"  " + myRes.getString("outofservice") +
						"  " + myRes.getString("origincountry") +
						"  " + myRes.getString("yearmade") + " at "+date.toString());
			}
			myRes = myST.executeQuery("select gunname from guninfo where quantity = 1");
			while(myRes.next()){
				pw.println("Delete: Deleting the gun " + myRes.getString("gunname") + " because of low quantity " + "at " + date.toString() );
			}
			String del = "delete from guninfo " +
						 "where quantity = 1";
			myST.execute(del);
			myRes = myST.executeQuery("select * from guninfo");
			pw.println("Iventory after deletion");
			while(myRes.next()){
				pw.println(myRes.getString("gunname") + 
						"  $" + myRes.getInt("price") +
						"  " + myRes.getInt("capacity") +
						"  " + myRes.getString("typeofgun") + 
						"  " + myRes.getString("length") +
						"  " + myRes.getString("weight") +
						"  " + myRes.getInt("quantity") + 
						"  " + myRes.getString("outofservice") +
						"  " + myRes.getString("origincountry") +
						"  " + myRes.getString("yearmade") + " " + date.toString());
			}
			myRes = myST.executeQuery("select gunname from guninfo where quantity < 100");
			while(myRes.next()){
				pw.println("modify: Restocking " + myRes.getString("gunname") + " because of low quantity at " + date.toString());
			}
			String update ="update guninfo set quantity = quantity + 23 where quantity < 100";
			myST.execute(update);
			myRes = myST.executeQuery("select * from guninfo");
			pw.println("Iventory after restock");
			while(myRes.next()){
				pw.println(myRes.getString("gunname") + 
						"  $" + myRes.getInt("price") +
						"  " + myRes.getInt("capacity") +
						"  " + myRes.getString("typeofgun") + 
						"  " + myRes.getString("length") +
						"  " + myRes.getString("weight") +
						"  " + myRes.getInt("quantity") + 
						"  " + myRes.getString("outofservice") +
						"  " + myRes.getString("origincountry") +
						"  " + myRes.getString("yearmade") );
			}
			String add = "insert into guninfo (gunname, price, capacity, typeofgun, length, weight, quantity, outofservice, origincountry, yearmade) "
					+ "values ('ArmaLite AR-15', 5000, 20, 'Assault Rifle', '39-in', '6.55 lbs', 33, 'YES', 'USA', '1959')";
			myST.execute(add);
			myRes = myST.executeQuery("select * from guninfo");
			pw.println("Insert: Adding new gun ArmaLite AR-15 to the inventory at " + date.toString());
			pw.println("Iventory adding a new item" );
			while(myRes.next()){
				pw.println(myRes.getString("gunname") + 
						"  $" + myRes.getInt("price") +
						"  " + myRes.getInt("capacity") +
						"  " + myRes.getString("typeofgun") + 
						"  " + myRes.getString("length") +
						"  " + myRes.getString("weight") +
						"  " + myRes.getInt("quantity") + 
						"  " + myRes.getString("outofservice") +
						"  " + myRes.getString("origincountry") +
						"  " + myRes.getString("yearmade") );
			}
			myRes = myST.executeQuery("select gunname from guninfo where price > 1000");
			pw.println("Query: The following guns are over $1000: " + date.toString());
			while(myRes.next()){
				pw.println(myRes.getString("gunname"));
			}
			pw.close();
			String tt = "drop table guninfo";
			myST.execute(tt);
		}
		catch (Exception ex){
			System.out.println(ex);
		}
	}
}

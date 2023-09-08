package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import model.Donut;

public class DonutDAO {
	Connection con;
	PreparedStatement stmt;
	ResultSet rs;
	
	private void connect() throws NamingException, SQLException {
		Context context = new InitialContext();
		DataSource ds = (DataSource)context.lookup("java:comp/env/mariadb");
		this.con = ds.getConnection();
	}

	private void disconnect()  {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void connectCheck() {
		try {
			this.connect();
			System.out.println("OK");
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}finally {
			this.disconnect();
		}
	}
	
	public void insertOnt(Donut donut) {
		try {
			this.connect();
			stmt = con.prepareStatement("INSERT INTO donuts(name,price,imgname) VALUE(?,?,?)");
			stmt.setString(1, donut.getName());
			stmt.setInt(2, donut.getPrice());
			stmt.setString(3, donut.getImgname());
			stmt.execute();
		}catch(NamingException | SQLException e) {
			e.printStackTrace();
		}finally {
			this.disconnect();
		}
	}
	
	public List<Donut> findAll(){
		List<Donut> list = new ArrayList<Donut>();
		try {
			this.connect();
			stmt = con.prepareStatement("SELECT * FROM donuts");
			rs = stmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int price = rs.getInt("price");
				String imgname = rs.getString("imgname");
				Donut donut = new Donut(id,name,price,imgname);
				list.add(donut);
			}
		}catch(NamingException | SQLException e) {
			e.printStackTrace();
		}finally {
			this.disconnect();
		}
		return list;
	}
}

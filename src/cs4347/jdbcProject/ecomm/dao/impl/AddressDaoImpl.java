/* NOTICE: All materials provided by this project, and materials derived 
 * from the project, are the property of the University of Texas. 
 * Project materials, or those derived from the materials, cannot be placed 
 * into publicly accessible locations on the web. Project materials cannot 
 * be shared with other project teams. Making project materials publicly 
 * accessible, or sharing with other project teams will result in the 
 * failure of the team responsible and any team that uses the shared materials. 
 * Sharing project materials or using shared materials will also result 
 * in the reporting of every team member to the Provost Office for academic 
 * dishonesty. 
 */ 

package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cs4347.jdbcProject.ecomm.dao.AddressDAO;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class AddressDaoImpl implements AddressDAO
{
	
	private static final String insertSQL = 
            "INSERT INTO address (address1, address2, city, state, zipcode, customer_id) VALUES (?, ?, ?, ?, ?, ?);";
	
	private static final String retrieveByCustomerIDSQL =
    		"SELECT a.* FROM address a WHERE a.customer_id = ?";
	
	private static final String deleteByCustomerIDSQL =
    		"DELETE FROM address WHERE customer_id = ?";
	
    @Override
    public Address create(Connection connection, Address address, Long customerID) throws SQLException, DAOException
    {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(insertSQL);
            ps.setString(1, address.getAddress1());
            ps.setString(2, address.getAddress2());
            ps.setString(3, address.getCity());
            ps.setString(4, String.valueOf(address.getState()));
            ps.setString(5, address.getZipcode());
            ps.setLong(6, customerID);
            ps.executeUpdate();

            
            return address;
        }
        finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public Address retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try {
        	ps = connection.prepareStatement(retrieveByCustomerIDSQL);
        	ps.setLong(1, customerID);
        	
        	ResultSet rs = ps.executeQuery();
        	
        	if(rs.next()) {
        		return fillFromResultSet(rs);
        	} else {
        		return null;
        	}
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
        		ps.close();
        	}
        }
    }

    @Override
    public void deleteForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(deleteByCustomerIDSQL);
            ps.setLong(1, customerID);
            ps.executeUpdate();
        }
        finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }
    
    private Address fillFromResultSet(ResultSet rs) throws SQLException
    {
    	Address address = new Address();
    	address.setAddress1(rs.getString("a.address1"));
    	address.setAddress2(rs.getString("a.address2"));
    	address.setCity(rs.getString("a.city"));
    	address.setState(rs.getString("a.state"));
    	address.setZipcode(rs.getString("a.zipcode"));
    	return address;
    }

}

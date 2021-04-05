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

import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.util.DAOException;


public class CreditCardDaoImpl implements CreditCardDAO
{
	
	private static final String insertSQL = 
            "INSERT INTO creditcard (ccnumber, name, expDate, securityCode, customer_id) VALUES (?, ?, ?, ?, ?);";
	
	private static final String retrieveByCustomerIDSQL =
    		"SELECT cc.* FROM creditcard cc WHERE cc.customer_id = ?";
	
	private static final String deleteByCustomerIDSQL =
    		"DELETE FROM creditcard WHERE customer_id = ?";
	
    @Override
    public CreditCard create(Connection connection, CreditCard creditCard, Long customerID) throws SQLException, DAOException
    {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(insertSQL);
            ps.setString(1, creditCard.getCcNumber());
            ps.setString(2, creditCard.getName());
            ps.setString(3, creditCard.getExpDate());
            ps.setString(4, creditCard.getSecurityCode());
            ps.setLong(5, customerID);
            ps.executeUpdate();

            
            return creditCard;
        }
        finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public CreditCard retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException
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
    
    private CreditCard fillFromResultSet(ResultSet rs) throws SQLException
    {
    	CreditCard creditCard = new CreditCard();
    	creditCard.setCcNumber(rs.getString("cc.ccNumber"));
    	creditCard.setExpDate(rs.getString("cc.expDate"));
    	creditCard.setName(rs.getString("cc.name"));
    	creditCard.setSecurityCode(rs.getString("cc.securityCode"));
    	
    	return creditCard;
    }


}

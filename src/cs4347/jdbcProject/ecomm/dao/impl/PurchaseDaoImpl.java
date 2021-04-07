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
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchaseDaoImpl implements PurchaseDAO {
	
	private static final String insertSQL = 
            "INSERT INTO purchase (product_id, customer_id, purchasedate, purchaseamount) VALUES (?, ?, ?, ?);";
	
	private static final String retrieveSQL = 
			"SELECT * FROM purchase WHERE id = ?";
	
	private static final String updateSQL = 
			"Update purchase SET product_id = (?), customer_id = (?), purchasedate = (?), purchaseAmount = (?) where id= (?);";
	
	private static final String deleteSQL = 
			"delete from purchase where id = (?);";
	
	private static final String retrieveForCustomerIDSQL = 
			"select id,product_id, purchasedate, purchaseamount from purchase where customer_id= (?);";
	
	private static final String retrieveForProductIDSQL = 
			"select id,customer_id, purchasedate, purchaseamount from purchase where product_id= (?);";
	
	private static final String retrievePurchaseSummarySQL = 
			"SELECT MIN(purchaseamount) as min,MAX(purchaseamount) as max,AVG(purchaseamount) as average from purchase where customer_id= (?);";
	
	@Override
	public Purchase create(Connection connection, Purchase purchase) throws SQLException, DAOException {
		if (purchase.getId() != null) {
			throw new DAOException("error purchase id is not null");
		}
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			
			ps.setLong(1, purchase.getProductID());
			ps.setLong(2, purchase.getCustomerID());
			ps.setDate(3, purchase.getPurchaseDate());
			ps.setDouble(4, purchase.getPurchaseAmount());
			
			ps.executeUpdate();
			
			ResultSet keys = ps.getGeneratedKeys();
			keys.next();
			purchase.setId((long) keys.getInt(1));
			return purchase;
			
		} finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
	}

	@Override
	public Purchase retrieve(Connection connection, Long id) throws SQLException, DAOException {
		if (id == null) {
			throw new DAOException("error id can't be null");
		}
		
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(retrieveSQL);
			ps.setLong(1, id);
			
			ResultSet rs = ps.executeQuery();
			
			Purchase purch = null;
			if(rs.next()) {
				purch = new Purchase();
				purch.setCustomerID((long) rs.getInt("customer_id"));
				purch.setPurchaseDate(rs.getDate("purchasedate"));
				purch.setPurchaseAmount(rs.getDouble("purchaseamount"));
				purch.setProductID((long) rs.getInt("product_id"));
				purch.setId(id);
			}
			return purch;
			
		} finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }			
		}
	}

	@Override
	public int update(Connection connection, Purchase purchase) throws SQLException, DAOException {
		if (purchase.getId() == null) {
			throw new DAOException("error purchase id is null");
		}
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateSQL);
			
			ps.setLong(1, purchase.getProductID());
			ps.setLong(2, purchase.getCustomerID());
			ps.setDate(3, purchase.getPurchaseDate());
			ps.setDouble(4, purchase.getPurchaseAmount());
			ps.setLong(5, purchase.getId());
			int amount = ps.executeUpdate();
			return amount;
			
		} finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
	}

	@Override
	public int delete(Connection connection, Long id) throws SQLException, DAOException {
		if (id == null) {
			throw new DAOException("error id can't be null");
		}
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(deleteSQL);
			ps.setLong(1, id);
			int amount = ps.executeUpdate();
			return amount;
			
		} finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Connection connection, Long customerID)
			throws SQLException, DAOException {
		if (customerID == null) {
			throw new DAOException("error customer ID can't be null");
		}
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(retrieveForCustomerIDSQL);
			ps.setLong(1, customerID);
			ResultSet rs = ps.executeQuery();
			List<Purchase> purchlist = new ArrayList<Purchase>();
			while (rs.next()) {
				Purchase purch = new Purchase();
				purch.setCustomerID(customerID);
				purch.setPurchaseDate(rs.getDate("purchasedate"));
				purch.setPurchaseAmount(rs.getDouble("purchaseamount"));
				purch.setProductID((long) rs.getInt("product_id"));
				purch.setId((long) rs.getInt("id"));
				purchlist.add(purch);
			}
			return purchlist;
			
		} finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
	}

	@Override
	public List<Purchase> retrieveForProductID(Connection connection, Long productID)
			throws SQLException, DAOException {
		if (productID == null) {
			throw new DAOException("error product ID can't be null");
		}
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(retrieveForProductIDSQL);
			ps.setLong(1, productID);
			ResultSet rs = ps.executeQuery();
			List<Purchase> purchlist = new ArrayList<Purchase>();
			while (rs.next()) {
				Purchase purch = new Purchase();
				purch.setCustomerID((long) rs.getInt("customer_id"));
				purch.setPurchaseDate(rs.getDate("purchasedate"));
				purch.setPurchaseAmount(rs.getDouble("purchaseamount"));
				purch.setProductID(productID);
				purch.setId((long) rs.getInt("id"));
				purchlist.add(purch);
			}
			return purchlist;
			
		} finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Connection connection, Long customerID)
			throws SQLException, DAOException {
		if (customerID == null) {
			throw new DAOException("error customer ID can't be null");
		}
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(retrievePurchaseSummarySQL);
			ps.setLong(1, customerID);
			ResultSet rs = ps.executeQuery();
			PurchaseSummary sum = new PurchaseSummary();
			rs.next();
			sum.minPurchase = (float) rs.getDouble("min");
			sum.maxPurchase = (float) rs.getDouble("max");
			sum.avgPurchase = (float) rs.getDouble("average");
			return sum;
		} finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
	}

}

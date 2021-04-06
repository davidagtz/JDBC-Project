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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchaseDaoImpl implements PurchaseDAO
{

	@Override
    public Purchase create(Connection connection, Purchase purchase) throws SQLException, DAOException
    {
    	if(purchase.getId() != null) {
		throw new DAOException("error purchase id is not null");
	}
	//connection.setAutoCommit(false);
	Statement statement = connection.createStatement();
	statement.executeUpdate("INSERT INTO purchase (product_id, customer_id, purchasedate, purchaseamount) "
			+ "values ("+purchase.getProductID()+","+purchase.getCustomerID()+",'"+purchase.getPurchaseDate()+"',"+purchase.getPurchaseAmount()+");",Statement.RETURN_GENERATED_KEYS);
	//connection.commit();
	ResultSet  keys = statement.getGeneratedKeys();
	keys.next();
	purchase.setId((long) keys.getInt(1));
	//connection.setAutoCommit(true);
	return purchase;
    }

    @Override
    public Purchase retrieve(Connection connection, Long id) throws SQLException, DAOException
    {
    	if(id == null) {
		throw new DAOException("error id can't be null");
	}
	Statement statement = connection.createStatement();
	ResultSet rs = statement.executeQuery("select product_id, customer_id, purchasedate, purchaseamount  "
			+ "from Purchase where id="+id+";");
	rs.next();
	Purchase purch = new Purchase();
	purch.setCustomerID((long) rs.getInt("customer_id"));
	purch.setPurchaseDate(rs.getDate("purchasedate"));
	purch.setPurchaseAmount(rs.getDouble("purchaseamount"));
	purch.setProductID((long) rs.getInt("product_id"));
	purch.setId(id);
	return purch;
    }

    @Override
    public int update(Connection connection, Purchase purchase) throws SQLException, DAOException
    {
    	if(purchase.getId() == null) {
		throw new DAOException("error purchase id is null");
	}
	//connection.setAutoCommit(false);
	Statement statement = connection.createStatement();
	int upamount= statement.executeUpdate("Update purchase "
			+ "SET product_id = "+purchase.getProductID()+", customer_id = "+purchase.getCustomerID()+", purchasedate = '"+purchase.getPurchaseDate()+"', purchaseAmount = "+purchase.getPurchaseAmount()+
			"where id="+purchase.getId()+";");
	//connection.commit();
	//connection.setAutoCommit(true);
	return upamount;
    }

    @Override
    public int delete(Connection connection, Long id) throws SQLException, DAOException
    {
    	if(id == null) {
		throw new DAOException("error id can't be null");
	}
	//connection.setAutoCommit(false);
	Statement statement = connection.createStatement();
	int delamount= statement.executeUpdate("delete from purchase where id = "+id+";");
	//connection.commit();
	//connection.setAutoCommit(true);
	return delamount;
    }

    @Override
    public List<Purchase> retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException
    {
    	if(customerID == null) {
		throw new DAOException("error customer ID can't be null");
	}
	Statement statement = connection.createStatement();
	ResultSet rs = statement.executeQuery("select id,product_id, purchasedate, purchaseamount  "
			+ "from purchase where customer_id="+customerID+";");
	List<Purchase> purchlist = new ArrayList<Purchase>();
	while(rs.next()) {
		Purchase purch = new Purchase();
		purch.setCustomerID(customerID);
		purch.setPurchaseDate(rs.getDate("purchasedate"));
		purch.setPurchaseAmount(rs.getDouble("purchaseamount"));
		purch.setProductID((long) rs.getInt("product_id"));
		purch.setId((long) rs.getInt("id"));
		purchlist.add(purch);
	}
	return purchlist;
    }

    @Override
    public List<Purchase> retrieveForProductID(Connection connection, Long productID) throws SQLException, DAOException
    {
    	if(productID == null) {
		throw new DAOException("error product ID can't be null");
	}
	Statement statement = connection.createStatement();
	ResultSet rs = statement.executeQuery("select id,customer_id, purchasedate, purchaseamount  "
			+ "from purchase where product_id="+productID+";");
	List<Purchase> purchlist = new ArrayList<Purchase>();
	while(rs.next()) {
		Purchase purch = new Purchase();
		purch.setCustomerID((long) rs.getInt("customer_id"));
		purch.setPurchaseDate(rs.getDate("purchasedate"));
		purch.setPurchaseAmount(rs.getDouble("purchaseamount"));
		purch.setProductID(productID);
		purch.setId((long) rs.getInt("id"));
		purchlist.add(purch);
	}
	return purchlist;
    }

    @Override
    public PurchaseSummary retrievePurchaseSummary(Connection connection, Long customerID) throws SQLException, DAOException
    {
    	if(customerID == null) {
		throw new DAOException("error customer ID can't be null");
	}
	Statement statement = connection.createStatement();
	ResultSet rs = statement.executeQuery("SELECT MIN(purchaseamount) as min,MAX(purchaseamount) as max,AVG(purchaseamount) as average"
			+ " from purchase where customer_id="+customerID+";");
	PurchaseSummary sum = new PurchaseSummary();
	rs.next();
	sum.minPurchase = (float) rs.getDouble("min");
	sum.maxPurchase = (float) rs.getDouble("max");
	sum.avgPurchase = (float) rs.getDouble("average");
	return sum;
    }
	
}

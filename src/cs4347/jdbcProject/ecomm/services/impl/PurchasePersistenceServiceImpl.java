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

package cs4347.jdbcProject.ecomm.services.impl;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.dao.impl.PurchaseDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchasePersistenceService;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	@Override
    public Purchase create(Purchase purchase) throws SQLException, DAOException
    {
        if(purchase.getId() != null) {
        	throw new DAOException("Error: pruchase id not null");
        }
		Connection connection = dataSource.getConnection();
		PurchaseDAO p = new PurchaseDaoImpl();
		connection.setAutoCommit(false);
		Purchase pur = null;
		try {
			pur = p.create(connection, purchase);
			connection.commit();
			return pur;
		}
		finally {
			connection.setAutoCommit(true);
			connection.close();
		}
    }

    @Override
    public Purchase retrieve(Long id) throws SQLException, DAOException
    {
    	if(id == null) {
        	throw new DAOException("Error: pruchase id not null");
        }
		Connection connection = dataSource.getConnection();
		PurchaseDAO p = new PurchaseDaoImpl();
		connection.setAutoCommit(false);
		Purchase pur = null;
		try {
			pur = p.retrieve(connection, id);
			connection.commit();
			return pur;
		}
		finally {
			connection.setAutoCommit(true);
			connection.close();
		}
    }

    @Override
    public int update(Purchase purchase) throws SQLException, DAOException
    {
    	if(purchase == null) {
        	throw new DAOException("Error: pruchase id not null");
        }
		Connection connection = dataSource.getConnection();
		PurchaseDAO p = new PurchaseDaoImpl();
		connection.setAutoCommit(false);
		int amount = 0;
		try {
			amount = p.update(connection, purchase);
			connection.commit();
			return amount;
		}
		finally {
			connection.setAutoCommit(true);
			connection.close();
		}
    }

    @Override
    public int delete(Long id) throws SQLException, DAOException
    {
    	if(id == null) {
        	throw new DAOException("Error: pruchase id not null");
        }
		Connection connection = dataSource.getConnection();
		PurchaseDAO p = new PurchaseDaoImpl();
		connection.setAutoCommit(false);
		int amount = 0;
		try {
			amount = p.delete(connection, id);
			connection.commit();
			return amount;
		}
		finally {
			connection.setAutoCommit(true);
			connection.close();
		}
    }

    @Override
    public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException
    {
		Connection connection = dataSource.getConnection();
		PurchaseDAO p = new PurchaseDaoImpl();
		connection.setAutoCommit(false);
		List<Purchase> list = null;
		try {
			list = p.retrieveForCustomerID(connection, customerID);
			connection.commit();
			return list;
		}
		finally {
			connection.setAutoCommit(true);
			connection.close();
		}
    }

    @Override
    public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException
    {
		Connection connection = dataSource.getConnection();
		PurchaseDAO p = new PurchaseDaoImpl();
		connection.setAutoCommit(false);
		PurchaseSummary ps = null;
		try {
			ps = p.retrievePurchaseSummary(connection, customerID);
			connection.commit();
			return ps;
		}
		finally {
			connection.setAutoCommit(true);
			connection.close();
		}
    }

    @Override
    public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException
    {
		Connection connection = dataSource.getConnection();
		PurchaseDAO p = new PurchaseDaoImpl();
		connection.setAutoCommit(false);
		List<Purchase> list = null;
		try {
			list = p.retrieveForProductID(connection, productID);
			connection.commit();
			return list;
		}
		finally {
			connection.setAutoCommit(true);
			connection.close();
		}
    }

    private DataSource dataSource;

	public PurchasePersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

}

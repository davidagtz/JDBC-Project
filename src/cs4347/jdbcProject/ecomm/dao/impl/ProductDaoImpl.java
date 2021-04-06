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

import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.util.DAOException;

/* Product
 * - id: Long
 * - prodName: String
 * - prodDescription: String
 * - prodCategory: Integer
 * - prodUPC: String
 */

public class ProductDaoImpl implements ProductDAO
{
//    private static final String insertSQL = 
//            "INSERT INTO product (prodName, prodDescription, prodCategory, prodUPC) VALUES (?, ?, ?, ?);";
//    
//    private static final String updateSQL =
//    		"UPDATE product SET prodName = ?, prodDescription = ?, prodCategory = ?, prodUPC = ? WHERE id = ?";
//    
//    private static final String deleteSQL =
//    		"DELETE FROM product WHERE id = ?";
//    
//    private static final String retrieveSQL =
//    		"SELECT * FROM product WHERE id = ?";
//    
//    private static final String retrieveByCategorySQL =
//    		"SELECT * FROM product WHERE prodCategory = ?";
//    
//    private static final String retrieveByProdUPCSQL =
//    		"SELECT * FROM product WHERE prodUPC = ?";
    private static final String insertSQL = 
            "INSERT INTO PRODUCT (prod_name, prod_description, prod_category, prod_upc) VALUES (?, ?, ?, ?);";
    
    private static final String updateSQL =
    		"UPDATE PRODUCT SET prod_name = ?, prod_description = ?, prod_category = ?, prod_upc = ? WHERE id = ?";
    
    private static final String deleteSQL =
    		"DELETE FROM PRODUCT WHERE id = ?";
    
    private static final String retrieveSQL =
    		"SELECT * FROM PRODUCT WHERE id = ?";
    
    private static final String retrieveByCategorySQL =
    		"SELECT * FROM PRODUCT WHERE prod_category = ?";
    
    private static final String retrieveByProdUPCSQL =
    		"SELECT * FROM PRODUCT WHERE prod_upc = ?";

    @Override
    public Product create(Connection connection, Product product) throws SQLException, DAOException
    {
    	if (product.getId() != null) {
            throw new DAOException("Trying to insert Product with NON-NULL ID");
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getProdName());
            ps.setString(2, product.getProdDescription());
            ps.setInt(3, product.getProdCategory());
            ps.setString(4, product.getProdUPC());
            ps.executeUpdate();

            // Copy the assigned ID to the product instance.
            ResultSet keyRS = ps.getGeneratedKeys();
            keyRS.next();
            int lastKey = keyRS.getInt(1);
            product.setId((long) lastKey);
            return product;
        }
        finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public Product retrieve(Connection connection, Long id) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try {
        	ps = connection.prepareStatement(retrieveSQL);
        	ps.setLong(1, id);
        	
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
    public int update(Connection connection, Product product) throws SQLException, DAOException
    {
    	if (product.getId() == null) {
            throw new DAOException("Trying to update Product with NULL ID");
        }
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(updateSQL);
            ps.setString(1, product.getProdName());
            ps.setString(2, product.getProdDescription());
            ps.setInt(3, product.getProdCategory());
            ps.setString(4, product.getProdUPC());
            ps.setLong(5, product.getId());
            return ps.executeUpdate();

        }
        finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }
    
    
    /* Product
     * - id: Long
     * - prodName: String
     * - prodDescription: String
     * - prodCategory: Integer
     * - prodUPC: String
     */

    @Override
    public int delete(Connection connection, Long id) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(deleteSQL);
            ps.setLong(1, id);
            return ps.executeUpdate();
        }
        finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        }
    }

    @Override
    public List<Product> retrieveByCategory(Connection connection, int category) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
    	List<Product> results = new ArrayList<>();
    	
        try {
        	ps = connection.prepareStatement(retrieveByCategorySQL);
        	ps.setInt(1, category);
        	
        	ResultSet rs = ps.executeQuery();
        	
        	while(rs.next()) {
        		Product product = fillFromResultSet(rs);
        		results.add(product);
        	}
        	
        	return results;
        }
        finally {
        	if (ps != null && !ps.isClosed()) {
        		ps.close();
        	}
        }
    }

    @Override
    public Product retrieveByUPC(Connection connection, String upc) throws SQLException, DAOException
    {
    	PreparedStatement ps = null;
        try {
        	ps = connection.prepareStatement(retrieveByProdUPCSQL);
        	ps.setString(1, upc);
        	
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

    private Product fillFromResultSet(ResultSet rs) throws SQLException
    {
    	Product product = new Product();
    	product.setId((long) rs.getInt("id"));
    	product.setProdName(rs.getString("prodName"));
    	product.setProdDescription(rs.getString("prodDescription"));
    	product.setProdCategory(rs.getInt("prodCategory"));
    	product.setProdUPC(rs.getString("prodUPC"));
//    	product.setId((long) rs.getInt("id"));
//    	product.setProdName(rs.getString("prod_name"));
//    	product.setProdDescription(rs.getString("prod_description"));
//    	product.setProdCategory(rs.getInt("prod_category"));
//    	product.setProdUPC(rs.getString("prod_upc"));
    	return product;
    }
}

package it.unibo.lmc.pjdbc.core.udt;

import it.unibo.lmc.pjdbc.utils.PSQLException;

import java.sql.Array;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;

public class PArray implements Array {
	
	
	public PArray() {
		
	}

	public PArray(Term t) {
		
		try {
		
			Struct list = (Struct)t;
			
			System.out.println(list);
			
			Iterator i = list.listIterator();	//se non è una lista qui fallisce...
			while(i.hasNext()){
				
				Term internalTerm = (Term)i.next();
				
				System.out.println(">>"+internalTerm);
				
				
			}
		
		} catch (Exception e) {
			System.out.println("errore: "+e);
		}
		
		
	}

	/**
	   * Retrieves the contents of the SQL <code>ARRAY</code> value designated 
	   * by this
	   * <code>Array</code> object in the form of an array in the Java
	   * programming language. This version of the method <code>getArray</code>
	   * uses the type map associated with the connection for customizations of 
	   * the type mappings.
	   *
	   * @return an array in the Java programming language that contains 
	   * the ordered elements of the SQL <code>ARRAY</code> value
	   * designated by this <code>Array</code> object
	   * @exception SQLException if an error occurs while attempting to
	   * access the array
	   * @since 1.2
	   */
	public Object getArray() throws PSQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	   * Retrieves the contents of the SQL <code>ARRAY</code> value designated by this 
	   * <code>Array</code> object.
	   * This method uses 
	   * the specified <code>map</code> for type map customizations
	   * unless the base type of the array does not match a user-defined 
	   * type in <code>map</code>, in which case it 
	   * uses the standard mapping. This version of the method
	   * <code>getArray</code> uses either the given type map or the standard mapping;
	   * it never uses the type map associated with the connection.
	   *
	   * @param map a <code>java.util.Map</code> object that contains mappings
	   *            of SQL type names to classes in the Java programming language
	   * @return an array in the Java programming language that contains the ordered 
	   *         elements of the SQL array designated by this object
	   * @exception SQLException if an error occurs while attempting to 
	   *                         access the array
	   * @since 1.2
	   */
	public Object getArray(Map<String, Class<?>> map) throws PSQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	   * Retrieves a slice of the SQL <code>ARRAY</code>
	   * value designated by this <code>Array</code> object, beginning with the
	   * specified <code>index</code> and containing up to <code>count</code> 
	   * successive elements of the SQL array.  This method uses the type map
	   * associated with the connection for customizations of the type mappings.
	   *
	   * @param index the array index of the first element to retrieve;
	   *              the first element is at index 1
	   * @param count the number of successive SQL array elements to retrieve
	   * @return an array containing up to <code>count</code> consecutive elements 
	   * of the SQL array, beginning with element <code>index</code>
	   * @exception SQLException if an error occurs while attempting to
	   * access the array
	   * @since 1.2
	   */
	public Object getArray(long index, int count) throws PSQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	   * Retreives a slice of the SQL <code>ARRAY</code> value 
	   * designated by this <code>Array</code> object, beginning with the specified
	   * <code>index</code> and containing up to <code>count</code>
	   * successive elements of the SQL array.  
	   * <P>
	   * This method uses 
	   * the specified <code>map</code> for type map customizations
	   * unless the base type of the array does not match a user-defined 
	   * type in <code>map</code>, in which case it 
	   * uses the standard mapping. This version of the method
	   * <code>getArray</code> uses either the given type map or the standard mapping;
	   * it never uses the type map associated with the connection.
	   *
	   * @param index the array index of the first element to retrieve;
	   *              the first element is at index 1
	   * @param count the number of successive SQL array elements to 
	   * retrieve
	   * @param map a <code>java.util.Map</code> object
	   * that contains SQL type names and the classes in
	   * the Java programming language to which they are mapped
	   * @return an array containing up to <code>count</code>
	   * consecutive elements of the SQL <code>ARRAY</code> value designated by this
	   * <code>Array</code> object, beginning with element 
	   * <code>index</code>
	   * @exception SQLException if an error occurs while attempting to
	   * access the array
	   * @since 1.2
	   */
	public Object getArray(long index, int count, Map<String, Class<?>> map) throws PSQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	   * Retrieves the JDBC type of the elements in the array designated
	   * by this <code>Array</code> object.
	   *
	   * @return a constant from the class {@link java.sql.Types} that is
	   * the type code for the elements in the array designated by this
	   * <code>Array</code> object
	   * @exception SQLException if an error occurs while attempting
	   * to access the base type 
	   * @since 1.2
	   */
	public int getBaseType() throws PSQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	   * Retrieves the SQL type name of the elements in 
	   * the array designated by this <code>Array</code> object.
	   * If the elements are a built-in type, it returns
	   * the database-specific type name of the elements. 
	   * If the elements are a user-defined type (UDT),
	   * this method returns the fully-qualified SQL type name.
	   *
	   * @return a <code>String</code> that is the database-specific
	   * name for a built-in base type; or the fully-qualified SQL type
	   * name for a base type that is a UDT
	   * @exception SQLException if an error occurs while attempting
	   * to access the type name
	   * @since 1.2
	   */
	public String getBaseTypeName() throws PSQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	   * Retrieves a result set that contains the elements of the SQL 
	   * <code>ARRAY</code> value
	   * designated by this <code>Array</code> object.  If appropriate,
	   * the elements of the array are mapped using the connection's type 
	   * map; otherwise, the standard mapping is used.
	   * <p>
	   * The result set contains one row for each array element, with
	   * two columns in each row.  The second column stores the element
	   * value; the first column stores the index into the array for 
	   * that element (with the first array element being at index 1). 
	   * The rows are in ascending order corresponding to
	   * the order of the indices.
	   *
	   * @return a {@link ResultSet} object containing one row for each
	   * of the elements in the array designated by this <code>Array</code>
	   * object, with the rows in ascending order based on the indices.
	   * @exception SQLException if an error occurs while attempting to
	   * access the array
	   * @since 1.2
	   */
	public ResultSet getResultSet() throws PSQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	   * Retrieves a result set that contains the elements of the SQL 
	   * <code>ARRAY</code> value designated by this <code>Array</code> object.
	   * This method uses 
	   * the specified <code>map</code> for type map customizations
	   * unless the base type of the array does not match a user-defined 
	   * type in <code>map</code>, in which case it 
	   * uses the standard mapping. This version of the method
	   * <code>getResultSet</code> uses either the given type map or the standard mapping;
	   * it never uses the type map associated with the connection.
	   * <p>
	   * The result set contains one row for each array element, with
	   * two columns in each row.  The second column stores the element
	   * value; the first column stores the index into the array for 
	   * that element (with the first array element being at index 1). 
	   * The rows are in ascending order corresponding to
	   * the order of the indices.
	   *
	   * @param map contains the mapping of SQL user-defined types to 
	   * classes in the Java programming language
	   * @return a <code>ResultSet</code> object containing one row for each
	   * of the elements in the array designated by this <code>Array</code>
	   * object, with the rows in ascending order based on the indices.
	   * @exception SQLException if an error occurs while attempting to
	   * access the array
	   * @since 1.2
	   */
	public ResultSet getResultSet(Map<String, Class<?>> map) throws PSQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	   * Retrieves a result set holding the elements of the subarray that
	   * starts at index <code>index</code> and contains up to 
	   * <code>count</code> successive elements.  This method uses
	   * the connection's type map to map the elements of the array if
	   * the map contains an entry for the base type. Otherwise, the
	   * standard mapping is used.
	   * <P>
	   * The result set has one row for each element of the SQL array
	   * designated by this object, with the first row containing the 
	   * element at index <code>index</code>.  The result set has
	   * up to <code>count</code> rows in ascending order based on the
	   * indices.  Each row has two columns:  The second column stores
	   * the element value; the first column stores the index into the
	   * array for that element.
	   *
	   * @param index the array index of the first element to retrieve;
	   *              the first element is at index 1
	   * @param count the number of successive SQL array elements to retrieve
	   * @return a <code>ResultSet</code> object containing up to
	   * <code>count</code> consecutive elements of the SQL array
	   * designated by this <code>Array</code> object, starting at
	   * index <code>index</code>.
	   * @exception SQLException if an error occurs while attempting to
	   * access the array
	   * @since 1.2
	   */
	public ResultSet getResultSet(long index, int count) throws PSQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	   * Retrieves a result set holding the elements of the subarray that
	   * starts at index <code>index</code> and contains up to
	   * <code>count</code> successive elements.
	   * This method uses 
	   * the specified <code>map</code> for type map customizations
	   * unless the base type of the array does not match a user-defined 
	   * type in <code>map</code>, in which case it 
	   * uses the standard mapping. This version of the method
	   * <code>getResultSet</code> uses either the given type map or the standard mapping;
	   * it never uses the type map associated with the connection.
	   * <P>
	   * The result set has one row for each element of the SQL array
	   * designated by this object, with the first row containing the
	   * element at index <code>index</code>.  The result set has   
	   * up to <code>count</code> rows in ascending order based on the
	   * indices.  Each row has two columns:  The second column stores  
	   * the element value; the first column stroes the index into the
	   * array for that element.
	   *
	   * @param index the array index of the first element to retrieve;
	   *              the first element is at index 1
	   * @param count the number of successive SQL array elements to retrieve
	   * @param map the <code>Map</code> object that contains the mapping
	   * of SQL type names to classes in the Java(tm) programming language
	   * @return a <code>ResultSet</code> object containing up to               
	   * <code>count</code> consecutive elements of the SQL array
	   * designated by this <code>Array</code> object, starting at
	   * index <code>index</code>.
	   * @exception SQLException if an error occurs while attempting to
	   * access the array
	   * @since 1.2
	   */
	public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws PSQLException {
		// TODO Auto-generated method stub
		return null;
	}

}

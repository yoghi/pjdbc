package it.unibo.lmc.pjdbc.core.utils;


//public class PTypes {
//
//	static final public int getSqlType(String typeName){
//		
//		//java.sql.Types
//		
//		return 0;
//		
//		
//		
//	}
//}	
	
public enum PTypes {
	
	STRING (java.sql.Types.VARCHAR),
	INT ( java.sql.Types.INTEGER ),
	ARRAY ( java.sql.Types.ARRAY ),
	REAL ( java.sql.Types.REAL );

	
	private final int jType;
	
	PTypes(int javaType) {
        this.jType = javaType;
    }
	
	public int getSqlType(){
		return this.jType;
	}
}

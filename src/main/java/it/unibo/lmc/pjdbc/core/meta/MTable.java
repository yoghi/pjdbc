package it.unibo.lmc.pjdbc.core.meta;


public class MTable {
	
	//private ArrayList<String[]> columns = new ArrayList<String[]>();
	String[][] columns;
	
	public MTable(int columNumber) {
		 this.columns = new String[columNumber][2];
	}
	
	public void addField(int position, String name, String type) {
	
		if ( position < 0 ) {
			//TODO eccezzione
		}
		
		String[] column = new String[2];
		column[0] = name;
		column[1] = type;
	
		if ( position < this.columns.length ) {
			this.columns[position] = column;
		} else {
			
			int newsize = position+1;
			String[][] temp = new String[newsize][2];
			
			for(int i = 0; i < position; i++ ){
				temp[i] = this.columns[i];
			}
			
			temp[position] = column;
			
			this.columns = temp;
		}
		
	}
	
	public String toString(){
		
		StringBuffer buffer = new StringBuffer();
		
		for ( int i = 0; i < this.columns.length; i++ ){
			
			if ( this.columns[i][0] != null ){
			
				buffer.append(i);
				buffer.append(":");
				buffer.append(this.columns[i][0].toString());
				buffer.append(":");
				buffer.append(this.columns[i][1].toString());
				buffer.append("\n");
				
			} else {
				
			}
		}
		
		return buffer.toString();
	}

}

//if ( field_type.toString().equalsIgnoreCase("int") ) {
//int ty = java.sql.Types.INTEGER;
//}
//string => java.sql.Types.VARCHAR

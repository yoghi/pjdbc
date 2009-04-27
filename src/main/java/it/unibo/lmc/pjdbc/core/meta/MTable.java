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
			this.columns[position] = column;		//OVERRIDE??
		} else {
			
			try { 
				
				int newsize = position+1;
				String[][] temp = new String[newsize][2];
				
				for(int i = 0; i < this.columns.length; i++ ){
					temp[i] = this.columns[i];
				}
				
				temp[position] = column;
				this.columns = temp;
		
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public String toString(){
		
		StringBuffer buffer = new StringBuffer();
		
		for ( int i = 0; i < this.columns.length; i++ ){
			
			buffer.append(i);
			buffer.append(":");
			
			if ( this.columns[i][0] != null ){
			
				buffer.append(this.columns[i][0].toString());
				buffer.append(":");
				buffer.append(this.columns[i][1].toString());
				
			} else {
				buffer.append("unknown:unknown");
			}
			
			buffer.append("\n");
		}
		
		return buffer.toString();
	}
	
	/**
	 * Quante colonne ha questa tabella
	 * @return numero di colonne
	 */
	public int numColum(){
		return this.columns.length;
	}
	
	/**
	 * Verifico se la colonna X è una colonna di numeri
	 * @param position posizione della colonna nella tabella
	 * @return vero se la colonna è composta da numeri
	 */
	public boolean columnIsNumber(int position){
		
		if ( this.columns[position][1].equalsIgnoreCase("int") ) return true;
		if ( this.columns[position][1].equalsIgnoreCase("double") ) return true;
		if ( this.columns[position][1].equalsIgnoreCase("float") ) return true;
		return false;
		
	}

}

//if ( field_type.toString().equalsIgnoreCase("int") ) {
//int ty = java.sql.Types.INTEGER;
//}
//string => java.sql.Types.VARCHAR

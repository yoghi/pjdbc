package it.unibo.lmc.pjdbc.core.meta;


public class MTable {
	
	String[][] tcolumns;
	private String tname;
	
	public MTable(String name, int columNumber) {
		 this.tcolumns = new String[columNumber][2];
		 this.tname = name;
	}
	
	public void addField(int position, String name, String type) {
	
		if ( position < 0 ) {
			//TODO eccezzione
		}
		
		String[] column = new String[2];
		column[0] = name;
		column[1] = type;
	
		if ( position < this.tcolumns.length ) {
			this.tcolumns[position] = column;		//OVERRIDE??
		} else {
			
			try { 
				
				int newsize = position+1;
				String[][] temp = new String[newsize][2];
				
				for(int i = 0; i < this.tcolumns.length; i++ ){
					temp[i] = this.tcolumns[i];
				}
				
				temp[position] = column;
				this.tcolumns = temp;
		
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public String toString(){
		
		StringBuffer buffer = new StringBuffer();
		
		for ( int i = 0; i < this.tcolumns.length; i++ ){
			
			buffer.append(i);
			buffer.append(":");
			
			if ( this.tcolumns[i][0] != null ){
			
				buffer.append(this.tcolumns[i][0].toString());
				buffer.append(":");
				buffer.append(this.tcolumns[i][1].toString());
				
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
		return this.tcolumns.length;
	}
	
	/**
	 * Verifico se la colonna X è una colonna di numeri
	 * @param position posizione della colonna nella tabella
	 * @return vero se la colonna è composta da numeri
	 */
	public boolean columnIsNumber(int position){
		
		if ( this.tcolumns[position][1].equalsIgnoreCase("int") || this.tcolumns[position][1].equalsIgnoreCase("double") || this.tcolumns[position][1].equalsIgnoreCase("float") ) return true;
		return false;
			
	}

	public int containsField(String columnName) {
		for (int i = 0; i < tcolumns.length; i++) {
			
			if ( tcolumns[i][0] != null ){
				if ( tcolumns[i][0].equalsIgnoreCase(columnName) ) return i;
			} else {
				//TODO: non ci sono i metadati... come mi comporto??
			}
			
		}
		return -1;
	}

}

//if ( field_type.toString().equalsIgnoreCase("int") ) {
//int ty = java.sql.Types.INTEGER;
//}
//string => java.sql.Types.VARCHAR

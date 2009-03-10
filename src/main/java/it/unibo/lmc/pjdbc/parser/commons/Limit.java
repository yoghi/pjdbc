package it.unibo.lmc.pjdbc.parser.commons;

public class Limit {

	long rowCount = 0;
	long offset = 0;
	
	
	/**
	 * @return the rowCount
	 */
	public long getRowCount() {
		return rowCount;
	}

	/**
	 * @return the offset
	 */
	public long getOffset() {
		return offset;
	}

	public void setOffset(long off){
		if ( off > 0 ) this.offset = off;
	}
	
	public void setRowCount(long row){
		if ( row > 0 ) this.rowCount = row;
	}
	
	public String toString(){
		return "["+this.offset+","+this.rowCount+"]";
	}
	
}

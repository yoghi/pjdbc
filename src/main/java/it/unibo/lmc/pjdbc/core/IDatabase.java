package it.unibo.lmc.pjdbc.core;

import java.sql.DatabaseMetaData;

public interface IDatabase {

	IDatabase getSnapshot();
	
	boolean joinSnapshot(IDatabase snapshot);

	DatabaseMetaData getMetaData();

}

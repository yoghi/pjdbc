package it.unibo.lmc.pjdbc.core;

public interface IDatabase {

	IDatabase getSnapshot();
	
	boolean joinSnapshot(IDatabase snapshot);

}

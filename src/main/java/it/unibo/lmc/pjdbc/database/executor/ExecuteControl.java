package it.unibo.lmc.pjdbc.database.executor;

import it.unibo.lmc.pjdbc.database.command.PResultSet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecuteControl {
	
	private ExecutorService factory;

	public ExecuteControl() {		
		factory = Executors.newCachedThreadPool();
	}
	
	public Future<PResultSet> execute(ExecuteChild task){
		return (Future<PResultSet>) factory.submit(task);
	}
	
}

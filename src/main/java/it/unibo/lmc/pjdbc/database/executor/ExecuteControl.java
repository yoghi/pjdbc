package it.unibo.lmc.pjdbc.database.executor;

import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ExecuteControl {
	
	private ExecutorService factory;

	public ExecuteControl() {		
		factory = Executors.newCachedThreadPool();
	}
	
	public PResultSet execute(ExecuteChild task) throws PSQLException {
		
		try{
		
			FutureTask<PResultSet> fTask = new FutureTask<PResultSet>(task);
			factory.execute(fTask);
			
			while(!fTask.isDone()){
				Thread.sleep(1);	
			}
			
			return fTask.get();
		
		} catch (InterruptedException e) {
			throw new PSQLException("Interrupted execution", PSQLState.SYSTEM_ERROR);
		} catch (ExecutionException e) {
			throw new PSQLException(e.getLocalizedMessage(), PSQLState.SYSTEM_ERROR);
		}
	}
	
}

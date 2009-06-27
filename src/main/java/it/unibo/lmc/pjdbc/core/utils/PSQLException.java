package it.unibo.lmc.pjdbc.core.utils;


import java.sql.SQLException;

@SuppressWarnings("serial")
public class PSQLException extends SQLException
{

    public PSQLException(String msg, PSQLState state, Throwable cause)
    {
        super(msg, state == null ? null : state.getState());
        initCause(cause);
    }

    public PSQLException(String msg, PSQLState state)
    {
        this(msg, state, null);
    }

}
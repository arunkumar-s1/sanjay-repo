package com.msf.libsb.connection;
import java.sql.Connection;
import java.sql.SQLException;

public interface DBPool 
{
	public Connection getConnection() throws SQLException;
	public Connection getQuoteDBConnection() throws SQLException;
	public Connection getPushDBConnection() throws SQLException;
	public Connection getStockBasketDBConnection() throws SQLException;
	public Connection getSamcoConnection() throws SQLException;
	public Connection getSamcoSessionConnection() throws SQLException;
	//	public Connection getMarginDBConnection() throws SQLException;
}
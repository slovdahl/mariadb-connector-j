/*
MariaDB Client for Java

Copyright (c) 2012-2014 Monty Program Ab.

This library is free software; you can redistribute it and/or modify it under
the terms of the GNU Lesser General Public License as published by the Free
Software Foundation; either version 2.1 of the License, or (at your option)
any later version.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
for more details.

You should have received a copy of the GNU Lesser General Public License along
with this library; if not, write to Monty Program Ab info@montyprogram.com.

This particular MariaDB Client for Java file is work
derived from a Drizzle-JDBC. Drizzle-JDBC file which is covered by subject to
the following copyright and notice provisions:

Copyright (c) 2009-2011, Marcus Eriksson, Trond Norbye, Stephane Giron

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:
Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of the driver nor the names of its contributors may not be
used to endorse or promote products derived from this software without specific
prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS  AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
OF SUCH DAMAGE.
*/

package org.mariadb.jdbc;

import org.mariadb.jdbc.internal.stream.PrepareSqlException;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public class MariaDbPrepareStatementFacade implements PreparedStatement {
    
    private PreparedStatement preparedStatement;
    private boolean isPrepared = true;

    /**
     * Facade that permit in case of fail PREPARE to switch to Client prepared statement (that doesn't need prepare).
     *
     * @param sql sql command
     * @param resultSetScrollType resultSet scroll type
     * @param connection current connection
     * @throws SQLException if exception occur
     */
    public MariaDbPrepareStatementFacade(final String sql, final int resultSetScrollType,
                                                           MariaDbConnection connection) throws SQLException {
        preparedStatement = new MariaDbServerPreparedStatement(connection, sql, resultSetScrollType, false);
    }

    private void clientFailover() throws SQLException {
        MariaDbServerPreparedStatement currentPrepStmt = ((MariaDbServerPreparedStatement) preparedStatement);
        MariaDbClientPreparedStatement newPrepStmt = new MariaDbClientPreparedStatement(
                (MariaDbConnection) currentPrepStmt.getConnection(),
                currentPrepStmt.sql, currentPrepStmt.resultSetScrollType);
        newPrepStmt.initializeFallbackClient(currentPrepStmt);

        this.isPrepared = false;
        this.preparedStatement = newPrepStmt;

    }

    @Override
    public void clearParameters() throws SQLException {
        preparedStatement.clearParameters();
    }

    @Override
    public boolean execute() throws SQLException {
        try {
            return preparedStatement.execute();
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.execute();
        }
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        try {
            return preparedStatement.execute(sql);
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.execute(sql);
        }
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        try {
            return preparedStatement.execute(sql, autoGeneratedKeys);
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.execute(sql, autoGeneratedKeys);
        }
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        try {
            return preparedStatement.execute(sql, columnIndexes);
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.execute(sql, columnIndexes);
        }
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        try {
            return preparedStatement.execute(sql, columnNames);
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.execute(sql, columnNames);
        }
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        try {
            return preparedStatement.executeQuery();
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.executeQuery();
        }
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        try {
            return preparedStatement.executeQuery(sql);
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.executeQuery(sql);
        }
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        try {
            return preparedStatement.executeUpdate(sql);
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.executeUpdate(sql);
        }
    }

    @Override
    public int executeUpdate() throws SQLException {
        try {
            return preparedStatement.executeUpdate();
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.executeUpdate();
        }
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        try {
            return preparedStatement.executeUpdate(sql, autoGeneratedKeys);
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.executeUpdate(sql, autoGeneratedKeys);
        }
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        try {
            return preparedStatement.executeUpdate(sql, columnIndexes);
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.executeUpdate(sql, columnIndexes);
        }
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        try {
            return preparedStatement.executeUpdate(sql, columnNames);
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.executeUpdate(sql, columnNames);
        }
    }

    @Override
    public int[] executeBatch() throws SQLException {
        try {
            return preparedStatement.executeBatch();
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.executeBatch();
        }
    }


    @Override
    public void addBatch() throws SQLException {
        preparedStatement.addBatch();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        throw new SQLException("Cannot do addBatch(String) on preparedStatement");
    }

    @Override
    public void setArray(int parameterIndex, Array array) throws SQLException {
        preparedStatement.setArray(parameterIndex, array);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        preparedStatement.setAsciiStream(parameterIndex, inputStream, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream inputStream, int length) throws SQLException {
        preparedStatement.setAsciiStream(parameterIndex, inputStream, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream inputStream) throws SQLException {
        preparedStatement.setAsciiStream(parameterIndex, inputStream);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        preparedStatement.setBinaryStream(parameterIndex, inputStream, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream inputStream) throws SQLException {
        preparedStatement.setBinaryStream(parameterIndex, inputStream);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream inputStream, int length) throws SQLException {
        preparedStatement.setBinaryStream(parameterIndex, inputStream, length);
    }

    @Override
    public void setBlob(int parameterIndex, Blob blob) throws SQLException {
        preparedStatement.setBlob(parameterIndex, blob);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        preparedStatement.setBinaryStream(parameterIndex, inputStream, length);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        preparedStatement.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean booleanValue) throws SQLException {
        preparedStatement.setBoolean(parameterIndex, booleanValue);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal bigDecimalValue) throws SQLException {
        preparedStatement.setBigDecimal(parameterIndex, bigDecimalValue);
    }

    @Override
    public void setByte(int parameterIndex, byte byteValue) throws SQLException {
        preparedStatement.setByte(parameterIndex, byteValue);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] bytes) throws SQLException {
        preparedStatement.setBytes(parameterIndex, bytes);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        preparedStatement.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        preparedStatement.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        preparedStatement.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        preparedStatement.setClob(parameterIndex, reader);
    }

    @Override
    public void setClob(int parameterIndex, Clob clob) throws SQLException {
        preparedStatement.setClob(parameterIndex, clob);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        preparedStatement.setClob(parameterIndex, reader, length);
    }

    @Override
    public void setDate(int parameterIndex, Date date, Calendar cal) throws SQLException {
        preparedStatement.setDate(parameterIndex, date, cal);
    }

    @Override
    public void setDate(int parameterIndex, Date dateValue) throws SQLException {
        preparedStatement.setDate(parameterIndex, dateValue);
    }

    @Override
    public void setDouble(int parameterIndex, double doubleValue) throws SQLException {
        preparedStatement.setDouble(parameterIndex, doubleValue);
    }

    @Override
    public void setFloat(int parameterIndex, float floatValue) throws SQLException {
        preparedStatement.setFloat(parameterIndex, floatValue);
    }

    @Override
    public void setInt(int parameterIndex, int intValue) throws SQLException {
        preparedStatement.setInt(parameterIndex, intValue);
    }

    @Override
    public void setLong(int parameterIndex, long longValue) throws SQLException {
        preparedStatement.setLong(parameterIndex, longValue);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        preparedStatement.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        preparedStatement.setNCharacterStream(parameterIndex, value);
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        preparedStatement.setNClob(parameterIndex, value);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        preparedStatement.setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        preparedStatement.setNClob(parameterIndex, reader);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        preparedStatement.setNString(parameterIndex, value);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        preparedStatement.setNull(parameterIndex, sqlType, typeName);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        preparedStatement.setNull(parameterIndex, sqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object object, int targetSqlType, int scaleOrLength) throws SQLException {
        preparedStatement.setObject(parameterIndex, object, targetSqlType, scaleOrLength);
    }

    @Override
    public void setObject(int parameterIndex, Object obj, int targetSqlType) throws SQLException {
        preparedStatement.setObject(parameterIndex, obj, targetSqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object obj) throws SQLException {
        preparedStatement.setObject(parameterIndex, obj);
    }

    @Override
    public void setRowId(int parameterIndex, RowId rowid) throws SQLException {
        preparedStatement.setRowId(parameterIndex, rowid);
    }

    @Override
    public void setShort(int parameterIndex, short shortValue) throws SQLException {
        preparedStatement.setShort(parameterIndex, shortValue);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        preparedStatement.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setString(int parameterIndex, String stringValue) throws SQLException {
        preparedStatement.setString(parameterIndex, stringValue);
    }

    @Override
    public void setTime(int parameterIndex, Time time, Calendar cal) throws SQLException {
        preparedStatement.setTime(parameterIndex, time, cal);
    }

    @Override
    public void setTime(int parameterIndex, Time timeValue) throws SQLException {
        preparedStatement.setTime(parameterIndex, timeValue);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp timestampValue) throws SQLException {
        preparedStatement.setTimestamp(parameterIndex, timestampValue);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp timeStamp, Calendar cal) throws SQLException {
        preparedStatement.setTimestamp(parameterIndex, timeStamp, cal);
    }

    @Deprecated
    @Override
    public void setUnicodeStream(int parameterIndex, InputStream inputStream, int length) throws SQLException {
        preparedStatement.setUnicodeStream(parameterIndex, inputStream, length);
    }

    @Override
    public void setURL(int parameterIndex, URL url) throws SQLException {
        preparedStatement.setURL(parameterIndex, url);
    }

    @Override
    public void setRef(int parameterIndex, Ref ref) throws SQLException {
        preparedStatement.setRef(parameterIndex, ref);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        try {
            return preparedStatement.getMetaData();
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.getMetaData();
        }
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        try {
            return preparedStatement.getParameterMetaData();
        } catch (PrepareSqlException prepareException) {
            clientFailover();
            return preparedStatement.getParameterMetaData();
        }
    }

    @Override
    public void close() throws SQLException {
        preparedStatement.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return preparedStatement.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        preparedStatement.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return preparedStatement.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        preparedStatement.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        preparedStatement.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return preparedStatement.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        preparedStatement.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        preparedStatement.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return preparedStatement.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        preparedStatement.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        preparedStatement.setCursorName(name);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return preparedStatement.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return preparedStatement.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return preparedStatement.getMoreResults();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return preparedStatement.getMoreResults(current);
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        preparedStatement.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return preparedStatement.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        preparedStatement.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return preparedStatement.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return preparedStatement.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return preparedStatement.getResultSetType();
    }

    @Override
    public void clearBatch() throws SQLException {
        preparedStatement.clearBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return preparedStatement.getConnection();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return preparedStatement.getGeneratedKeys();
    }


    @Override
    public int getResultSetHoldability() throws SQLException {
        return preparedStatement.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return preparedStatement.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        preparedStatement.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return preparedStatement.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        preparedStatement.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return preparedStatement.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return preparedStatement.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return preparedStatement.isWrapperFor(iface);
    }

    @Override
    public String toString() {
        return preparedStatement.toString();
    }
}

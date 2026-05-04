package org.example.demotsbdts;

import java.sql.*;
import java.util.List;

public class OracleCodeRepository {
    private static final String JDBC_URL = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";
    private static final String USERNAME = "vector_user";
    private static final String PASSWORD = "vector_password";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    public void deleteAllChunks() throws SQLException {
        String sql = "DELETE FROM code_chunks";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            int deletedRows = statement.executeUpdate(sql);
            System.out.println("Randuri sterse din code_chunks: " + deletedRows);
        }
    }

    public void save(CodeChunk chunk) throws SQLException {
        String sql = """
                INSERT INTO code_chunks (
                    file_path,
                    package_name,
                    class_name,
                    symbol_name,
                    chunk_type,
                    source_code,
                    embedding
                )
                VALUES (?, ?, ?, ?, ?, ?, NULL)
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, chunk.getFilePath());
            statement.setString(2, chunk.getPackageName());
            statement.setString(3, chunk.getClassName());
            statement.setString(4, chunk.getSymbolName());
            statement.setString(5, chunk.getChunkType());
            statement.setString(6, chunk.getSourceCode());

            statement.executeUpdate();
        }
    }

    public void saveAll(List<CodeChunk> chunks) throws SQLException {
        String sql = """
                INSERT INTO code_chunks (
                    file_path,
                    package_name,
                    class_name,
                    symbol_name,
                    chunk_type,
                    source_code,
                    embedding
                )
                VALUES (?, ?, ?, ?, ?, ?, NULL)
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (CodeChunk chunk : chunks) {
                statement.setString(1, chunk.getFilePath());
                statement.setString(2, chunk.getPackageName());
                statement.setString(3, chunk.getClassName());
                statement.setString(4, chunk.getSymbolName());
                statement.setString(5, chunk.getChunkType());
                statement.setString(6, chunk.getSourceCode());

                statement.addBatch();
            }

            int[] insertedRows = statement.executeBatch();

            System.out.println("Metode inserate in DB: " + insertedRows.length);
        }
    }
}

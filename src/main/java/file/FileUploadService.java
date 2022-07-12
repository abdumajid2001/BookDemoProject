package file;

import connection.DBConnection;
import oracle.jdbc.OracleConnection;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.UUID;

public class FileUploadService {
    public Integer upload(String filePath, Part part) {
        String fileName = null;
        try {
            fileName = part.getSubmittedFileName();
            String uniqueFileName = UUID.randomUUID().toString();
            part.write(filePath + File.separator + uniqueFileName);
            return savedFileToDatabase(fileName, uniqueFileName);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer savedFileToDatabase(String fileName, String uniqueFileName) throws SQLException {
        OracleConnection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBConnection.getSingletonConnection();
            String generatedColumns[] = {"ID"};
            ps = connection.prepareStatement("insert into RND_TEST.files(UNIQUE_FILE_NAME, FILE_NAME) VALUES (?,?)", generatedColumns);
            ps.setString(1, uniqueFileName);
            ps.setString(2, fileName);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating failed");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) ps.close();
            connection.close();
            connection = null;
        }
    }
}

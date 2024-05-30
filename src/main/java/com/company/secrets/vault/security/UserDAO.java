package com.company.secrets.vault.security;




import com.company.secrest.vault.entity.AuditInfo;
import com.company.secrest.vault.user.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.company.secrest.vault.util.DBConnection;
import java.text.SimpleDateFormat;

public class UserDAO {

    public boolean registerUser(User user) {
    AuditInfo auditInfo = user.getAuditInfo();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    String formattedAddDate = dateFormat.format(auditInfo.getAddDate());
    String formattedUpdDate = (auditInfo.getUpdDate() != null) ? dateFormat.format(auditInfo.getUpdDate()) : null;  // updDate null kontrolü

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(
             "INSERT INTO users (username, password, email, isAdmin, addUser, addDate, updUser, updDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getEmail());
        pstmt.setBoolean(4, user.isIsAdmin());
        pstmt.setString(5, auditInfo.getAddUser());
        pstmt.setString(6, formattedAddDate);
        pstmt.setString(7, auditInfo.getUpdUser());
        pstmt.setString(8, formattedUpdDate);  // updDate null ise veritabanında NULL olarak set edilecek

        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    
    

}
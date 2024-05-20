package user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import util.DBConnection;

@Named("userMB")
@SessionScoped
public class UserMB implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<User> users;

    @PostConstruct
    public void init() {
        users = new ArrayList<>();
        loadUsers();
    }

    public void loadUsers() {
        users.clear();
        String sql = "SELECT * FROM users";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setIsAdmin(rs.getBoolean("isAdmin"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

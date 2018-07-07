import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
public class UserDao {
    private ConnectionMaker cm;
    private DataSource dataSource;
    private JdbcContext jdbcContext;

    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.cm = connectionMaker;
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        this.jdbcContext.workWithStatementStrategy(
                new StatementStrategy() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        // TODO Auto-generated method stub
                        PreparedStatement ps = c.prepareStatement("insert into user(id, name, password, level, login, recommend) values(?,?,?,?,?,?)");
                        ps.setString(1, user.getId());
                        ps.setString(2, user.getName());
                        ps.setString(3, user.getPassword());
                        ps.setInt(4, user.getLevel().intValue());
                        ps.setInt(5, user.getLogin());
                        ps.setInt(6, user.getRecommend());
                        return ps;
                    }
                }
        );
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("select * from User where id = ?");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        User user = new User();
        if(rs.next()) {
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
        }

        rs.close();
        ps.close();
        c.close();

        if(user == null) throw new EmptyResultDataAccessException(1);
        return user;
    }

    public void deleteAll() throws SQLException {
        this.jdbcContext.executeSql("delete from user");
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from user");
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch(SQLException e) {
            throw e;
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {

                }
            }
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if(c != null) {
                try {
                    c.close();
                } catch (SQLException e) {

                }
            }
        }
    }
}


// 이 상황에서 deleteall 메서드는 일을 수행하는 클라이언트 이고, 때문에 클라이언트는 이전에 daofactory 처럼 실제로
//일을수행하는 메서드에 의존성을 주입하고실행하는 역활을 담당해야 하는 것이지, 그 자체로서 기능을 제공하지 않게 해야
//의존성주입의 측면에서 클래스를 사용할 수 있다. 이 제어의역전을 통해 코드 재사용성을 높이고 인터페이스화 된 contextmethod를 잘 사용할 수 있게 된다.

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();// excuteQuery 대신 이 메서드를 써야함,  리턴값이 없는 경우에는 이것, 아니라면 excutequery
        } catch (SQLException e) {
            throw e;
        } finally {
            if( ps != null) {try { ps.close();} catch (SQLException e) {} }
            if( c != null) {try { c.close();} catch (SQLException e) {} }
        }
    }

    public void executeSql(final String query) throws SQLException {
        workWithStatementStrategy(
                new StatementStrategy() {// 익명 내부 클래스로, 인터페이스 형을 생성하고, 여기에 내부 클래스로 직접 재정의해서, 메서드 내부에서 실행시킬수 있게
                    @Override
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        // TODO Auto-generated method stub
                        return c.prepareStatement(query);
                    }
                }
        );
    }
}

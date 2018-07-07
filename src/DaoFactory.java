import javax.sql.DataSource;

        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.jdbc.datasource.SimpleDriverDataSource;
@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setDataSource(dataSource());
        return userDao;
    }
    @Bean
    public ConnectionMaker connectionMaker() {
        return new AConnectionMaker();
    }
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/Tobi?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("test123");

        return dataSource;
    }
}

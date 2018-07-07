import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class UserDaoConnectionCountingTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        // TODO Auto-generated method stub
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("122234f");
        user.setName("asdsad");
        user.setPassword("married");
        user.setLevel(Level.BASIC);
        user.setLogin(1);
        user.setRecommend(3123);

        dao.add(user);
        System.out.println(user.getId()+ "등록 성공");
        System.out.println(user.getName());

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());

        System.out.println(user2.getId() + "조회 성공");

        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println("Connection counter : " + ccm.getCounter());
    }
}

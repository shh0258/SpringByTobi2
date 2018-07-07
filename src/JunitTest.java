import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class JunitTest {
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
    @Autowired // bean 을 di 할때 어노테이션 명시로 자동화해줘서 가능 했던 것, 나아가서 applicationcontext 같은bean 의존성 을 전달하거나 주입하는 객체 또한 자신을 bean으로 등록하기 떄문에 같은 방식으로 호출 가능
    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        System.out.println(this);
//		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//		dao = context.getBean("userDao", UserDao.class);
        this.user1 = new User("123", "Tom", "spring1", Level.BASIC, 1, 0 );
        this.user2 = new User("1234", "Tyler", "spring2", Level.SILVER, 55, 10);
        this.user3 = new User("1235", "Seth", "spring3", Level.GOLD, 100, 40);
    }
    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        dao.add(user2);
        User user4 = dao.get(user1.getId());
        User user5 = dao.get(user2.getId());
        assertThat(user1.getId(), is(user4.getId()));
        assertThat(user2.getId(), is(user5.getId()));
        checkSameUser(user1, user4);
        checkSameUser(user2, user5);
    }
    public void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }

    @Test
    public void count() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected=EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException{
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
        dao.get("unknown_id");
    }
}


//@DirtiesContext 메서드에도 적용가능, application context를 그 클래스에 한정해서 바꾸고 사용할 때 사용

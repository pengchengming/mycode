import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;

public class mm {
	@Autowired
	private static SessionFactory sessionFactory;

	
	@BeforeClass
	public static void beforeClass() {
//	sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
	}
	@AfterClass
	public static void afterClass() {
		sessionFactory.close();
	}
}


package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Series;
import ch.jtaf.test.util.TestSessionContext;
import ch.jtaf.test.util.UnallowedTestSessionContext;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.WebApplicationException;
import java.util.List;

import static ch.jtaf.test.util.TestData.SERIES_ID;
import static ch.jtaf.test.util.TestData.SPACE_ID;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeriesResourceTest {

    private static SeriesResource sr;
    private static DataService ds;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory("jtaf-test");
        em = emf.createEntityManager();
        ds = new DataService();
        ds.em = em;
        sr = new SeriesResource();
        sr.dataService = ds;
    }

    @AfterClass
    public static void afterClass() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Before
    public void before() {
        em.clear();
        sr.sessionContext = new TestSessionContext();
    }

    @Test
    public void testList() throws Exception {
        List<Series> list = sr.list(SPACE_ID, null);

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testSave() throws Exception {
        Series s = ds.get(Series.class, SERIES_ID);
        assertNotNull(s);

        Series save = sr.save(s);
        assertNotNull(save);
    }

    @Test(expected = WebApplicationException.class)
    public void testSaveUnallowed() throws Exception {
        Series s = ds.get(Series.class, SERIES_ID);
        assertNotNull(s);

        sr.sessionContext = new UnallowedTestSessionContext();
        sr.save(s);
    }

    @Test
    public void testCopy() throws Exception {
        sr.copy(SERIES_ID, null);
    }

    @Test
    public void testGet() throws Exception {
        Series s = sr.get(SERIES_ID, null);
        assertNotNull(s);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetNotFound() throws Exception {
        sr.get(0L, null);
    }

    @Test
    public void testDelete() throws Exception {
        sr.delete(SERIES_ID);
    }

    @Test(expected = WebApplicationException.class)
    public void testDeleteUnallowed() throws Exception {
        sr.sessionContext = new UnallowedTestSessionContext();
        sr.delete(SERIES_ID);
    }
}

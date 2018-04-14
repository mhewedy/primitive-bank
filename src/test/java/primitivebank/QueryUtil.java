package primitivebank;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class QueryUtil {

    public static <T> T getObject(EntityManager em, String sql, Object... args) {

        List<T> objects = getList(em, sql, args);
        if (objects != null && !objects.isEmpty()) {
            return objects.get(0);
        }
        return null;
    }

    public static <T> List<T> getList(EntityManager em, String sql, Object... args) {
        Query query = em.createQuery(sql);

        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                query.setParameter(i, args[i]);
            }
        }
        return query.getResultList();
    }
}

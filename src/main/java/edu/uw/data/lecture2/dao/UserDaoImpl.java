package edu.uw.data.lecture2.dao;

import edu.uw.data.lecture2.model.*;
import org.hibernate.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.orm.hibernate4.support.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * simple single-table Jdbc example with try-resources and datasource
 */
@Repository(value = "userDao")

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

    static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    SessionFactory sessionFactory;

    public User findById(Integer id) {
        // return getHibernateTemplate().get(User.class, id);
        return (User) sessionFactory.getCurrentSession().get(User.class, id);
    }


    public List<User> findAll() {
        return (List<User>) getHibernateTemplate().find("from User");

        //  return sessionFactory.getCurrentSession().createQuery("from User").list();

    }


    @Override
    public User findByUsername(String uname) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "SELECT u FROM User u WHERE u.userName LIKE :uname");
        query.setParameter("uname", uname);
        User  user =(User) query.uniqueResult();
        return user;

    }

    @Override
    //@Transactional(readOnly = true)
    public User save(User user) {
        sessionFactory.getCurrentSession().saveOrUpdate(user);
        log.info("saved " + user.getId());
        return user;
    }


    public void delete (User user){
        if (user.getId()!=null) {
            sessionFactory.getCurrentSession().delete(user);
        }else if (user.getUserName()!=null) {
            sessionFactory.getCurrentSession()
                    .createQuery("DELETE FROM User u WHERE u.userName = :username")
                    .setString("username", user.getUserName())
                    .executeUpdate();

        } else {
            throw new IllegalArgumentException("User does not contain identifying information "+user);
        }
    }


}

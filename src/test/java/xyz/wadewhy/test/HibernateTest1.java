package xyz.wadewhy.test;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import xyz.wadewhy.pojo.Person;
import xyz.wadewhy.util.HibernateUtil;

public class HibernateTest1 {
    /**
     * 全查询 Query对象 Criteria对象
     * 
     * @throws Exception
     */
    @Test
    public void fun1() throws Exception {
        // 获取session
        Session session = HibernateUtil.openSession();
        // 事务开启
        Transaction tx = session.beginTransaction();
        // HQL查询 Person是对象名，不是操作数据库的表
        String hql = "from Person p";
        // String hql = " from xyz.wade.pojo.Persion";
        // 根据HQL语句创建查询对象
        Query query = session.createQuery(hql);
        // 根据查询对象的到结果集
        List<Person> list = query.list();

        for (Person person : list) {
            System.err.println(person.toString());
        }
        /**
         * 方法二
         */
        // 1：创建query对象，方法里面实体类class
        Criteria criteria = session.createCriteria(Person.class);
        // 2：利用query对象中的方法得到结果
        List<Person> list1 = criteria.list();
        for (Person person : list1) {
            System.err.println(person.toString());
        }
        // 事务提交，sesson关闭
        tx.commit();
        session.close();

    }

    /**
     * 
     */
    @Test
    public void fun2() {
        // 获取session
        Session session = HibernateUtil.openSession();
        // 事务开启
        Transaction tx = session.beginTransaction();
        // HQL语句创建查询对象
        String hql1 = "from Person where id = 4";
        String hql2 = "from Person where name in ('wade','JJ')";
        String hql3 = "from Person where name not in ('wade','JJ')";
        Query query1 = session.createQuery(hql1);
        Query query2 = session.createQuery(hql2);
        Query query3 = session.createQuery(hql3);
        // 根据主键查询结果
        Person p1 = (Person) query1.uniqueResult();
        System.err.println(p1.toString());
        // 根据查询对象的到结果集------in
        List<Person> list2 = query2.list();

        for (Person person : list2) {
            System.err.println(person.toString());
        }

        // 根据查询对象的到结果集-----not in
        List<Person> list3 = query3.list();

        for (Person person : list3) {
            System.err.println(person.toString());
        }
        // 事务提交，sesson关闭
        tx.commit();
        session.close();

    }

    /**
     * 占位符
     */
    @Test
    public void fun3() {
        // 获取session
        Session session = HibernateUtil.openSession();
        // 事务开启
        Transaction tx = session.beginTransaction();
        // HQL语句创建查询对象
        String hql1 = "from Person where work = ?";
        Query query = session.createQuery(hql1);
        query.setParameter(0, "basketballPlayer");
        List<Person> list3 = query.list();
        for (Person person : list3) {
            System.err.println(person.toString());
        }
        // 事务提交，sesson关闭
        tx.commit();
        session.close();

    }

    /**
     * 分页查询
     */
    @Test
    // 分页查询
    public void fun4() {
        // 1获得session
        Session session = HibernateUtil.openSession();
        // 2控制事务
        Transaction tx = session.beginTransaction();
        // 3执行操作
        // -------------------------------------------
        // 书写HQL语句
        String hql = " from Person  "; // 查询所有Customer对象
        // 根据HQL语句创建查询对象
        Query query = session.createQuery(hql);
        // 设置分页信息 limit ?,?
        query.setFirstResult(1);
        query.setMaxResults(2);
        // 据查询对象获得查询结果
        List<Person> list3 = query.list();
        for (Person person : list3) {
            System.err.println(person.toString());
        }

        // -------------------------------------------
        // 4提交事务.关闭资源
        tx.commit();
        session.close();
    }

    /**
     * 模糊查询
     */
    @Test
    // 模糊查询
    public void fun5() {
        // 1获得session
        Session session = HibernateUtil.openSession();
        // 2控制事务
        Transaction tx = session.beginTransaction();
        // 3执行操作
        // -------------------------------------------
        // 书写HQL语句
        String name = "wade";
        String hql = " from Person p where p.name like '%" + name + "%'"; // 查询所有Customer对象
        // 根据HQL语句创建查询对象
        Query query = session.createQuery(hql);
        // 据查询对象获得查询结果
        List<Person> list = query.list();
        for (Person person : list) {
            System.err.println(person.toString());
        }
        /**
         * 方法二
         */
        String hql2 = "from Person p where p.name like :name";
        Query query2 = session.createQuery(hql2);
        query2.setString("name", "%" + name + "%");
        // 或者query2.setParameter("name", "%" + name + "%");
        // 据查询对象获得查询结果
        List<Person> list2 = query2.list();
        for (Person person : list2) {
            System.err.println(person.toString());
        }
        // -------------------------------------------
        // 4提交事务.关闭资源
        tx.commit();
        session.close();
    }

    /**
     * 排序查询
     */
    @Test
    public void fun6() {
        Session session = HibernateUtil.openSession();
        Transaction tx = session.beginTransaction();
        // Person必须和类名一致
        String hql = "from Person order by name,id desc";
        Query query = session.createQuery(hql);
        List<Person> list = query.list();
        for (Person person : list) {
            System.err.println(person.toString());
        }
        tx.commit();
        session.close();
    }

    /**
     * 分组查询
     */
    @Test
    public void fun7() {
        Session session = HibernateUtil.openSession();
        Transaction tx = session.beginTransaction();
        // Person必须和类名一致
        // 按电话号码分类,并且号码要大于111
        String hql1 = "select phone, count(*) from Person group by phone having phone > 111";
        Query query = session.createQuery(hql1);
        List<Object> list = query.list();
        System.err.println("电话号码" + "      " + "个数");
        for (Object person : list) {
            Object[] objs = (Object[]) person;
            System.err.println(objs[0] + "      " + objs[1]);
        }
        tx.commit();
        session.close();
    }

    /**
     * 统计查询
     */
    @Test
    // 统计查询
    public void fun8() {
        Session session = HibernateUtil.openSession();
        Transaction tx = session.beginTransaction();
        // ----------------------------------------------------
        String hql1 = " select count(*) from  xyz.wadewhy.pojo.Person  ";
        String hql2 = " select sum(id) from  xyz.wadewhy.pojo.Person  ";
        String hql3 = " select avg(id) from  xyz.wadewhy.pojo.Person  ";
        String hql4 = " select max(id) from  xyz.wadewhy.pojo.Person  ";
        String hql5 = " select min(id) from  xyz.wadewhy.pojo.Person  ";

        Query query1 = session.createQuery(hql1);
        Query query2 = session.createQuery(hql2);
        Query query3 = session.createQuery(hql3);
        Query query4 = session.createQuery(hql4);
        Query query5 = session.createQuery(hql5);

        Number number1 = (Number) query1.uniqueResult();
        Number number2 = (Number) query2.uniqueResult();
        Number number3 = (Number) query3.uniqueResult();
        Number number4 = (Number) query4.uniqueResult();
        Number number5 = (Number) query5.uniqueResult();

        System.err.println("总条数：" + number1);
        System.err.println("id的和：" + number2);
        System.err.println("id的平均值：" + number3);
        System.err.println("id的最大值：" + number4);
        System.err.println("id的最小值：" + number5);
        // ----------------------------------------------------
        tx.commit();
        session.close();

    }

    /**
     * 删改查
     */
    @Test
    public void fun9() {
        Session session = HibernateUtil.openSession();
        Transaction tx = session.beginTransaction();
        // 下面要实现的是更新操作。
        String hql1 = "update Person set name='wadewhy1' where work='Java'";
        // hibernate3 没有insert 语句 但是通过这种SQL形式可以实现
        String hql2 = "insert into Person(name,age) values('ce',30)";
        // 这里是删除语句
        String hql4 = "delete Person where name='wadewhy1'";
        Query q1 = session.createQuery(hql1);
        Query q2 = session.createSQLQuery(hql2);
        Query q4 = session.createQuery(hql4);

        // q1.setInteger(0, 32323);
        q1.executeUpdate();
        q2.executeUpdate();
        q4.executeUpdate();

        tx.commit();
        session.close();
    }

}

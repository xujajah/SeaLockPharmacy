package database;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import model.Employee;
import model.Theme;


public class DBUtil {
	

	public static <T>Object isLogin(Class<?> object,String column,String value, String column2,String value2) {
		Session session = null;
		Transaction tx =null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(object);
			criteria.add(Restrictions.and(Restrictions.eq(column, value), Restrictions.eq(column2, value2))).add(Restrictions.eq("empActive", true));
			
			Object obj = criteria.uniqueResult();
			tx.commit();
			if(obj!=null) {
				return obj;
			}
			else {
				return null;	
			}
		}catch (Exception ex){

			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
			return null;
		} finally {
			if(session != null)
				session.close();
		}
	}
	
	public static boolean uniqueResult(Class<?> object, String column, String value) {
		Session session = null;
		Transaction tx =null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(object);
			criteria.add(Restrictions.eq(column, value));
			Object obj = criteria.uniqueResult();
			tx.commit();
			if(obj==null) {
				return true;
			}
			else {
				return false;
				
			}
			
		}catch (Exception ex){
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
			return false;
		} finally {
			if(session != null)
				session.close();
		}
	}
	
	public static boolean uniqueResult(Class<?> object, String column, String value, String column2, String value2 ) {
		Session session = null;
		Transaction tx =null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(object);
			criteria.add(Restrictions.eq(column, value)).add(Restrictions.eq(column2, value2));
			Object obj = criteria.uniqueResult();
			tx.commit();
			if(obj==null) {
				return true;
			}
			else {
				return false;		
			}
		}catch (Exception ex){
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
			return false;
		} finally {
			if(session != null)
				session.close();
		}
	}
	
	public static boolean uniqueResultUpdate(Class<?> object,String column1, int value1 ,String column2, String value2) {
		Session session = null;
		Transaction tx =null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(object);
			criteria.add(Restrictions.not(Restrictions.eq(column1,value1))).add(Restrictions.eq(column2, value2));
			Object obj = criteria.uniqueResult();
			tx.commit();
			if(obj==null) {
				return true;
			}
			else {
				return false;		
			}
		}catch (Exception ex){
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
			return false;
		} finally {
			if(session != null)
				session.close();
		}
	}
	
	public static boolean uniqueResultUpdate(Class<?> object,String column1, int value1 ,String column2, String value2, String column3, String value3 ) {
		Session session = null;
		Transaction tx =null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(object);
			criteria.add(Restrictions.not(Restrictions.eq(column1,value1))).add(Restrictions.eq(column2, value2)).add(Restrictions.eq(column3, value3));
			Object obj = criteria.uniqueResult();
			tx.commit();
			if(obj==null) {
				return true;
			}
			else {
				return false;		
			}
		}catch (Exception ex){
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
			return false;
		} finally {
			if(session != null)
				session.close();
		}
	}
	
	public static <T>Object getObject(Class<?> object,int id) {
		Session session = null;
		Transaction tx =null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Object obj = session.get(object, id);
			tx.commit();
			if(obj!=null) {
				return obj;
			}
			else {
				return null;
				
			}
			
		}catch (Exception ex){
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
			return null;
		} finally {
			if(session != null)
				session.close();
		}
	}
	
	public static boolean dataInTable(Class<?> object) {
		Session session = null;
		Transaction tx =null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(object);
			if(criteria.list().isEmpty()) {
				return false;
			}else {
				return true;
			}
			
		}catch (Exception ex){
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
			return false;
		} finally {
			if(session != null)
				session.close();
		}
	}
	
	public static <T>Object getObject(Class<?> object,String Column, int id) {
		Session session = null;
		Transaction tx =null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(object);
			criteria.add(Restrictions.eq(Column,id));
			Object obj = criteria.uniqueResult();
			tx.commit();
			if(obj!=null) {
				return obj;
			}
			else {
				return null;
			}
		}catch (Exception ex){
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
			return null;
		} finally {
			if(session != null)
				session.close();
		}
	}
	
	
	public static <T>Object getObject(Class<?> object,String Column, String value) {
		Session session = null;
		Transaction tx =null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(object);
			criteria.add(Restrictions.eq(Column,value));
			Object obj = criteria.uniqueResult();
			tx.commit();
			if(obj!=null) {
				return obj;
			}
			else {
				return null;
			}
		}catch (Exception ex){
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
			return null;
		} finally {
			if(session != null)
				session.close();
		}
	}

	public static Theme getTheme(Employee emp) {
		Session session = null;
		Transaction tx =null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Theme.class);
			criteria.add(Restrictions.eq("employeeTheme",emp));
			Theme obj = (Theme) criteria.uniqueResult();
			tx.commit();
			if(obj!=null) {
				return obj;
			}
			else {
				return null;
			}
		}catch (Exception ex){
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
			return null;
		} finally {
			if(session != null)
				session.close();
		}
	}

}

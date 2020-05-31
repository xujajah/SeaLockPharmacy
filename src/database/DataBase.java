package database;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import utils.Util;

public class DataBase {


	protected ObservableList<Object> list;


	public ObservableList<Object> getList() {
		return list;
	}
	public void setList(ObservableList<Object> list) {
		this.list = list;
	}
	public DataBase() {
		list = FXCollections.observableArrayList();
	}
	public void create(Object object, Node node, String msg)
	{
		Transaction tx = null;
		Session session = null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			session.save(object);
			tx.commit();
			list.add(object);
			Util.showNotification(node, msg);

		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
		} finally {
			if(session != null)
				session.close();
		}
	}

	public void create(Object object)
	{
		Transaction tx = null;
		Session session = null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			session.save(object);
			tx.commit();
			list.add(object);

		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
		} finally {
			if(session != null)
				session.close();
		}
	}

	public boolean createBoolean(Object object)
	{
		Transaction tx = null;
		Session session = null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			session.save(object);
			tx.commit();
			list.add(object);
			return true;

		} catch (Exception ex) {
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

	@SuppressWarnings("unchecked")
	public <T> ObservableList<T> retrieve(Class<?> objClass) {
		Transaction tx = null;
		Session session = null;
		list.clear();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(objClass);
			list.addAll(crit.list());
			tx.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
		} finally {
			if(session != null)
				session.close();
		}

		return (ObservableList<T>) list;
	}

	@SuppressWarnings("unchecked")
	public <T> ObservableList<T> retrieve(Class<?> objClass,String column,boolean active) {
		Transaction tx = null;
		Session session = null;
		list.clear();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Criteria crit = session.createCriteria(objClass);
			crit.add(Restrictions.eq(column, active));
			list.addAll(crit.list());
			tx.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
		} finally {
			if(session != null)
				session.close();
		}

		return (ObservableList<T>) list;
	}

	@SuppressWarnings("unchecked")
	public <T> ObservableList<T> retrieveAscOrder(Class<?> objClass,String column,boolean active,String columnOrder) {
		Transaction tx = null;
		Session session = null;
		list.clear();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(objClass);
			crit.add(Restrictions.eq(column, active)).addOrder(Order.asc(columnOrder));
			list.addAll(crit.list());
			tx.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
		} finally {
			if(session != null)
				session.close();
		}

		return (ObservableList<T>) list;
	}

	@SuppressWarnings("unchecked")
	public <T> ObservableList<T> retrieveDescOrder(Class<?> objClass,String column,boolean active,String columnOrder) {
		Transaction tx = null;
		Session session = null;
		list.clear();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(objClass);
			crit.add(Restrictions.eq(column, active)).addOrder(Order.desc(columnOrder));
			list.addAll(crit.list());
			tx.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
		} finally {
			if(session != null)
				session.close();
		}

		return (ObservableList<T>) list;
	}



	public void update(Object object)
	{

		Transaction tx = null;
		Session session = null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			session.update(object);
			tx.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
		} finally {
			if(session != null)
				session.close();
		}
	}

	public void softDelete(Object object)
	{

		Transaction tx = null;
		Session session = null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			session.update(object);
			tx.commit();
			list.remove(object);

		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
		} finally {
			if(session != null)
				session.close();
		}
	}

	public void silentUpdate(Object object)
	{

	}
	public void delete(Object object)
	{
		Transaction tx = null;
		Session session = null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			session.delete(object);
			tx.commit();
			list.remove(object);

		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
		} finally {
			if(session != null)
				session.close();
		}
	}

}

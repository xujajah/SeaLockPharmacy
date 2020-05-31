package database;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.LoginHistory;
import model.Sale;
import model.SaleDetail;
import model.SaleReturn;

public class ReportsDB {

	@SuppressWarnings("unchecked")
	public static List<Object[]> customerOrders(Date fromDate, Date toDate) {
		Transaction tx = null;
		Session session = null;
		List<Object[]> list;
		ProjectionList projectionList = Projections.projectionList();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			projectionList.add(Projections.groupProperty("saleCustomer.custId"))
			.add(Projections.rowCount());
			Criteria crit = session.createCriteria(Sale.class);
			crit.setProjection(projectionList);
			crit.add(Restrictions.conjunction().add(Restrictions.between("saleDate", fromDate, toDate)));
			list = crit.list();
			tx.commit();
			return list;

		} catch (Exception ex) {
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


	@SuppressWarnings("unchecked")
	public static List<Object[]> popularMedicine() {
		Transaction tx = null;
		Session session = null;
		List<Object[]> list;
		ProjectionList projectionList = Projections.projectionList();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(SaleDetail.class);
			projectionList.add(Projections.groupProperty("saleStockDrug"))
			.add(Projections.alias(Projections.sum("saleQty"), "saleQty"));
			crit.setProjection(projectionList);
			crit.addOrder(Order.desc("saleQty"));
			list = crit.list();
			tx.commit();
			return list;

		} catch (Exception ex) {
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



	@SuppressWarnings("unchecked")
	public static List<Object[]> topSelling(Date fromDate, Date toDate) {
		Transaction tx = null;
		Session session = null;
		List<Object[]> list;
		ProjectionList projectionList = Projections.projectionList();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();			
			Criteria crit = session.createCriteria(SaleDetail.class);
			projectionList.add(Projections.groupProperty("saleStockDrug"))
			.add(Projections.rowCount())
			.add(Projections.alias(Projections.sum("saleQty"), "saleQty"))
			.add(Projections.sqlProjection("sum(retailPrice * saleQty) as retailPrice",
					new String[] { "retailPrice" }, 
					( new Type[] {StandardBasicTypes.DOUBLE}) ))
			.add(Projections.sqlProjection("sum(tradePrice * saleQty) as tradePrice",
					new String[] { "tradePrice" }, 
					( new Type[] {StandardBasicTypes.DOUBLE}) ));
			crit.setProjection(projectionList);
			crit.createAlias("saleId", "sale");
			crit.add(Restrictions.conjunction().add(Restrictions.between("sale.saleDate", fromDate, toDate)));
			crit.addOrder(Order.desc("saleQty"));
			list = crit.list();
			tx.commit();
			return list;

		} catch (Exception ex) {
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


	@SuppressWarnings("unchecked")
	public static ObservableList<Sale> totalSale(Date fromDate, Date toDate) {
		Transaction tx = null;
		Session session = null;
		ObservableList<Sale> list = FXCollections.observableArrayList();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(Sale.class);
			crit.add(Restrictions.eq("saleActive", true));
			crit.add(Restrictions.conjunction().add(Restrictions.between("saleDate", fromDate, toDate)));
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

		return list;
	}

	@SuppressWarnings("unchecked")
	public static ObservableList<SaleReturn> totalReturn(Date fromDate, Date toDate) {
		Transaction tx = null;
		Session session = null;
		ObservableList<SaleReturn> list = FXCollections.observableArrayList();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(SaleReturn.class);
			crit.add(Restrictions.conjunction().add(Restrictions.between("returnDate", fromDate, toDate)));
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

		return list;
	}

	@SuppressWarnings("unchecked")
	public static ObservableList<Sale> dailySale(Date date) {
		Transaction tx = null;
		Session session = null;
		ObservableList<Sale> list = FXCollections.observableArrayList();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(Sale.class);
			crit.add(Restrictions.eq("saleActive", true));
			crit.add(Restrictions.eq("saleDate", date));
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

		return list;
	}


	@SuppressWarnings("unchecked")
	public static List<Object[]> weeklyMonthly(Date fromDate, Date toDate) {
		Transaction tx = null;
		Session session = null;
		List<Object[]> list;
		ProjectionList projectionList = Projections.projectionList();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();			
			Criteria crit = session.createCriteria(Sale.class);
			projectionList.add(Projections.groupProperty("saleDate"))
			.add(Projections.sum("totalPayable"))
			.add(Projections.sum("totalProfit"));
			crit.setProjection(projectionList);
			crit.add(Restrictions.conjunction().add(Restrictions.between("saleDate", fromDate, toDate)));
			list = crit.list();
			tx.commit();
			return list;

		} catch (Exception ex) {
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

	@SuppressWarnings("unchecked")
	public static ObservableList<LoginHistory> loginHistory(Date date) {
		Transaction tx = null;
		Session session = null;
		ObservableList<LoginHistory> list = FXCollections.observableArrayList();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(LoginHistory.class);
			crit.add(Restrictions.eq("date", date));
			crit.addOrder(Order.desc("time"));
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

		return list;
	}
	
	public static long rowCount(Class<?> objClass,String column,boolean active) {
		Transaction tx = null;
		Session session = null;
		long count = 0;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(objClass);
			crit.add(Restrictions.eq(column, active));
			count = (long) crit.setProjection(Projections.rowCount()).uniqueResult();
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

		return count;
	}

}

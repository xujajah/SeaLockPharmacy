package database;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javafx.collections.ObservableList;
import model.Sale;
import model.SaleDetail;
import model.Stock;
import utils.Dialog;

public class SaleDetailDB extends DataBase{
	
	public int createSale(Sale sale, List<SaleDetail> saleDetails) {
		Transaction tx = null;
		Session session = null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			session.save(sale);
			for(SaleDetail saleDetail : saleDetails) {
				saleDetail.setSaleId(sale);
				session.save(saleDetail);
				Stock stock = (Stock) DBUtil.getObject(Stock.class, "stockDrug.drugId", saleDetail.getSaleStockDrug().getStockDrug().getDrugId());
				if(stock!= null) {
					stock.setStockTotal(stock.getStockTotal()-saleDetail.getSaleQty());
					session.update(stock);
				}
			}
			tx.commit();
			list.addAll(saleDetails);
			return sale.getSaleId();
		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex.getCause().toString().contains("Communications link failure")) {
				System.exit(0);
			}
			if(tx!=null) {
				tx.rollback();
			}
			return 0;
		} finally {
			if(session != null)
				session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> ObservableList<T> getSaleDetail(int id) {
		Transaction tx = null;
		Session session = null;
		list.clear();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(SaleDetail.class);
			crit.add(Restrictions.eq("saleId.saleId", id));
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
	
	public void voidSale(Sale sale, List<SaleDetail> saleDetails) {
		Transaction tx = null;
		Session session = null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Sale s = (Sale) DBUtil.getObject(Sale.class, sale.getSaleId());
			if(s!=null) {
				if(s.getSaleActive()==true) {
					sale.setSaleActive(false);
					session.update(sale);
					for(SaleDetail saleDetail : saleDetails) {
						Stock stock = (Stock) DBUtil.getObject(Stock.class, "stockDrug.drugId", saleDetail.getSaleStockDrug().getStockDrug().getDrugId());
						if(stock!= null) {
							stock.setStockTotal(stock.getStockTotal()+saleDetail.getSaleQty());
							session.update(stock);
						}
					}
				}else {
					Dialog.error("Sale is Already Void");
				}
			}
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

	@SuppressWarnings("unchecked")
	public <T> ObservableList<T> getControlDrugSale(Date fromDate, Date toDate) {
		Transaction tx = null;
		Session session = null;
		list.clear();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(SaleDetail.class);
			crit.createAlias("saleStockDrug", "controlDrug");
			crit.createAlias("controlDrug.stockDrug", "drug");
			crit.add(Restrictions.eq("drug.drugControlDrug", "Yes"));
			crit.createAlias("saleId", "sale");
			crit.add(Restrictions.conjunction().add(Restrictions.between("sale.saleDate", fromDate, toDate)));
			crit.addOrder(Order.desc("sale.saleDate"));
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

}

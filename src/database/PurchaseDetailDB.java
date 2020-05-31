package database;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javafx.collections.ObservableList;
import model.Purchase;
import model.PurchaseDetail;
import model.Stock;

public class PurchaseDetailDB extends DataBase{
	public void expiryDelete(PurchaseDetail purchaseDetail)
	{

		Transaction tx = null;
		Session session = null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			Stock stock = (Stock) DBUtil.getObject(Stock.class, "stockDrug.drugId", purchaseDetail.getPurchaseDrug().getDrugId());
			stock.setStockTotal(stock.getStockTotal()-purchaseDetail.getQtyPerUnit());
			session.update(stock);
			session.update(purchaseDetail);
			tx.commit();
			list.remove(purchaseDetail);

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

	public void createPurcahse(Purchase purchase, List<PurchaseDetail> purchaseDetails) {
		Transaction tx = null;
		Session session = null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			session.save(purchase);
			for(PurchaseDetail purchaseDetail : purchaseDetails) {
				purchaseDetail.setPurchase(purchase);
				session.save(purchaseDetail);
				Stock stock = (Stock) DBUtil.getObject(Stock.class, "stockDrug.drugId", purchaseDetail.getPurchaseDrug().getDrugId());
				if(stock!= null) {
					stock.setStockRetailPrice(purchaseDetail.getRetailPrice());
					stock.setStockTradePrice(purchaseDetail.getTradePrice());
					stock.setStockTotal(stock.getStockTotal()+purchaseDetail.getQtyPerUnit());
					session.update(stock);
				}else if(stock == null) {
					stock = new Stock();
					stock.setStockDrug(purchaseDetail.getPurchaseDrug());
					stock.setStockRetailPrice(purchaseDetail.getRetailPrice());
					stock.setStockTradePrice(purchaseDetail.getTradePrice());
					stock.setStockTotal(purchaseDetail.getQtyPerUnit());
					session.save(stock);
				}
			}
			tx.commit();
			list.addAll(purchaseDetails);

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
	public <T> ObservableList<T> expiryReterive(){
		Transaction tx = null;
		Session session = null;
		list.clear();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			
			Criteria crit = session.createCriteria(PurchaseDetail.class);
			crit.add(Restrictions.eq("expiryActive" , true)).addOrder(Order.asc("expiryDate"));
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

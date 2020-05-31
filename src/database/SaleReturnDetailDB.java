package database;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.SaleReturn;
import model.SaleReturnDetail;
import model.Stock;

public class SaleReturnDetailDB extends DataBase{
	
	public int createReturn(SaleReturn saleReturn, List<SaleReturnDetail> saleReturnDetails) {
		Transaction tx = null;
		Session session = null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			session.save(saleReturn);
			for(SaleReturnDetail saleReturnDetail : saleReturnDetails) {
				if(saleReturnDetail.getReturnQty() != 0 ) {
					saleReturnDetail.setReturnId(saleReturn);
					session.save(saleReturnDetail);
					Stock stock = (Stock) DBUtil.getObject(Stock.class, "stockDrug.drugId", saleReturnDetail.getReturnStockDrug().getStockDrug().getDrugId());
					if(stock!= null) {
						stock.setStockTotal(stock.getStockTotal() + saleReturnDetail.getReturnQty());
						session.update(stock);
					}
				}
			}
			tx.commit();
			list.addAll(saleReturnDetails);
			return saleReturn.getReturnId();
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

}

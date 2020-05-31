package database;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import javafx.collections.ObservableList;
import model.Region;

public class RegionDB extends DataBase{
	@SuppressWarnings("unchecked")
	public <T> ObservableList<T> getRegions(int id) {
		Transaction tx = null;
		Session session = null;
		list.clear();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			
			Criteria crit = session.createCriteria(Region.class);
			crit.createAlias("regionProvince", "province");
			crit.add(Restrictions.eq("province.provinceId", id));
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

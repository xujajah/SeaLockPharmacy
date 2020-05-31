package database;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import javafx.collections.ObservableList;
import model.Prescription;
import model.PrescriptionDetail;

public class PrescriptionDetailDB extends DataBase{
	@SuppressWarnings("unchecked")
	public <T> ObservableList<T> getPrescriptionDetail(int id) {
		Transaction tx = null;
		Session session = null;
		list.clear();
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			
			Criteria crit = session.createCriteria(PrescriptionDetail.class);
			crit.add(Restrictions.eq("prescriptionDetailPrescription.prescriptionId", id));
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
	
	public void createPrescription(Prescription prescription,List<PrescriptionDetail> prescriptionDetails)
	{
		Transaction tx = null;
		Session session = null;
		try {
			session = DBData.getSession();
			tx = session.beginTransaction();
			session.save(prescription);
			for(PrescriptionDetail prescriptionDetail: prescriptionDetails) {
				prescriptionDetail.setPrescriptionDetailPrescription(prescription);
				session.save(prescriptionDetail);
			}
			tx.commit();
			list.addAll(prescriptionDetails);

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

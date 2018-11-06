package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.Bill;

@Service("billService")
@Transactional

public class BillServiceImpl implements BillService {

	GenericDao< Bill,Integer > billDao;

	@Autowired
	public void setDao( GenericDao< Bill,Integer> daoToSet ){
		billDao = daoToSet;
		billDao.setEntityClass( Bill.class );
	}
	
	@Override
	public Bill createBill(Bill bill) {
		return billDao.create(bill);
	}

	@Override
	public Bill findBillById(int id) {
		return billDao.findById(id);
	}

	@Override
	public Bill updateBill(Bill bill) {
		return billDao.update(bill);
	}

}

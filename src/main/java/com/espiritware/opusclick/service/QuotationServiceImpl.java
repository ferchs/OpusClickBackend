package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.OnlineQuote;
import com.espiritware.opusclick.model.State;

@Service("quotationService")
@Transactional
public class QuotationServiceImpl implements QuotationService {
	
	GenericDao<OnlineQuote,Integer> quotationDao;
	
	@Autowired
	public void setDao( GenericDao<OnlineQuote,Integer> daoToSet ){
		quotationDao = daoToSet;
		quotationDao.setEntityClass( OnlineQuote.class );
	}

	@Override
	public OnlineQuote createQuote(OnlineQuote quote) {
		return quotationDao.create(quote);
	}

	@Override
	public OnlineQuote updateQuote(OnlineQuote quote) {
		return quotationDao.update(quote);
	}

	@Override
	public OnlineQuote findQuoteById(int id) {
		return quotationDao.findById(id);
	}

	@Override
	public State getQuoteState(int id) {
		return quotationDao.findById(id).getState();	
	}
}

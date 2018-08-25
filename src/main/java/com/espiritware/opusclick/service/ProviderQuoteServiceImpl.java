package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.ProviderQuote;

@Service("providerQuoteService")
@Transactional
public class ProviderQuoteServiceImpl implements ProviderQuoteService {

	GenericDao<ProviderQuote,Integer> quotationDao;
	
	@Autowired
	public void setDao( GenericDao<ProviderQuote,Integer> daoToSet ){
		quotationDao = daoToSet;
		quotationDao.setEntityClass( ProviderQuote.class );
	}
	
	@Override
	public ProviderQuote createProviderQuote(ProviderQuote providerQuote) {
		return quotationDao.create(providerQuote);
	}

	@Override
	public ProviderQuote findQuoteById(int id) {
		return quotationDao.findById(id);
	}

	@Override
	public ProviderQuote updateProviderQuote(ProviderQuote providerQuote) {
		return quotationDao.update(providerQuote);
	}

}

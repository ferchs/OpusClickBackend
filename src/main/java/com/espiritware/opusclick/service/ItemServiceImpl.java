package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.model.Item;

@Service("itemService")
@Transactional
public class ItemServiceImpl implements ItemService{

	GenericDao<Item,Integer> itemDao;
	
	@Autowired
	public void setDao( GenericDao<Item,Integer> daoToSet ){
		itemDao = daoToSet;
		itemDao.setEntityClass( Item.class );
	}
	
	@Override
	public Item updateItem(Item item) {
		return itemDao.update(item);
	}

}

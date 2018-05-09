package com.espiritware.opusclick.dao;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class GenericHibernateDao<T,E extends Serializable> extends AbstractHibernateDao<T,E> implements GenericDao<T,E>{

}

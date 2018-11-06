package com.espiritware.opusclick.service;

import com.espiritware.opusclick.model.InvalidTransaction;

public interface InvalidTransactionService {

	InvalidTransaction createInvalidTransaction(InvalidTransaction invalidTransaction);

	InvalidTransaction findInvalidTransactionById(int id);

	InvalidTransaction updateInvalidTransaction(InvalidTransaction invalidTransaction);
}

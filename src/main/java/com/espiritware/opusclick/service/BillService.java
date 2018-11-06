package com.espiritware.opusclick.service;

import com.espiritware.opusclick.model.Bill;

public interface BillService {

	Bill createBill(Bill bill);

	Bill findBillById(int id);

	Bill updateBill(Bill bill);
}

package com.espiritware.opusclick.service;

import java.util.List;
import com.espiritware.opusclick.model.Profession;

public interface ProfessionService {
	
	Profession findProfessionById(int id);
	
	Profession findProfessionByName(String professionName);
	
	List<Profession> getAllProfessions();
	
}

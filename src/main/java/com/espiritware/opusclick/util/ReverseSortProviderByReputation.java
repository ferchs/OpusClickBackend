package com.espiritware.opusclick.util;

import java.util.Comparator;
import com.espiritware.opusclick.model.Provider;

public class ReverseSortProviderByReputation implements Comparator<Provider>{

	@Override
	public int compare(Provider o1, Provider o2) {
		
		if(o1.getGlobalRating().getGlobalSatisfactionLevel() > o2.getGlobalRating().getGlobalSatisfactionLevel()) {
			return -1;
		}
		else if(o1.getGlobalRating().getGlobalSatisfactionLevel() < o2.getGlobalRating().getGlobalSatisfactionLevel()) {
			return 1;
		}
		return 0;
	}

}

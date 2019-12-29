package com.espiritware.opusclick.util;

import java.util.Comparator;

import com.espiritware.opusclick.model.Work;

public class ReverseSortWorkByDate implements Comparator<Work>{

	@Override
	public int compare(Work o1, Work o2) {
		// TODO Auto-generated method stub
		if(o1.getCreationDate().after(o2.getCreationDate())) {
			return -1;
		}
		else if(o1.getCreationDate().before(o2.getCreationDate())) {
			return 1;
		}
		return 0;
	}

}

package com.espiritware.opusclick.util;

import java.util.Comparator;
import com.espiritware.opusclick.dto.ProviderGetContractReviewDto;


public class ReverseSortProviderReviewByDate implements Comparator<ProviderGetContractReviewDto> {
	@Override
	public int compare(ProviderGetContractReviewDto o1, ProviderGetContractReviewDto o2) {
		// TODO Auto-generated method stub
		if(o1.getDate().after(o2.getDate())) {
			return -1;
		}
		else if(o1.getDate().before(o2.getDate())) {
			return 1;
		}
		return 0;
	}
}

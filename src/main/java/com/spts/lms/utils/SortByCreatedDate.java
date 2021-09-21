package com.spts.lms.utils;

import java.util.Comparator;

import com.spts.lms.beans.BaseBean;



public class SortByCreatedDate implements Comparator<BaseBean> {

	@Override
	public int compare(BaseBean o1, BaseBean o2) {
		if (o1.getCreatedDate().after(o2.getCreatedDate())) {
			return -1;
		} else if (o1.getCreatedDate().before(o2.getCreatedDate())) {
			return 1;
		} else {
			return 0;
		}
	}
}

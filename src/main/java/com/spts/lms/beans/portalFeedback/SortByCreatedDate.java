package com.spts.lms.beans.portalFeedback;

import java.util.Comparator;

public class SortByCreatedDate implements Comparator<PortalFeedbackQuery> {

	@Override
	public int compare(PortalFeedbackQuery o1, PortalFeedbackQuery o2) {
		if (o1.getCreatedDate().after(o2.getCreatedDate())) {
			return -1;
		} else if (o1.getCreatedDate().before(o2.getCreatedDate())) {
			return 1;
		} else {
			return 0;
		}
	}
}

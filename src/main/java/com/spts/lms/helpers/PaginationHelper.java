package com.spts.lms.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaginationHelper<E> {

	private static final String LIMIT_QUERY = " LIMIT ";
	private static final String OFFSET_QUERY = " OFFSET ";

	public static class Page<E> {

		private int pageNumber;
		private List<E> pageItems;
		private int pagesAvailable;
		private int rowCount;
		private int noOfPagesPerView;
		private int currentIndex;
		private int beginIndex;
		private int endIndex;
		private int totalPages;

		public Page(int i) {
			pageItems = new ArrayList<E>(i);
		}

		public void setPageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
		}

		public void setPagesAvailable(int pagesAvailable) {
			this.pagesAvailable = pagesAvailable;
		}

		public void setPageItems(List<E> pageItems) {
			this.pageItems = pageItems;
		}

		public int getPageNumber() {
			return pageNumber;
		}

		public int getPagesAvailable() {
			return pagesAvailable;
		}

		public List<E> getPageItems() {
			return pageItems;
		}

		public int getRowCount() {
			return rowCount;
		}

		public void setRowCount(int rowCount) {
			this.rowCount = rowCount;
		}

		public int getNoOfPagesPerView() {
			return noOfPagesPerView;
		}

		public void setNoOfPagesPerView(int noOfPagesPerView) {
			this.noOfPagesPerView = noOfPagesPerView;
		}

		public int getCurrentIndex() {
			return currentIndex;
		}

		public void setCurrentIndex(int currentIndex) {
			this.currentIndex = currentIndex;
		}

		public int getBeginIndex() {
			return beginIndex;
		}

		public void setBeginIndex(int beginIndex) {
			this.beginIndex = beginIndex;
		}

		public int getEndIndex() {
			return endIndex;
		}

		public void setEndIndex(int endIndex) {
			this.endIndex = endIndex;
		}

		public int getTotalPages() {
			return totalPages;
		}

		public void setTotalPages(int totalPages) {
			this.totalPages = totalPages;
		}

		@Override
		public String toString() {
			return "Page [pageNumber=" + pageNumber + ", pagesAvailable="
					+ pagesAvailable + ", rowCount=" + rowCount
					+ ", noOfPagesPerView=" + noOfPagesPerView
					+ ", currentIndex=" + currentIndex + ", beginIndex="
					+ beginIndex + ", endIndex=" + endIndex + ", totalPages="
					+ totalPages + ", pageItems=" + pageItems + "]";
		}

	}

	public Page<E> fetchPage(final JdbcTemplate jt, final String sqlCountRows,
			final String sqlFetchRows, final Object args[], final int pageNo,
			final int pageSize, final BeanPropertyRowMapper<E> rowMapper) {

		// determine how many rows are available
		int rowCount = 0;
		try {
			rowCount = jt.queryForObject(sqlCountRows, Integer.class, args);
		} catch (Exception e) {

		}

		final int noOfPagesPerView = 10;
		// calculate the number of pages
		int pageCount = 1;
		if (pageSize > 0) {
			pageCount = rowCount / pageSize;
			if (rowCount > pageSize * pageCount) {
				pageCount++;
			}
		}

		// create the page object
		final Page<E> page = new Page<E>(pageSize);
		page.setPageNumber(pageNo);
		page.setCurrentIndex(pageNo);
		page.setPagesAvailable(pageCount);
		page.setTotalPages(pageCount);
		page.setRowCount(rowCount);
		page.setNoOfPagesPerView(noOfPagesPerView);

		int beginIndex = (pageSize == 0 ? 0 : (pageNo / noOfPagesPerView)
				* noOfPagesPerView) + 1;
		int endIndex = pageSize == 0 ? pageCount
				: ((pageNo / noOfPagesPerView) + 1) * noOfPagesPerView;

		if (endIndex > pageCount) {
			endIndex = pageCount;
		}
		page.setBeginIndex(beginIndex);
		page.setEndIndex(endIndex);

		// fetch a single page of results
		final int startRow = (pageNo - 1) * pageSize;
		// control the pagination using database
		final StringBuilder queryBuilder = new StringBuilder(sqlFetchRows);
		/*if (pageSize > 0) {
			queryBuilder.append(LIMIT_QUERY).append(pageSize)
					.append(OFFSET_QUERY).append(startRow);
		}*/

		jt.query(queryBuilder.toString(), args,
				new ResultSetExtractor<Page<E>>() {
					public Page<E> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						final List<E> pageItems = page.getPageItems();
						int currentRow = 0;
						while (rs.next()) {
							pageItems.add(rowMapper.mapRow(rs, currentRow));
							currentRow++;
						}
						return page;
					}
				});
		return page;
	}
	
	public Page<E> fetchPages(final NamedParameterJdbcTemplate jt, final String sqlCountRows,
			final String sqlFetchRows, Map<String,Object> param, final int pageNo,
			final int pageSize, final BeanPropertyRowMapper<E> rowMapper) {
		System.out.println("Hello------------------");
		// determine how many rows are available
		int rowCount=0;
		try {
			 rowCount=jt.queryForObject(sqlCountRows, param, Integer.class);
			 System.out.println("counts----------------->" + rowCount);
		} catch (Exception e) {
				System.out.println("some error---------------" + e.toString());
		}

		final int noOfPagesPerView = 10;
		// calculate the number of pages
		int pageCount = 1;
		if (pageSize > 0) {
			pageCount = rowCount / pageSize;
			if (rowCount > pageSize * pageCount) {
				pageCount++;
			}
		}

		// create the page object
		final Page<E> page = new Page<E>(pageSize);
		page.setPageNumber(pageNo);
		page.setCurrentIndex(pageNo);
		page.setPagesAvailable(pageCount);
		page.setTotalPages(pageCount);
		page.setRowCount(rowCount);
		page.setNoOfPagesPerView(noOfPagesPerView);

		int beginIndex = (pageSize == 0 ? 0 : (pageNo / noOfPagesPerView)
				* noOfPagesPerView) + 1;
		int endIndex = pageSize == 0 ? pageCount
				: ((pageNo / noOfPagesPerView) + 1) * noOfPagesPerView;

		if (endIndex > pageCount) {
			endIndex = pageCount;
		}
		page.setBeginIndex(beginIndex);
		page.setEndIndex(endIndex);

		// fetch a single page of results
		final int startRow = (pageNo - 1) * pageSize;
		// control the pagination using database
		final StringBuilder queryBuilder = new StringBuilder(sqlFetchRows);
		/*if (pageSize > 0) {
			queryBuilder.append(LIMIT_QUERY).append(pageSize)
					.append(OFFSET_QUERY).append(startRow);
		}*/

		jt.query(queryBuilder.toString(), param,
				new ResultSetExtractor<Page<E>>() {
					public Page<E> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						final List<E> pageItems = page.getPageItems();
						int currentRow = 0;
						while (rs.next()) {
							pageItems.add(rowMapper.mapRow(rs, currentRow));
							currentRow++;
						}
						return page;
					}
				});
		return page;
	}

}

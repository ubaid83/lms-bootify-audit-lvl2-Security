package com.spts.lms.services.report;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.report.UtilityReport;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.report.UtilityReportDAO;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.services.BaseService;
import com.spts.lms.web.utils.Utils;

@Service("utilityReportService")
@Transactional
public class UtilityReportService extends BaseService<UtilityReport> {

	private static final Logger logger = LoggerFactory
			.getLogger(UtilityReportService.class);

	@Autowired
	UtilityReportDAO utilityReportDAO;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	@Override
	protected BaseDAO<UtilityReport> getDAO() {

		return utilityReportDAO;
	}

	public List<UtilityReport> getDetailedUtilityReport(String campusId,
			String fromDate, String toDate) {

		return utilityReportDAO.getDetailedUtilityReport(campusId, fromDate,
				toDate);
	}

	public List<UtilityReport> getSummaryUtilityReport(String campusId,
			String fromDate, String toDate) {

		return utilityReportDAO.getSummaryUtilityReport(campusId, fromDate,
				toDate);
	}

	public String getXlsxforDetailedUtilityReport(
			List<UtilityReport> utilityReportList) {
		Date date = new Date();
		File file = new File(downloadAllFolder + File.separator
				+ "DetailedUtilityReport"
				+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
				+ ".xlsx");
		try {
			file.createNewFile();

			List<UtilityReport> admStatus = utilityReportList;

			Map<String, List<Map<String, Object>>> lstExcelData = new HashMap<String, List<Map<String, Object>>>();

			List<String> headers = new ArrayList<String>(Arrays.asList("Sr.No",
					"Campus", "Dept", "Acad Year", "UserName", "Full Name",
					"User Type", "Faculty Type(Core/Visiting)", "Program",
					"Program Name", "Acad Session", "Course Id",
					"Course/Event Name", "Module Id", "Module Name",
					"Features", "Total Used"));

			int count = 1;
			for (UtilityReport ad : admStatus) {

				Map<String, Object> map = new HashMap();

				map.put("Sr.No", count);
				map.put("Campus", ad.getCampus() == null ? "-" : ad.getCampus());
				map.put("Dept", ad.getDept() == null ? "-" : ad.getDept());
				map.put("Acad Year",
						ad.getAcadYear() == null ? "-" : ad.getAcadYear());
				map.put("UserName",
						ad.getUsername() == null ? "-" : ad.getUsername());
				map.put("Full Name",
						ad.getFullname() == null ? "-" : ad.getFullname());
				map.put("User Type",
						ad.getUserType() == null ? "-" : ad.getUserType());
				map.put("Faculty Type(Core/Visiting)",
						ad.getFacultyType() == null ? "-" : ad.getFacultyType());
				map.put("Program",
						ad.getProgram() == null ? "-" : ad.getProgram());
				map.put("Program Name",
						ad.getProgramName() == null ? "-" : ad.getProgramName());
				map.put("Acad Session",
						ad.getAcadSession() == null ? "-" : ad.getAcadSession());
				map.put("Course Id",
						ad.getCourseId() == null ? "-" : ad.getCourseId());
				map.put("Course/Event Name", ad.getCourseName() == null ? "-"
						: ad.getCourseName());
				map.put("Module Id",
						ad.getModuleId() == null ? "-" : ad.getModuleId());
				map.put("Module Name",
						ad.getModuleName() == null ? "-" : ad.getModuleName());
				map.put("Features",
						ad.getFeatures() == null ? "-" : ad.getFeatures());
				map.put("Total Used",
						ad.getTotalUse() == null ? "0" : ad.getTotalUse());

				if (lstExcelData.containsKey("DetailedUtilityReport")) {
					List<Map<String, Object>> lst = lstExcelData
							.get("DetailedUtilityReport");
					lst.add(map);

				} else {
					List<Map<String, Object>> lst = new ArrayList<Map<String, Object>>();
					lst.add(map);

					lstExcelData.put("DetailedUtilityReport", lst);
				}

				count++;

			}

			ExcelCreater.createExcelFile(lstExcelData, headers,
					file.getAbsolutePath());

		} catch (Exception e) {
			logger.error("Exception ", e);
		}

		return file.getAbsolutePath();
	}

	public String getXlsxforSummaryUtilityReport(
			List<UtilityReport> utilityReportList) {
		Date date = new Date();
		File file = new File(downloadAllFolder + File.separator
				+ "SummaryUtilityReport"
				+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
				+ ".xlsx");
		try {
			file.createNewFile();

			List<UtilityReport> admStatus = utilityReportList;

			Map<String, List<Map<String, Object>>> lstExcelData = new HashMap<String, List<Map<String, Object>>>();

			List<String> headers = new ArrayList<String>(Arrays.asList("Sr.No",
					"Group Name", "Students' Usage", "Students",
					"Total Student", "Student Percentage", "Admins' Usage",
					"Admin", "Total Admin", "Admin Percentage",
					"Visiting Faculties' Usage", "Faculty Visiting",
					"Total Visiting Faculty", "Visiting Faculty Percentage",
					"Core Faculties' Usage", "Faculty Core",
					"Total Core Faculty", "Core Faculty Percentage"));

			int count = 1;
			for (UtilityReport ad : admStatus) {

				Map<String, Object> map = new HashMap();

				map.put("Sr.No", ad.getSrno());
				map.put("Group Name", ad.getFeatures());
				map.put("Students' Usage", ad.getStudentsHit() == null ? "0"
						: ad.getStudentsHit());
				map.put("Students",
						ad.getStudents() == null ? "0" : ad.getStudents());
				map.put("Total Student", ad.getTotalStudent() == null ? "0"
						: ad.getTotalStudent());
				map.put("Student Percentage",
						ad.getStudentUsagePerc() == null ? "0" : ad
								.getStudentUsagePerc());
				map.put("Admins' Usage",
						ad.getAdminsHit() == null ? "0" : ad.getAdminsHit());
				map.put("Admin", ad.getAdmins() == null ? "0" : ad.getAdmins());
				map.put("Total Admin",
						ad.getTotalAdmin() == null ? "0" : ad.getTotalAdmin());
				map.put("Admin Percentage",
						ad.getAdminUsagePerc() == null ? "0" : ad
								.getAdminUsagePerc());
				map.put("Visiting Faculties' Usage",
						ad.getvFacultiesHit() == null ? "0" : ad
								.getvFacultiesHit());
				map.put("Faculty Visiting", ad.getvFaculties() == null ? "0"
						: ad.getvFaculties());
				map.put("Total Visiting Faculty",
						ad.getTotalVFaculty() == null ? "0" : ad
								.getTotalVFaculty());
				map.put("Visiting Faculty Percentage",
						ad.getvFacultyUsagePerc() == null ? "0" : ad
								.getvFacultyUsagePerc());
				map.put("Core Faculties' Usage",
						ad.getcFacultiesHit() == null ? "0" : ad
								.getcFacultiesHit());
				map.put("Faculty Core",
						ad.getcFaculties() == null ? "0" : ad.getcFaculties());
				map.put("Total Core Faculty",
						ad.getTotalCFaculty() == null ? "0" : ad
								.getTotalCFaculty());
				map.put("Core Faculty Percentage",
						ad.getcFacultyUsagePerc() == null ? "0" : ad
								.getcFacultyUsagePerc());

				if (lstExcelData.containsKey("SummaryUtilityReport")) {
					List<Map<String, Object>> lst = lstExcelData
							.get("SummaryUtilityReport");
					lst.add(map);

				} else {
					List<Map<String, Object>> lst = new ArrayList<Map<String, Object>>();
					lst.add(map);

					lstExcelData.put("SummaryUtilityReport", lst);
				}

				count++;

			}

			ExcelCreater.createExcelFile(lstExcelData, headers,
					file.getAbsolutePath());

		} catch (Exception e) {
			logger.error("Exception ", e);
		}

		return file.getAbsolutePath();
	}

	public List<Map<String, Object>> getSummaryUtilityReportMap(
			String campusId, String fromDate, String toDate) {

		return utilityReportDAO.getSummaryUtilityReportMap(campusId, fromDate,
				toDate);
	}

	public List<Map<String, Object>> getDetailedReport1(String campusId,
			String fromDate, String toDate) {

		return utilityReportDAO.getDetailedReport1(campusId, fromDate, toDate);
	}

	public List<Map<String, Object>> getDetailedReport2(String campusId,
			String fromDate, String toDate) {

		return utilityReportDAO.getDetailedReport2(campusId, fromDate, toDate);
	}

}

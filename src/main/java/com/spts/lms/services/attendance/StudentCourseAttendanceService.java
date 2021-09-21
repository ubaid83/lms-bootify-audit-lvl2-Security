package com.spts.lms.services.attendance;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.attendance.StudentCourseAttendance;
import com.spts.lms.beans.classParticipation.ClassParticipation;
import com.spts.lms.beans.timetable.Timetable;
import com.spts.lms.beans.user.User;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.attendance.StudentCourseAttendanceDAO;
import com.spts.lms.daos.timetable.TimetableDAO;
import com.spts.lms.daos.user.UserDAO;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.sap.attendance.ZATTSTPORTALUPDATEWS;
import com.spts.lms.sap.attendance.ZattStPortalLt;
import com.spts.lms.sap.attendance.ZattStPortalTt;
import com.spts.lms.services.BaseService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.utils.MultipleDBConnection;
import com.spts.lms.web.utils.Utils;

@Service("studentCourseAttendanceService")
@Transactional
public class StudentCourseAttendanceService extends
		BaseService<StudentCourseAttendance> {

	private static final Logger logger = Logger
			.getLogger(StudentCourseAttendanceService.class);

	@Autowired
	StudentCourseAttendanceDAO studentCourseAttendanceDAO;
	
	@Autowired
	TimetableDAO timetableDAO;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	Notifier notifier;

	@Value("${app}")
	private String app;

	@Value("${spring.datasource.url}")
	String defaultUrl;

	@Value("${database.username}")
	String defaultUsername;

	@Value("${database.password}")
	String defaultPassword;

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;
	
	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	Client client = ClientBuilder.newClient();

	@Override
	protected BaseDAO<StudentCourseAttendance> getDAO() {
		// TODO Auto-generated method stub
		return studentCourseAttendanceDAO;
	}

	public List<StudentCourseAttendance> findStudentsForAttendance(Long courseId) {
		return studentCourseAttendanceDAO.findStudentsForAttendance(courseId);
	}

	public List<StudentCourseAttendance> findByCourseId(Long courseId) {
		return studentCourseAttendanceDAO.findByCourseId(courseId);
	}

	public StudentCourseAttendance findByCourseIdAndStudent(Long courseId,
			String username) {
		return studentCourseAttendanceDAO.findByCourseIdAndStudent(courseId,
				username);
	}

	public List<StudentCourseAttendance> findByCourseIdAndDateTime(
			String courseId, String startTime, String endTime, String facultyId) {

		return studentCourseAttendanceDAO.findByCourseIdAndDateTime(courseId,
				startTime, endTime, facultyId);
	}

	public List<StudentCourseAttendance> getAttendanceStatByUsernameAndCourseId(
			String username, String courseId) {
		return studentCourseAttendanceDAO
				.getAttendanceStatByUsernameAndCourseId(username, courseId);
	}

	public List<StudentCourseAttendance> getAttendanceStatByUsernameAndCourseId(
			String username, String startDate, String endDate) {
		return studentCourseAttendanceDAO
				.getAttendanceStatByUsernameAndCourseId(username, startDate,
						endDate);
	}

	public StudentCourseAttendance getAllPresentRecord(String courseId,
			String startDate, String endDate, String facultyId) {

		return studentCourseAttendanceDAO.getAllPresentRecord(courseId,
				startDate, endDate, facultyId);
	}

	public void updateDelFlag(String value, long id) {
		studentCourseAttendanceDAO.updateDelFlag(value, id);
	}

	public List<StudentCourseAttendance> getAbsentRecords() {
		return studentCourseAttendanceDAO.getAbsentRecords();
	}

	public List<StudentCourseAttendance> getPresentRecords() {
		return studentCourseAttendanceDAO.getPresentRecords();
	}

	public List<StudentCourseAttendance> getLastAbsentRecords() {
		return studentCourseAttendanceDAO.getLastAbsentRecords();
	}

	public List<StudentCourseAttendance> getAbsentRecordsByDate(String date) {
		return studentCourseAttendanceDAO.getAbsentRecordsByDate(date);
	}

	public List<StudentCourseAttendance> getPresentRecordsByDate(String date) {
		return studentCourseAttendanceDAO.getPresentRecordsByDate(date);
	}
	
	public String sendAttendanceToSAPByDateForApp(String date, String dateLike) {
        String responseData="";
        List<StudentCourseAttendance> absentList = getAbsentRecordsByDateForApp(date, dateLike);
        logger.info("absentList---->" + absentList);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        ZATTSTPORTALUPDATEWS ws = new ZATTSTPORTALUPDATEWS();
        ZattStPortalTt lstLT = new ZattStPortalTt();
        try {
              if (absentList.size() > 0) {
                    String ids = "";
                    int countForAbsent = 0;
                    for (StudentCourseAttendance sa : absentList) {
                          String courseId = sa.getCourseId();
                          String eventId = courseId.substring(0, 8);
                          String programId = courseId.substring(8);
                          String studAcadSession = "", acadYear = "", startTime = "", endTime = "";
                          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                          Date dt = format.parse(sa.getStartTime());
                          Date dt1 = format.parse(sa.getEndTime());
                          SimpleDateFormat formatDate1 = new SimpleDateFormat("HH:mm:ss");
                          startTime = formatDate1.format(dt);
                          endTime = formatDate1.format(dt1);
                          Date date3 = Utils.getInIST();
                          SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                          String logDate = formatDate.format(date3);
                          String logTime = formatDate1.format(date3);
                          // logger.info("logDate---->" + logDate);

                          logger.info("logTime---->" + logTime);


                          String strtDateStr = Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",sa.getStartTime());

                          XMLGregorianCalendar xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(startTime);
                          XMLGregorianCalendar xmlGregCal1 = DatatypeFactory .newInstance().newXMLGregorianCalendar(endTime);
                          XMLGregorianCalendar xmlGregCal2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(logTime);

                          studAcadSession = sa.getAcadSession();
                          acadYear = sa.getAcadYear();

                          String studentObjId = "null";
                          User sObj = userDAO.getStudentObjIdFromStudentMapping(sa.getUsername());

                          if(null == sObj){

                          WebTarget webTarget12 = client.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl+ "/getStudentObjIdByUsername?username="+ sa.getUsername()));

                          Invocation.Builder invocationBuilder12 = webTarget12.request(MediaType.APPLICATION_JSON);

                          studentObjId = invocationBuilder12.get(String.class);
                          User u = new User();
                          u.setUsername(sa.getUsername());
                          u.setStudentObjectId(studentObjId);
                          userDAO.insertStudentObjIdToStudentMapping(u);
                          }else{

                                studentObjId = sObj.getStudentObjectId();

                          }

                          logger.info("stoID---> " + studentObjId);
                          if (!studentObjId.equals("null")) {
                                ZattStPortalLt lst = new ZattStPortalLt();
                                lst.setOrgunit(app);
                                lst.setProgId(programId);
                                lst.setAyear(acadYear);
                                lst.setAsession(studAcadSession);
                                lst.setEventId(eventId);
                                lst.setEventDate(strtDateStr);
                                lst.setStartTime(xmlGregCal);
                                lst.setEndTime(xmlGregCal1);
                                if (sa.getFacultyId().contains("_")) {
                                      sa.setFacultyId(sa.getFacultyId().substring(0,
                                                  sa.getFacultyId().indexOf("_")));
                                }
                                //lst.setFacultyId(sa.getFacultyId());
                                if(null != sa.getPresentFacultyId()) {
            						lst.setFacultyId(sa.getPresentFacultyId());
            						lst.setUpdtFacFlag("Y");
            					}else {
            						lst.setFacultyId(sa.getFacultyId());
            						lst.setUpdtFacFlag("N");
            					}
                                lst.setStObjid(studentObjId);
                                lst.setStNumber(sa.getUsername());
                                lst.setFlag(sa.getFlag());
                                lst.setLogDate(logDate);
                                lst.setLogTime(xmlGregCal2);
                                lstLT.getItem().add(lst);
                                logger.info("list------>" + lst);

                          }

                          ids = ids + sa.getId().toString() + ",";
                          countForAbsent++;
                          if(countForAbsent == 200){
                                logger.info("countForAbsent----->"+countForAbsent);
                                logger.info("ids:----> " + ids);
                                if (!ids.isEmpty() || !ids.equals("")) {
                                      ids = ids.substring(0, ids.length() - 1);
                                }

                                logger.info("responseids:----> " + ids);
                                if (lstLT.getItem().size() > 0) {
                                      logger.info("list TT------>" + lstLT);
                                      try {
                                            String response = ws.getZATTSTPORTALUPDATEBIN().zattStPortalUpdate(lstLT);
                                            logger.info("response:----> " + response);
                                            if (response.equalsIgnoreCase("SUCCESS")) {
                                                  studentCourseAttendanceDAO.updateSapOperation("S", ids);
                                            } else {
                                                  studentCourseAttendanceDAO.updateSapOperation("F", ids);
                                            }
                                      } catch (Exception e) {
                                            logger.error("Exception while calling a webservice manually Absent", e);
                                      }
                                }

                                ids = "";
                                countForAbsent = 0;
                                lstLT.getItem().clear();
                          }

                    }
                    logger.info("ids:----> " + ids);
                    if (!ids.isEmpty() || !ids.equals("")) {
                          ids = ids.substring(0, ids.length() - 1);
                    }
                    logger.info("responseids:----> " + ids);
                    if (lstLT.getItem().size() > 0) {
                          logger.info("list TT------>" + lstLT);
                          try {
                                String response = ws.getZATTSTPORTALUPDATEBIN().zattStPortalUpdate(lstLT);
                                logger.info("response:----> " + response);
                                if (response.equalsIgnoreCase("SUCCESS")) {
                                      studentCourseAttendanceDAO.updateSapOperation("S", ids);
                                } else {
                                      studentCourseAttendanceDAO.updateSapOperation("F", ids);
                                }
                          } catch (Exception e) {
                                logger.error( "Exception while calling a webservice manually Absent", e);
                          }
                    }
              }

              List<StudentCourseAttendance> presentList = getPresentRecordsByDateForApp(date,dateLike);
              if (presentList.size() > 0) {
                    String idsForPresent = "";
                    for (StudentCourseAttendance sa : presentList) {
                          String courseId = sa.getCourseId();
                          String eventId = courseId.substring(0, 8);
                          String programId = courseId.substring(8);
                          String startTime = "", endTime = "";
                          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                          Date dt = format.parse(sa.getStartTime());
                          Date dt1 = format.parse(sa.getEndTime());
                          SimpleDateFormat formatDate1 = new SimpleDateFormat( "HH:mm:ss");

                          startTime = formatDate1.format(dt);
                          endTime = formatDate1.format(dt1);
                          Date date3 = Utils.getInIST();
                          SimpleDateFormat formatDate = new SimpleDateFormat( "yyyy-MM-dd");

                          String logDate = formatDate.format(date3);
                          String logTime = formatDate1.format(date3);

                          logger.info("logTime---->" + logTime);
                          String strtDateStr = Utils.formatDate( "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", sa.getStartTime());

                          XMLGregorianCalendar xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(startTime);
                          XMLGregorianCalendar xmlGregCal1 = DatatypeFactory.newInstance().newXMLGregorianCalendar(endTime);
                          XMLGregorianCalendar xmlGregCal2 = DatatypeFactory .newInstance().newXMLGregorianCalendar(logTime);

                          ZattStPortalLt lst = new ZattStPortalLt();
                          lst.setOrgunit(app);
                          lst.setProgId(programId);
                          lst.setAyear(sa.getAcadYear());
                          lst.setAsession(sa.getAcadSession());
                          lst.setEventId(eventId);
                          lst.setEventDate(strtDateStr);
                          lst.setStartTime(xmlGregCal);
                          lst.setEndTime(xmlGregCal1);
                          if (sa.getFacultyId().contains("_")) {
                                sa.setFacultyId(sa.getFacultyId().substring(0, sa.getFacultyId().indexOf("_")));
                          }
                          //lst.setFacultyId(sa.getFacultyId());
                          if(null != sa.getPresentFacultyId()) {
	      						lst.setFacultyId(sa.getPresentFacultyId());
	      						lst.setUpdtFacFlag("Y");
	      					}else {
	      						lst.setFacultyId(sa.getFacultyId());
	      						lst.setUpdtFacFlag("N");
	      					}
                          lst.setFlag(sa.getFlag());
                          lst.setLogDate(logDate);
                          lst.setLogTime(xmlGregCal2);
                          lstLT.getItem().add(lst);
                          logger.info("list------>" + lst);
                          idsForPresent = idsForPresent + sa.getId() + ",";

                    }

                    logger.info("ids:----> " + idsForPresent);
                    if (!idsForPresent.isEmpty() || !idsForPresent.equals("")) {
                          idsForPresent = idsForPresent.substring(0, idsForPresent.length() - 1);
                    }
                    logger.info("responseids:----> " + idsForPresent);
                    if (lstLT.getItem().size() > 0) {
                          logger.info("list TT------>" + lstLT);
                          try {
                                String response = ws.getZATTSTPORTALUPDATEBIN().zattStPortalUpdate(lstLT);
                                logger.info("response:----> " + response);
                                if (response.equalsIgnoreCase("SUCCESS")) {
                                      studentCourseAttendanceDAO.updateSapOperation("S", idsForPresent);
                                } 
                                  else {
                                     studentCourseAttendanceDAO.updateSapOperation("F", idsForPresent);

                                }
                            responseData = response;
                              return responseData;
                          } catch (Exception e) {
                               logger.error("Exception while calling a webservice manually", e);
                            responseData = e.getMessage()+"_ErrorOccurred400";
                          }
                    }
              }

        } catch (Exception e) {

              logger.error("Exception manually in SAP attend", e);
             responseData = e.getMessage()+"_ErrorOccurred400";
        }
               return responseData;
  }
  
  public List<StudentCourseAttendance> getAbsentRecordsByDateForApp(String date, String dateLike) {
		return studentCourseAttendanceDAO.getAbsentRecordsByDateForApp(date, dateLike);
	}
	
	public List<StudentCourseAttendance> getPresentRecordsByDateForApp(String date, String dateLike) {
		return studentCourseAttendanceDAO.getPresentRecordsByDateForApp(date,dateLike);
	}


	
	//hiren-17-06-2020
	public void sendAttendanceToSAP() {

		
		
		List<StudentCourseAttendance> markedAttendaceCourseId = getMarkedAttendaceCourseId();
		Map<String,String> courseEndTimeMap = new HashMap<String, String>();
		
		MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

		DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

		.createDefaultConnectionByDS(defaultUrl, defaultUsername,

		defaultPassword);

		DriverManagerDataSource dataSourceTimetable = multipleDBConnection

		.createConnectionByDS("jdbc:mysql://10.25.10.50:3307/", defaultUsername,
				defaultPassword, "timetable_metadata");

		timetableDAO.setDS(dataSourceTimetable);
	
		
		for(StudentCourseAttendance mac: markedAttendaceCourseId){
			String courseId = mac.getCourseId();
			String eventId = courseId.substring(0,8);
			String programId = courseId.substring(8);
			Date d= Utils.getInIST();

			SimpleDateFormat formatDate = new SimpleDateFormat(
					"dd-MM-yyyy");
			String currDate = formatDate.format(d);
			currDate = "%"+ currDate + "%";
			Timetable maxCourseEndTime = timetableDAO.getTimeToSend(eventId,programId,currDate);
			courseEndTimeMap.put(courseId,maxCourseEndTime.getEnd_time().substring(1, maxCourseEndTime.getEnd_time().length() - 1).replace(".", ":"));
		}
		
		timetableDAO.setDS(dataSourceDefaultLms);
		
		List<StudentCourseAttendance> sendAttendanceToSapList = new ArrayList<>();
		for(StudentCourseAttendance mac: markedAttendaceCourseId){
			String endTime = courseEndTimeMap.get(mac.getCourseId());
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			try {
				Date d1 = format.parse(endTime);
				
				Date crDate = Utils.getInIST();
				long diff = crDate.getTime() -  d1.getTime();
				if(diff>0){
					List<StudentCourseAttendance> absentListTmp = new ArrayList<>();
					absentListTmp = getAbsentRecordsByCourseId(mac.getCourseId());
					sendAttendanceToSapList.addAll(absentListTmp);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		logger.info("absentList---->" + sendAttendanceToSapList);
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		ZATTSTPORTALUPDATEWS ws = new ZATTSTPORTALUPDATEWS();
		ZattStPortalTt lstLT = new ZattStPortalTt();
		
		try {
			if (sendAttendanceToSapList.size() > 0) {
				String ids = "";
				int countForAbsent = 0;
				for (StudentCourseAttendance sa : sendAttendanceToSapList) {
					String courseId = sa.getCourseId();
					String eventId = courseId.substring(0, 8);
					String programId = courseId.substring(8);
					String studAcadSession = "", acadYear = "", startTime = "", endTime = "";

					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date dt = format.parse(sa.getStartTime());
					Date dt1 = format.parse(sa.getEndTime());
					SimpleDateFormat formatDate1 = new SimpleDateFormat(
							"HH:mm:ss");
					startTime = formatDate1.format(dt);
					endTime = formatDate1.format(dt1);

					Date date3 = Utils.getInIST();

					SimpleDateFormat formatDate = new SimpleDateFormat(
							"yyyy-MM-dd");
					String logDate = formatDate.format(date3);
					String logTime = formatDate1.format(date3);
					logger.info("logTime---->" + logTime);

					String strtDateStr = Utils.formatDate(
							"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
							sa.getStartTime());

					XMLGregorianCalendar xmlGregCal = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(startTime);

					XMLGregorianCalendar xmlGregCal1 = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(endTime);

					XMLGregorianCalendar xmlGregCal2 = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(logTime);

					studAcadSession = sa.getAcadSession();
					acadYear = sa.getAcadYear();
					
					
					if(null != sa.getUsername() && !sa.getUsername().isEmpty()){
						String studentObjId = "null";
						
					      User sObj = userDAO.getStudentObjIdFromStudentMapping(sa.getUsername());
					      if(null == sObj){
						      WebTarget webTarget12 = client.target(URIUtil
						                        .encodeQuery(userRoleMgmtCrudUrl
						                        + "/getStudentObjIdByUsername?username="
						                        + sa.getUsername()));
						      Invocation.Builder invocationBuilder12 = webTarget12
						                        .request(MediaType.APPLICATION_JSON);
	
						      studentObjId = invocationBuilder12.get(String.class);
						      User u = new User();
						      u.setUsername(sa.getUsername());
						      u.setStudentObjectId(studentObjId);
						      userDAO.insertStudentObjIdToStudentMapping(u);
						      logger.info("InsertObj---> " + studentObjId);
					      }else{
					            studentObjId = sObj.getStudentObjectId();
					      }

						/*WebTarget webTarget12 = client.target(URIUtil
								.encodeQuery(userRoleMgmtCrudUrl
										+ "/getStudentObjIdByUsername?username="
										+ sa.getUsername()));
						Invocation.Builder invocationBuilder12 = webTarget12
								.request(MediaType.APPLICATION_JSON);

						studentObjId = invocationBuilder12.get(String.class);*/
						
						logger.info("stoID---> " + studentObjId);

						if (!studentObjId.equals("null")) {
							ZattStPortalLt lst = new ZattStPortalLt();
							lst.setOrgunit(app);
							lst.setProgId(programId);
							lst.setAyear(acadYear);
							lst.setAsession(studAcadSession);
							lst.setEventId(eventId);
							lst.setEventDate(strtDateStr);
							lst.setStartTime(xmlGregCal);
							lst.setEndTime(xmlGregCal1);
							if (sa.getFacultyId().contains("_")) {
								sa.setFacultyId(sa.getFacultyId().substring(0,
										sa.getFacultyId().indexOf("_")));
							}
							//lst.setFacultyId(sa.getFacultyId());
							if(null != sa.getPresentFacultyId()) {
								lst.setFacultyId(sa.getPresentFacultyId());
								lst.setUpdtFacFlag("Y");
							}else {
								lst.setFacultyId(sa.getFacultyId());
								lst.setUpdtFacFlag("N");
							}
							lst.setStObjid(studentObjId);
							lst.setStNumber(sa.getUsername());
							lst.setFlag(sa.getFlag());
							lst.setLogDate(logDate);
							lst.setLogTime(xmlGregCal2);

							lstLT.getItem().add(lst);

							logger.info("list------>" + lst);
						}
						ids = ids + sa.getId().toString() + ",";
						
						
					}else{
						ZattStPortalLt lst = new ZattStPortalLt();
						lst.setOrgunit(app);
						lst.setProgId(programId);
						lst.setAyear(sa.getAcadYear());
						lst.setAsession(sa.getAcadSession());
						lst.setEventId(eventId);
						lst.setEventDate(strtDateStr);
						lst.setStartTime(xmlGregCal);
						lst.setEndTime(xmlGregCal1);
						if (sa.getFacultyId().contains("_")) {
							sa.setFacultyId(sa.getFacultyId().substring(0,
									sa.getFacultyId().indexOf("_")));
						}
						//lst.setFacultyId(sa.getFacultyId());
						if(null != sa.getPresentFacultyId()) {
							lst.setFacultyId(sa.getPresentFacultyId());
							lst.setUpdtFacFlag("Y");
						}else {
							lst.setFacultyId(sa.getFacultyId());
							lst.setUpdtFacFlag("N");
						}
						lst.setFlag(sa.getFlag());
						lst.setLogDate(logDate);
						lst.setLogTime(xmlGregCal2);
						

						lstLT.getItem().add(lst);
						
						logger.info("list------>" + lst);

						ids = ids + sa.getId() + ",";
						
					}
					
					countForAbsent++;
                    if(countForAbsent == 200){
                          logger.info("countForAbsent----->"+countForAbsent);
                          logger.info("ids:----> " + ids);
                          if (!ids.isEmpty() || !ids.equals("")) {
                                ids = ids.substring(0, ids.length() - 1);
                          }
                          logger.info("responseids:----> " + ids);
                          if (lstLT.getItem().size() > 0) {

                                logger.info("list TT------>" + lstLT);

                                try {

                                      String response = ws.getZATTSTPORTALUPDATEBIN()
                                                  .zattStPortalUpdate(lstLT);

                                      logger.info("response:----> " + response);
                                      if (response.equalsIgnoreCase("SUCCESS")) {
                                            studentCourseAttendanceDAO.updateSapOperation("S", ids);
                                            sendEmailOfAttendance("Attendance Status of "+app,"Attendacne Record Sent Success count: "+String.valueOf(lstLT.getItem().size()));

                                      } else {
                                            studentCourseAttendanceDAO.updateSapOperation("F", ids);
                                            sendEmailOfAttendance("Attendance Status of "+app,"Failed From SAP ");
                                      }
                                } catch (Exception e) {
                                		sendEmailOfAttendance("Attendance Status of "+app,"Error: "+e.getMessage());
                                      logger.error(
                                                  "Exception while calling a webservice manually Absent", e);

                                }
                          }
                          ids = "";
                          countForAbsent = 0;
                          lstLT.getItem().clear();
                    }
					
				}
				logger.info("ids:----> " + ids);
				if (!ids.isEmpty() || !ids.equals("")) {
					ids = ids.substring(0, ids.length() - 1);
				}
				logger.info("responseids:----> " + ids);
				if (lstLT.getItem().size() > 0) {

					

					try {

						String response = ws.getZATTSTPORTALUPDATEBIN()
								.zattStPortalUpdate(lstLT);
						
						logger.info("response:----> " + response);
						if (response.equalsIgnoreCase("SUCCESS")) {
							studentCourseAttendanceDAO.updateSapOperation("S", ids);
							sendEmailOfAttendance("Attendance Status of "+app,"Attendacne Record Sent Success count: "+String.valueOf(lstLT.getItem().size()));

						} else {
							studentCourseAttendanceDAO.updateSapOperation("F", ids);
							sendEmailOfAttendance("Attendance Status of "+app,"Failed From SAP ");
						}
					} catch (Exception e) {
						sendEmailOfAttendance("Attendance Status of "+app,"Error: "+e.getMessage());
						logger.error(
								"Exception while calling a webservice hourwise", e);

					}
				}
			}
			
		} catch (Exception e) {
			sendEmailOfAttendance("Attendance Status of "+app,"Error: "+e.getMessage());
			logger.error("Exception hourwise in SAP attend", e);
		}
	}

	public void sendLastAttendanceToSAP() {

		String ids = "";
		List<StudentCourseAttendance> absentList = getLastAbsentRecords();
		logger.info("absentList---->" + absentList);
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		ZATTSTPORTALUPDATEWS ws = new ZATTSTPORTALUPDATEWS();
		ZattStPortalTt lstLT = new ZattStPortalTt();
		try {
			if (absentList.size() > 0) {
				for (StudentCourseAttendance sa : absentList) {
					String courseId = sa.getCourseId();
					String eventId = courseId.substring(0, 8);
					String programId = courseId.substring(8);
					String studAcadSession = "", acadYear = "", startTime = "", endTime = "";

					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date dt = format.parse(sa.getStartTime());
					Date dt1 = format.parse(sa.getEndTime());
					SimpleDateFormat formatDate1 = new SimpleDateFormat(
							"HH:mm:ss");
					// logger.info("times------->" + formatDate1.format(dt));
					// logger.info("times1------->" + formatDate1.format(dt1));
					startTime = formatDate1.format(dt);
					endTime = formatDate1.format(dt1);

					Date date3 = Utils.getInIST();

					SimpleDateFormat formatDate = new SimpleDateFormat(
							"yyyy-MM-dd");
					String logDate = formatDate.format(date3);
					String logTime = formatDate1.format(date3);
					// logger.info("logDate---->" + logDate);
					logger.info("logTime---->" + logTime);

					// String strtDateStr = formatDate.format(dt);
					// logger.info("sa start time" + sa.getStartTime());
					String strtDateStr = Utils.formatDate(
							"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
							sa.getStartTime());
					/* strtDateStr = strtDateStr.replaceAll("-", ""); */
					// logger.info("strtDateStr------>" + strtDateStr);

					XMLGregorianCalendar xmlGregCal = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(startTime);

					XMLGregorianCalendar xmlGregCal1 = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(endTime);

					XMLGregorianCalendar xmlGregCal2 = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(logTime);

					studAcadSession = sa.getAcadSession();
					acadYear = sa.getAcadYear();

//					String studentObjId = "null";
//					WebTarget webTarget12 = client.target(URIUtil
//							.encodeQuery(userRoleMgmtCrudUrl
//									+ "/getStudentObjIdByUsername?username="
//									+ sa.getUsername()));
//					Invocation.Builder invocationBuilder12 = webTarget12
//							.request(MediaType.APPLICATION_JSON);
//
//					studentObjId = invocationBuilder12.get(String.class);
//					logger.info("stoID---> " + studentObjId);
					String studentObjId = "null";

					  User sObj = userDAO.getStudentObjIdFromStudentMapping(sa.getUsername());
					  if(null == sObj){
						  WebTarget webTarget12 = client.target(URIUtil
											.encodeQuery(userRoleMgmtCrudUrl
											+ "/getStudentObjIdByUsername?username="
											+ sa.getUsername()));
						  Invocation.Builder invocationBuilder12 = webTarget12
											.request(MediaType.APPLICATION_JSON);

						  studentObjId = invocationBuilder12.get(String.class);
						  User u = new User();
						  u.setUsername(sa.getUsername());
						  u.setStudentObjectId(studentObjId);
						  userDAO.insertStudentObjIdToStudentMapping(u);
						  logger.info("InsertObj---> " + studentObjId);
					  }else{
							studentObjId = sObj.getStudentObjectId();
					  }


					if (!studentObjId.equals("null")) {
						ZattStPortalLt lst = new ZattStPortalLt();
						lst.setOrgunit(app);
						lst.setProgId(programId);
						lst.setAyear(acadYear);
						lst.setAsession(studAcadSession);
						lst.setEventId(eventId);
						lst.setEventDate(strtDateStr);
						lst.setStartTime(xmlGregCal);
						lst.setEndTime(xmlGregCal1);
						if (sa.getFacultyId().contains("_")) {
							sa.setFacultyId(sa.getFacultyId().substring(0,
									sa.getFacultyId().indexOf("_")));
						}
						//lst.setFacultyId(sa.getFacultyId());
						if(null != sa.getPresentFacultyId()) {
							lst.setFacultyId(sa.getPresentFacultyId());
							lst.setUpdtFacFlag("Y");
						}else {
							lst.setFacultyId(sa.getFacultyId());
							lst.setUpdtFacFlag("N");
						}
						lst.setStObjid(studentObjId);
						lst.setStNumber(sa.getUsername());
						lst.setFlag(sa.getFlag());
						lst.setLogDate(logDate);
						lst.setLogTime(xmlGregCal2);

						lstLT.getItem().add(lst);

						logger.info("list------>" + lst);
					}
					ids = ids + sa.getId().toString() + ",";
				}

			}
			List<StudentCourseAttendance> presentList = getPresentRecords();
			if (presentList.size() > 0) {
				for (StudentCourseAttendance sa : presentList) {
					String courseId = sa.getCourseId();
					String eventId = courseId.substring(0, 8);
					String programId = courseId.substring(8);
					String startTime = "", endTime = "";

					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date dt = format.parse(sa.getStartTime());
					Date dt1 = format.parse(sa.getEndTime());
					SimpleDateFormat formatDate1 = new SimpleDateFormat(
							"HH:mm:ss");
					// logger.info("times------->" + formatDate1.format(dt));
					// logger.info("times1------->" + formatDate1.format(dt1));
					startTime = formatDate1.format(dt);
					endTime = formatDate1.format(dt1);

					Date date3 = Utils.getInIST();

					SimpleDateFormat formatDate = new SimpleDateFormat(
							"yyyy-MM-dd");
					String logDate = formatDate.format(date3);
					String logTime = formatDate1.format(date3);
					// logger.info("logDate---->" + logDate);
					logger.info("logTime---->" + logTime);

					// String strtDateStr = formatDate.format(dt);

					// logger.info("sa start time" + sa.getStartTime());
					String strtDateStr = Utils.formatDate(
							"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
							sa.getStartTime());
					/* strtDateStr = strtDateStr.replaceAll("-", ""); */
					// logger.info("StartTime------>" + strtDateStr);

					XMLGregorianCalendar xmlGregCal = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(startTime);

					XMLGregorianCalendar xmlGregCal1 = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(endTime);

					XMLGregorianCalendar xmlGregCal2 = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(logTime);

					ZattStPortalLt lst = new ZattStPortalLt();
					lst.setOrgunit(app);
					lst.setProgId(programId);
					lst.setAyear(sa.getAcadYear());
					lst.setAsession(sa.getAcadSession());
					lst.setEventId(eventId);
					lst.setEventDate(strtDateStr);
					lst.setStartTime(xmlGregCal);
					lst.setEndTime(xmlGregCal1);
					if (sa.getFacultyId().contains("_")) {
						sa.setFacultyId(sa.getFacultyId().substring(0,
								sa.getFacultyId().indexOf("_")));
					}
					//lst.setFacultyId(sa.getFacultyId());
					if(null != sa.getPresentFacultyId()) {
						lst.setFacultyId(sa.getPresentFacultyId());
						lst.setUpdtFacFlag("Y");
					}else {
						lst.setFacultyId(sa.getFacultyId());
						lst.setUpdtFacFlag("N");
					}
					/*
					 * lst.setStObjid(""); lst.setStNumber("");
					 */
					lst.setFlag(sa.getFlag());
					lst.setLogDate(logDate);
					lst.setLogTime(xmlGregCal2);
					lstLT.getItem().add(lst);

					logger.info("list------>" + lst);

					ids = ids + sa.getId() + ",";
				}

			}
			logger.info("ids:----> " + ids);
			if (!ids.isEmpty() || !ids.equals("")) {
				ids = ids.substring(0, ids.length() - 1);
			}
			logger.info("responseids:----> " + ids);
			if (lstLT.getItem().size() > 0) {

				logger.info("list TT------>" + lstLT);

				try {

					String response = ws.getZATTSTPORTALUPDATEBIN()
							.zattStPortalUpdate(lstLT);

					logger.info("response:----> " + response);
					if (response.equalsIgnoreCase("SUCCESS")) {
						studentCourseAttendanceDAO.updateSapOperation("S", ids);
						sendEmailOfAttendance("Attendance Status of "+app,"Attendacne Record Sent Success count: "+String.valueOf(lstLT.getItem().size()));

					} else {
						studentCourseAttendanceDAO.updateSapOperation("F", ids);
						sendEmailOfAttendance("Attendance Status of "+app,"Failed From SAP ");
					}
				} catch (Exception e) {
					sendEmailOfAttendance("Attendance Status of "+app,"Error: "+e.getMessage());
					logger.error(
							"Exception while calling a webservice at 12:30", e);

				}
			}

		} catch (Exception e) {
			sendEmailOfAttendance("Attendance Status of "+app,"Error: "+e.getMessage());
			logger.error("Exception at 12:30 in SAP attend", e);
		}
	}

	public void sendAttendanceToSAPByDate(String date) {

		
		List<StudentCourseAttendance> absentList = getAbsentRecordsByDate(date);
		logger.info("absentList---->" + absentList);
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		ZATTSTPORTALUPDATEWS ws = new ZATTSTPORTALUPDATEWS();
		ZattStPortalTt lstLT = new ZattStPortalTt();
		try {
			if (absentList.size() > 0) {
				String ids = "";
				int countForAbsent = 0;
				for (StudentCourseAttendance sa : absentList) {
					String courseId = sa.getCourseId();
					String eventId = courseId.substring(0, 8);
					String programId = courseId.substring(8);
					String studAcadSession = "", acadYear = "", startTime = "", endTime = "";

					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date dt = format.parse(sa.getStartTime());
					Date dt1 = format.parse(sa.getEndTime());
					SimpleDateFormat formatDate1 = new SimpleDateFormat(
							"HH:mm:ss");
					// logger.info("times------->" + formatDate1.format(dt));
					// logger.info("times1------->" + formatDate1.format(dt1));
					startTime = formatDate1.format(dt);
					endTime = formatDate1.format(dt1);

					Date date3 = Utils.getInIST();

					SimpleDateFormat formatDate = new SimpleDateFormat(
							"yyyy-MM-dd");
					String logDate = formatDate.format(date3);
					String logTime = formatDate1.format(date3);
					// logger.info("logDate---->" + logDate);
					logger.info("logTime---->" + logTime);

					// String strtDateStr = formatDate.format(dt);
					// logger.info("sa start time" + sa.getStartTime());
					String strtDateStr = Utils.formatDate(
							"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
							sa.getStartTime());
					/* strtDateStr = strtDateStr.replaceAll("-", ""); */
					// logger.info("strtDateStr------>" + strtDateStr);

					XMLGregorianCalendar xmlGregCal = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(startTime);

					XMLGregorianCalendar xmlGregCal1 = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(endTime);

					XMLGregorianCalendar xmlGregCal2 = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(logTime);

					studAcadSession = sa.getAcadSession();
					acadYear = sa.getAcadYear();

					String studentObjId = "null";
				      User sObj = userDAO.getStudentObjIdFromStudentMapping(sa.getUsername());
				      if(null == sObj){
				      WebTarget webTarget12 = client.target(URIUtil
				                        .encodeQuery(userRoleMgmtCrudUrl
				                        + "/getStudentObjIdByUsername?username="
				                        + sa.getUsername()));
				      Invocation.Builder invocationBuilder12 = webTarget12
				                        .request(MediaType.APPLICATION_JSON);

				      studentObjId = invocationBuilder12.get(String.class);
				      User u = new User();
				      u.setUsername(sa.getUsername());
				      u.setStudentObjectId(studentObjId);
				      userDAO.insertStudentObjIdToStudentMapping(u);
				      logger.info("InsertObj---> " + studentObjId);
				      }else{
				            studentObjId = sObj.getStudentObjectId();
				      }

					/*String studentObjId = "null";
					WebTarget webTarget12 = client.target(URIUtil
							.encodeQuery(userRoleMgmtCrudUrl
									+ "/getStudentObjIdByUsername?username="
									+ sa.getUsername()));
					Invocation.Builder invocationBuilder12 = webTarget12
							.request(MediaType.APPLICATION_JSON);

					studentObjId = invocationBuilder12.get(String.class);*/
					logger.info("stoID---> " + studentObjId);

					if (!studentObjId.equals("null")) {
						ZattStPortalLt lst = new ZattStPortalLt();
						lst.setOrgunit(app);
						lst.setProgId(programId);
						lst.setAyear(acadYear);
						lst.setAsession(studAcadSession);
						lst.setEventId(eventId);
						lst.setEventDate(strtDateStr);
						lst.setStartTime(xmlGregCal);
						lst.setEndTime(xmlGregCal1);
						if (sa.getFacultyId().contains("_")) {
							sa.setFacultyId(sa.getFacultyId().substring(0,
									sa.getFacultyId().indexOf("_")));
						}
						//lst.setFacultyId(sa.getFacultyId());
						if(null != sa.getPresentFacultyId()) {
							lst.setFacultyId(sa.getPresentFacultyId());
							lst.setUpdtFacFlag("Y");
						}else {
							lst.setFacultyId(sa.getFacultyId());
							lst.setUpdtFacFlag("N");
						}
						lst.setStObjid(studentObjId);
						lst.setStNumber(sa.getUsername());
						lst.setFlag(sa.getFlag());
						lst.setLogDate(logDate);
						lst.setLogTime(xmlGregCal2);

						lstLT.getItem().add(lst);

						logger.info("list------>" + lst);
					}
					ids = ids + sa.getId().toString() + ",";
					countForAbsent++;
					if(countForAbsent == 200){
						logger.info("countForAbsent----->"+countForAbsent);
						logger.info("ids:----> " + ids);
						if (!ids.isEmpty() || !ids.equals("")) {
							ids = ids.substring(0, ids.length() - 1);
						}
						logger.info("responseids:----> " + ids);
						if (lstLT.getItem().size() > 0) {

							logger.info("list TT------>" + lstLT);

							try {

								String response = ws.getZATTSTPORTALUPDATEBIN()
										.zattStPortalUpdate(lstLT);

								logger.info("response:----> " + response);
								if (response.equalsIgnoreCase("SUCCESS")) {
									studentCourseAttendanceDAO.updateSapOperation("S", ids);
									sendEmailOfAttendance("Attendance Status of "+app,"Attendacne Record Sent Success count: "+String.valueOf(lstLT.getItem().size()));
								} else {
									studentCourseAttendanceDAO.updateSapOperation("F", ids);
									sendEmailOfAttendance("Attendance Status of "+app,"Failed From SAP ");
								}
							} catch (Exception e) {
								sendEmailOfAttendance("Attendance Status of "+app,"Error: "+e.getMessage());
								logger.error(
										"Exception while calling a webservice manually Absent", e);

							}
						}
						ids = "";
						countForAbsent = 0;
						lstLT.getItem().clear();
					}
					
				}
				logger.info("ids:----> " + ids);
				if (!ids.isEmpty() || !ids.equals("")) {
					ids = ids.substring(0, ids.length() - 1);
				}
				logger.info("responseids:----> " + ids);
				if (lstLT.getItem().size() > 0) {

					logger.info("list TT------>" + lstLT);

					try {

						String response = ws.getZATTSTPORTALUPDATEBIN()
								.zattStPortalUpdate(lstLT);

						logger.info("response:----> " + response);
						if (response.equalsIgnoreCase("SUCCESS")) {
							studentCourseAttendanceDAO.updateSapOperation("S", ids);
							sendEmailOfAttendance("Attendance Status of "+app,"Attendacne Record Sent Success count: "+String.valueOf(lstLT.getItem().size()));
						} else {
							studentCourseAttendanceDAO.updateSapOperation("F", ids);
							sendEmailOfAttendance("Attendance Status of "+app,"Failed From SAP ");
						}
					} catch (Exception e) {
						sendEmailOfAttendance("Attendance Status of "+app,"Error: "+e.getMessage());
						logger.error(
								"Exception while calling a webservice manually Absent", e);

					}
				}

			}
			List<StudentCourseAttendance> presentList = getPresentRecordsByDate(date);
			if (presentList.size() > 0) {
				String idsForPresent = "";
				for (StudentCourseAttendance sa : presentList) {
					String courseId = sa.getCourseId();
					String eventId = courseId.substring(0, 8);
					String programId = courseId.substring(8);
					String startTime = "", endTime = "";

					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date dt = format.parse(sa.getStartTime());
					Date dt1 = format.parse(sa.getEndTime());
					SimpleDateFormat formatDate1 = new SimpleDateFormat(
							"HH:mm:ss");
					// logger.info("times------->" + formatDate1.format(dt));
					// logger.info("times1------->" + formatDate1.format(dt1));
					startTime = formatDate1.format(dt);
					endTime = formatDate1.format(dt1);

					Date date3 = Utils.getInIST();

					SimpleDateFormat formatDate = new SimpleDateFormat(
							"yyyy-MM-dd");
					String logDate = formatDate.format(date3);
					String logTime = formatDate1.format(date3);
					// logger.info("logDate---->" + logDate);
					logger.info("logTime---->" + logTime);

					// String strtDateStr = formatDate.format(dt);

					// logger.info("sa start time" + sa.getStartTime());
					String strtDateStr = Utils.formatDate(
							"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
							sa.getStartTime());
					/* strtDateStr = strtDateStr.replaceAll("-", ""); */
					// logger.info("StartTime------>" + strtDateStr);

					XMLGregorianCalendar xmlGregCal = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(startTime);

					XMLGregorianCalendar xmlGregCal1 = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(endTime);

					XMLGregorianCalendar xmlGregCal2 = DatatypeFactory
							.newInstance().newXMLGregorianCalendar(logTime);

					ZattStPortalLt lst = new ZattStPortalLt();
					lst.setOrgunit(app);
					lst.setProgId(programId);
					lst.setAyear(sa.getAcadYear());
					lst.setAsession(sa.getAcadSession());
					lst.setEventId(eventId);
					lst.setEventDate(strtDateStr);
					lst.setStartTime(xmlGregCal);
					lst.setEndTime(xmlGregCal1);
					if (sa.getFacultyId().contains("_")) {
						sa.setFacultyId(sa.getFacultyId().substring(0,
								sa.getFacultyId().indexOf("_")));
					}
					//lst.setFacultyId(sa.getFacultyId());
					if(null != sa.getPresentFacultyId()) {
						lst.setFacultyId(sa.getPresentFacultyId());
						lst.setUpdtFacFlag("Y");
					}else {
						lst.setFacultyId(sa.getFacultyId());
						lst.setUpdtFacFlag("N");
					}
					/*
					 * lst.setStObjid(""); lst.setStNumber("");
					 */
					lst.setFlag(sa.getFlag());
					lst.setLogDate(logDate);
					lst.setLogTime(xmlGregCal2);
					lstLT.getItem().add(lst);

					logger.info("list------>" + lst);

					idsForPresent = idsForPresent + sa.getId() + ",";
				}
				logger.info("ids:----> " + idsForPresent);
				if (!idsForPresent.isEmpty() || !idsForPresent.equals("")) {
					idsForPresent = idsForPresent.substring(0, idsForPresent.length() - 1);
				}
				logger.info("responseids:----> " + idsForPresent);
				if (lstLT.getItem().size() > 0) {

					logger.info("list TT------>" + lstLT);

					try {

						String response = ws.getZATTSTPORTALUPDATEBIN()
								.zattStPortalUpdate(lstLT);

						logger.info("response:----> " + response);
						if (response.equalsIgnoreCase("SUCCESS")) {
							studentCourseAttendanceDAO.updateSapOperation("S", idsForPresent);
							sendEmailOfAttendance("Attendance Status of "+app,"Attendacne Record Sent Success count: "+String.valueOf(lstLT.getItem().size()));
						} else {
							studentCourseAttendanceDAO.updateSapOperation("F", idsForPresent);
							sendEmailOfAttendance("Attendance Status of "+app,"Failed From SAP ");
						}
					} catch (Exception e) {
						sendEmailOfAttendance("Attendance Status of "+app,"Error: "+e.getMessage());
						logger.error(
								"Exception while calling a webservice manually Present", e);

					}
				}

			}
			

		} catch (Exception e) {
			sendEmailOfAttendance("Attendance Status of "+app,"Error: "+e.getMessage());
			logger.error("Exception manually in SAP attend", e);
		}
	}
	
	public List<StudentCourseAttendance> getStudentCourseAttendanceForReport(String dateString, List<String> facultyList) {
		
		return studentCourseAttendanceDAO.getStudentCourseAttendanceForReport(dateString, facultyList);
	}
	
	public String getXlsxforMarkAttendanceReport(List<StudentCourseAttendance> scaList) {
		Date date = new Date();
		File file = new File(downloadAllFolder + File.separator
				+ "MarkAttendanceReport"
				+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
				+ ".xlsx");
		try {
			file.createNewFile();
			
			
			List<StudentCourseAttendance> admStatus = scaList;

			Map<String, List<Map<String, Object>>> lstExcelData = new HashMap<String, List<Map<String, Object>>>();
			
			
			List<String> headers = new ArrayList<String>(Arrays.asList("Sr.No",
					"Faculty Id", "First Name", "Last Name", "Total Lectures", "Total Marked Lectures","Campus"));
			
			
			int count = 1;
			for (StudentCourseAttendance ad : admStatus) {
				

				Map<String, Object> map = new HashMap();
				
				
				map.put("Sr.No", count);
				map.put("Faculty Id", ad.getFacultyId() == null ? "-" : ad.getFacultyId());
				map.put("First Name", ad.getFirstname() == null ? "-" : ad.getFirstname());
				map.put("Last Name", ad.getLastname() == null ? "-" : ad.getLastname());
				map.put("Total Lectures", ad.getTotal_count() == null ? "-" : ad.getTotal_count());
				map.put("Total Marked Lectures", ad.getAttendanceTakenCount() == null ? "-" : ad.getAttendanceTakenCount());
				map.put("Campus", ad.getCampusName() == null ? "-" : ad.getCampusName());
				

				if (lstExcelData.containsKey("MarkAttendanceReport")) {
					List<Map<String, Object>> lst = lstExcelData
							.get("MarkAttendanceReport");
					lst.add(map);

				} else {
					List<Map<String, Object>> lst = new ArrayList<Map<String, Object>>();
					lst.add(map);

					lstExcelData.put("MarkAttendanceReport", lst);
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
	
	public List<StudentCourseAttendance> getMarkedAttendaceCourseId() {
		return studentCourseAttendanceDAO.getMarkedAttendaceCourseId();
	}
	public List<StudentCourseAttendance> getAbsentRecordsByCourseId(String courseId) {
		return studentCourseAttendanceDAO.getAbsentRecordsByCourseId(courseId);
	}
	
	public List<StudentCourseAttendance> findByCourseIdAndDateTime(
			List<Long> courseId, String startTime, String endTime, String facultyId) {
		
		return studentCourseAttendanceDAO.findByCourseIdAndDateTime(courseId,startTime,endTime,facultyId);
	}
	public List<StudentCourseAttendance> getAllPresentRecord(List<Long> courseId,
			String startDate, String endDate, String facultyId) {

		return studentCourseAttendanceDAO.getAllPresentRecord(courseId,
				startDate, endDate, facultyId);
	}
	
	  public List<StudentCourseAttendance> getDistinctEndTime(String startTime, String status) {
		return studentCourseAttendanceDAO.getDistinctEndTime(startTime, status);
	}
	
	
	
	public String sendAttendanceToSAPByDateForApp(String date) {

        String responseData="";
        List<StudentCourseAttendance> absentList = getAbsentRecordsByDate(date);

        logger.info("absentList---->" + absentList);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        ZATTSTPORTALUPDATEWS ws = new ZATTSTPORTALUPDATEWS();

        ZattStPortalTt lstLT = new ZattStPortalTt();

        try {

              if (absentList.size() > 0) {

                    String ids = "";

                    int countForAbsent = 0;

                    for (StudentCourseAttendance sa : absentList) {

                          String courseId = sa.getCourseId();

                          String eventId = courseId.substring(0, 8);

                          String programId = courseId.substring(8);

                          String studAcadSession = "", acadYear = "", startTime = "", endTime = "";



                          SimpleDateFormat formatter = new SimpleDateFormat(

                                      "yyyy-MM-dd HH:mm:ss");

                          Date dt = format.parse(sa.getStartTime());

                          Date dt1 = format.parse(sa.getEndTime());

                          SimpleDateFormat formatDate1 = new SimpleDateFormat(

                                      "HH:mm:ss");

                          // logger.info("times------->" + formatDate1.format(dt));

                          // logger.info("times1------->" + formatDate1.format(dt1));

                          startTime = formatDate1.format(dt);

                          endTime = formatDate1.format(dt1);



                          Date date3 = Utils.getInIST();



                          SimpleDateFormat formatDate = new SimpleDateFormat(

                                      "yyyy-MM-dd");

                          String logDate = formatDate.format(date3);

                          String logTime = formatDate1.format(date3);

                          // logger.info("logDate---->" + logDate);

                          logger.info("logTime---->" + logTime);



                          // String strtDateStr = formatDate.format(dt);

                          // logger.info("sa start time" + sa.getStartTime());

                          String strtDateStr = Utils.formatDate(

                                      "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",

                                      sa.getStartTime());

                          /* strtDateStr = strtDateStr.replaceAll("-", ""); */

                          // logger.info("strtDateStr------>" + strtDateStr);



                          XMLGregorianCalendar xmlGregCal = DatatypeFactory

                                      .newInstance().newXMLGregorianCalendar(startTime);



                          XMLGregorianCalendar xmlGregCal1 = DatatypeFactory

                                      .newInstance().newXMLGregorianCalendar(endTime);



                          XMLGregorianCalendar xmlGregCal2 = DatatypeFactory

                                      .newInstance().newXMLGregorianCalendar(logTime);



                          studAcadSession = sa.getAcadSession();

                          acadYear = sa.getAcadYear();



                          String studentObjId = "null";

                          User sObj = userDAO.getStudentObjIdFromStudentMapping(sa.getUsername());

                          if(null == sObj){

                          WebTarget webTarget12 = client.target(URIUtil

                                            .encodeQuery(userRoleMgmtCrudUrl

                                            + "/getStudentObjIdByUsername?username="

                                            + sa.getUsername()));

                          Invocation.Builder invocationBuilder12 = webTarget12

                                            .request(MediaType.APPLICATION_JSON);



                          studentObjId = invocationBuilder12.get(String.class);

                          User u = new User();

                          u.setUsername(sa.getUsername());

                          u.setStudentObjectId(studentObjId);

                          userDAO.insertStudentObjIdToStudentMapping(u);

                          }else{

                                studentObjId = sObj.getStudentObjectId();

                          }



                          /*String studentObjId = "null";

                          WebTarget webTarget12 = client.target(URIUtil

                                      .encodeQuery(userRoleMgmtCrudUrl

                                                  + "/getStudentObjIdByUsername?username="

                                                  + sa.getUsername()));

                          Invocation.Builder invocationBuilder12 = webTarget12

                                      .request(MediaType.APPLICATION_JSON);



                          studentObjId = invocationBuilder12.get(String.class);*/

                          logger.info("stoID---> " + studentObjId);



                          if (!studentObjId.equals("null")) {

                                ZattStPortalLt lst = new ZattStPortalLt();

                                lst.setOrgunit(app);

                                lst.setProgId(programId);

                                lst.setAyear(acadYear);

                                lst.setAsession(studAcadSession);

                                lst.setEventId(eventId);

                                lst.setEventDate(strtDateStr);

                                lst.setStartTime(xmlGregCal);

                                lst.setEndTime(xmlGregCal1);

                                if (sa.getFacultyId().contains("_")) {

                                      sa.setFacultyId(sa.getFacultyId().substring(0,

                                                  sa.getFacultyId().indexOf("_")));

                                }

                                //lst.setFacultyId(sa.getFacultyId());
                                
                                if(null != sa.getPresentFacultyId()) {
            						lst.setFacultyId(sa.getPresentFacultyId());
            						lst.setUpdtFacFlag("Y");
            					}else {
            						lst.setFacultyId(sa.getFacultyId());
            						lst.setUpdtFacFlag("N");
            					}

                                lst.setStObjid(studentObjId);

                                lst.setStNumber(sa.getUsername());

                                lst.setFlag(sa.getFlag());

                                lst.setLogDate(logDate);

                                lst.setLogTime(xmlGregCal2);



                                lstLT.getItem().add(lst);



                                logger.info("list------>" + lst);

                          }

                          ids = ids + sa.getId().toString() + ",";

                          countForAbsent++;

                          if(countForAbsent == 200){

                                logger.info("countForAbsent----->"+countForAbsent);

                                logger.info("ids:----> " + ids);

                                if (!ids.isEmpty() || !ids.equals("")) {

                                      ids = ids.substring(0, ids.length() - 1);

                                }

                                logger.info("responseids:----> " + ids);

                                if (lstLT.getItem().size() > 0) {



                                      logger.info("list TT------>" + lstLT);



                                      try {



                                            String response = ws.getZATTSTPORTALUPDATEBIN()

                                                        .zattStPortalUpdate(lstLT);



                                            logger.info("response:----> " + response);

                                            if (response.equalsIgnoreCase("SUCCESS")) {

                                                  studentCourseAttendanceDAO.updateSapOperation("S", ids);



                                            } else {

                                                  studentCourseAttendanceDAO.updateSapOperation("F", ids);

                                            }

                                      } catch (Exception e) {



                                            logger.error(

                                                        "Exception while calling a webservice manually Absent", e);



                                      }

                                }

                                ids = "";

                                countForAbsent = 0;

                                lstLT.getItem().clear();

                          }

                         

                    }

                    logger.info("ids:----> " + ids);

                    if (!ids.isEmpty() || !ids.equals("")) {

                          ids = ids.substring(0, ids.length() - 1);

                    }

                    logger.info("responseids:----> " + ids);

                    if (lstLT.getItem().size() > 0) {



                          logger.info("list TT------>" + lstLT);



                          try {



                                String response = ws.getZATTSTPORTALUPDATEBIN()

                                            .zattStPortalUpdate(lstLT);



                                logger.info("response:----> " + response);

                                if (response.equalsIgnoreCase("SUCCESS")) {

                                      studentCourseAttendanceDAO.updateSapOperation("S", ids);



                                } else {

                                      studentCourseAttendanceDAO.updateSapOperation("F", ids);

                                }

                          } catch (Exception e) {



                                logger.error(

                                            "Exception while calling a webservice manually Absent", e);



                          }

                    }



              }

              List<StudentCourseAttendance> presentList = getPresentRecordsByDate(date);

              if (presentList.size() > 0) {

                    String idsForPresent = "";

                    for (StudentCourseAttendance sa : presentList) {

                          String courseId = sa.getCourseId();

                          String eventId = courseId.substring(0, 8);

                          String programId = courseId.substring(8);

                          String startTime = "", endTime = "";



                          SimpleDateFormat formatter = new SimpleDateFormat(

                                      "yyyy-MM-dd HH:mm:ss");

                          Date dt = format.parse(sa.getStartTime());

                          Date dt1 = format.parse(sa.getEndTime());

                          SimpleDateFormat formatDate1 = new SimpleDateFormat(

                                      "HH:mm:ss");

                          // logger.info("times------->" + formatDate1.format(dt));

                          // logger.info("times1------->" + formatDate1.format(dt1));

                          startTime = formatDate1.format(dt);

                          endTime = formatDate1.format(dt1);



                          Date date3 = Utils.getInIST();



                          SimpleDateFormat formatDate = new SimpleDateFormat(

                                      "yyyy-MM-dd");

                          String logDate = formatDate.format(date3);

                          String logTime = formatDate1.format(date3);

                          // logger.info("logDate---->" + logDate);

                          logger.info("logTime---->" + logTime);



                          // String strtDateStr = formatDate.format(dt);



                          // logger.info("sa start time" + sa.getStartTime());

                          String strtDateStr = Utils.formatDate(

                                      "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",

                                      sa.getStartTime());

                          /* strtDateStr = strtDateStr.replaceAll("-", ""); */

                          // logger.info("StartTime------>" + strtDateStr);



                          XMLGregorianCalendar xmlGregCal = DatatypeFactory

                                      .newInstance().newXMLGregorianCalendar(startTime);



                          XMLGregorianCalendar xmlGregCal1 = DatatypeFactory

                                      .newInstance().newXMLGregorianCalendar(endTime);



                          XMLGregorianCalendar xmlGregCal2 = DatatypeFactory

                                      .newInstance().newXMLGregorianCalendar(logTime);



                          ZattStPortalLt lst = new ZattStPortalLt();

                          lst.setOrgunit(app);

                          lst.setProgId(programId);

                          lst.setAyear(sa.getAcadYear());

                          lst.setAsession(sa.getAcadSession());

                          lst.setEventId(eventId);

                          lst.setEventDate(strtDateStr);

                          lst.setStartTime(xmlGregCal);

                          lst.setEndTime(xmlGregCal1);

                          if (sa.getFacultyId().contains("_")) {

                                sa.setFacultyId(sa.getFacultyId().substring(0,

                                            sa.getFacultyId().indexOf("_")));

                          }

                          //lst.setFacultyId(sa.getFacultyId());
                          
                          if(null != sa.getPresentFacultyId()) {
	      						lst.setFacultyId(sa.getPresentFacultyId());
	      						lst.setUpdtFacFlag("Y");
	      					}else {
	      						lst.setFacultyId(sa.getFacultyId());
	      						lst.setUpdtFacFlag("N");
	      					}

                          /*

                          * lst.setStObjid(""); lst.setStNumber("");

                          */

                          lst.setFlag(sa.getFlag());

                          lst.setLogDate(logDate);

                          lst.setLogTime(xmlGregCal2);

                          lstLT.getItem().add(lst);



                          logger.info("list------>" + lst);



                          idsForPresent = idsForPresent + sa.getId() + ",";

                    }

                    logger.info("ids:----> " + idsForPresent);

                    if (!idsForPresent.isEmpty() || !idsForPresent.equals("")) {

                          idsForPresent = idsForPresent.substring(0, idsForPresent.length() - 1);

                    }

                    logger.info("responseids:----> " + idsForPresent);

                    if (lstLT.getItem().size() > 0) {



                          logger.info("list TT------>" + lstLT);



                          try {



                                String response = ws.getZATTSTPORTALUPDATEBIN()

                                            .zattStPortalUpdate(lstLT);



                                logger.info("response:----> " + response);

                                if (response.equalsIgnoreCase("SUCCESS")) {

                                      studentCourseAttendanceDAO.updateSapOperation("S", idsForPresent);



                                } 
else {

                                      studentCourseAttendanceDAO.updateSapOperation("F", idsForPresent);

                                }
responseData = response;
return responseData;

                          } catch (Exception e) {



                               logger.error(
"Exception while calling a webservice manually", e);
responseData = e.getMessage()+"_ErrorOccurred400";



                          }

                    }



              }

        } catch (Exception e) {

              logger.error("Exception manually in SAP attend", e);
responseData = e.getMessage()+"_ErrorOccurred400";

        }
return responseData;

  }


	
	
    
    public void upsertAttendance(List<StudentCourseAttendance> studentCourseAttendance){
                    studentCourseAttendanceDAO.upsertAttendance(studentCourseAttendance);
    }

    public void sendEmailOfAttendance(String subject, String msg){
        Map<String, Map<String, String>> result = new HashMap<String, Map<String,String>>();
        Map<String, String> email = new HashMap<String, String>();
        email.put("Hiren","Hiren.Kanzariya.EXT@nmims.edu");
        result.put("emails", email);
        //String subject = " Attendance Record Sen ";
        String defaultMsg = "\\r\\n\\r\\n Attendance record: \\r\\n";
        String notificationEmailMessage = Jsoup.parse(defaultMsg + msg).text();
        
        //result = userService.findEmailByUserName(userList);
        Map<String, String> email1 = result.get("emails");

        Map<String, String> mobiles = new HashMap();
        logger.info("email -----> "+email);
        logger.info("mobile -----> "+mobiles);
        logger.info("subject -----> "+subject);
        logger.info("notificationEmailMessage -----> "+notificationEmailMessage);
        notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
  }
    
	/*
	 * public Integer getStudentCountByCourseIdStartEndTime(List<Long> courseId,
	 * String startTime, String endTime) { return
	 * studentCourseAttendanceDAO.getStudentCountByCourseIdStartEndTime(courseId,
	 * startTime,endTime); }
	 */
    
    public Integer getStudentCountByCourseIdStartEndTime(List<Long> courseId, String startTime, String endTime, String facultyId) 
    {
    	return studentCourseAttendanceDAO.getStudentCountByCourseIdStartEndTime(courseId,startTime,endTime,facultyId);
    }

    public List<StudentCourseAttendance> findByCourseIdAndDate(List<Long> courseId, String startTime, String facultyId) {	
    	return studentCourseAttendanceDAO.findByCourseIdAndDate(courseId,startTime,facultyId);
    }

    public String getMarkedAttendanceMaxEndTime(List<Long> courseId, String startDate, String facultyId){
    	return studentCourseAttendanceDAO.getMarkedAttendanceMaxEndTime(courseId,startDate,facultyId);
    }
	
    
    
    // 08-05-2020 shubham
    
    public String getPlayerIdForFaculty(String username)
    {
		return studentCourseAttendanceDAO.getPlayerIdForFaculty( username);
    }
	
	 public String getCourseNameFromCourseId(String id)
     {
		 return studentCourseAttendanceDAO.getCourseNameFromCourseId(id);
      }
	  
	  public int[] upsertBatchByApp(final List<StudentCourseAttendance> studentCourseAttdList) {
		return studentCourseAttendanceDAO.upsertBatchByApp(studentCourseAttdList);
	 }
	 
	  public Integer getStudentDataCountSentToSap(String courseId, String startTime, String endTime, String facultyId) 
    {
    	return studentCourseAttendanceDAO.getStudentDataCountSentToSap(courseId, startTime, endTime, facultyId) ;
    }
	
	 public int insertApp(StudentCourseAttendance bean)
	{
    	return studentCourseAttendanceDAO.insertApp(bean);
	}
	
	public int[] updateBatchByApp(final List<StudentCourseAttendance> studentCourseAttdList) {
		return studentCourseAttendanceDAO.updateBatchByApp(studentCourseAttdList);
	}
	//17-06-2020
	public void updateDelFlag(String value,String presentFacultyId, long id) {
		studentCourseAttendanceDAO.updateDelFlag(value, presentFacultyId, id);
	}

	public List<StudentCourseAttendance> findByCourseIdAndDateTimeAndPresentFacultyId(
			List<Long> courseId, String startTime, String endTime, String facultyId) {
		
		return studentCourseAttendanceDAO.findByCourseIdAndDateTimeAndPresentFacultyId(courseId,startTime,endTime,facultyId);
	}
	public List<StudentCourseAttendance> findByCourseIdAndDateTimeForAbsentFaculty(
			List<Long> courseId, String startTime, String endTime, String facultyId,List<String> facultyIds) {
		
		return studentCourseAttendanceDAO.findByCourseIdAndDateTimeForAbsentFaculty(courseId,startTime,endTime,facultyId,facultyIds);
	}
	
	
	
	
	
	public List<StudentCourseAttendance> getDataForAllFacultyId(List<Long> courseId,
			String startTime, String endTime, List<Long> facultyId) 
    {
    	return studentCourseAttendanceDAO.getDataForAllFacultyId(courseId, startTime, endTime, facultyId) ;
    }
	
	 public Integer getDataCountByAllFacultyId(String courseId, String startTime, String endTime, List<Long> facultyId) 
    {
    	return studentCourseAttendanceDAO.getDataCountByAllFacultyId(courseId, startTime, endTime, facultyId) ;
    }
    
    public Integer getDataCountByOneFacultyId(String courseId, String startTime, String endTime, String facultyId) 
    {
    	return studentCourseAttendanceDAO.getDataCountByOneFacultyId(courseId, startTime, endTime, facultyId) ;
    }
}

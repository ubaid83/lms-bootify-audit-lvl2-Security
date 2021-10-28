package com.spts.lms.web.controllers;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.attendance.StudentCourseAttendance;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.timetable.Timetable;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.variables.LmsVariables;
import com.spts.lms.daos.timetable.TimetableDAO;
import com.spts.lms.services.attendance.AttendanceService;
import com.spts.lms.services.attendance.StudentCourseAttendanceService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.variables.LmsVariablesService;
import com.spts.lms.utils.MultipleDBConnection;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.Utils;
import com.spts.lms.web.utils.ValidationException;

@Controller
public class StudentAttendanceController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	StudentCourseAttendanceService studentCourseAttendanceService;

	@Autowired
	UserService userService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	CourseService courseService;
	
	@Autowired
	AttendanceService attendanceService;
	
	@Autowired
	ProgramService programService;

	@Autowired
	TimetableDAO timetableDAO;
	
	@Autowired
	LmsVariablesService lmsVariablesService;

	@Autowired 
	BusinessBypassRule BusinessBypassRule ; 
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

	Client client = ClientBuilder.newClient();

	private static final Logger logger = Logger
			.getLogger(StudentAttendanceController.class);

	//vishal changes for view 23-13-2021
	
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value="/viewAttendance",method = {RequestMethod.GET,RequestMethod.POST})
	public String viewAttendance( RedirectAttributes redirectAttributes,@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) String attdDate,Model m, Principal principal) 
	{
		Token userdetails = (Token) principal;
		String username = principal.getName();
		StudentCourseAttendance attendance = new StudentCourseAttendance();
		long programId =Long.parseLong(userdetails.getProgramId());
		List<Course> courseList = courseService.findCoursesByUserAndProgramIdForApp(username, programId);
		System.out.println("courseList------"+courseList);
		m.addAttribute("allCourses", courseList);
		
		
		
		
	
		try {
			Map<Timetable,List<StudentCourseAttendance>> studentData =new HashMap();
			List<Timetable> ttFinal = new ArrayList<>();
			if(null!=courseId && !attdDate.isEmpty()) {
			
			Course coursedetails=courseService.findByID(courseId); 
			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

			DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

					.createDefaultConnectionByDS(defaultUrl, defaultUsername,

							defaultPassword);

			DriverManagerDataSource dataSourceTimetable = multipleDBConnection

					.createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername, defaultPassword,
							"timetable_metadata");

			timetableDAO.setDS(dataSourceTimetable);
			String cid = "";
			long eventId = 0;
			long pgrmId = 0;
			
			
				Date dt = new Date();
				attdDate = "%" + attdDate + "%";
			
			if (null != courseId) {
				cid =courseId.toString();
				eventId = Long.parseLong(cid.substring(0, 8));
				pgrmId = Long.parseLong(cid.substring(8));
			}
			
			List<Timetable> tt = new ArrayList<>();
			Token userdetails1 = (Token) principal;
				tt = timetableDAO.getTimetableByCourse(pgrmId, eventId, username, attdDate);
				timetableDAO.setDS(dataSourceDefaultLms);
			
			if (username.contains("_")) {
				username = username.substring(0, username.indexOf("_"));
			}
			String curDate = "";

			long pgrmId1;
			String cid1;
		
			if(null==tt || tt.isEmpty()) {
				setNote(m, "NO lectures found for selected Date");
			}
			for (Timetable tmtl : tt) {
				
				
				tmtl.setStart_time(tmtl.getStart_time().toString().replace(".", ":"));
				tmtl.setEnd_time(tmtl.getEnd_time().replace(".", ":"));
				
				
				
				//ttFinal.add(tmtl);

				DateTimeFormatter inSDF = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				DateTimeFormatter outSDF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
				
				
			String	starttime =outSDF.format(inSDF.parse(tmtl.getStart_time()));
				
			String	endTime=outSDF.format(inSDF.parse(tmtl.getEnd_time()));
				
			
				
				
				List<StudentCourseAttendance> studentsList = studentCourseAttendanceService.findByCourseIdAndDateTime(courseId.toString(), starttime,endTime, username);
				
				studentData.put(tmtl, studentsList);
			}
			attendance.setCourseId(courseId.toString());
			attdDate=attdDate.replaceAll("%", "");
			attendance.setAttdDate(attdDate);;
			
			
			
			}
			
			
			logger.info("ttFinal"+ttFinal);
			m.addAttribute("studentData", studentData);
		} catch (Exception e) {
			
			setError(m, "Error  Getting Data");
			logger.error("Exception", e);

		}
		m.addAttribute("attendance", attendance);
        return "attendance/viewAttendance";
        
	}
	
	
	@RequestMapping(value="/getstudentAttendancedata",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody public String getstudentAttendancedata (@RequestParam String courseId,@RequestParam String starttime, @RequestParam String endTime, Principal principal ) throws ParseException
	{
		logger.info("starttime"+courseId);
		logger.info("starttime"+starttime);
		logger.info("starttime"+endTime);
		
		
		
		
		
		DateTimeFormatter inSDF = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		DateTimeFormatter outSDF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		
		
		System.out.println(outSDF.format(inSDF.parse(starttime)));
		System.out.println(outSDF.format(inSDF.parse(endTime)));
		starttime =outSDF.format(inSDF.parse(starttime));
		
		endTime=outSDF.format(inSDF.parse(endTime));
		
	
		
		String username = principal.getName();
		List<StudentCourseAttendance> studentsList = studentCourseAttendanceService.findByCourseIdAndDateTime(courseId, starttime,endTime, username);
		
		
		
		
		String json = new Gson().toJson(studentsList);

		

	

		return json;
		
		
	}
	
	
	//hiren=17-06-2020
    @RequestMapping(value = "/markAttendanceForm", method = {
                RequestMethod.GET, RequestMethod.POST })
    public String markAttendanceForm(Model m,
                @RequestParam(required = false) Long courseId,
                @RequestParam(required = false) String lecture,
                @RequestParam(required = false) String isAbsentAll,
                @RequestParam(required = false) String isContinueLecture,
                @RequestParam(required = false) String facultyId,
                RedirectAttributes redirectAttributes, Principal principal) {

          String username = principal.getName();

          Token userdetails1 = (Token) principal;
          String ProgramName = userdetails1.getProgramName();
          User u = userService.findByUserName(username);

          String acadSession = u.getAcadSession();
          
          	/* Lms Variables */
			LmsVariables lv = new LmsVariables();
			lv = lmsVariablesService.getLmsVariableBykeyword("faculty_attendance");
			m.addAttribute("faculty_attendance", lv.getActive());
			
			LmsVariables timeLimitValue = lmsVariablesService.getLmsVariableBykeyword("attendance_timelimit");
			m.addAttribute("attendance_timelimit", Integer.parseInt(timeLimitValue.getValue()) * 60);
			String attendance_timelimit = timeLimitValue.getValue();
			
			LmsVariables maxEndTimeOfDay = lmsVariablesService.getLmsVariableBykeyword("maxEndTimeOfDay");
			m.addAttribute("maxEndTimeOfDayStr", maxEndTimeOfDay.getValue().toString());
			String maxEndTimeOfDayStr = maxEndTimeOfDay.getValue();

          m.addAttribute("Program_Name", ProgramName);
          m.addAttribute("AcadSession", acadSession);
          m.addAttribute("webPage", new WebPage("markAttendance",
                      "Mark Attendance", true, false));
          StudentCourseAttendance attendance = new StudentCourseAttendance();



          List<Timetable> tt1 = getFacultyLectures(username);
          
          List<Timetable> tt = new ArrayList<>();
          for (Timetable tmtl : tt1) {
  			int check=0;
  			if (tmtl.getProgramId().contains(" , ")) {
  				String[] programidsStrings = tmtl.getProgramId().split(
  						" , ");
  				for (int i = 0; i < programidsStrings.length; i++) {
  					Course c = courseService.findByID(Long.valueOf(tmtl.getEventId()+programidsStrings[i]));
  					if(null != c){
  						logger.info("getCourseName ------->" + c.getCourseName());
  					}else{check = 1;}
  				}
  				if(check == 0){
  					tt.add(tmtl);
  				}else{
                    for (int i = 0; i < programidsStrings.length; i++) {
                        String cid1 = "" + tmtl.getEventId() + programidsStrings[i];
                        Course c1 = courseService
                                                        .findByID(Long.parseLong(cid1));
                        long pgrmId1 = Long.parseLong(programidsStrings[i]);
                        Program p1 = programService.findByID(pgrmId1);

                        if (null != c1 && null != p1) {
                                        Timetable tmtlSub = new Timetable();

                                        tmtlSub.setClass_date(tmtl.getClass_date());
                                        tmtlSub.setStart_time(tmtl.getStart_time());
                                        tmtlSub.setEnd_time(tmtl.getEnd_time());
                                        tmtlSub.setEventId(tmtl.getEventId());
                                        tmtlSub.setFacultyId(tmtl.getFacultyId());
                                        tmtlSub.setFlag(tmtl.getFlag());

                                        // logger.info("inside for------>" +
                                        // programidsStrings[i]);
                                        cid1 = "" + tmtl.getEventId()
                                                                        + programidsStrings[i];

                                        // logger.info("inside for------>" + cid1);
                                        // Course c1 =
                                        // courseService.findByID(Long.parseLong(cid1));
                                        tmtlSub.setCourseId(cid1);
                                        tmtlSub.setCourseName(c1.getCourseName());
                                        tmtlSub.setProgramId(programidsStrings[i]);
                                        tmtlSub.setProgramName(p1.getProgramName());
                                        logger.info(p1.getProgramName());
                                        tt.add(tmtlSub);
                        }

        }
}

  			}else{
  				Course c = courseService.findByID(Long.valueOf(tmtl.getEventId()+tmtl.getProgramId()));
  				if(c != null){
  					logger.info("getCourseName1 ------->" + c.getCourseName());
  					tt.add(tmtl);
  				}
  			}
  		}
          if (tt.size() == 0 || tt.isEmpty()) {
                setError(m, "No Lectures Found!");
          } else {
                
                Map<String, String> lecutreEndTimeMap = new HashMap<String, String>();
                
                for (Timetable tmtl : tt) {
                      
                      tmtl.setClass_date(tmtl.getClass_date().split(" ")[0]);
                      tmtl.setStart_time(tmtl.getStart_time().split(" ")[1].replace(
                                  ".", ":"));
                      tmtl.setEnd_time(tmtl.getEnd_time().split(" ")[1].replace(".",
                                  ":"));
                      
                      logger.info("eventId ------->" + tmtl.getEventId());
                      logger.info("programId ------->" + tmtl.getProgramId());
                      List<String> courseIdList = new ArrayList<>();
                      if (tmtl.getProgramId().contains(" , ")) {
                            String[] programidsStrings = tmtl.getProgramId().split(
                                        " , ");
                            int check1=0;
                            for (int i = 0; i < programidsStrings.length; i++) {
                              Course c = courseService.findByID(Long.valueOf(tmtl.getEventId()+programidsStrings[i]));
                              if(null != c){
                                                logger.info("getCourseName ------->" + c.getCourseName());
                                                courseIdList.add(tmtl.getEventId()
                                            + programidsStrings[i]);
                              }else{check1 = 1;} 
                            }
                            if(check1 != 0){
                              for (int i = 0; i < programidsStrings.length; i++) {
                                    Course c = courseService.findByID(Long.valueOf(tmtl.getEventId()+programidsStrings[i]));
                                    if(null != c){
                                                      logger.info("getCourseName ------->" + c.getCourseName());
                                                      courseIdList.add(tmtl.getEventId()
                                                + programidsStrings[i]);
                                    }
                                }
                              }

                           /* for (int i = 0; i < programidsStrings.length; i++) {
                                  courseIdList.add(tmtl.getEventId()
                                              + programidsStrings[i]);
                            }*/
                            

                            tmtl.setMultipleCourseId(courseIdList);
                      } else {
                            courseIdList.add(tmtl.getEventId() + tmtl.getProgramId());
                            tmtl.setMultipleCourseId(courseIdList);
                      }
                      
                      //caculating remaining lectures
                      String usernameForWSDL = userdetails1.getName();
                      if(userdetails1.getName().contains("_")){
                          usernameForWSDL = userdetails1.getName().substring(0,username.indexOf("_"));
                         }
//                      String jsonResponse = attendanceService.
//                    		  pullFacultyWorkload(String.valueOf(tmtl.getEventId()),usernameForWSDL);
//                      Timetable workloadResponse = new Gson().fromJson(jsonResponse,Timetable.class);
//                      if(null != workloadResponse){
//                    	  tmtl.setAllottedLecture(workloadResponse.getAllottedLecture());
//                          tmtl.setConductedLecture(workloadResponse.getConductedLecture());
//                          logger.info("allotated lectures-->"+tmtl.getAllottedLecture());
//                          logger.info("conducted lectures-->"+tmtl.getConductedLecture());
//                          /*String remainingLecture ="";
//                          if(Double.parseDouble(tmtl.getAllottedLecture()) > Double.parseDouble(tmtl.getConductedLecture())){
//                        	  remainingLecture = String.valueOf(Double.parseDouble(tmtl.getAllottedLecture()) - Double.parseDouble(tmtl.getConductedLecture()));
//                          }*/
//                          
//                          //m.addAttribute("remainingLecture",remainingLecture);
//                      }
                      
                      tmtl.setMaxEndTimeForCourse(tmtl.getEnd_time());
                      lecutreEndTimeMap.put(courseIdList.toString(),
                                  tmtl.getEnd_time());
                      
                      m.addAttribute("courseId", courseIdList);
                      
                }
                
                logger.info("lecutreEndTimeMap---->" + lecutreEndTimeMap);
                
                for (String key : lecutreEndTimeMap.keySet()) {
                      logger.info("KEY---->" + key + "\n");
                      
                      List<Timetable> courseTimetable = new ArrayList<>();
                      
                      int i = 0;
                      while (i < tt.size()) {
                            
                            if (key.equals(tt.get(i).getMultipleCourseId().toString())) {
                                  courseTimetable.add(tt.get(i));
                            }
                            i++;
                      }
                      i = 0;
                      /*for (Timetable ct : courseTimetable) {
                            if(i<courseTimetable.size()-1){
                            String lecEndTime = ct.getClass_date() + " "
                                        + courseTimetable.get(i).getEnd_time();
                            String nxtLecStartTime = ct.getClass_date() + " "
                                        + courseTimetable.get(i + 1).getStart_time();
                            logger.info("lecEndTime >" + lecEndTime + " nextLecStartTime >" + nxtLecStartTime);
                            long diff = timeDifference(lecEndTime, nxtLecStartTime);
                            if(diff < 60000){
                                  courseTimetable.get(i).setMaxEndTimeForCourse(lecutreEndTimeMap.get(key));
                            }else {
                                  for(int j=i;j>0;j--){
                                        String lecStartTime = ct.getClass_date() + " "
                                                    + courseTimetable.get(j).getStart_time();
                                        String prevLecEndTime = ct.getClass_date() + " "
                                                    + courseTimetable.get(j-1).getEnd_time();
                                        logger.info("lecStartTime >" + lecStartTime + " prevLecEndTime >" + prevLecEndTime);
                                        long diff1 = timeDifference(prevLecEndTime,lecStartTime);
                                        logger.info("diff1--------->"+diff1);
                                        if(diff1<60000){
                                              courseTimetable.get(j-1).setMaxEndTimeForCourse(courseTimetable.get(i).getEnd_time());
                                        }
                                        
                                  }
                                  
                           courseTimetable.get(i).setMaxEndTimeForCourse(courseTimetable.get(i).getEnd_time());
                            }
                            

                            }
                            i++;
                      }*/
                      for (Timetable ct : courseTimetable) {
                    		if(i<courseTimetable.size()-1){
                    			String lecEndTime = ct.getClass_date() + " " + courseTimetable.get(i).getEnd_time();
                    			String nxtLecStartTime = ct.getClass_date() + " " + courseTimetable.get(i + 1).getStart_time();
                    			logger.info("lecEndTime >" + lecEndTime + " nextLecStartTime >" + nxtLecStartTime);
                    			long diff = timeDifference(lecEndTime, nxtLecStartTime);
                    			if(diff < 60000){
                    				courseTimetable.get(i).setMaxEndTimeForCourse(lecutreEndTimeMap.get(key));
                    				courseTimetable.get(i).setIsContinueLecture("Y");
                    				logger.info("setIsContinueLecture--1->Y");
                    			}else {
                    				for(int j=i;j>0;j--){
                    				String lecStartTime = ct.getClass_date() + " " + courseTimetable.get(j).getStart_time();
                    				String prevLecEndTime = ct.getClass_date() + " " + courseTimetable.get(j-1).getEnd_time();
                    				logger.info("lecStartTime1 >" + lecStartTime + " prevLecEndTime1 >" + prevLecEndTime);
                    				long diff1 = timeDifference(prevLecEndTime,lecStartTime);
                    				logger.info("diff1--------->"+diff1);
                    				if(diff1<60000){
                    					courseTimetable.get(j-1).setMaxEndTimeForCourse(courseTimetable.get(i).getEnd_time());
                    					courseTimetable.get(j-1).setIsContinueLecture("Y");
                    					logger.info("setIsContinueLecture--2->Y");
                    				}
                    			}
                    			courseTimetable.get(i).setMaxEndTimeForCourse(courseTimetable.get(i).getEnd_time());
                    			courseTimetable.get(i).setIsContinueLecture("Y");
                    			logger.info("setIsContinueLecture--3->Y");
                    		}
                    		}else if(i==0){
                    			courseTimetable.get(i).setIsContinueLecture("N");
                    			logger.info("setIsContinueLecture--->N");
                    		}else{
                    			String lecStartTime = ct.getClass_date() + " " + courseTimetable.get(i).getStart_time();
                    			String prevLecEndTime = ct.getClass_date() + " " + courseTimetable.get(i-1).getEnd_time();
                    			long diff1 = timeDifference(prevLecEndTime,lecStartTime);
                    			logger.info("lecStartTime1 >" + lecStartTime + " prevLecEndTime1 >" + prevLecEndTime);
                    			logger.info("diff1--------->"+diff1);
                    			if(diff1<60000){
                    				courseTimetable.get(i).setIsContinueLecture("Y");
                    				logger.info("setIsContinueLecture--4->Y");
                    			}else{
                    				courseTimetable.get(i).setIsContinueLecture("N");
                    			}
                    		}
                    	i++;
                    	}
                      
                }

          
                m.addAttribute("flags", tt);

          }
          List<String> facultyIdList = new ArrayList<String>();
          if(null != facultyId) {
  		  	facultyId =  facultyId.replaceAll(" ","");
      		facultyIdList = Arrays.asList(facultyId.split(","));
  	  }
        //12-12-2020
          Map<String,String> facultyNameAndId = new HashMap<String, String>();
          for(String facultyUsername: facultyIdList) {
          User uname = userService.findByUserName(facultyUsername);
          facultyNameAndId.put(uname.getUsername(),uname.getFirstname()+" "+uname.getLastname());
          }
          m.addAttribute("facultyNameAndId",facultyNameAndId);
          if (lecture == null) {

          } else if (lecture != null || !lecture.isEmpty()) {
        	  	
	        	  if(null == isAbsentAll){
	      	  		attendance.setIsAbsentAll("N");	
	      	  	  }else{
	      	  		attendance.setIsAbsentAll(isAbsentAll);
	      	  	}
        	  
                logger.info("elseCalled--->");
                logger.info("lecture---------->" + lecture);
                logger.info("lecturedate---------->"
                            + lecture.split(" To ")[1].split(",")[1]);
                logger.info("maxEndTimeForCourse-------->"
                            + lecture.split(" To ")[1].split(",")[2]);
                String maxEndTimeForCourse = lecture.split(" To ")[1].split(",")[2];
                logger.info("courseId---------->"
                            + lecture.substring(lecture.indexOf("[") + 1,
                                        lecture.indexOf("]")));
                String tmpCourseId = lecture.substring(lecture.indexOf("[") + 1,
                            lecture.indexOf("]"));
                String[] courseidsStrings = tmpCourseId.split(", ");

                List<Long> courseIdList = new ArrayList<>();
                for (int i = 0; i < courseidsStrings.length; i++) {
                      courseIdList.add(Long.valueOf(courseidsStrings[i]));
                }
                logger.info("courseIdList---------->" + courseIdList);
                String courseName = "";
                String eventIdForWorkload="";
                for(Long cids : courseIdList){
                      logger.info("courseIdList1---------->" + cids);
                      
                      Course c = courseService.findByID(cids);
                      if(null != c){
                            logger.info("courseIdList1---------->" + c.getCourseName());
                            if(!courseName.contains(c.getCourseName())){
                                  if(!courseName.equals("")){
                                        courseName = courseName +", "+ c.getCourseName();
                                  }else{
                                        courseName = c.getCourseName();
                                  }
                            }else{
                                  courseName = c.getCourseName();
                            }
                            eventIdForWorkload = cids.toString().substring(0,8);
                      }
                      
                      
                      
                }
              //caculating remaining lectures
                String usernameForWSDL = userdetails1.getName();
                if(userdetails1.getName().contains("_")){
                    usernameForWSDL = userdetails1.getName().substring(0,username.indexOf("_"));
                   }
                
//                String jsonResponse = attendanceService.
//              		  pullFacultyWorkload(eventIdForWorkload,usernameForWSDL);
//                Timetable workloadResponse = new Gson().fromJson(jsonResponse,Timetable.class);
//                if(null != workloadResponse){
//                attendance.setAllottedLecture(workloadResponse.getAllottedLecture());
//                attendance.setConductedLecture(workloadResponse.getConductedLecture());
//                String remainingLecture = String.valueOf(Double.parseDouble(attendance.getAllottedLecture()) - Double.parseDouble(attendance.getConductedLecture()));
//                //m.addAttribute("remainingLecture", ((Long.valueOf(attendance.getAllottedLecture().toString())) - (Long.valueOf(attendance.getConductedLecture().toString()))));
//                logger.info("remainingLecture"+remainingLecture);
//                m.addAttribute("remainingLecture",remainingLecture);
//                
//                logger.info("allotated lectures-->"+attendance.getAllottedLecture());
//                logger.info("conducted lectures-->"+attendance.getConductedLecture());
//                }
                m.addAttribute("courseName", courseName);
                attendance.setLecture(lecture);
                SimpleDateFormat format = new SimpleDateFormat(
                            "dd-MM-yyyy HH:mm:ss");
                Date dt1;
                Date dt2;
                Date maxEndDateTime;
                Date maxEndDate = Utils.getInIST();

                try {
                      dt1 = format.parse(lecture.split(" To ")[0]);
                      dt2 = format.parse(lecture.split(" To ")[1].split(",")[0]);

                      SimpleDateFormat formatter = new SimpleDateFormat(
                                  "yyyy-MM-dd HH:mm:ss");
                      
                      SimpleDateFormat formatter1 = new SimpleDateFormat(
                              "yyyy-MM-dd");
                      String maxEnd = formatter1.format(maxEndDate);
                      maxEndDateTime = formatter.parse(maxEnd+" "+maxEndTimeForCourse);
                      
                      List<StudentCourseAttendance> studentsList ,studentsDateList=null;
                      /*if(isContinueLecture.equals("Y")){
                        String startTime = formatter1.format(dt1);
                        studentsDateList  = studentCourseAttendanceService.findByCourseIdAndDate(courseIdList, startTime, username);
                      }*/
                      logger.info("continue---->"+isContinueLecture);
                      if(isContinueLecture.equals("Y")){
                    	  String startTime = formatter.format(dt1);
                    	  String startDate = formatter1.format(dt1);
                          String endTime = formatter1.format(dt2);
                          String lastMarkEndTime = studentCourseAttendanceService.getMarkedAttendanceMaxEndTime(courseIdList, startDate, username);
                          long diff = 0;
                          if(null != lastMarkEndTime){
                        	  diff = timeDifference(lastMarkEndTime, startTime);
                          }
                          logger.info("diff prev---->"+diff);
                          if(diff < 60000){
                        	  studentsDateList  = studentCourseAttendanceService.findByCourseIdAndDate(courseIdList, startDate, username);
                        	  logger.info("studentsDateList--->"+studentsDateList.size());
                          }
                      }
                      String startTime = formatter.format(dt1);
                      String endTime = formatter.format(dt2);
                      logger.info("startTime--->" + startTime);
                      logger.info("endTime--->" + endTime);
                      studentsList = studentCourseAttendanceService
                                  .findByCourseIdAndDateTime(courseIdList, startTime,
                                              endTime, username);

                      Date checkDt = Utils.getInIST();
                      String checkDate = formatter.format(checkDt);
                      logger.info("checkDate--->" + checkDate);
                      //Date newDate = DateUtils.addHours(maxEndDateTime, 2);
                      Date newDate = DateUtils.addHours(maxEndDateTime, Integer.parseInt(attendance_timelimit));
                      logger.info("newDate--->" + newDate);
                      String newEndTime = formatter.format(newDate);
                      logger.info("newEndTime--->" + newEndTime);
                      
                      //max end 11pm
                      String currDate = checkDate.split(" ")[0];
                      Date allowToMarkDate = formatter.parse(currDate+" "+maxEndTimeOfDayStr);
                      String allowToMark = formatter.format(allowToMarkDate);
                      int allowToMark_Att = checkDate.compareToIgnoreCase(allowToMark);
                      if(allowToMark_Att > 0) {
                        newEndTime = allowToMark;
                      }
                      //max end 11pm
                      
                      int result = checkDate.compareToIgnoreCase(newEndTime);
                      logger.info("result--->" + result);
                      /*if (studentsList.size() == 0) {

                            List<Course> students = courseService
                                        .findStudentsByMultipleCourseId(courseIdList);
                            
                            m.addAttribute("students", students);
                            m.addAttribute("notMarked", true);
                      }*/
                      //logger.info("studentsDateList--->"+studentsDateList.size());
                      logger.info("studentsList--->"+studentsList.size());
//                      if (studentsList.size() == 0) {
//                    	  if(null == studentsDateList){
//                    		  List<Course> students = courseService
//                  					.findStudentsByMultipleCourseId(courseIdList);
//                  			 m.addAttribute("students", students);
//                  			 m.addAttribute("notMarked", true);
//                     	  }else if(isContinueLecture.equals("Y") && (studentsDateList.size() != 0 ) ){
//                			
//                			 m.addAttribute("students", studentsDateList);
//                			 m.addAttribute("notMarked", "continue");
//                		}else{
//                			List<Course> students = courseService
//                					.findStudentsByMultipleCourseId(courseIdList);
//                			 m.addAttribute("students", students);
//                			 m.addAttribute("notMarked", true);
//                		}
//                	}else if (result > 0) {
//                            logger.info("result--->" + result);
//                            m.addAttribute("notMarked", "showList");
//                            m.addAttribute("students", studentsList);
//                      } else {
//                            logger.info("elseIn--->");	
//                            m.addAttribute("notMarked", "update");
//                            m.addAttribute("students", studentsList);
//                      }
                      
                      if(lv.getActive().equals("Y")) {
                    		 List<StudentCourseAttendance> studentsListByPresentFaculty = studentCourseAttendanceService.findByCourseIdAndDateTimeAndPresentFacultyId(courseIdList, startTime,endTime, username);
                    		 List<StudentCourseAttendance> absentFacultyStudentList = studentCourseAttendanceService.findByCourseIdAndDateTimeForAbsentFaculty(courseIdList, startTime,endTime, username,facultyIdList);
                    			 if (studentsList.size() == 0) {
                    					if(null == studentsDateList){
                    						 if(studentsListByPresentFaculty.size() > 0) {
                    							 attendance.setPresentFacultyId(studentsListByPresentFaculty.get(0).getPresentFacultyId());
                    							 m.addAttribute("notMarked", "showList");
                    							 m.addAttribute("students", studentsListByPresentFaculty);
                    						 }else if (absentFacultyStudentList.size() > 0) {
                    							 attendance.setPresentFacultyId(absentFacultyStudentList.get(0).getPresentFacultyId());
                    							 m.addAttribute("notMarked", "showList");
                    							 m.addAttribute("students", absentFacultyStudentList);
                    						 }else {
                    							 List<Course> students = courseService.findStudentsByMultipleCourseId(courseIdList);
                    							 m.addAttribute("students", students);
                    							 m.addAttribute("notMarked", true);
                    						 }
                    					}else if(isContinueLecture.equals("Y") && (studentsDateList.size() != 0 ) ){
                    					
                    						 m.addAttribute("students", studentsDateList);
                    						 m.addAttribute("notMarked", "continue");
                    					}else{
                    						 List<Course> students = courseService.findStudentsByMultipleCourseId(courseIdList);
                    						 m.addAttribute("students", students);
                    						 m.addAttribute("notMarked", true);
                    						 if(studentsListByPresentFaculty.size() > 0) {
                    							 attendance.setPresentFacultyId(studentsListByPresentFaculty.get(0).getPresentFacultyId());
                    							 m.addAttribute("notMarked", "showList");
                    							 m.addAttribute("students", studentsListByPresentFaculty);
                    						 
                    						 }
                    					}
                    				  
                    				//m.addAttribute("students", students);
                    				//m.addAttribute("notMarked", true);
                    			  }else if (result > 0) {
                    					attendance.setPresentFacultyId(studentsList.get(0).getPresentFacultyId());
                    					logger.info("result--->" + result);
                    					m.addAttribute("notMarked", "showList");
                    					m.addAttribute("students", studentsList);
                    			  } else {
                    					attendance.setPresentFacultyId(studentsList.get(0).getPresentFacultyId());
                    					logger.info("elseIn--->");
                    					if(studentsList.get(0).getFacultyId().equals(username)) {
                    						m.addAttribute("notMarked", "update");
                    						m.addAttribute("students", studentsList);
                    					}else {
                    						m.addAttribute("notMarked", "showList");
                    						m.addAttribute("students", studentsList);
                    					}
                    			  }
                    	 }else {
                    		  if (studentsList.size() == 0) {
                    				if(null == studentsDateList){
                    					  List<Course> students = courseService
                    							.findStudentsByMultipleCourseId(courseIdList);
                    					 m.addAttribute("students", students);
                    				 m.addAttribute("notMarked", true);
                    				}else if(isContinueLecture.equals("Y") && (studentsDateList.size() != 0 ) ){
                    				
                    				 m.addAttribute("students", studentsDateList);
                    				 m.addAttribute("notMarked", "continue");
                    				}else{
                    					List<Course> students = courseService
                    							.findStudentsByMultipleCourseId(courseIdList);
                    					 m.addAttribute("students", students);
                    				 m.addAttribute("notMarked", true);
                    				}
                    			  
                    			//m.addAttribute("students", students);
                    			//m.addAttribute("notMarked", true);
                    		  }else if (result > 0) {
                    				logger.info("result--->" + result);
                    				m.addAttribute("notMarked", "showList");
                    				m.addAttribute("students", studentsList);
                    		  } else {
                    				logger.info("elseIn--->");
                    				m.addAttribute("notMarked", "update");
                    				m.addAttribute("students", studentsList);
                    		  }
                    	 }
                } catch (ParseException e) {
                      logger.error("Exception", e);
                }
                

          } else {

          }

          m.addAttribute("facultyIdList",facultyIdList);
          if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
                m.addAttribute("allCourses",
                            courseService.findByCoursesBasedOnProgramName(ProgramName));
          } else {
                if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)
                            || userdetails1.getAuthorities()
                                        .contains(Role.ROLE_STUDENT)) {
                      m.addAttribute("allCourses", courseService.findByUserActive(
                                  principal.getName(), userdetails1.getProgramName()));
                }

          }
          List<String> statusList = new ArrayList<String>();
          statusList.add("Present");
          statusList.add("Absent");
          attendance.setStatusList(statusList);
          m.addAttribute("attendance", attendance);
          m.addAttribute("username", username);
          return "attendance/markAttendance";

    }

	
public long timeDifference(String time1,String time2){
		
		SimpleDateFormat format = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		long diff = 0;
		try {
			d1 = format.parse(time1);
			d2 = format.parse(time2);
			diff = d2.getTime() - d1.getTime();
			logger.info("diff----->" + String.valueOf(diff));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diff;
	}



	//hiren=17-06-2020
@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
@RequestMapping(value = "/saveStudentCourseAttendance", method = {
		RequestMethod.GET, RequestMethod.POST })
public String saveStudentCourseAttendance(
		@ModelAttribute StudentCourseAttendance attendance, Model m,
		RedirectAttributes redirectAttributes, Principal principal) {
	m.addAttribute("webPage", new WebPage("markAttendance",
			"Mark Attendance", true, false));
	String username = principal.getName();

	Token userdetails1 = (Token) principal;
	String ProgramName = userdetails1.getProgramName();
	User u = userService.findByUserName(username);

	String acadSession = u.getAcadSession();

	m.addAttribute("Program_Name", ProgramName);
	m.addAttribute("AcadSession", acadSession);

	
	ArrayList<StudentCourseAttendance> studentAttendanceMappingList = new ArrayList<StudentCourseAttendance>();
	List<String> studentList = new ArrayList<String>();

	
	Map<String,String> courseAcadYearMap = new HashMap<String, String>();

	if ((attendance.getCourseId() != null || !attendance.getCourseId()
			.equals(""))
			&& (!attendance.getLecture().equals("") || attendance
					.getLecture() != null)) {
		
		String lectureDetails = attendance.getLecture();
		logger.info("lectureDetails--->" + lectureDetails);
		logger.info("CourseId--->" + attendance.getCourseId());
		String tmpCourseId = lectureDetails.substring(
				lectureDetails.indexOf("[") + 1,
				lectureDetails.indexOf("]"));
		String[] courseidsStrings = tmpCourseId.split(", ");
		try {
		List<Long> courseIdList = new ArrayList<>();
		for (int i = 0; i < courseidsStrings.length; i++) {
			courseIdList.add(Long.valueOf(courseidsStrings[i]));
			Course courseData=courseService.findByID(Long.valueOf(courseidsStrings[i]));
			
			 if(null ==courseData)
			 {
					throw new ValidationException("Invalid Course selected.");

			 }
			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

		       DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

		       .createDefaultConnectionByDS(defaultUrl, defaultUsername,

		       defaultPassword);

//		       DriverManagerDataSource dataSourceTimetable = multipleDBConnection
//
//		       .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
//		                   defaultPassword, "sap_master_inc");

		   //    timetableDAO.setDS(dataSourceTimetable);
		       
				//	String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(courseidsStrings[i].substring(0, 8), courseidsStrings[i].substring(8));
					//String sapAcadYear = timetableDAO.getAcadYearFromSapMaster("51709397", "50474742");
				//	courseAcadYearMap.put(courseidsStrings[i], sapAcadYear);
			
				//timetableDAO.setDS(dataSourceDefaultLms);
		}
		List<Course> students = courseService
				.findStudentsByMultipleCourseId(courseIdList);
		List<Course> studentCount = courseService
				.findStudentCountCourseWise(courseIdList);

		
		List<String> statusList = new ArrayList<String>(
				Arrays.asList(attendance.getStatus().split(",")));
		
		String flag = attendance.getLecture().split(" To ")[1].split(",")[1];
		SimpleDateFormat format = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss");
		String acadYear = "";
		String studAcadSession = "";
		//
			Date dt = format
					.parse(attendance.getLecture().split(" To ")[0]);
			Date dt1 = format
					.parse(attendance.getLecture().split(" To ")[1]
							.split(",")[0]);
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			
			Date d1 = null;
			Date d3 = null;
			Date d2 = Utils.getInIST();
			String date2 = formatter.format(d2);
			date2 = date2.split(" ")[0].concat(" 00:00:00");
			
				
				d1 = formatter.parse(attendance.getLecture().split(" To ")[0]);
				d3 = formatter.parse(attendance.getLecture().split(" To ")[1].split(",")[0]);
				
				
				if(d1.before(d2)) {
//					System.out.println("False - date before currentDate");
					throw new ValidationException("Invalid date selected.");
				}
				if(d1.after(d2)) {
//					System.out.println("False - date before currentDate");
					throw new ValidationException("Invalid date selected.");
				}
				
				if(d3.before(d2)) {
//					System.out.println("False - date before currentDate");
					throw new ValidationException("Invalid date selected.");
				}
				if(d3.after(d2)) {
//					System.out.println("False - date before currentDate");
					throw new ValidationException("Invalid date selected.");
				}
			
				
			
			
			/*Map<String, List<String>> presentStudentMap = new HashMap<String, List<String>>();*/
			List<String> absentCourseIds = new ArrayList<>();
			Map<String,String> suc = new HashMap<String,String>();
            for(int j=0;j<statusList.size();j++){
                  String[] statusArray = null;
                  
                  statusArray = statusList.get(j).split("_");
                  suc.put(statusArray[1], statusArray[0]+"_"+statusArray[2]);
            }

			if (students != null && students.size() > 0) {
				int i = 0;
				for (Course sca : students) {
					StudentCourseAttendance bean = new StudentCourseAttendance();
					User u1 = userService.findByUserName(sca.getUsername());
					//acadYear = u1.getEnrollmentYear();
					studAcadSession = u1.getAcadSession();
					
					bean.setFacultyId(username);
					bean.setPresentFacultyId(attendance.getPresentFacultyId());
					bean.setCreatedBy(username);
					bean.setLastModifiedBy(username);
					bean.setAcadYear(u1.getEnrollmentYear());
					bean.setAcadSession(u1.getAcadSession());

					bean.setStartTime(formatter.format(dt));
					bean.setEndTime(formatter.format(dt1));
					bean.setRollNo(sca.getRollNo());
					logger.info("i--->" + i);
					bean.setUsername(sca.getUsername());
                    
                    String statusCourse = "";
                    if(suc.containsKey(sca.getUsername())){
                          statusCourse = suc.get(sca.getUsername());
                    }
                    //suc.get(sca.getUsername());
                    
                    String[] statusArray = null;
                    if(statusCourse.contains("_")){
						statusArray = statusCourse.split("_");
						bean.setStatus(statusArray[0]);
						bean.setCourseId(statusArray[1]);
						Course c = courseService.findByID(Long.valueOf(statusArray[1]));
						
						//bean.setAcadYear(c.getAcadYear());
						//acadYear = c.getAcadYear();
						bean.setAcadSession(c.getAcadSession());
						studAcadSession = c.getAcadSession();
						acadYear = courseAcadYearMap.get(bean.getCourseId());
						bean.setAcadYear(acadYear);
					}

					
					/*String[] statusArray = null;
					statusArray = statusList.get(i).split("_");
					bean.setStatus(statusArray[0]);
					bean.setUsername(statusArray[1]);
					bean.setCourseId(statusArray[2]);*/
					
					logger.info("flag---->"
							+ attendance.getLecture().split(" To ")[1]
									.split(",")[1]);
					bean.setFlag(flag);
					
					bean.setActive("Y");
					
					i++;
					studentAttendanceMappingList.add(bean);

				}

				studentCourseAttendanceService.upsertBatch(studentAttendanceMappingList);

				int count = 0;

				for (String status : statusList) {

					String[] statusArray = null;
					statusArray = status.split("_");
					if (statusArray[0].equalsIgnoreCase("Absent")) {
						count++;
						if (!absentCourseIds.contains(statusArray[2])) {
							absentCourseIds.add(statusArray[2]);

						}
					}
				}

				/*logger.info("presentStudentMap--->"
						+ presentStudentMap.get("5146163850713770"));
				logger.info("presentStudentMap1--->"
						+ presentStudentMap.get("5146163850713378"));*/
				logger.info("studentCountList--->" + studentCount);
				for (Course sc : studentCount) {
					logger.info("courseId: " + sc.getId() + " Count:"
							+ sc.getCount());
				}
				logger.info("studentCount----->" + students.size());
				logger.info("statusCount----->" + statusList.size());
				
				logger.info("count--->" + count);
				logger.info("absentCourseIds--->" + absentCourseIds);
				if (count == 0) {
					
					logger.info("All present--->");
					for (Long cid : courseIdList) {
						StudentCourseAttendance bean = new StudentCourseAttendance();
						bean.setCourseId(String.valueOf(cid));
						
						bean.setFacultyId(username);
						bean.setPresentFacultyId(attendance.getPresentFacultyId());
						bean.setCreatedBy(username);
						bean.setLastModifiedBy(username);
						bean.setAcadYear(acadYear);
						bean.setAcadSession(studAcadSession);

						bean.setStartTime(formatter.format(dt));
						bean.setEndTime(formatter.format(dt1));
						
						bean.setStatus("Present");
						logger.info("flag---->"
								+ attendance.getLecture().split(" To ")[1]
										.split(",")[1]);
						bean.setFlag(flag);
						bean.setActive("Y");
						bean.setDelFlag("N");

						//studentCourseAttendanceService.insert(bean);
						StudentCourseAttendance s = studentCourseAttendanceService
                                .getAllPresentRecord(bean.getCourseId(),
                                            formatter.format(dt), formatter.format(dt1),
                                            username);
                    logger.info("checkExist--->"+s);
                    if(null == s){
                          studentCourseAttendanceService.insert(bean);
                    }


					}

				} else if (count > 0) {
					for (Long cid : courseIdList) {
						if (!absentCourseIds.contains(String.valueOf(cid))) {
							logger.info("cid--->" + cid + "absentId"
									+ absentCourseIds);
							StudentCourseAttendance bean = new StudentCourseAttendance();
							bean.setCourseId(String.valueOf(cid));
						
							bean.setFacultyId(username);
							bean.setPresentFacultyId(attendance.getPresentFacultyId());
							bean.setCreatedBy(username);
							bean.setLastModifiedBy(username);
							bean.setAcadYear(acadYear);
							bean.setAcadSession(studAcadSession);

							bean.setStartTime(formatter.format(dt));
							bean.setEndTime(formatter.format(dt1));
							

							bean.setStatus("Present");
							logger.info("flag---->"
									+ attendance.getLecture().split(" To ")[1]
											.split(",")[1]);
							bean.setFlag(flag);
							bean.setActive("Y");
							bean.setDelFlag("N");

							//studentCourseAttendanceService.insert(bean);
							StudentCourseAttendance s = studentCourseAttendanceService
                                    .getAllPresentRecord(bean.getCourseId(),
                                                formatter.format(dt), formatter.format(dt1),
                                                username);
                        logger.info("checkExist--->"+s);
                        if(null == s){
                              studentCourseAttendanceService.insert(bean);
                        }

						}

					}
				}
			
				setSuccess(redirectAttributes, "Marked Attendance for "
						+ students.size() + " students successfully");

			}

		} catch (ValidationException e) {
			logger.error(e.getMessage(), e);
			
			setError(redirectAttributes, e.getMessage());

		} 
		
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttributes, "Error in marking attendance");

		}
		m.addAttribute("attendance", attendance);
		return "redirect:/markAttendanceForm";
	} else {
		return "redirect:/markAttendanceForm";
	}
}

	@RequestMapping(value = "/showAttendance", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String showAttendance(Model m,
			RedirectAttributes redirectAttributes, Principal principal) {

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("markAttendance",
				"Show Attendance", true, false));

		m.addAttribute("attendance", new StudentCourseAttendance());
		return "attendance/showAttendance";
	}

	@RequestMapping(value = "/searchForAttendance", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchForAttendance(
			@ModelAttribute StudentCourseAttendance attendance, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("markAttendance",
				"Show Attendance", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		logger.info("startTime----->" + attendance.getStartTime());
		logger.info("endTime----->" + attendance.getEndTime());
		try {
			SimpleDateFormat dateFormatApp = new SimpleDateFormat("yyyy-MM-dd");
			Date sd = dateFormatApp.parse(attendance.getStartTime());
			Date ed = dateFormatApp.parse(attendance.getEndTime());
			logger.info("sDate---------> " + sd);
			logger.info("eDate---------> " + ed);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = dateFormat.format(sd) + " " + "00:00:00";
			logger.info(startDate);
			String endDate = dateFormat.format(ed) + " " + "23:59:59";
			logger.info(endDate);
			List<StudentCourseAttendance> AttendanceStatisticsList = studentCourseAttendanceService
					.getAttendanceStatByUsernameAndCourseId(username,
							startDate, endDate);
			logger.info("AttendanceStatisticsList--->"
					+ AttendanceStatisticsList);
			m.addAttribute("attendanceList", AttendanceStatisticsList);
			m.addAttribute("attendance", attendance);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "attendance/showAttendance";
	}

	
	//hiren=15-10-2019
	@RequestMapping(value = "/checkLectureTime", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String checkLectureTime(@RequestParam String lecture,
			@RequestParam String courseId,@RequestParam String facultyId, Model m, Principal p) {

		logger.info("lecture---->" + lecture);
		logger.info("courseId---->" + courseId);
		logger.info("usernameP---->" + p.getName());
		String username = p.getName();
		Gson g = new Gson();
		StudentCourseAttendance sca = new StudentCourseAttendance();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		try {
			Date dt = format.parse(lecture.split(" To ")[0]);
			Date dt1 = format.parse(lecture.split(" To ")[1].split(",")[0]);
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			String startTime = formatter.format(dt);
			String endTime = formatter.format(dt1);

			logger.info("checkstartTime--->" + startTime);
			logger.info("checkendTime--->" + endTime);

			
			String tmpCourseId = lecture.substring(
					lecture.indexOf("[") + 1,
					lecture.indexOf("]"));
			String[] courseidsStrings = tmpCourseId.split(", ");

			List<Long> courseIdList = new ArrayList<>();
			for (int i = 0; i < courseidsStrings.length; i++) {
				courseIdList.add(Long.valueOf(courseidsStrings[i]));
			}
			List<StudentCourseAttendance> studentsList = studentCourseAttendanceService.findByCourseIdAndDateTime(courseIdList, startTime,endTime, username);
//
//			if (studentsList.size() == 0) {
//				sca.setMsg("Lecture time ended!");
//			} else {
//				sca.setMsg("show marked students");
//			}
			List<String> facultyIdList = new ArrayList<String>();
			facultyId = facultyId.replaceAll(" ", "");
			facultyIdList = Arrays.asList(facultyId.split(","));
			
			List<StudentCourseAttendance> studentsListByPresentFaculty = studentCourseAttendanceService.findByCourseIdAndDateTimeAndPresentFacultyId(courseIdList, startTime,endTime, username);
			List<StudentCourseAttendance> absentFacultyStudentList = studentCourseAttendanceService.findByCourseIdAndDateTimeForAbsentFaculty(courseIdList, startTime,endTime, username,facultyIdList);
			if (studentsList.size() == 0) {
				if (studentsListByPresentFaculty.size() >0 ) {
					sca.setMsg("show marked students");
				}else if (absentFacultyStudentList.size() > 0) {
					sca.setMsg("show marked students");
				}else {
					sca.setMsg("Lecture time ended!");
				}
			} else {
				sca.setMsg("show marked students");
			}
		} catch (Exception e) {
			logger.error("Exception:", e);
		}

		return g.toJson(sca);
	}

	
	//hiren=17-06-2020
	@RequestMapping(value = "/updStudentCourseAttendance", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String updStudentCourseAttendance(
			@ModelAttribute StudentCourseAttendance attendance, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("markAttendance",
				"Mark Attendance", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		ArrayList<StudentCourseAttendance> studentAttendanceMappingList = new ArrayList<StudentCourseAttendance>();
		List<String> studentList = new ArrayList<String>();
		Map<String,String> courseAcadYearMap = new HashMap<String, String>();

		String lectureDetails = attendance.getLecture();
		logger.info("lectureDetails--->" + lectureDetails);
		logger.info("CourseId--->" + attendance.getCourseId());
		String tmpCourseId = lectureDetails.substring(
				lectureDetails.indexOf("[") + 1, lectureDetails.indexOf("]"));
		String[] courseidsStrings = tmpCourseId.split(", ");

		List<Long> courseIdList = new ArrayList<>();
		for (int i = 0; i < courseidsStrings.length; i++) {
			courseIdList.add(Long.valueOf(courseidsStrings[i]));
			
			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

		       DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

		       .createDefaultConnectionByDS(defaultUrl, defaultUsername,

		       defaultPassword);

//		       DriverManagerDataSource dataSourceTimetable = multipleDBConnection
//
//		       .createConnectionByDS("jdbc:mysql://localhhost:3306/", defaultUsername,
//		                   defaultPassword, "sap_master_inc");
//
//		       timetableDAO.setDS(dataSourceTimetable);
//		       
//					String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(courseidsStrings[i].substring(0, 8), courseidsStrings[i].substring(8));
//					//String sapAcadYear = timetableDAO.getAcadYearFromSapMaster("51709397", "50474742");
//					courseAcadYearMap.put(courseidsStrings[i], sapAcadYear);
//			
//				timetableDAO.setDS(dataSourceDefaultLms);
		
		}
		List<Course> students = courseService
				.findStudentsByMultipleCourseId(courseIdList);

		List<String> absentCourseIds = new ArrayList<>();
		

		List<String> statusList = new ArrayList<String>(
				Arrays.asList(attendance.getStatus().split(",")));

		String flag = attendance.getLecture().split(" To ")[1].split(",")[1];
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,String> suc = new HashMap<String,String>();
		for(int j=0;j<statusList.size();j++){
		      String[] statusArray = null;
		                  
		      statusArray = statusList.get(j).split("_");
		      suc.put(statusArray[1], statusArray[0]+"_"+statusArray[2]);
		}
		try {
			Date dt = format.parse(attendance.getLecture().split(" To ")[0]);
			Date dt1 = format.parse(attendance.getLecture().split(" To ")[1]
					.split(",")[0]);

			if (students != null && students.size() > 0) {
				int i = 0;
				for (Course sca : students) {
					StudentCourseAttendance bean = new StudentCourseAttendance();

					
					bean.setFacultyId(username);
					bean.setPresentFacultyId(attendance.getPresentFacultyId());
					bean.setCreatedBy(username);
					bean.setLastModifiedBy(username);

					bean.setStartTime(formatter.format(dt));
					bean.setEndTime(formatter.format(dt1));
					bean.setRollNo(sca.getRollNo());
					logger.info("i--->" + i);
					bean.setUsername(sca.getUsername());
                    
					String statusCourse = "";
					if(suc.containsKey(sca.getUsername())){
					      statusCourse = suc.get(sca.getUsername());
					}
					//suc.get(sca.getUsername());
					                              
					String[] statusArray = null;
					if(statusCourse.contains("_")){
					      statusArray = statusCourse.split("_");
					      bean.setStatus(statusArray[0]);
					      bean.setCourseId(statusArray[1]);
					}
					

					/*String[] statusArray = null;
					statusArray = statusList.get(i).split("_");
					bean.setStatus(statusArray[0]);
					bean.setUsername(statusArray[1]);
					bean.setCourseId(statusArray[2]);*/

					
					logger.info("flag---->"
							+ attendance.getLecture().split(" To ")[1]
									.split(",")[1]);
					bean.setFlag(flag);

					bean.setActive("Y");

					i++;
					studentAttendanceMappingList.add(bean);

				}

				studentCourseAttendanceService
						.updateBatch(studentAttendanceMappingList);

				int count = 0;

				for (String status : statusList) {

					String[] statusArray = null;
					statusArray = status.split("_");
					if (statusArray[0].equalsIgnoreCase("Absent")) {
						count++;
						if (!absentCourseIds.contains(statusArray[2])) {
							absentCourseIds.add(statusArray[2]);

						}
					}
				}

				
				List<StudentCourseAttendance> s = studentCourseAttendanceService
						.getAllPresentRecord(courseIdList,
								formatter.format(dt), formatter.format(dt1),
								username);
				logger.info("countUPD--->" + count);
				if (count == 0) {
					if (s == null) {
						for (Long cid : courseIdList) {
							
							StudentCourseAttendance bean = new StudentCourseAttendance();

							bean.setCourseId(String.valueOf(cid));

							bean.setFacultyId(username);
							bean.setPresentFacultyId(attendance.getPresentFacultyId());
							bean.setCreatedBy(username);
							bean.setLastModifiedBy(username);

							bean.setStartTime(formatter.format(dt));
							bean.setEndTime(formatter.format(dt1));

							bean.setStatus("Present");
							logger.info("flag---->"
									+ attendance.getLecture().split(" To ")[1]
											.split(",")[1]);
							bean.setFlag(flag);

							bean.setActive("Y");
							bean.setDelFlag("N");
							bean.setAcadYear(courseAcadYearMap.get(cid.toString()));
							Course c = courseService.findByID(Long.valueOf(cid));
							bean.setAcadSession(c.getAcadSession());

							studentCourseAttendanceService.insert(bean);
						
						}
					} else {
//						for (StudentCourseAttendance sca : s) {
//							studentCourseAttendanceService.updateDelFlag("N",
//									sca.getId());
//						}
						if(s.size() > 0) {
							for (StudentCourseAttendance sca : s) {
								studentCourseAttendanceService.updateDelFlag("N",attendance.getPresentFacultyId(),sca.getId());
							}
						}else {
							for (Long cid : courseIdList) {
								
								StudentCourseAttendance bean = new StudentCourseAttendance();

								bean.setCourseId(String.valueOf(cid));

								bean.setFacultyId(username);
								bean.setPresentFacultyId(attendance.getPresentFacultyId());
								bean.setCreatedBy(username);
								bean.setLastModifiedBy(username);

								bean.setStartTime(formatter.format(dt));
								bean.setEndTime(formatter.format(dt1));

								bean.setStatus("Present");
								logger.info("flag---->"
										+ attendance.getLecture().split(" To ")[1]
												.split(",")[1]);
								bean.setFlag(flag);

								bean.setActive("Y");
								bean.setDelFlag("N");
								bean.setAcadYear(courseAcadYearMap.get(cid.toString()));
								Course c = courseService.findByID(Long.valueOf(cid));
								bean.setAcadSession(c.getAcadSession());

								studentCourseAttendanceService.insert(bean);
							
							}
						}
					}
				} else if (count > 0) {
					if (s != null) {
						for (StudentCourseAttendance sca : s) {
							if (absentCourseIds.contains(sca.getCourseId())) {
								studentCourseAttendanceService.updateDelFlag("Y",attendance.getPresentFacultyId(), sca.getId());
							} else {
								for (Long cid : courseIdList) {
									if (!absentCourseIds.contains(cid)) {
										if (absentCourseIds.contains(sca
												.getCourseId())) {
											StudentCourseAttendance bean = new StudentCourseAttendance();

											bean.setCourseId(String
													.valueOf(cid));

											bean.setFacultyId(username);
											bean.setPresentFacultyId(attendance.getPresentFacultyId());
											bean.setCreatedBy(username);
											bean.setLastModifiedBy(username);

											bean.setStartTime(formatter
													.format(dt));
											bean.setEndTime(formatter
													.format(dt1));

											bean.setStatus("Present");
											logger.info("flag---->"
													+ attendance.getLecture()
															.split(" To ")[1]
															.split(",")[1]);
											bean.setFlag(flag);

											bean.setActive("Y");
											bean.setDelFlag("N");
											bean.setAcadYear(courseAcadYearMap.get(cid.toString()));
											Course c = courseService.findByID(Long.valueOf(cid));
											bean.setAcadSession(c.getAcadSession());

											studentCourseAttendanceService
													.insert(bean);
										} else {
											studentCourseAttendanceService.updateDelFlag("N",attendance.getPresentFacultyId(),sca.getId());
										}
									}
								}
							}
						}
					} else {
						for (Long cid : courseIdList) {
							if (!absentCourseIds.contains(cid)) {
								StudentCourseAttendance bean = new StudentCourseAttendance();

								bean.setCourseId(String.valueOf(cid));

								bean.setFacultyId(username);
								bean.setPresentFacultyId(attendance.getPresentFacultyId());
								bean.setCreatedBy(username);
								bean.setLastModifiedBy(username);

								bean.setStartTime(formatter.format(dt));
								bean.setEndTime(formatter.format(dt1));

								bean.setStatus("Present");
								logger.info("flag---->"
										+ attendance.getLecture().split(" To ")[1]
												.split(",")[1]);
								bean.setFlag(flag);

								bean.setActive("Y");
								bean.setDelFlag("N");
								bean.setAcadYear(courseAcadYearMap.get(cid.toString()));
								Course c = courseService.findByID(Long.valueOf(cid));
								bean.setAcadSession(c.getAcadSession());

								studentCourseAttendanceService.insert(bean);
							}
						}

					}
				}
				setSuccess(redirectAttributes, "Attendance Updated for "
						+ students.size() + " students successfully");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttributes, "Error in updating attendance");

		}

		m.addAttribute("attendance", attendance);
		return "redirect:/markAttendanceForm?courseId="
				+ attendance.getCourseId();
	}
	
	@RequestMapping(value = "/sendAttendanceDataBySAP", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void sendAttendanceDataBySAP(@RequestParam String date) {
		logger.info("Started manually--->" + Utils.getInIST());
		studentCourseAttendanceService.sendAttendanceToSAPByDate(date);
		logger.info("Ended manually--->" + Utils.getInIST());

	}
	
	public List<Timetable> getPendingLecturesList(String username) {

		List<Timetable> tt = new ArrayList<>();
		try {
			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
			DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection.createDefaultConnectionByDS(defaultUrl,
					defaultUsername, defaultPassword);

			DriverManagerDataSource dataSourceTimetable = multipleDBConnection.createConnectionByDS(
					"jdbc:mysql://localhost:3306/", defaultUsername, defaultPassword, "timetable_metadata");
			timetableDAO.setDS(dataSourceTimetable);

			Date dt = Utils.getInIST();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String curDate = "%" + dateFormat.format(dt) + "%";

			List<Timetable> tt1 = timetableDAO.getTimetableByUsernameAndDate(username, curDate);
			timetableDAO.setDS(dataSourceDefaultLms);

			for (Timetable tmtl : tt1) {
				int check = 0;
				if (tmtl.getProgramId().contains(" , ")) {
					String[] programidsStrings = tmtl.getProgramId().split(" , ");
					for (int i = 0; i < programidsStrings.length; i++) {
						Course c = courseService.findByID(Long.valueOf(tmtl.getEventId() + programidsStrings[i]));
						if (null != c) {
							logger.info("getCourseName ------->" + c.getCourseName());
						} else {
							check = 1;
						}
					}
					if (check == 0) {
						tt.add(tmtl);
					} else {
						for (int i = 0; i < programidsStrings.length; i++) {
							String cid1 = "" + tmtl.getEventId() + programidsStrings[i];
							Course c1 = courseService.findByID(Long.parseLong(cid1));
							long pgrmId1 = Long.parseLong(programidsStrings[i]);
							Program p1 = programService.findByID(pgrmId1);

							if (null != c1 && null != p1) {
								Timetable tmtlSub = new Timetable();
								tmtlSub.setClass_date(tmtl.getClass_date());
								tmtlSub.setStart_time(tmtl.getStart_time());
								tmtlSub.setEnd_time(tmtl.getEnd_time());
								tmtlSub.setEventId(tmtl.getEventId());
								tmtlSub.setFacultyId(tmtl.getFacultyId());
								tmtlSub.setFlag(tmtl.getFlag());
								cid1 = "" + tmtl.getEventId() + programidsStrings[i];
								tmtlSub.setCourseId(cid1);
								tmtlSub.setCourseName(c1.getCourseName());
								tmtlSub.setProgramId(programidsStrings[i]);
								tmtlSub.setProgramName(p1.getProgramName());

								tt.add(tmtlSub);
							}

						}
					}
				} else {
					Course c = courseService.findByID(Long.valueOf(tmtl.getEventId() + tmtl.getProgramId()));
					if (c != null) {
						tt.add(tmtl);
					}
				}
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			for (Timetable tmtl : tt) {
				List<String> courseIdList = new ArrayList<>();
				if (tmtl.getProgramId().contains(" , ")) {
					String[] programidsStrings = tmtl.getProgramId().split(" , ");
					int check1 = 0;
					for (int i = 0; i < programidsStrings.length; i++) {
						Course c = courseService.findByID(Long.valueOf(tmtl.getEventId() + programidsStrings[i]));
						if (null != c) {
							courseIdList.add(tmtl.getEventId() + programidsStrings[i]);
						} else {
							check1 = 1;
						}
					}
					if (check1 != 0) {
						for (int i = 0; i < programidsStrings.length; i++) {
							Course c = courseService.findByID(Long.valueOf(tmtl.getEventId() + programidsStrings[i]));
							if (null != c) {
								courseIdList.add(tmtl.getEventId() + programidsStrings[i]);
							}
						}
					}
					tmtl.setMultipleCourseId(courseIdList);
				} else {
					courseIdList.add(tmtl.getEventId() + tmtl.getProgramId());
					tmtl.setMultipleCourseId(courseIdList);
				}
				List<Long> courseIdLongList = new ArrayList<>();
				for (String cl : courseIdList) {
					courseIdLongList.add(Long.valueOf(cl));
				}
				Date dt1 = format.parse(tmtl.getStart_time().replace(".", ":"));
				Date dt2 = format.parse(tmtl.getEnd_time().replace(".", ":"));
				String startTime = formatter.format(dt1);
				String endTime = formatter.format(dt2);
				List<StudentCourseAttendance> studentsList = studentCourseAttendanceService
						.findByCourseIdAndDateTime(courseIdLongList, startTime, endTime, username);
				if (studentsList.size() > 0) {
					tmtl.setIsAttendanceMarked("Y");
					logger.info("Marked--->");
				}else {
					tmtl.setIsAttendanceMarked("N");
				}
			}

		} catch (Exception e) {
			logger.error("Error-->" + e.getMessage());
		}
		return tt;
	}

	public List<Timetable> getFacultyLectures(String username) {
		List<Timetable> tt = new ArrayList<>();
		try {
			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
			DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection.createDefaultConnectionByDS(defaultUrl,
					defaultUsername, defaultPassword);

			DriverManagerDataSource dataSourceTimetable = multipleDBConnection.createConnectionByDS(
					"jdbc:mysql://localhost:3306/", defaultUsername, defaultPassword, "timetable_metadata");
			timetableDAO.setDS(dataSourceTimetable);

			Date dt = Utils.getInIST();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String curDate = "%" + dateFormat.format(dt) + "%";

			tt = timetableDAO.getTimetableByUsernameAndDate(username, curDate);
			
			timetableDAO.setDS(dataSourceDefaultLms);
		} catch (Exception e) {
			logger.error("Error-->" + e.getMessage());
		}
		return tt;
	}
}

package com.spts.lms.web.controllers;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spts.lms.auth.Token;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.forum.Forum;
import com.spts.lms.beans.forum.ForumReply;
import com.spts.lms.beans.forum.ForumReplyDisLike;
import com.spts.lms.beans.forum.ForumReplyLike;
import com.spts.lms.beans.message.Message;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.forum.ForumCounterReplyService;
import com.spts.lms.services.forum.ForumReplyService;
import com.spts.lms.services.forum.ForumService;
import com.spts.lms.services.message.MessageService;
import com.spts.lms.services.message.StudentMessageService;
import com.spts.lms.services.user.UserRoleService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.BusinessBypassRule;

import com.spts.lms.web.utils.HtmlValidation;

import com.spts.lms.web.utils.Utils;
import com.spts.lms.web.utils.ValidationException;

@Controller
public class ForumController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	ForumService forumService;

	@Autowired
	ForumReplyService forumReplyService;

	@Autowired
	UserService userService;

	@Autowired
	CourseService courseService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	MessageService messageService;

	@Autowired
	UserRoleService userRoleService;

	@Autowired
	StudentMessageService studentMessageService;

	@Autowired
	ForumCounterReplyService forumCounterReplyService;
	
	@Autowired
	BusinessBypassRule BusinessBypassRule;
	
	private static final Logger logger = Logger
			.getLogger(ForumController.class);

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/createForumForm", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String createForumForm(Principal principal, Model m,
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) Long courseId,
			@ModelAttribute Forum forum) {
		m.addAttribute("webPage", new WebPage("forum", "Create Forum", true,
				false));

		Token userDetails = (Token) principal;

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);


		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
				|| userDetails.getAuthorities().contains(Role.ROLE_STUDENT)
				|| userDetails.getAuthorities().contains(Role.ROLE_DEAN)
				|| userDetails.getAuthorities().contains(Role.ROLE_HOD)) {

			m.addAttribute(
					"allCourses",
					courseService.findByUserActive(username,
							userDetails.getProgramName()));
		}

		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			String progName = userDetails.getProgramName();

			m.addAttribute("allCourses",
					courseService.findByCoursesBasedOnProgramName(progName));
		}
		if (courseId == null) {
			m.addAttribute(
					"courses",
					courseService.findByUserActive(username,
							userdetails1.getProgramName()));
			m.addAttribute("showDropDown", true);
			m.addAttribute("showRandom", false);
			forum.setCourseId(courseId);
		} else {
			// .addAttribute("courseId", "true");
			forum.setCourseId(courseId);
			m.addAttribute("showDropDown", false);
			m.addAttribute("showRandom", true);
		}
		forum.setCourse(courseService.findByID(forum.getCourseId()));

		// m.addAttribute("allForums", forumService.findAll());

		m.addAttribute("forum", forum);
		if (id != null) {
			setSuccess(m, "Forum Created successfully!");
		}

		return "forum/createForum";

	}
	
   // 22-10-2021 change
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/createForum", method = { RequestMethod.POST })
	public String createForum(@ModelAttribute Forum forum, Model m,
			@RequestParam(required = false, name = "id") Long id,
			RedirectAttributes redirectAttributes, Long courseId,
			Principal principal) {
		m.addAttribute("webPage", new WebPage("forum", "Create Forum", true,
				false));
		String username = principal.getName();
		System.out.println("COURSEID>>>>>>>>>>>>>>>>"+courseId);
		
		String question = forum.getQuestion();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();

	
		
		User u = userService.findByUserName(username);
	
		try {
	
		HtmlValidation.validateHtml(forum, Arrays.asList("description"));



		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		
		Course c = courseService.findByID(courseId);
		
         if(c == null) {
	    	 throw new ValidationException("Invalid Course Selected");
	     }
	         
	       //  BusinessBypassRule.validateNumericForLong(courseId); 
	         BusinessBypassRule.validateAlphaNumeric(forum.getTopic());
	     
	         if(question == null || question.isEmpty())
	         {
	        	 throw new ValidationException("Question can not be empty.");	 
	         }
	
			forum.setCreatedBy(username);
			forum.setLastModifiedBy(username);
			
			
			
			if (id == null) {
				forumService.insertWithIdReturn(forum);
			}

			forum.setCourse(courseService.findByID(courseId));

			setSuccess(m, "Forum Created successfully");

			m.addAttribute(
					"allCourses",
					courseService.findByUserActive(username,
							userdetails1.getProgramName()));
			m.addAttribute("forum", forum);
			m.addAttribute("course",
					courseService.findByID(forum.getCourseId()));
			m.addAttribute("id", forum.getId());


		}
		catch (ValidationException e) {
			
			logger.error(e.getMessage(), e);
			setError(m,e.getMessage());
			m.addAttribute("webPage", new WebPage("forum", "Create Forum",
					false, false));
			m.addAttribute(
					"allCourses",
					courseService.findByUserActive(username,
							userdetails1.getProgramName()));
			return "forum/createForum";
		}
		
	
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			
			setError(m, "Error in creating forum");
			m.addAttribute("webPage", new WebPage("forum", "Create Forum",
					false, false));
			m.addAttribute(
					"allCourses",
					courseService.findByUserActive(username,
							userdetails1.getProgramName()));
			return "forum/createForum";
		}
		redirectAttributes.addAttribute("id", forum.getId());
		return "redirect:/replyToQuestionForm";
	
    
	}
	
	@Secured({ "ROLE_STUDENT", "ROLE_FACULTY" })
	@RequestMapping(value = "/replyToQuestionForm", method = {RequestMethod.GET, RequestMethod.POST})
	public String replyToQuestionForm(@ModelAttribute ForumReply forumReply,
			Model m, @RequestParam(required = false) Long id, Long courseId,
			RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("forum", "Reply to Question",
				true, false));
	
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
			

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("username", username);

		DateFormat df = new SimpleDateFormat("E, MMM dd yyyy");
		try {
			
			
			if (id != null) {
				List<ForumReply> forumReplyList = forumReplyService
						.getRepliesFromQuestion(id);
				
				for (ForumReply fr : forumReplyList) {

					/*
					 * String reply = fr.getReply();
					 * logger.info("--------------------------->" + reply);
					 * Document doc = Jsoup.parse(reply); String cReply =
					 * doc.text(); logger.info("------------------------asd--->"
					 * + cReply); fr.setReply(cReply);
					 */
					User user = userService.findByUserName(fr.getCreatedBy());
					fr.setFirstname(user.getFirstname());
					fr.setLastname(user.getLastname());

					// to convert Date to String, use format method of
					// SimpleDateFormat class.
					String strDate = df.format(fr.getCreatedDate());
					fr.setRepliedDate(strDate);

					List<ForumReply> counterReplies = forumCounterReplyService
							.findCounterReplies(fr.getId());

					if (counterReplies.size() > 0) {
						fr.setCounterReplyList(counterReplies);
						for (ForumReply ffr : counterReplies) {
							User us = userService.findByUserName(ffr
									.getCreatedBy());
							ffr.setFirstname(us.getFirstname());
							ffr.setLastname(us.getLastname());

							// to convert Date to String, use format method of
							// SimpleDateFormat class.
							String str = df.format(ffr.getCreatedDate());
							ffr.setRepliedDate(str);
							counterReplies
									.set(counterReplies.indexOf(ffr), ffr);
						}

					}

					forumReplyList.set(forumReplyList.indexOf(fr), fr);

				}

				Forum forum = forumService.findByID(id);
				User ur = userService.findByUserName(forum.getCreatedBy());
				forum.setFirstname(ur.getFirstname());
				forum.setLastname(ur.getLastname());
				String strDate = df.format(forum.getCreatedDate());
				forum.setDateCreated(strDate);
				m.addAttribute("forum", forum);

				for (ForumReply fr : forumReplyList) {
					Long replyId = fr.getId();
				
					if (fr.getCreatedBy().equalsIgnoreCase(username)) {

						fr.setShowReportAbuse("false");
					} else {
						fr.setShowReportAbuse("true");
					}
					List<ForumReplyLike> forumReplyLike = forumReplyService
							.findByReplyAndUsernameForLike(replyId, username);
					
					if (forumReplyLike.isEmpty()) {
						
						fr.setShowLike("true");

					} else {
						fr.setShowLike("false");
						

					}

					List<ForumReplyDisLike> forumReplyDisLike = forumReplyService
							.findByReplyAndUsernameForDisLike(replyId, username);
					

					if (forumReplyDisLike.isEmpty()) {
						fr.setShowDislike("true");
						
					} else {
						fr.setShowDislike("false");
						
					}

					List<ForumReplyLike> likes = forumReplyService
							.getLikeCount(replyId);
					forumReply.setLikes(likes.size());
					fr.setLikes(likes.size());
					List<ForumReplyDisLike> disLikes = forumReplyService
							.getDisLikeCount(replyId);
					forumReply.setDisLikes(disLikes.size());
					fr.setDisLikes(disLikes.size());
					

					m.addAttribute("allReplies", forumReplyList);
					m.addAttribute("courseId", forum.getCourseId());

					forumReplyList.set(forumReplyList.indexOf(fr), fr);
				}
				m.addAttribute("forum", forum);

			} else {
				setError(redirectAttributes, "Error!");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error");
			m.addAttribute("webPage", new WebPage("forum", "Create Forum",
					false, false));
			return "forum/reply";
		}
		return "forum/reply";
	}

	@Secured({ "ROLE_STUDENT", "ROLE_FACULTY" })
	@RequestMapping(value = "/replyToQuestion", method = { RequestMethod.POST })
	public String replyToQuestion(@ModelAttribute ForumReply forumreply, RedirectAttributes redirectAttributes, Model m,
			@RequestParam(required = false) Long questionId, Long courseId, Principal principal) {
		m.addAttribute("webPage", new WebPage("forum", "Reply to Question", true, false));


		String username = principal.getName();

		String reply = forumreply.getReply();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

//			//reply validation
		

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			

			
			HtmlValidation.validateHtml(forumreply, new ArrayList<>());
//			if(reply == null ||reply.isEmpty())
//			{
//				 throw new ValidationException("Invalid Reply to Forum");
//			}

			if (reply == null || reply.isEmpty()) {
				throw new ValidationException("Reply can't be blank");
			}

			
			if (questionId != null) {

				Forum forum = forumService.findByID(questionId);
				forumreply.setCourseId(forum.getCourseId());
				forumreply.setTopic(forum.getTopic());
				forumreply.setQuestionId(questionId);
				forumreply.setQuestion(forum.getQuestion());
				forumreply.setCreatedBy(username);
				forumreply.setLastModifiedBy(username);
				forumReplyService.insertWithIdReturn(forumreply);
				m.addAttribute("allForums", forumService.findAll());
				List<ForumReply> allReplies = forumReplyService.getRepliesFromQuestion(questionId);
				for (ForumReply fr : allReplies) {
					List<ForumReply> counterReplies = forumCounterReplyService.findCounterReplies(fr.getId());

					if (counterReplies.size() > 0) {
						fr.setCounterReplyList(counterReplies);

						allReplies.set(allReplies.indexOf(fr), fr);
					}

				}

				m.addAttribute("allReplies", allReplies);

				setSuccess(redirectAttributes, "Replied Successfully!");

			} else {
				setError(m, "Error");

			}

		}
		catch (ValidationException e) {
			logger.error(e.getMessage(), e);
			setError(m, e.getMessage());
			m.addAttribute("webPage", new WebPage("forum", "Create Forum", false, false));
			return "forum/reply";
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in creating forum");
			m.addAttribute("webPage", new WebPage("forum", "Create Forum", false, false));
			return "forum/reply";
		}
		redirectAttributes.addAttribute("id", questionId);
		return "redirect:/replyToQuestionForm";
	}

	/*
	 * @RequestMapping(value = "/replyToQuestionForm", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * replyToQuestionForm(@ModelAttribute ForumReply forumReply, Model m,
	 * 
	 * @RequestParam(required = false) Long id, Long courseId,
	 * RedirectAttributes redirectAttributes, Principal principal) {
	 * m.addAttribute("webPage", new WebPage("forum", "Reply to Question", true,
	 * false)); String username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * logger.info("ACAD SESSION------------------------->" +
	 * u.getAcadSession());
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName);
	 * m.addAttribute("AcadSession", acadSession);
	 * 
	 * DateFormat df = new SimpleDateFormat("E, MMM dd yyyy"); try { if (id !=
	 * null) { List<ForumReply> forumReplyList = forumReplyService
	 * .getRepliesFromQuestion(id); logger.info("forumReplyList" +
	 * forumReplyList); for (ForumReply fr : forumReplyList) {
	 * 
	 * 
	 * String reply = fr.getReply(); logger.info("--------------------------->"
	 * + reply); Document doc = Jsoup.parse(reply); String cReply = doc.text();
	 * logger.info("------------------------asd--->" + cReply);
	 * fr.setReply(cReply);
	 * 
	 * User user = userService.findByUserName(fr.getCreatedBy());
	 * fr.setFirstname(user.getFirstname()); fr.setLastname(user.getLastname());
	 * 
	 * // to convert Date to String, use format method of // SimpleDateFormat
	 * class. String strDate = df.format(fr.getCreatedDate());
	 * fr.setRepliedDate(strDate);
	 * forumReplyList.set(forumReplyList.indexOf(fr), fr);
	 * 
	 * }
	 * 
	 * Forum forum = forumService.findByID(id); User ur =
	 * userService.findByUserName(forum.getCreatedBy());
	 * forum.setFirstname(ur.getFirstname());
	 * forum.setLastname(ur.getLastname()); String strDate =
	 * df.format(forum.getCreatedDate()); forum.setDateCreated(strDate);
	 * m.addAttribute("forum", forum);
	 * 
	 * for (ForumReply fr : forumReplyList) { Long replyId = fr.getId();
	 * logger.info("ID" + replyId); if
	 * (fr.getCreatedBy().equalsIgnoreCase(username)) {
	 * 
	 * fr.setShowReportAbuse("false"); } else { fr.setShowReportAbuse("true"); }
	 * List<ForumReplyLike> forumReplyLike = forumReplyService
	 * .findByReplyAndUsernameForLike(replyId, username);
	 * logger.info("LISTforumReplyLike" + forumReplyLike);
	 * logger.info("fr.getReportAbuse()" + fr.getReportAbuse());
	 * 
	 * if (forumReplyLike.isEmpty()) { logger.info("IF Enterd");
	 * fr.setShowLike("true");
	 * 
	 * } else { fr.setShowLike("false"); logger.info("Else Enterd");
	 * 
	 * }
	 * 
	 * List<ForumReplyDisLike> forumReplyDisLike = forumReplyService
	 * .findByReplyAndUsernameForDisLike(replyId, username);
	 * logger.info("LISTforumReplyDisLike" + forumReplyDisLike);
	 * 
	 * if (forumReplyDisLike.isEmpty()) { fr.setShowDislike("true");
	 * logger.info("IF Enterd"); } else { fr.setShowDislike("false");
	 * logger.info("Else Enterd"); }
	 * 
	 * List<ForumReplyLike> likes = forumReplyService .getLikeCount(replyId);
	 * forumReply.setLikes(likes.size()); fr.setLikes(likes.size());
	 * List<ForumReplyDisLike> disLikes = forumReplyService
	 * .getDisLikeCount(replyId); forumReply.setDisLikes(disLikes.size());
	 * fr.setDisLikes(disLikes.size()); logger.info("COUNTLikes" +
	 * likes.size()); logger.info("COUNTDisLikes" + disLikes.size());
	 * 
	 * m.addAttribute("allReplies", forumReplyList); m.addAttribute("courseId",
	 * forum.getCourseId());
	 * 
	 * forumReplyList.set(forumReplyList.indexOf(fr), fr); }
	 * m.addAttribute("forum", forum);
	 * 
	 * } else { setError(redirectAttributes, "Error!"); }
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e); setError(m,
	 * "Error"); m.addAttribute("webPage", new WebPage("forum", "Create Forum",
	 * false, false)); return "forum/reply"; } return "forum/reply"; }
	 * 
	 * @RequestMapping(value = "/replyToQuestion", method = { RequestMethod.POST
	 * }) public String replyToQuestion(@ModelAttribute ForumReply forumreply,
	 * RedirectAttributes redirectAttributes, Model m,
	 * 
	 * @RequestParam(required = false) Long questionId, Long courseId, Principal
	 * principal) { m.addAttribute("webPage", new WebPage("forum",
	 * "Reply to Question", true, false)); String username =
	 * principal.getName(); logger.info("replyToQuestion courseId is " +
	 * courseId); Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * logger.info("ACAD SESSION------------------------->" +
	 * u.getAcadSession());
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName);
	 * m.addAttribute("AcadSession", acadSession);
	 * 
	 * try { if (questionId != null) {
	 * 
	 * Forum forum = forumService.findByID(questionId);
	 * forumreply.setCourseId(forum.getCourseId());
	 * forumreply.setTopic(forum.getTopic());
	 * forumreply.setQuestionId(questionId);
	 * forumreply.setQuestion(forum.getQuestion());
	 * forumreply.setCreatedBy(username);
	 * forumreply.setLastModifiedBy(username);
	 * forumReplyService.insertWithIdReturn(forumreply);
	 * m.addAttribute("allForums", forumService.findAll());
	 * m.addAttribute("allReplies",
	 * forumReplyService.getRepliesFromQuestion(questionId));
	 * logger.info("Replies" +
	 * forumReplyService.getRepliesFromQuestion(questionId));
	 * 
	 * setSuccess(redirectAttributes, "Replied Successfully!");
	 * 
	 * } else { setError(m, "Error"); }
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e); setError(m,
	 * "Error in creating forum"); m.addAttribute("webPage", new
	 * WebPage("forum", "Create Forum", false, false)); return "forum/reply"; }
	 * redirectAttributes.addAttribute("id", questionId); return
	 * "redirect:/replyToQuestionForm"; }
	 */

	@Secured({ "ROLE_STUDENT", "ROLE_FACULTY" })
	@RequestMapping(value = "/repliedToQuestion", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String repliedToQuestion(@ModelAttribute ForumReply forumReply,
			Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("forum", "Create Forum", true,
				false));
		
		//System.out.println("<<<<<<<<<<<<<<<Inside Reply to question>>>>>>>>>>");

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("forumReply", forumReply);
		setSuccess(m, "Replied successfully");
		return "forum/forum";

	}

	@Secured({ "ROLE_STUDENT", "ROLE_FACULTY" })
	@RequestMapping(value = "/reportAbuseForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String reportAbuseForm(@ModelAttribute Message message, Model m,
			Principal principal, @RequestParam Long replyId,
			@RequestParam Long questionId) {
		m.addAttribute("webPage", new WebPage("forum", "Report Abuse", true,
				false));

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		ForumReply forumReply = forumReplyService.findByID(replyId);
		String abuseReply = forumReply.getReply();
		message.setCreatedBy(principal.getName());
		Date dt = Utils.getInIST();
		message.setCreatedDate(dt);
		message.setDescription("I found this content to be inappropriate");
		message.setSubject("Report Abuse");
		message.setForumReply(abuseReply);
		messageService.insertWithIdReturn(message);
		m.addAttribute("message", message);
		m.addAttribute("questionId", questionId);
		m.addAttribute("replyId", replyId);
		m.addAttribute("forumReply", abuseReply);
		return "forum/reportAbuse";
	}

	/*
	 * @RequestMapping(value = "/reportAbuse", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String reportAbuse(@ModelAttribute Message
	 * message, Model m, Principal principal, RedirectAttributes
	 * redirectAttributes,
	 * 
	 * @RequestParam Long questionId, @RequestParam Long replyId) {
	 * m.addAttribute("webPage", new WebPage("forum", "Report Abuse", true,
	 * true)); logger.info("ID" + replyId); logger.info("questionId" +
	 * questionId); ForumReply forumReply = forumReplyService.findByID(replyId);
	 * forumReplyService.updateReportAbuse(replyId);
	 * forumReplyService.updateRecentActivityOnReply(replyId); ForumReply
	 * forumReply1 = forumReplyService.findByID(replyId);
	 * logger.info("forumReply1.getReportAbuse()" +
	 * forumReply1.getReportAbuse()); if (forumReply1.getReportAbuse() > 5 ||
	 * forumReply1.getReportAbuse() == 5) {
	 * forumReplyService.setInActive(replyId); } UserRole userRole =
	 * userRoleService.findAdmin();
	 * 
	 * StudentMessage bean = new StudentMessage();
	 * bean.setDescription(message.getDescription());
	 * bean.setSubject(message.getSubject());
	 * bean.setUsername(userRole.getUsername());
	 * bean.setCreatedBy(principal.getName());
	 * bean.setLastModifiedBy(principal.getName());
	 * bean.setMessageId(message.getId());
	 * bean.setForumReply(message.getForumReply()); bean.setReplyId(replyId);
	 * studentMessageService.insertWithIdReturn(bean);
	 * 
	 * m.addAttribute("message", message); m.addAttribute("studentMessage",
	 * bean); m.addAttribute("forumReply", message.getForumReply());
	 * redirectAttributes.addAttribute("id", questionId);
	 * 
	 * return "redirect:/replyToQuestionForm";
	 * 
	 * }
	 */

	@Secured({ "ROLE_STUDENT", "ROLE_FACULTY" })
	@RequestMapping(value = "/reportAbuse", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String reportAbuse(Model m, Principal principal,
			RedirectAttributes redirectAttributes,
			@RequestParam Long questionId, @RequestParam Long replyId) {
		m.addAttribute("webPage", new WebPage("forum", "Report Abuse", true,
				false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		
		ForumReply forumReply = forumReplyService.findByID(replyId);
		String abuseReply = forumReply.getReply();

		forumReplyService.updateReportAbuse(replyId);
		forumReplyService.updateRecentActivityOnReply(replyId);
		ForumReply forumReply1 = forumReplyService.findByID(replyId);
		
		if (forumReply1.getReportAbuse() > 5
				|| forumReply1.getReportAbuse() == 5) {
			forumReplyService.setInActive(replyId);
		}

		redirectAttributes.addAttribute("id", questionId);
		setSuccess(redirectAttributes, "Reported Successfully!");

		return "redirect:/replyToQuestionForm";

	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/viewForum", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewForum(Principal principal, Model m,
			@RequestParam(required = false) Long courseId,
			@ModelAttribute Forum forum) {
		m.addAttribute("webPage", new WebPage("forum", "View Forum", true,
				false));


		String username = principal.getName();
		m.addAttribute("courseId", courseId);
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		Token userDetails = (Token) principal;
		List<Forum> forumList = forumService.findByCourse(courseId);
		
		List<Integer> replyCount = new ArrayList<Integer>();
		for (Forum f : forumList) {
			

			User user = userService.findByUserName(f.getCreatedBy());
			f.setFirstname(user.getFirstname());
			f.setLastname(user.getLastname());

			String question = f.getQuestion();

			/*
			 * Document doc = Jsoup.parse(question); String cQuestion =
			 * doc.text();
			 * 
			 * f.setQuestion(cQuestion);
			 */

			List<ForumReply> replies = forumReplyService
					.getRepliesFromQuestion(f.getId());

			
			replyCount.add(replies.size());
			

			m.addAttribute("replies", replies.size());
			forumList.set(forumList.indexOf(f), f);

		}

		m.addAttribute("forumList", forumList);

		

		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
				|| userDetails.getAuthorities().contains(Role.ROLE_STUDENT)
				|| userDetails.getAuthorities().contains(Role.ROLE_DEAN)
				|| userDetails.getAuthorities().contains(Role.ROLE_HOD)) {

			m.addAttribute(
					"allCourses",
					courseService.findByUserActive(username,
							userDetails.getProgramName()));
		}

		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			String progName = userDetails.getProgramName();
			m.addAttribute("allCourses",
					courseService.findByCoursesBasedOnProgramName(progName));
		}
		m.addAttribute("replyCount", replyCount);
		m.addAttribute("forum", forum);
		// m.addAttribute("allCourses",
		// courseService.findByUserActive(username));
		m.addAttribute("question", forum.getQuestion());
		List<Forum> allForums = forumService.findByCourse(courseId);
		for (Forum f : allForums) {
			User user = userService.findByUserName(f.getCreatedBy());
			f.setFirstname(user.getFirstname());
			f.setLastname(user.getLastname());
			List<String> students = forumReplyService.findStudentsReplied(f
					.getId());
			f.setStudents(students);
			f.setStudentsInvolved(students.size());
			allForums.set(allForums.indexOf(f), f);
		}

		m.addAttribute("allForums", allForums);
		return "forum/viewForum";
	}

	/*
	 * @RequestMapping(value="/updateLike",method={RequestMethod.GET,RequestMethod
	 * .POST}) public @ResponseBody String updateLike(@RequestParam ("replyId")
	 * Long replyId,Model m,Principal principal,@ModelAttribute ForumReply
	 * forumReply){ logger.info("replyId-----" + replyId); String username =
	 * principal.getName(); Gson gson = new Gson(); try{
	 * forumReplyService.updateLike(replyId,principal.getName()); forumReply =
	 * forumReplyService.findByID(replyId); Integer vote = forumReply.getVote();
	 * vote++; forumReply.setVote(vote); forumReplyService.update(forumReply);
	 * System.out.println("fr vote"+forumReply.getVote());
	 * m.addAttribute("vote",forumReply.getVote());
	 * logger.info("response of like "+gson.toJson(forumReply)); return
	 * gson.toJson(forumReply); }catch(Exception e){ e.printStackTrace();
	 * logger.error("Error "+e.getMessage()); return "Error"; }
	 * 
	 * 
	 * }
	 */

	/*
	 * @RequestMapping(value = "/updateLike", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public @ResponseBody String updateLike(
	 * 
	 * @RequestParam("replyId") Long replyId, Model m, Principal principal,
	 * 
	 * @ModelAttribute ForumReply forumReply) { logger.info("replyId-----" +
	 * replyId); String username = principal.getName(); try {
	 * forumReplyService.updateLike(replyId, principal.getName()); forumReply =
	 * forumReplyService.findByID(replyId); List<ForumReplyLike> likes =
	 * forumReplyLikeService .getVoteCount(replyId); logger.info("COUNT" +
	 * likes.size());
	 * 
	 * m.addAttribute("likes", likes.size());
	 * 
	 * Integer vote = forumReply.getVote(); vote++; forumReply.setVote(vote);
	 * forumReplyService.update(forumReply);
	 * 
	 * return "Success"; } catch (Exception e) { e.printStackTrace();
	 * logger.error("Error " + e.getMessage()); return "Error"; }
	 * 
	 * }
	 */
	@RequestMapping(value = "/updateLike", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String updateLike(
			@RequestParam("replyId") Long replyId, Model m,
			Principal principal, @ModelAttribute ForumReply forumReply) {
		
		String username = principal.getName();
		try {
			forumReplyService.updateLike(replyId, principal.getName());
			forumReplyService.updateRecentActivityOnReply(replyId);
			forumReply = forumReplyService.findByID(replyId);
			List<ForumReplyLike> votes = forumReplyService
					.getLikeCount(replyId);
			

			m.addAttribute("votes", votes.size());
			/*
			 * Integer vote = forumReply.getVote(); vote++;
			 * forumReply.setVote(vote); forumReplyService.update(forumReply);
			 */
			return "Success";
		} catch (Exception e) {

			logger.error("Error " + e.getMessage());
			return "Error";
		}

	}

	@RequestMapping(value = "/updateDisLike", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String updateDisLike(
			@RequestParam("replyId") Long replyId, Model m,
			Principal principal, @ModelAttribute ForumReply forumReply) {
		
		String username = principal.getName();
		try {
			forumReplyService.updateDisLike(replyId, principal.getName());
			forumReplyService.updateRecentActivityOnReply(replyId);
			forumReply = forumReplyService.findByID(replyId);
			List<ForumReplyDisLike> dislikes = forumReplyService
					.getDisLikeCount(replyId);
			

			m.addAttribute("dislikes", dislikes.size());
			/*
			 * Integer vote = forumReply.getVote(); vote++;
			 * forumReply.setVote(vote); forumReplyService.update(forumReply);
			 */
			return "Success";
		} catch (Exception e) {

			logger.error("Error " + e.getMessage());
			return "Error";
		}

	}

	@Secured({ "ROLE_STUDENT", "ROLE_FACULTY" })
	@RequestMapping(value = "/replyToReply", method = { RequestMethod.POST })
	public String replyToReply(@ModelAttribute ForumReply forumreply,
			RedirectAttributes redirectAttributes, Model m,
			@RequestParam Long replyId, @RequestParam Long questionId,
			Principal principal) {
		m.addAttribute("webPage", new WebPage("forum", "Reply to Question",
				true, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			if (replyId != null) {
				forumreply.setReplyId(String.valueOf(replyId));
				forumreply.setAnswer(forumreply.getCounterReply());

				forumreply.setCreatedBy(username);
				forumreply.setLastModifiedBy(username);
				forumCounterReplyService.insertWithIdReturn(forumreply);

				List<ForumReply> allReplies = forumReplyService
						.getRepliesFromQuestion(questionId);
				for (ForumReply fr : allReplies) {
					List<ForumReply> counterReplies = forumCounterReplyService
							.findCounterReplies(fr.getId());

					if (counterReplies.size() > 0) {
						fr.setCounterReplyList(counterReplies);

						allReplies.set(allReplies.indexOf(fr), fr);
					}

				}
				

				m.addAttribute("allReplies", allReplies);
				setSuccess(redirectAttributes, "Replied Successfully!");

			} else {
				setError(m, "Error");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in creating forum");
			m.addAttribute("webPage", new WebPage("forum", "Create Forum",
					false, false));
			return "forum/reply";
		}
		redirectAttributes.addAttribute("id", questionId);
		return "redirect:/replyToQuestionForm";
	}

	@Secured({ "ROLE_STUDENT", "ROLE_FACULTY" })
	@RequestMapping(value = "/updateForumForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String updateForumForm(@RequestParam("id") Long id, Model m,
			Principal principal) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		Token userDetails = (Token) principal;
		try {
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
					|| userDetails.getAuthorities().contains(Role.ROLE_STUDENT)
					|| userDetails.getAuthorities().contains(Role.ROLE_DEAN)
					|| userDetails.getAuthorities().contains(Role.ROLE_HOD)) {

				m.addAttribute(
						"allCourses",
						courseService.findByUserActive(username,
								userDetails.getProgramName()));
			}

			if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				String progName = userDetails.getProgramName();

				m.addAttribute("allCourses",
						courseService.findByCoursesBasedOnProgramName(progName));
			}
			Forum forum = forumService.findByID(id);
			m.addAttribute("forum", forum);

		} catch (Exception e) {

			logger.error("Error " + e.getMessage());
		}
		return "forum/updateForum";

	}

	@Secured({ "ROLE_STUDENT", "ROLE_FACULTY" })
	@RequestMapping(value = "/updateForum", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String updateForum(@ModelAttribute Forum forum, Model m,
			RedirectAttributes redirectAttrs, Principal principal) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		Token userDetails = (Token) principal;

		try {
			System.out.println("FORUM OBJ>>>>:"+forum);
			 if(forum.getCourseId() == null || forum.getCourseId().toString().isEmpty())
             {
             	throw new ValidationException("Invalid Course Selected.");
             }
			 BusinessBypassRule.validateAlphaNumeric(forum.getTopic());
			 
			if (forum != null) {
				Forum forumdb = forumService.findByID(forum.getId());
                forum.setCreatedBy(forumdb.getCreatedBy());
                forum.setCreatedDate(forumdb.getCreatedDate());
                forum.setLastModifiedBy(username);
				forumService.update(forum);
				setSuccess(redirectAttrs, "Forum updates successfully!");
			}
		}
		catch (ValidationException ve) {
			setError(redirectAttrs, ve.getMessage());
			logger.error(ve);
			return "redirect:/updateForumForm?id=" + forum.getId();

		}
		  catch (Exception e) {
			setError(redirectAttrs, "Error while updating forum!");
			logger.error(e);
			return "redirect:/updateForumForm?id=" + forum.getId();

		}
		return "redirect:/replyToQuestionForm?id=" + forum.getId();

	}

	@Secured({ "ROLE_STUDENT", "ROLE_FACULTY" })
	@RequestMapping(value = "/deleteForum", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String deleteForum(@RequestParam Long id, Model m,
			RedirectAttributes redirectAttrs, Principal principal) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		Token userDetails = (Token) principal;
		Forum forum = new Forum();
		
		try {
			if (id != null) {
				forum = forumService.findByID(id);

				forumService.deleteSoftById(String.valueOf(id));
				setSuccess(redirectAttrs, "Forum deleted successfully!");
			}
		} catch (Exception e) {
			setError(redirectAttrs, "Error while deleting forum!");
			logger.error(e);
			return "redirect:/replyToQuestionForm?id=" + id;

		}
		return "redirect:/viewForum?courseId=" + forum.getCourseId();

	}

}

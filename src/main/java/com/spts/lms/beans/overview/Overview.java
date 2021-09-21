package com.spts.lms.beans.overview;
 
import org.apache.commons.lang3.StringUtils;
 
import com.spts.lms.beans.BaseBean;
 
public class Overview extends BaseBean {
 
          /**
          * The persistent class for the program database table.
          *
           */
 
          private static final long serialVersionUID = 1L;
         
          public Long overviewid;
          public String city;
          public String persontocontact;
          public String emailid;
          public String number;
          public String dept;
          public Long getOverviewid() {
                   return overviewid;
          }
          public void setOverviewid(Long overviewid) {
                   this.overviewid = overviewid;
          }
          public String getCity() {
                   return city;
          }
          public void setCity(String city) {
                   this.city = city;
          }
          public String getPersontocontact() {
                   return persontocontact;
          }
          public void setPersontocontact(String persontocontact) {
                   this.persontocontact = persontocontact;
          }
          public String getEmailid() {
                   return emailid;
          }
          public void setEmailid(String emailid) {
                   this.emailid = emailid;
          }
          public String getNumber() {
                   return number;
          }
          public void setNumber(String number) {
                   this.number = number;
          }
          public String getDept() {
                   return dept;
          }
          public void setDept(String dept) {
                   this.dept = dept;
          }
          public static long getSerialversionuid() {
                   return serialVersionUID;
          }
          @Override
          public String toString() {
                   return "Overview [overviewid=" + overviewid + ", city=" + city
                                      + ", persontocontact=" + persontocontact + ", emailid="
                                      + emailid + ", number=" + number + ", dept=" + dept + "]";
          }
         
 
}
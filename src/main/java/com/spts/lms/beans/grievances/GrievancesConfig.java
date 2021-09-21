package com.spts.lms.beans.grievances;
 
import com.spts.lms.beans.BaseBean;
 
@SuppressWarnings("serial")
public class GrievancesConfig extends BaseBean{
          public String typeOfGrievance;
          public String grievanceCase;
          public String getTypeOfGrievance() {
                   return typeOfGrievance;
          }
          public void setTypeOfGrievance(String typeOfGrievance) {
                   this.typeOfGrievance = typeOfGrievance;
          }
          public String getGrievanceCase() {
                   return grievanceCase;
          }
          public void setGrievanceCase(String grievanceCase) {
                   this.grievanceCase = grievanceCase;
          }
          @Override
          public String toString() {
                   return "GrievancesConfig [typeOfGrievance=" + typeOfGrievance
                                      + ", grievanceCase=" + grievanceCase + "]";
          }
         
 
}
 
package com.spts.lms.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.spts.lms.beans.BaseBean;

public class LMSHelper {
	private static final Logger logger = Logger
			.getLogger(LMSHelper.class);
	public static <T extends BaseBean> T copyNonNullFields(T beanFromDb, T beanFromUI) {
		try { 
		BeanInfo beanInfo = Introspector.getBeanInfo(beanFromDb.getClass());
		    PropertyDescriptor[] pdList = beanInfo.getPropertyDescriptors();
		    for (PropertyDescriptor pd : pdList) {
		        Method writeMethod = null;
		        Method readMethod = null;
		        try {
		            writeMethod = pd.getWriteMethod();
		            readMethod = pd.getReadMethod();
		        } catch (Exception e) {
		        }

		        if (readMethod == null || writeMethod == null) {
		            continue;
		        }

		        Object val = readMethod.invoke(beanFromUI);
		        writeMethod.invoke(beanFromDb, val);
		    }
		} catch(IntrospectionException ite) {
			logger.error("Exception",ite);
		} catch (IllegalAccessException e) {
			logger.error("Exception",e);
		} catch (IllegalArgumentException e) {
			logger.error("Exception",e);
		} catch (InvocationTargetException e) {
			logger.error("Exception",e);
		}
		
		return beanFromDb;
	}

}

package com.spts.lms.daos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.UserLog;


public interface MongoDAO extends MongoRepository<UserLog, String>{
//	database: lmsmongo
//	port: 27017
	
}

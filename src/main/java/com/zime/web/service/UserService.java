package com.zime.web.service;

import com.zime.web.dao.entity.User;
import com.zime.web.util.ResponseEntity;

public interface UserService {

	ResponseEntity<User> login(String username, String password);

	ResponseEntity<String> register(User user);

	ResponseEntity<String> checkValid(String str, String type);

	ResponseEntity<String> selectQuestion(String username);

	ResponseEntity<String> checkAnswer(String username, String question, String answer);

	ResponseEntity<String> forgetResetPassword(String username, String newPassword, String forgetToken);

	ResponseEntity<String> resetPassword(String passwordOld, String passwordNew, User user);

	ResponseEntity<User> updateInformation(User user);

	ResponseEntity<User> getInformation(Integer id);

	ResponseEntity<String> checkAdminRole(User user);

}

package com.example.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

//import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@SpringBootTest
class InsertUserFormTest {

	@Autowired
	Validator validator;

	private InsertUserForm insertUserForm = new InsertUserForm();
	private BindingResult result = new BindException(insertUserForm, "insertUserForm");

//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterAll
//	static void tearDownAfterClass() throws Exception {
//	}

	@BeforeEach
	void setUp() throws Exception {
		insertUserForm.setLastName("テスト性");
		insertUserForm.setFirstName("テスト名");
		insertUserForm.setEmail("test@test.com");
		insertUserForm.setZipcode("123-1234");
		insertUserForm.setAddress("東京都港区");
		insertUserForm.setTelephone("080-1111-1111");
		insertUserForm.setPassword("12345678");
		insertUserForm.setConfirmationPassword("12345678");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSuccessForm() {
		validator.validate(insertUserForm, result);
		assertNull(result.getFieldError());
	}

	@Test
	void testAllNullForm() {
		insertUserForm.setLastName(null);
		insertUserForm.setFirstName(null);
		insertUserForm.setEmail(null);
		insertUserForm.setZipcode(null);
		insertUserForm.setAddress(null);
		insertUserForm.setTelephone(null);
		insertUserForm.setPassword(null);
		insertUserForm.setConfirmationPassword(null);
		validator.validate(insertUserForm, result);
		assertEquals("名前を入力して下さい",result.getFieldError().getDefaultMessage(), "値を入力してください");
	}

	@Test
	void testNameNull() {
		insertUserForm.setLastName("");
		insertUserForm.setFirstName("");
		validator.validate(insertUserForm, result);
		assertEquals("名前を入力して下さい", result.getFieldError().getDefaultMessage(), "氏名が入力されていません");
	}

	@Test
	void testEmailNull() {
		insertUserForm.setEmail(null);
		validator.validate(insertUserForm, result);
		assertEquals("メールアドレスを入力して下さい", result.getFieldError().getDefaultMessage(), "メールアドレスが入力されていません");
	}

	@Test
	void testZipcodeNull() {
		insertUserForm.setZipcode("");
		validator.validate(insertUserForm, result);
		assertEquals("郵便番号はXXX-XXXXの形式で入力してください", result.getFieldError().getDefaultMessage(), "郵便番号が入力されていません");
	}
}

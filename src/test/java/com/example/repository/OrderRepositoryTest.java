package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.example.domain.Order;

@SpringBootTest
class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterAll
//	static void tearDownAfterClass() throws Exception {
//	}

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("DB初期化処理開始");
		Order order = new Order();
		order.setUserId(10);
		order.setStatus(0);
		order.setTotalPrice(0);
		orderRepository.insert(order.getUserId(), order.getStatus());
		System.out.println("Insert処理が完了しました。");
		System.out.println("DB初期化処理終了");
	}

	@Test
	void testFindAll() {
		System.out.println("全件検索するテスト開始");
		String sql = "SELECT max(user_id) FROM orders;";
		Integer maxUserId = template.queryForObject(sql, new MapSqlParameterSource(),
				Integer.class);
		Order resultOrder = orderRepository.load(maxUserId, 0);
		assertEquals(10, resultOrder.getUserId(), "ユーザーIDが登録されていません");
		assertEquals(0, resultOrder.getStatus(),"ステータス情報が登録されていません");
		assertEquals(0, resultOrder.getTotalPrice(), "合計金額が登録されていません");
		
		System.out.println("全件検索するテスト終了");
	}
	
	@Test
	public void testUpdate() {
		System.out.println("対象情報を更新するテスト開始");
		StringBuilder updateSql = new StringBuilder();
		updateSql.append("UPDATE orders SET status = :status, total_price = :totalPrice, ");
		updateSql.append("order_date=:orderDate, destination_name=:destinationName, destination_email=:destinationEmail, ");
		updateSql.append("destination_zipcode=:destinationZipcode,destination_address=:destinationAddress,destination_tel=:destinationTel, ");
		updateSql.append("delivery_time=:deliveryTime ,payment_method=:paymentMethod WHERE user_id=:userId;");
		String update = updateSql.toString();
		
		// 更新後の注文情報を準備		
		Order order = new Order();
		order.setUserId(10);
		order.setStatus(1);
		order.setTotalPrice(1000);
		Date date = new Date();
		date.setYear(2023);
		date.setMonth(3);
		date.setDate(9);
		order.setOrderDate(date);
		order.setDestinationName("テスト");
		order.setDestinationEmail("test@sample.com");
		order.setDestinationZipcode("999-999");
		order.setDestinationAddress("東京都新宿区9丁目99番地9号");
		order.setDestinationTel("080-9999-9999");
		
		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime = localDateTime.plusHours(3);
		Timestamp timestamp = Timestamp.valueOf(localDateTime);
		
		order.setDeliveryTime(timestamp);
		order.setPaymentMethod(0);
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		template.update(update, param);
		
		Order resultAfterUpdateOrder = orderRepository.load(10, 1);
		
		assertEquals(10, resultAfterUpdateOrder.getUserId(), "ユーザーIDが登録されていません");
		assertEquals(1, resultAfterUpdateOrder.getStatus(),"ステータス情報が登録されていません");
		assertEquals(1000, resultAfterUpdateOrder.getTotalPrice(), "合計金額が登録されていません");
	}
	
	@AfterEach
	void tearDown() throws Exception {
		MapSqlParameterSource param = new MapSqlParameterSource().addValue("userId", 10);
		String deleteSql = "DELETE FROM orders WHERE user_id = :userId;";
		template.update(deleteSql, param);
		System.out.println("挿入したデータを削除しました。");
	}

}

package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.domain.Order;
import com.example.form.ShoppingCartForm;
import com.example.repository.OrderRepository;

@SpringBootTest
class ShoppingCartServiceTest {

	@Autowired
	private ShoppingCartService shoppingCartService;
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
		order.setUserId(90);
		order.setStatus(0);
		order.setTotalPrice(0);
		orderRepository.insert(order.getUserId(), order.getStatus());
		System.out.println("Insert処理が完了しました。");
		System.out.println("DB初期化処理終了");
	}

	@AfterEach
	void tearDown() throws Exception {
		MapSqlParameterSource param = new MapSqlParameterSource().addValue("userId", 90);
		String deleteSql = "DELETE FROM orders WHERE user_id = :userId;";
		template.update(deleteSql, param);
		System.out.println("挿入したデータを削除しました。");
	}

	@Test
	void testLoad() {
		Order order = new Order();
		order = shoppingCartService.load(90);
		assertEquals(90, order.getUserId(), "ユーザーIDが登録されていません");
		assertEquals(0, order.getStatus(), "ステータス情報が登録されていません");
	}

	@Test
	void testInsert() {
		System.out.println("インサート処理のテスト開始");
		ShoppingCartForm shoppingCartForm = new ShoppingCartForm();
		shoppingCartForm.setItemId(1);
		shoppingCartForm.setQuantity(5);
		shoppingCartForm.setSize('M');
		
		List<Integer> toppingList = new ArrayList<>();
		toppingList.add(0, 1);
		toppingList.add(1, 5);
		toppingList.add(2, 7);
		
		shoppingCartForm.setToppingIdList(toppingList);

		shoppingCartService.insert(shoppingCartForm, 90);
		Order order = shoppingCartService.load(90);
		
		assertEquals(1, order.getOrderItemList().get(0).getItemId(), "商品IDが登録されていません");
		assertEquals(5, order.getOrderItemList().get(0).getQuantity(), "量が登録されていません");
		assertEquals('M', order.getOrderItemList().get(0).getSize(), "サイズが登録されていません");
		assertEquals(1, order.getOrderItemList().get(0).getOrderToppingList().get(0).getToppingId(), "注文トッピングにIDが登録されていません");
		assertEquals(7, order.getOrderItemList().get(0).getOrderToppingList().get(2).getToppingId(), "注文トッピングにIDが登録されていません");

		System.out.println("インサート処理のテスト終了");
	}

}

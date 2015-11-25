package com.epam.preprod.tymchenko.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.epam.preprod.tymchenko.db.JdbcHolder;
import com.epam.preprod.tymchenko.exception.DataException;
import com.epam.preprod.tymchenko.service.bean.OrderInfoBean;
import com.epam.preprod.tymchenko.service.bean.ProductBean;
import com.epam.preprod.tymchenko.util.ClassNameUtil;

public class MySqlOrderItemDAO implements OrderItemDAO {

	private static final Logger LOG = Logger.getLogger(ClassNameUtil.getCurrentClassName());

	private static final String INSERT_NEW_ORDER_ITEM = "INSERT INTO orderItems VALUES(DEFAULT, ?, ?, ?, ?);";

	@Override
	public void add(OrderInfoBean bean, long orderId) throws DataException {
		LOG.info("MySqlOrderItemDAO.add(OrderInfoBean bean, long orderId)");
		Connection connection = JdbcHolder.getConnection();

		for (Map.Entry<ProductBean, Integer> entry : bean.getMap().entrySet()) {
			try (PreparedStatement st = connection.prepareStatement(INSERT_NEW_ORDER_ITEM)) {
				st.setLong(1, orderId);
				st.setLong(2, entry.getKey().getId());
				st.setLong(3, entry.getValue());
				st.setBigDecimal(4, entry.getKey().getPrice());
				st.executeUpdate();

				connection.commit();
			} catch (SQLException e) {
				LOG.error(e.getMessage());
				throw new DataException(e.getMessage());
			}
		}
	}
}

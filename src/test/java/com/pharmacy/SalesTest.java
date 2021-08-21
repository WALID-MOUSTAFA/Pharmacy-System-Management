package com.pharmacy;


import com.pharmacy.POGO.*;
import com.pharmacy.services.*;
import org.junit.Test;
import  org.junit.Assert;

import java.sql.SQLException;
import java.sql.Timestamp;

public class SalesTest {

	public void testInsertCustomer() throws SQLException {
		CustomerService customerService= new CustomerService();
		Customer customer= new Customer();
		customer.setName("walid");
		customer.setAddress("saqulta");
		customer.setCash(0.0);
		customer.setDateAt(new Timestamp(System.currentTimeMillis()).toString());
	}


	public void testInsertSale() throws SQLException{
		SalesService ss= new SalesService();
		Sale sale= new Sale();
		sale.setName("this is the name");
		sale.setCustomerId(3);
		sale.setTotal(40);
		sale.setNetTotal(sale.getTotal() - sale.getDiscount());
		//Assert.assertTrue(ss.insertSale(sale));
	}

	public void testInsertSaleDetails() throws SQLException{
		SaleDetails sd= new SaleDetails();
		SalesDetailsService ssd= new SalesDetailsService();
		sd.setSaleId(1);

	}

	public void testSearch() throws SQLException{
		BalanceService bs= new BalanceService();
		Assert.assertNull(bs.searchBalanceForTreatments("fdslplklp", "اقراص"));
	}
}

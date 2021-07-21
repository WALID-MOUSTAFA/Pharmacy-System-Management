package com.pharmacy;


import com.pharmacy.POGO.Purchase;
import com.pharmacy.POGO.Supplier;
import com.pharmacy.services.PurchasesService;
import com.pharmacy.services.SuppliersService;
import org.junit.Test;
import  org.junit.Assert;

import java.sql.SQLException;
import java.sql.Timestamp;

public class PurchaseTest {

	@Test
	public void testGetAllPurchases() throws SQLException {
		PurchasesService ps= new PurchasesService();
		Assert.assertEquals(ps.getAllPurchases().size(), 1);
	}
	
	@Test
	public void insertTest() throws SQLException {
		PurchasesService ps= new PurchasesService();
		SuppliersService ss= new SuppliersService();
		Purchase pur= new Purchase();
		Supplier sup= ss.getSupplierByName("شمنبا");
		
		pur.setDatePur((new Timestamp(System.currentTimeMillis())).toString());
		pur.setPillNum("6748632878");
		pur.setTotalPeople(32);
		pur.setTotalPharmacy(30);
		pur.setCountUnit(49);
		pur.setDescription("this is the perfect description");
		pur.setSupplier(sup);

		Assert.assertTrue(ps.insertPurchase(pur));
	     
	}
		
}

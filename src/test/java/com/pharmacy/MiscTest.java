package com.pharmacy;


import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.Treatment;
import com.pharmacy.services.TreatmentService;
import org.hamcrest.core.Is;
import org.junit.Test;
import  org.junit.Assert;

import java.sql.SQLException;
import java.util.List;

public class MiscTest {
    public void TestSelectTreatments() throws SQLException {
        TreatmentService tr= new TreatmentService();
        List<Treatment> l = tr.getAllTreatments();
        Assert.assertTrue(l.get(0) instanceof Treatment);
    }

    public void testInsertTreatment() throws SQLException {
        DetailedTreatment dt= new DetailedTreatment();
        dt.setName("كيتوفان");
        dt.setParcode("45678");
        dt.setTypeTreatName("شراب");
        TreatmentService ts= new TreatmentService();
        ts.insertTreatment(dt);
    }


    public void testGetTypeTreatByTypename() throws SQLException {
        TreatmentService ts= new TreatmentService();
        Assert.assertNotNull(ts.getTypeTreatByTypename("اقراص"));
    }


}

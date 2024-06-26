package com.pharmacy.controllers;

import com.pharmacy.POGO.BillItemModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

public class PrintInventory extends JFrame {

    //TOTO(walid): implement the code to open jasper report.
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void showReport(List<?> items) throws JRException, ClassNotFoundException, URISyntaxException {

        InputStream reportSrcFile = getClass().getResourceAsStream("/inventory.jrxml");
        //File file = Paths.get(resourceFile.toURI()).toFile();
        //String reportSrcFile = file.getAbsolutePath();


        // First, compile jrxml file.
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        // Fields for report
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        //parameters.put("title", "صيدلية المساعيد القبلية");
       // parameters.put("client", clientName);
        //parameters.put("total", total);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(items);
        parameters.put("items", beanColDataSource);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(700, 500);
        this.setVisible(true);
        System.out.print("Done!");

    }
}

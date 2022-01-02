package com.pharmacy.controllers;

import com.pharmacy.POGO.BillItemModel;
import com.pharmacy.services.SettingsService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import net.sf.jasperreports.swing.JRViewer;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PrintReport extends JFrame {

    private static final long serialVersionUID = 1L;

    private SettingsService settingsService = new SettingsService();

    public PrintReport() throws SQLException {
    }

    public void showReport(List<BillItemModel> items, String clientName, double total) throws JRException, ClassNotFoundException, URISyntaxException, SQLException {

        InputStream reportSrcFile = getClass().getResourceAsStream("/fatura.jrxml");
        //File file = Paths.get(resourceFile.toURI()).toFile();
        //String reportSrcFile = file.getAbsolutePath();


        // First, compile jrxml file.
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        // Fields for report
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("title", "صيدلية المساعيد القبلية");
       // parameters.put("client", clientName);
        parameters.put("total", total);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(items);
        parameters.put("items", beanColDataSource);

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        String printerName = this.settingsService.getSetting("reciptPrinterName");
        PrintService printService = this.getPrintService(printerName);

        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add(new Copies(1));
        PrinterName printerNamee = new PrinterName(printerName, null); //gets printer
        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        printServiceAttributeSet.add(printerNamee);

        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
        configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
        configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
        configuration.setDisplayPageDialog(false);
        configuration.setDisplayPrintDialog(false);
        exporter.setConfiguration(configuration);

       new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    exporter.exportReport();
                } catch (JRException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private PrintService getPrintService(String printerName) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService selectedService = null;

        for (PrintService ps : services) {
            if (ps.getName().equals(printerName)) {
                selectedService = ps;
                break;
            }
        }
        return selectedService;
    }



	public void printBarCode(String barcodeNumber, String treatName, String expire,double price) throws JRException {

        InputStream reportSrcFile = getClass().getResourceAsStream("/barcode.jrxml");
        //File file = Paths.get(resourceFile.toURI()).toFile();
        //String reportSrcFile = file.getAbsolutePath();


        // First, compile jrxml file.
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        // Fields for report
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("title", "ص/ د شعبان");
        // parameters.put("client", clientName);
        parameters.put("phone", "012012");
	parameters.put("treatName", treatName);
	parameters.put("price", price);
	parameters.put("expire", expire);
        parameters.put("barcodeNumber", barcodeNumber);

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());


        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        this.add(viewer);
        this.setSize(700, 500);
        this.setVisible(true);
    }
}

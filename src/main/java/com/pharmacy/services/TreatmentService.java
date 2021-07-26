package com.pharmacy.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.Treatment;
import com.pharmacy.POGO.TypeTreat;

public class TreatmentService {

    private Connection dbConnection;

    public List<Treatment> getAllTreatments() throws SQLException {
        List<Treatment> treatments= new ArrayList<>();
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "SELECT * FROM treat";
        Statement stmt= this.dbConnection.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        Treatment treatment;

        while(rs.next()) {
            treatment= new Treatment();
            treatment.setId(rs.getLong("id"));
            treatment.setName(rs.getString("name"));
            treatment.setTypet(rs.getLong("typet"));
            treatment.setStatus(rs.getInt("status"));
            //Note(walid): it's inherited typo in the original database;
            treatment.setParcode(rs.getString("parcpde"));
            treatment.setDateAt(rs.getString("date_at"));
            treatment.setLowcount(rs.getInt("lowcount"));
            treatment.setCompany(rs.getString("company"));
            treatment.setFormtreat(rs.getLong("formtreat"));
            treatment.setPlace(rs.getString("place"));
            treatments.add(treatment);
            treatment= null;
        }
        return treatments;
    }

    public DetailedTreatment getDetailedTreatment(long id) throws SQLException{
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "select treat.*, typename, formtreat.name as formtreatname\n" +
                "from treat\n" +
                "JOIN\n" +
                "typetreat\n" +
                "join formtreat\n" +
                "on\n" +
                "typet=typetreat.id and treat.formtreat=formtreat.id where treat.id=" + id+ "; ";
        Statement stmt= this.dbConnection.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        DetailedTreatment detailedTreatment= new DetailedTreatment();
        while(rs.next()) {
            detailedTreatment.setId(rs.getLong("id"));
            detailedTreatment.setName(rs.getString("name"));
            detailedTreatment.setTypet(rs.getLong("typet"));
            detailedTreatment.setStatus(rs.getInt("status"));
            //Note(walid): it's inherited typo in the original database;
            detailedTreatment.setParcode(rs.getString("parcpde"));
            detailedTreatment.setDateAt(rs.getString("date_at"));
            detailedTreatment.setLowcount(rs.getInt("lowcount"));
            detailedTreatment.setCompany(rs.getString("company"));
            detailedTreatment.setFormtreat(rs.getLong("formtreat"));
            detailedTreatment.setPlace(rs.getString("place"));
            detailedTreatment.setTypeTreatName(rs.getString("formtreatname"));
            detailedTreatment.setTypeTreatName(rs.getString("typename"));
        }
        return detailedTreatment;
    }


    public List<TypeTreat> getAllTreatTypes () throws  SQLException{
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "SELECT * FROM typetreat";
        Statement stmt= this.dbConnection.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        List<TypeTreat> types= new ArrayList<>();
        while (rs.next()) {
            types.add(new TypeTreat(rs.getString("typename")));
        }
        return types;
    }


    public TypeTreat getTypeTreatByTypename(String typename) throws SQLException{
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "SELECT * FROM typetreat where typename=\"" + typename + "\";";
        Statement stmt= this.dbConnection.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        TypeTreat typeTreat= new TypeTreat();
        while(rs.next()) {
            typeTreat.setTypename(rs.getString("typename"));
            typeTreat.setId(rs.getLong("id"));
        }
        System.out.println(stmt);
        return typeTreat;
    }

    public boolean insertTreatment(DetailedTreatment dt) throws SQLException {
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        TypeTreat typeTreat= this.getTypeTreatByTypename(dt.getTypeTreatName());
        long typet = 0;
        if(typeTreat != null) {
            typet= typeTreat.getId();
        }
        String query= "insert into treat (name, typet, status, parcpde, date_at, lowcount, company, place, formtreat) " +
                "VALUES (?,?,?,?,?,?,?,?, ?)";
        PreparedStatement preparedStatement= this.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, dt.getName());
        if(typet != 0) {
            preparedStatement.setLong(2, typet);
        }
        //TODO(walid): get the value from the ui;
        preparedStatement.setString(3, "1");
        preparedStatement.setString(4, dt.getParcode());
        preparedStatement.setString(5, new Timestamp(System.currentTimeMillis()).toString() );
        //TODO(walid): get the value from the ui;
        preparedStatement.setString(6, "4");
        //TODO(walid): get the value from the ui;
        preparedStatement.setString(7, "egypharma");
        //TODO(walid): get the value from the ui;
        preparedStatement.setString(8, "الرف العلوي على الشمال");
        //TODO(walid): get the value from the ui;
        preparedStatement.setString(9, "1");

        return preparedStatement.execute();
    }


	//TODO(walid): handle null values;
	public Treatment getTreatmentByName(String treatName) throws SQLException{
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        Treatment treatment= new Treatment();
		String query= "SELECT * FROM treat WHERE name="+"\"" + treatName + "\";" ;
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		while(rs.next()) {
			treatment.setName(rs.getString("name"));
			treatment.setId(rs.getLong("id"));

		}
		return treatment;
	}
}

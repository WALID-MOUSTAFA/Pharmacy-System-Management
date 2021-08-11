package com.pharmacy.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.TreatForm;
import com.pharmacy.POGO.Treatment;
import com.pharmacy.POGO.TypeTreat;

public class TreatmentService {

    private Connection dbConnection;

    public List<Treatment> getAllTreatments() throws SQLException {
        List<Treatment> treatments= new ArrayList<>();
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "SELECT treat.*, SUM(blance_treat.quantity) AS quantity FROM treat  LEFT OUTER JOIN blance_treat ON blance_treat.treat_id=treat.id GROUP BY treat.id;";
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
	         treatment.setQuantity(rs.getDouble("quantity"));
            treatments.add(treatment);
            treatment= null;
        }
        return treatments;
    }

    public DetailedTreatment getDetailedTreatmentById(long id) throws SQLException{
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
            detailedTreatment.setFormTreatName(rs.getString("formtreatname"));
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


    
    public TreatForm getTreatFormByName(String formName) throws SQLException{
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "SELECT * FROM formtreat where name=\"" + formName + "\";";
        Statement stmt= this.dbConnection.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        TreatForm treatForm= new TreatForm();
        while(rs.next()) {
            treatForm.setName(rs.getString("name"));
            treatForm.setId(rs.getLong("id"));
        }
        return treatForm;
    }

    public boolean insertTreatment(DetailedTreatment dt) throws SQLException {
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        TypeTreat typeTreat= this.getTypeTreatByTypename(dt.getTypeTreatName());
	TreatForm treatForm= this.getTreatFormByName(dt.getFormTreatName());
	
	long typet = 0;
	long formTreat_id=0;
        if(typeTreat != null) {
            typet= typeTreat.getId();
        }
	if(typeTreat != null) {
	    formTreat_id= treatForm.getId();
        }
	
        String query= "insert into treat (name, typet, status, parcpde, date_at, lowcount, company, place, formtreat) " +
                "VALUES (?,?,?,?,?,?,?,?, ?)";
        PreparedStatement preparedStatement=
	    this.dbConnection.prepareStatement(query);

	preparedStatement.setString(1, dt.getName());
        if(typet != 0) {
            preparedStatement.setLong(2, typet);
        }
        //TODO(walid): get the value from the ui;
        preparedStatement.setString(3, "1");
        preparedStatement.setString(4, dt.getParcode());
        preparedStatement.setString
	    (5, new Timestamp(System.currentTimeMillis()).toString() );
        //TODO(walid): get the value from the ui;
        preparedStatement.setDouble(6, dt.getLowcount());
        //TODO(walid): get the value from the ui;
        preparedStatement.setString(7, dt.getCompany());
        //TODO(walid): get the value from the ui;
        preparedStatement.setString(8, dt.getPlace());
        //TODO(walid): get the value from the ui;
        preparedStatement.setLong(9, formTreat_id);

        return preparedStatement.execute();
    }



	public boolean updateTreatmentById(DetailedTreatment dt) throws SQLException {
			
		String query= "UPDATE treat set "
			+"name=?,"
			+"typet=?,"
			+"parcpde=?,"
			+"date_at=?,"
			+"lowcount=?,"
			+"company=?,"
			+"place=?,"
			+"formtreat=? "
			+"WHERE id=" + dt.getId()+ ";";

		System.out.println(query);
		PreparedStatement preparedStatement=
				this.dbConnection.prepareStatement(query);

		preparedStatement.setString(1,dt.getName());
		preparedStatement.setLong(2,dt.getTypet()); 
		preparedStatement.setString(3,dt.getParcode()); 
		preparedStatement.setString(4,dt.getDateAt()); 
		preparedStatement.setDouble(5,dt.getLowcount()); 
		preparedStatement.setString(6,dt.getCompany()); 
		preparedStatement.setString(7,dt.getPlace()); 
		preparedStatement.setLong(8,dt.getFormtreat()); 

		if(preparedStatement.executeUpdate() > 0) {
				return true;
		}

		return false;
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


	public List<TreatForm> getAllTreatForms() throws SQLException{
		this.dbConnection=DatabaseConnection.getInstance().getConnection();
		List<TreatForm> forms= new ArrayList<>();
		String query= "SELECT * FROM formtreat";
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		TreatForm treatForm;
		
		if (!rs.isBeforeFirst()) {
			return null;
		}

		while(rs.next()){
			treatForm= new TreatForm();
			treatForm.setName(rs.getString("name"));
			forms.add(treatForm);
			treatForm= null;
		}
		return forms;
	}

    public List<DetailedTreatment> getAllTreatmentsByNameAndType(String treatName, String typeName)
            throws SQLException{

        this.dbConnection= DatabaseConnection.getInstance().getConnection();

        List<DetailedTreatment> treatments= new ArrayList<>();
        String query= "select treat.*, typename, formtreat.name as formtreatname\n" +
                "from treat\n" +
                "JOIN\n" +
                "typetreat\n" +
                "join formtreat\n" +
                "on\n" +
                "typet=typetreat.id and treat.formtreat=formtreat.id where treat.name like '%" + treatName+ "%'" +" and typetreat.typename=\"" +typeName+ "\";";

        Statement stmt= this.dbConnection.createStatement();
        ResultSet rs= stmt.executeQuery(query);

        if(!rs.isBeforeFirst()) {
            return null;
        }

        DetailedTreatment detailedTreatment;
        while(rs.next()) {
            detailedTreatment= new DetailedTreatment();
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
            detailedTreatment.setFormTreatName(rs.getString("formtreatname"));
            detailedTreatment.setTypeTreatName(rs.getString("typename"));
            treatments.add(detailedTreatment);
            detailedTreatment= null;
        }


        return treatments;
    }
}

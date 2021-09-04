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

    public List<DetailedTreatment> getAllTreatments() throws SQLException {
        List<DetailedTreatment> treatments= new ArrayList<>();
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "SELECT treat.*, typetreat.typename as typeName, SUM(blance_treat.quantity) AS quantity FROM treat  LEFT OUTER JOIN blance_treat ON blance_treat.treat_id=treat.id inner join typetreat on typetreat.id = treat.typet GROUP BY treat.id;";
        Statement stmt= this.dbConnection.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        DetailedTreatment treatment;

        while(rs.next()) {
            treatment= new DetailedTreatment();
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
	    treatment.setTypeTreatName(rs.getString("typeName"));
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

    public long insertTreatment(DetailedTreatment dt) throws SQLException {
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
        //NOTE(walid): get the value from the ui, or not;
        preparedStatement.setString(3, "1");
        preparedStatement.setString(4, dt.getParcode());
        preparedStatement.setString
	    (5, dt.getDateAt());
        preparedStatement.setDouble(6, dt.getLowcount());
        preparedStatement.setString(7, dt.getCompany());
        preparedStatement.setString(8, dt.getPlace());
        preparedStatement.setLong(9, formTreat_id);

        if(preparedStatement.executeUpdate() >0) {
            ResultSet keys= preparedStatement.getGeneratedKeys();
            if(keys.next()) {
                return keys.getLong(1);
            }
        }

        return 0;
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

	public long getTypeIdFromName(String name) throws SQLException{
        String query= "SELECT * from typetreat where typename=\""+name+"\" limit 1";
        ResultSet rs= this.dbConnection.createStatement().executeQuery(query);
        if(!rs.isBeforeFirst()) {
            return 0;
        }
        TypeTreat typeTreat= new TypeTreat();
        while(rs.next()) {
            typeTreat.setId(rs.getLong("id"));
        }
        return typeTreat.getId();
    }


	public DetailedTreatment getTreatmentByName(String treatName, String typeName) throws SQLException{
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        long treatTypeId= getTypeIdFromName(typeName);
        DetailedTreatment treatment= new DetailedTreatment();
		String query= "SELECT * FROM treat WHERE name="+"\"" + treatName + "\" AND typet="+treatTypeId+";" ;
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		while(rs.next()) {
			treatment.setName(rs.getString("name"));
			treatment.setId(rs.getLong("id"));

		}
		return treatment;
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

        public List<DetailedTreatment> getAllTreatmentsByParcode(String parcode)
            throws SQLException{

        this.dbConnection= DatabaseConnection.getInstance().getConnection();

        List<DetailedTreatment> treatments= new ArrayList<>();
        String query= "select treat.*, typename, formtreat.name as formtreatname\n" +
                "from treat\n" +
                "JOIN\n" +
                "typetreat\n" +
                "join formtreat\n" +
                "on\n" +
                "typet=typetreat.id and treat.formtreat=formtreat.id where treat.parcpde="+parcode+";";

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



	public boolean deleteTreatment(long id) throws SQLException {
		this.dbConnection= DatabaseConnection.getInstance().getConnection();
		String query= "DELETE FROM treat WHERE id=" + id + ";";
		Statement stmt= this.dbConnection.createStatement();
		if(stmt.executeUpdate(query) > 0) {
			return true;
		}
		return false;
	}


    public List<TreatForm> getAllTreatForms() throws SQLException{
        this.dbConnection=DatabaseConnection.getInstance().getConnection();
        List<TreatForm> forms= new ArrayList<>();
        String query= "SELECT * FROM formtreat";
        Statement stmt= this.dbConnection.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        TreatForm treatForm;


        while(rs.next()){
            treatForm= new TreatForm();
            treatForm.setName(rs.getString("name"));
            treatForm.setId(rs.getLong("id"));
            forms.add(treatForm);
            treatForm= null;
        }
        return forms;
    }


    public List<TypeTreat> getAllTreatTypes () throws  SQLException{
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "SELECT * FROM typetreat";
        Statement stmt= this.dbConnection.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        List<TypeTreat> types= new ArrayList<>();
        while (rs.next()) {

            types.add(new TypeTreat(rs.getLong("id"), rs.getString("typename")));
        }
        return types;
    }


    public long insertTreatType(TypeTreat typeTreat) throws SQLException{
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "INSERT INTO typetreat (typename, date_at) VALUES (?,?)";
        PreparedStatement preparedStatement= this.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, typeTreat.getTypename());
        preparedStatement.setString(2, typeTreat.getDateAt());
        if(preparedStatement.executeUpdate() > 0 ) {
            ResultSet keys= preparedStatement.getGeneratedKeys();
            if(keys.next()) {
                return keys.getLong(1);
            }
        }
        return 0;
    }

    public boolean updateTreatType(TypeTreat typeTreat) throws SQLException {
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "UPDATE typetreat SET typename=? WHERE id=" + typeTreat.getId() + ";";
        PreparedStatement preparedStatement= this.dbConnection.prepareStatement(query);
        preparedStatement.setString(1,typeTreat.getTypename());
        if(preparedStatement.executeUpdate() > 0 ){
            return true;
        }
        return false;
    }


    public boolean deleteTreatType(long id) throws SQLException {
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "DELETE FROM typetreat WHERE id=" + id + ";";
        Statement stmt= this.dbConnection.createStatement();
        if(stmt.executeUpdate(query) > 0){
            return true;
        }
        return false;
    }

    public long insertTreatForm(TreatForm treatForm) throws SQLException {
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query="INSERT INTO formtreat (name) VALUES (?)";
        PreparedStatement preparedStatement= this.dbConnection.prepareStatement(query);
        preparedStatement.setString(1,treatForm.getName());
        if(preparedStatement.executeUpdate()> 0) {
            ResultSet keys= preparedStatement.getGeneratedKeys();
            if(keys.next()) {
                return keys.getLong(1);
            }
        }
        return 0;
    }


    public boolean updateTreatForm(TreatForm treatForm) throws SQLException {
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query="UPDATE formtreat set name=? WHERE id=" + treatForm.getId() + ";";
        PreparedStatement preparedStatement= this.dbConnection.prepareStatement(query);
        preparedStatement.setString(1,treatForm.getName());
        if(preparedStatement.executeUpdate()> 0) {
           return true;
        }
        return false;
    }


    public boolean deleteTreatForm(long id) throws SQLException {
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "DELETE FROM formtreat WHERE id=" + id + ";";
        Statement stmt= this.dbConnection.createStatement();
        if(stmt.executeUpdate(query) > 0){
            return true;
        }
        return false;
    }

    public List<DetailedTreatment> getAltTreatments(DetailedTreatment dt)
    throws  SQLException{
        List<DetailedTreatment> treatments= new ArrayList<>();
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String query= "SELECT treat.*, typetreat.typename as typeName, SUM(blance_treat.quantity) AS quantity FROM treat  LEFT OUTER JOIN blance_treat ON blance_treat.treat_id=treat.id inner join typetreat on typetreat.id = treat.typet where treat.formtreat="+dt.getFormtreat()+" GROUP BY treat.id ;";
        Statement stmt= this.dbConnection.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        DetailedTreatment treatment;

        while(rs.next()) {
            treatment= new DetailedTreatment();
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
            treatment.setTypeTreatName(rs.getString("typeName"));
            treatments.add(treatment);
            treatment= null;
        }
        return treatments;

    }
}

package com.example.taxometr.Controller;

import java.util.ArrayList;
import java.util.Iterator;

import com.example.taxometr.R;
import com.example.taxometr.R.string;
import com.example.taxometr.Adapter.varsForAdapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbController extends SQLiteOpenHelper implements Parcelable {
	String query;//��� sql ��������
	public String tag;//��� ���������� ������, �� ������ ���������� �� ������
	Cursor c=null;//������ ��� ������ ����������, ���������� �� ��
	static Context cnt;//��������, ��������� ��� ������ �����������
	SQLiteDatabase database;//�������� ���������, ����������� �������� � ��
	private int userId, result;//userId - ���������� ��� id ������������, ������� �������� � ����������
	//result - ������� ��� �������� ���������� ������� � ����
	public int tempId = -1;//��������� ��� �������� ������������� id (id ������, �������� � ������)
	

	public dbController(Context applicationcontext) {
		super(applicationcontext, "driversBase.db", null, 1);
		cnt = applicationcontext;
		tag = cnt.getResources().getString(R.string.tag);
		database = this.getWritableDatabase();
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {//�������� ���� ������ ��� ������ ������� ���������
		query = "DROP TABLE IF EXISTS driver;"
				+"DROP TABLE IF EXISTS server;"
				+"DROP TABLE IF EXISTS mode;"
				+"DROP TABLE IF EXISTS polygon;"
				+"DROP TABLE IF EXISTS tarif;";
		database.execSQL(query);
		query = "CREATE TABLE driver ( id INTEGER PRIMARY KEY, fName varchar(20), sName varchar(20), tName varchar(20),"
				+"password varchar(20), licence varchar(20), spd varchar(20),"
				+"serialCar varchar(20), cityWork varchar(20), nameIDE varchar(20),"
				+"cityIDE varchar(20), streetIDE varchar(20), houseIDE varchar(20),"
				+"roomIDE varchar(20), okpo varchar(20) );";
		database.execSQL(query);
		query = "create table server(id INTEGER PRIMARY KEY,"
				+"userId integer, nameServer varchar(20)," 
				+"ipMain varchar(20), portMain varchar(20),"
				+"ipSafe varchar(20), portSafe varchar(20),"
				+"ipGPS varchar(20), portGPS varchar(20), workServer boolean);";
		database.execSQL(query);
		query = "create table mode(id integer primary key,"
				+"userId integer, name varchar(255), val varchar(255), work boolean);";
		database.execSQL(query);
		query = "create table polygon(id integer primary key,"
				+"userId integer, name varchar(255), address varchar(255));";
		database.execSQL(query);
		query = "create table tarif (id integer primary key,"
				+"userId integer, sitdown varchar(20), minCost varchar(20), kmCost varchar(20),"
				+"minuteCostStay varchar(20), minKmCost varchar(20), minuteMinCost varchar(20),"
				+"countryCost varchar(20), countryBackCost varchar(20), minSpeed varchar(20));";
		database.execSQL(query);
		
		String[] driverSql = {
				"insert into driver (id, serialCar, password) values (1, 1234, 1234);",
				"insert into tarif (userId, sitdown, minCost, kmCost, minuteCostStay, minKmCost, minuteMinCost, countryCost, countryBackCost, minSpeed) values "
						+"(1, '100', '100', '100', '100', '100', '100', '100', '100', '100');",
						"insert into mode (userId, name, val, work) values (1, '��������� �� ����� ������������ � �������', 'connectToServer', 'true');",
						"insert into mode (userId, name, val, work) values (1, '���� ����� ����������� ��������� �������', 'manualSetting', 'true');",
						"insert into mode (userId, name, val, work) values (1, '���������� �������', 'showMoney', 'true');"
		};
		for(String str : driverSql) database.execSQL(str);//��������� ���� �������
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {//��� ��������� ������ ���� ������
		query = "DROP TABLE IF EXISTS driver;"
				+"DROP TABLE IF EXISTS server;"
				+"DROP TABLE IF EXISTS mode;"
				+"DROP TABLE IF EXISTS polygon;"
				+"DROP TABLE IF EXISTS tarif;";
		database.execSQL(query);
		onCreate(database);
	}

	public int countDrivers(){//���������� ���-�� ������������� � ��
		
		query = "select * from driver";
		c = database.rawQuery(query, null);
		result = c.getCount();
		c.close();
		
		if(result == 1) userId = 1;
		
		
		return result;
	}
	
	public int checkLogin(String login, String pass){ //�������� �����������
		
		query = "select id from driver where serialCar = "+login+" and password = "+pass+";";
		c = database.rawQuery(query, null);
		
		if(c.moveToFirst()) {
			userId = c.getInt(c.getColumnIndex("id"));
			return userId;
		}
		else userId = 0;
		c.close();
		
		return userId;
	}
	
	public void test(){//������ ����������
		/*String query = "insert into server(userId, nameServer, ipMain, portMain, ipSafe, portSafe, ipGPS, portGPS, workServer)"
				+ "values (1, 'ema1', '127.0.0.1', '5555', '127.0.0.1', '5555', '127.0.0.1', '5555', 'true');";
		database.execSQL(query);

		query = "insert into server(userId, nameServer, ipMain, portMain, ipSafe, portSafe, ipGPS, portGPS, workServer)"
				+ "values (1, 'ema2', '127.0.0.2', '5555', '127.0.0.2', '5555', '127.0.0.2', '5555', 'false');";
		database.execSQL(query);
		
		query = "insert into mode (userId, name, val, work) values (1, '��������� �� ����� ������������ � �������', 'connectToServer', 'true');";
		database.execSQL(query);
		
		query = "insert into mode (userId, name, val, work) values (1, '���� ����� ����������� ��������� �������', 'manualSetting', 'true');";
		database.execSQL(query);
		
		query = "insert into mode (userId, name, val, work) values (1, ' ���������� �������', 'showMoney', 'true');";
		database.execSQL(query);
		
		query = "insert into polygon (userId, name, address) values (1, 'ema1', '12.13.15,14.15.18');";
		database.execSQL(query);
		
		query = "insert into polygon (userId, name, address) values (1, 'ema2', '12.13.15,14.15.18');";
		database.execSQL(query);*/
		
		query = "insert into tarif (userId, sitdown, minCost, kmCost, minuteCostStay, minKmCost, minuteMinCost, countryCost, countryBackCost, minSpeed) values "
				+"(1, '100', '100', '100', '100', '100', '100', '100', '100', '100')";
		database.execSQL(query);
	}
	
	public int getUserId(){//���������� id ������������
		return userId;
	}
	
	public ArrayList<varsForAdapter> selectDriver(){//������ ����������
		
		ArrayList<varsForAdapter> data = new ArrayList<varsForAdapter>();
		varsForAdapter temp;
		
		query = "select * from driver where id = "+tempId+";";
		c = database.rawQuery(query, null);
		c.moveToFirst();
		do {
				temp = new varsForAdapter();
				
				for(String str : c.getColumnNames()){
					temp.driver.add(c.getString(c.getColumnIndex(str)));
				}
				
				data.add(temp);
	            
	    } while (c.moveToNext());
		c.close();
		
		return data;
		
	}

	public ArrayList<varsForAdapter> select(String type){//������ ������ �� ��
		/* ���������� ������, ��������� �� ������� varsForAdapter
		 * (������ ����� ������ ���� ��� ���� ������ �� �����, ������� ����� �����������
		 * ����������� �� ��)
		 * !!!����� ���������� ���������� ��� ��������!!!
		 * 
		 * �������� - ������ ������ 
		 * query = "select id, serialCar, fName, sName, tName from driver;";
		 * c = database.rawQuery(query, null);
		 * ������� ������ ������� ��� ������ �������� ������
			if(!c.moveToFirst()) {
				Log.w(tag, "��� �� ����� ������, ����� ������� �������");
				return data;
			}
			
			do {
				temp = new varsForAdapter();//������� ����� ����������
				
				for(String str : c.getColumnNames()){
					temp.driver.add(c.getString(c.getColumnIndex(str)));
				}
				
				���������� ��������� ��� ������ �������, ����� ����� ����� ����� ����� ���
				temp.driver = {<id>, <serialCar>, <fName>, <sName>, <tName>}
	            
				data.add(temp);���������� ���������� ��������� � ���������� ��� ��������
	            
	    	} while (c.moveToNext());
			c.close();
		
			return data;
		 * 
		 * 
		 * 
		 */
		ArrayList<varsForAdapter> data = new ArrayList<varsForAdapter>();
		varsForAdapter temp;
		
		switch(type){
		case "selectAllDrivers":
			query = "select id, serialCar, fName, sName, tName from driver;";
			break;
		case "selectAllAboutDriver":
			query = "select * from driver where id = "+tempId+";";
			break;
		case "selectAllServers":
			query = "select id, nameServer, workServer from server where userId = "+userId+";";
			break;
		case "selectAllAboutServer":
			query = "select * from server where id = "+tempId+";";
			break;
		case "selectAllPolygon":
			query = "select id, name from polygon where userId = "+userId;
			break;
		case "selectAllAboutPolygon":
			query = "select * from polygon where id = "+tempId;
			break;
		case "selectAllModes":
			query = "select id, name, work from mode where userId = "+userId;
			break;
		case "selectAllAboutMode":
			query = "select * from mode where id = "+tempId;
			break;
		case "selectTarif":
			query = "select id from tarif where userId = "+userId;
			break;
		case "selectInfoTarif":
			query = "select * from tarif where id = "+tempId;
			break;
			
		}
		
		//Log.w(tag, "query = " + query);
		
		c = database.rawQuery(query, null);
		if(!c.moveToFirst()) {
			Log.w(tag, "��� �� ����� ������, ����� ������� �������");
			return data;
		}
		
		do {
				temp = new varsForAdapter();
				
				for(String str : c.getColumnNames()){
					temp.driver.add(c.getString(c.getColumnIndex(str)));
				}
	            
				data.add(temp);
	            
	    } while (c.moveToNext());
		c.close();
		
		return data;
		
	}
	
	public void update(String type, ArrayList<String> strArr){//������ ���������� ����������
		/*
		 * ��� ���������� ���������� � �����-���� ���� ������ ��������� ������ �� ������ (�����)
		 * ���� ������ ����������� � ����������� ��� �� �������� � ��� ������� ����� �������, �����
		 * ��������������� ��������� �� ������������ ���� ������. 
		 * 
		 * ��������, ��������� �� mode ���������
		 * ���� : id, userId, name, val, work
		 * 
		 * ������� ������ ����� ������������� ��������� �������:
		 * <������> = {name, val, work } - ��� ������ ����� �����������
		 * ���� id ������ �� ���������� tempId, ������� ���������� �� ���������� �����
		 * ���� userId ������ �� ���������� userId, ������� ��������������� ��� ����������� ������������
		 * 
		 * ������� ��� �������� �� �������� ��� ���� id, userId.
		 */
		//Log.w(tag, "type = "+strArr.size());
		switch(type){
		case "driver":
					query = "select * from driver where id = "+tempId;
					c = database.rawQuery(query, null);
					query = "";
					for(String str : c.getColumnNames())
						if(!str.equals("id")) 
							if(c.getColumnIndex(str)+1 != c.getColumnCount()) query = query + str+" = '"+strArr.get(c.getColumnIndex(str)-1)+"', ";
							else query = query + str+" = '"+strArr.get(c.getColumnIndex(str)-1)+"' ";
			break;
			
		default:
					query = "select * from "+type+" where id = "+tempId;
					c = database.rawQuery(query, null);
					query = "";
					for(String str : c.getColumnNames())
						if(!str.equals("id") & !str.equals("userId")) 
							if(c.getColumnIndex(str)+1 != c.getColumnCount()) query = query + str+" = '"+strArr.get(c.getColumnIndex(str)-2)+"', ";
							else query = query + str+" = '"+strArr.get(c.getColumnIndex(str)-2)+"' ";
			break;
		}
		
		query = "update '"+type+"' set "+query+"where id = "+tempId;
		Log.w(tag, query);
		//query = "update "+type+" set name = 'ema' where id = "+tempId;
		database.execSQL(query);
		c.close();
		tempId = -1;
	}

	
	public void delete(String type){//������ �������� �������
		query = "delete from "+type+" where id = "+tempId;
		database.execSQL(query);
		
		if(type.equals("driver")){
			String strArr[] = {
				"delete from server where UserId = "+tempId,
				"delete from mode where UserId = "+tempId,
				"delete from polygon where UserId = "+tempId,
				"delete from tarif where UserId = "+tempId				
			};
			
			for(String str : strArr) database.execSQL(str);
		}
		tempId = -1;
	}
	
	public void insert(String type, ArrayList<String> strArr){
		/*
		 * ������ ������ ����������� ����� �� �������, ��� � ��� ������� update() ��. ����
		 */
		String val= "", key = "";
		
		switch(type){
		case "driver"://��� ������� ������������� ���� �������� ���������� �������
				query = "select * from driver";
				c = database.rawQuery(query, null);
				
				for(String str : c.getColumnNames())
					if(!str.equals("id")) 
						if(c.getColumnIndex(str)+1 != c.getColumnCount()) {
							key = key + str + ", ";
							val = val + "'" + strArr.get(c.getColumnIndex(str)-1) + "', ";
						}
						else {
							key = key + str;
							val = val + "'" + strArr.get(c.getColumnIndex(str)-1) + "'";
						}
				
				//������ id ����������� ������
				query = "select max(id) as insertId from driver";
				c = database.rawQuery(query, null);
				c.moveToFirst();
				tempId = c.getInt(c.getColumnIndex("insertId"));

				//��������� ������ � ���� ������ �������� � ������ ��� ������ ������������
				String[] driverSql = {
						"insert into tarif (userId, sitdown, minCost, kmCost, minuteCostStay, minKmCost, minuteMinCost, countryCost, countryBackCost, minSpeed) values "
								+"("+tempId+", '100', '100', '100', '100', '100', '100', '100', '100', '100')",
								"insert into mode (userId, name, val, work) values ("+tempId+", '��������� �� ����� ������������ � �������', 'connectToServer', 'true');",
								"insert into mode (userId, name, val, work) values ("+tempId+", '���� ����� ����������� ��������� �������', 'manualSetting', 'true');",
								"insert into mode (userId, name, val, work) values ("+tempId+", ' ���������� �������', 'showMoney', 'true');"
				};
				for(String str : driverSql) database.rawQuery(str, null);//��������� ���� �������
				
				
			break;
		default://��� ��������� ������ ���� ��������
				query = "select * from "+type;
				c = database.rawQuery(query, null);
				
				for(String str : c.getColumnNames())
					if(!str.equals("id") & !str.equals("userId")) 
						if(c.getColumnIndex(str)+1 != c.getColumnCount()) {
							key = key + str + ", ";
							val = val + "'" + strArr.get(c.getColumnIndex(str)-2) + "', ";
						}
						else {
							key = key + str;
							val = val + "'" + strArr.get(c.getColumnIndex(str)-2) + "'";
						}
				key = key + ", userId";
				val = val + ", '" + userId + "'";
			break;
		}

		query = "insert into "+type+" ("+key+") values ("+val+")";
		
		Log.w(tag, "query = "+query);
		database.execSQL(query);
	}
	
	@Override//������ ���� - �� ������
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {//���������� ������, ����������� ��� ������� ����
		parcel.writeInt(userId);
		parcel.writeInt(tempId);
		parcel.writeString(tag);
		
	}
	
	//������ ���� - �� ������
	public static final Parcelable.Creator<dbController> CREATOR = new Parcelable.Creator<dbController>() {
	    public dbController createFromParcel(Parcel in) {
	      return new dbController(in);
	    }

	    public dbController[] newArray(int size) {
	      return new dbController[size];
	    }
	};

	private dbController(Parcel parcel) {//������ ������ ��� ������� ����
		super(cnt, "driversBase.db", null, 1);
		
		userId = parcel.readInt();
		tempId = parcel.readInt();
		tag = parcel.readString();
		
		database = this.getWritableDatabase();
	}
}
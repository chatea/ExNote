package com.ntu.iddc.exnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class SQLite extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "exDiary.db";	//資料庫名稱
	private static final int DATABASE_VERSION = 1;	//資料庫版本
 
	private SQLiteDatabase db;
 
	public SQLite(Context context) {	//建構子
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = this.getWritableDatabase();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		String DATABASE_CREATE_TABLE =
		    "create table DiaryContent ("
		        + "_ID INTEGER PRIMARY KEY,"
		        + "DiaryId TEXT,"
		        + "AuthorId TEXT,"
		        + "AuthorName TEXT,"
		        + "Date TEXT,"
		        + "Time TEXT,"
		        + "Title TEXT,"
		        + "Article TEXT"
		    + ");";
		//建立config資料表，詳情請參考SQL語法
		db.execSQL(DATABASE_CREATE_TABLE);
		
		DATABASE_CREATE_TABLE =
			    "create table DiaryList ("
			        + "_ID INTEGER PRIMARY KEY,"
			        + "DiaryId TEXT,"
			        + "DiaryName TEXT,"
			        + "DiaryIcon TEXT,"
			        + "OwnerId TEXT,"
			        + "UpdateTime TEXT"
			    + ");";
			//建立config資料表，詳情請參考SQL語法
			db.execSQL(DATABASE_CREATE_TABLE);
			DATABASE_CREATE_TABLE =
				    "create table CoWorkerList ("
				        + "_ID INTEGER PRIMARY KEY,"
				        + "DiaryId TEXT,"
				        + "UserId TEXT,"
				        + "UserName TEXT,"
				        + "OwnerId TEXT"
				        + ");";
				//建立config資料表，詳情請參考SQL語法
				db.execSQL(DATABASE_CREATE_TABLE);
			
		
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
		db.execSQL("DROP TABLE IF EXISTS DiaryContent");	//刪除舊有的資料表
		onCreate(db);
	}
	
	public Cursor getAllDiaryList() {
	    return db.rawQuery("SELECT * FROM DiaryList", null);
	}
	public String getLastUpdateTimeByDiaryId(String diaryId) {
		Cursor cursor=db.rawQuery( 
			     "SELECT UpdateTime FROM DiaryList WHERE diaryId=?", new String[]{diaryId});
		return cursor.getString(0);
	}
	public Cursor getAllDiaryContent() {
	    return db.rawQuery("SELECT * FROM DiaryContent", null);
	}
	public Cursor getAllDiaryContentByDiaryId(String diaryId) {
	    return db.rawQuery("SELECT * FROM DiaryContent WHER diaryId=?", new String[]{diaryId});
	}
	public Cursor getDiaryContentByLastUpdateTime(String time) {
	    return db.rawQuery("SELECT * FROM DiaryContent WHER UpdateTime> ?", new String[]{time});
	}
	public Cursor getAllCoWorkerByDiaryId(String diaryId) {
	    return db.rawQuery("SELECT * FROM CoWorkerList WHER diaryId=?", new String[]{diaryId});
	}
	
	
	
	public void setUpdateTimeByDiaryIdAndTime(String diaryId, String time){
		ContentValues cv = new ContentValues();
		        cv.put("UpdateTime", time);
		db.update("DiaryList", cv, "diaryId="+diaryId, null);
	}
	
	public Cursor getDiaryByDate(String date,String diaryId){
		Cursor cursor=db.rawQuery( 
		     "SELECT * FROM DiaryContent WHERE date=? AND diaryId=? ORDER BY Time", new String[]{date,diaryId});
	return cursor;
	}
//新增一筆記錄，成功回傳rowID，失敗回傳-1
	public void insertNewDiary(String diaryId, String authorId, String authorName, String date, String time, String title, String article) {

	db.execSQL("INSERT INTO DiaryContent(DiaryId,AuthorId,authorName,Date,Time,Title,Article) values(?,?,?,?,?,?,?)",
			new Object[]{diaryId,authorId,authorName,date,time,title,article});
	}	
	public void creatNewDiary(String diaryId, String diaryName, String diaryIcon, String ownerId, String updateTime) {

		db.execSQL("INSERT INTO DiaryList(DiaryId,DiaryName,DiaryIcon,OwnerId,UpdateTime) values(?,?,?,?,?)",
				new Object[]{diaryId,diaryName,diaryIcon,ownerId,updateTime});
	
	}
	public void insertNewCoWorker(String diaryId, String userId, String userName, String ownerId) {

		db.execSQL("INSERT INTO CoWorkerList(DiaryId,UserId,UserName,OwnerId) values(?,?,?,?)",
				new Object[]{diaryId,userId,userName,ownerId});
		}	

	
}
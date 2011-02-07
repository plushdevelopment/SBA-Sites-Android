/**
 * 
 */
package com.sbasite.sbasites;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author rosschapman
 *
 */
public class SitesSqliteOpenHelper extends SQLiteOpenHelper {
	//The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.sbasite.activities/databases/";
	public static final int VERSION = 1;
	public static final String DB_NAME = "sbasites_db.sqlite";
	public static final String SITES_TABLE = "SITES";
	public static final String SITE_ID = "ID";
	public static final String SITE_LATITUDE = "SITE_LATITUDE";
	public static final String SITE_LONGITUDE = "SITE_LONGITUDE";
	public static final String SITE_DELETED = "SITE_DELETED";
	public static final String SITE_ADDRESS = "SITE_ADDRESS";
	public static final String SITE_AGL = "SITE_AGL";
	public static final String SITE_BTA = "SITE_BTA";
	public static final String SITE_CITY = "SITE_CITY";
	public static final String SITE_CONTACT = "SITE_CONTACT";
	public static final String SITE_COUNTY = "SITE_COUNTY";
	public static final String SITE_EMAIL = "SITE_EMAIL";
	public static final String SITE_LASTUPDATED = "SITE_LASTUPDATED";
	public static final String SITE_MOBILEKEY = "SITE_MOBILEKEY";
	public static final String SITE_MTA = "SITE_MTA";
	public static final String SITE_PHONE = "SITE_PHONE";
	public static final String SITE_CODE = "SITE_CODE";
	public static final String SITE_LAYER = "SITE_LAYER";
    public static final String SITE_NAME = "SITE_NAME";
    public static final String SITE_STATUS = "SITE_STATUS";
    public static final String SITE_STATEPROVINCE = "SITE_STATEPROVINCE";
    public static final String SITE_STRUCTUREHEIGHT = "SITE_STRUCTUREHEIGHT";
    public static final String SITE_STRUCTUREID = "SITE_STRUCTUREID";
    public static final String SITE_STRUCTURETYPE = "SITE_STRUCTURETYPE";
    public static final String SITE_ZIP = "SITE_ZIP";
	
    private SQLiteDatabase myDataBase; 
    
    private final Context myContext;
	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public SitesSqliteOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.myContext = context;
	}
	
	/**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    		Log.d("DB", "Database Exists!!!!!!!!");
    	}else{
    		Log.d("DB", "Database doesn't exist");
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
    			dbExist = checkDataBase();
    			if(dbExist){
    				Log.d("DB", "Database Exists!!!!!!!!");
    			}
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
    		Log.d("DB", "Database does not exist yet");
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
    
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
    
    public void createDatabase() throws IOException {
    	 
        InputStream assetsDB = myContext.getAssets().open("sbasites_db.sqlite");
        OutputStream dbOut = new FileOutputStream("/data/data/com.sbasite.activities/database/sbasites_db.sqlite");
     
        byte[] buffer = new byte[1024];
        int length;
        while ((length = assetsDB.read(buffer))>0){
          dbOut.write(buffer, 0, length);
        }
     
        dbOut.flush();
        dbOut.close();
        assetsDB.close();
    }
 
    public SQLiteDatabase openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	return myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
	}


	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

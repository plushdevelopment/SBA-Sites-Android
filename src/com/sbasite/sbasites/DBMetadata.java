package com.sbasite.sbasites;

import java.sql.Date;

import android.content.Context;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "DBMetadata")
public class DBMetadata extends ActiveRecordBase<DBMetadata> {
	
	public DBMetadata(Context context) { super(context); }
	  
	@Column(name = "LAST_UPDATE")
	public Date lastUpdate;
	
	@Column(name = "SKIP")
	public int skip;
	
	@Column(name = "TAKE")
	public int take;

}

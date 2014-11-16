package com.datmaster.contentprovider;

import android.provider.Browser;

public class ContentConstants {

	public static final String DB_NAME = "BOOKMARKS_DB";
	public static final String TABLE_NAME = "BOOKMARKS_TABLE";
	public static final String SELECT_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
	
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" 
			+ Browser.BookmarkColumns._ID + " INTEGER PRIMARY KEY autoincrement, " 
			+ Browser.BookmarkColumns.TITLE + " TEXT, "
			+ Browser.BookmarkColumns.URL + " TEXT, "
			+ Browser.BookmarkColumns.FAVICON+ " TEXT)";
	
	public static final int DB_VERSION = 1;
	
}

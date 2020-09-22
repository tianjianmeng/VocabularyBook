package com.example.vocabularybook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordsDB {
    private static final String TAG = "DBtag";
    private static WordsDBHelper mDbHelper;
    private static WordsDB instance=new WordsDB();
    public static WordsDB getWordsDB(){
        return WordsDB.instance;
    }

    private WordsDB() {
        if (mDbHelper == null) {
            mDbHelper = new WordsDBHelper(WordsApplication.getContext());
        }
    }
    public void close() {
        if (mDbHelper != null)
            mDbHelper.close();
    }
    public Words.WordDescription getSingleWord(String id) {
        Words.WordDescription result = new Words.WordDescription();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from words where _id = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{id});
        if(cursor.moveToFirst()){
            result.setWord(cursor.getString(cursor.getColumnIndex("word")));
            result.setMeaning(cursor.getString(cursor.getColumnIndex("meaning")));
            result.setSample(cursor.getString(cursor.getColumnIndex("sample")));
            System.out.println("result: "+result.getWord());
            return result;
        }
        return null;
    }
    public ArrayList<Map<String, String>> getAllWords() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from words ";
        Cursor cursor = db.rawQuery(sql,null);
        return ConvertCursor2WordList(cursor);
    }
    private ArrayList<Map<String, String>> ConvertCursor2WordList(Cursor cursor) {
        ArrayList<Map<String,String>> list = new ArrayList<Map<String, String>>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Map<String,String> map = new HashMap<String,String>();
            System.out.println("dsl:"+cursor.getString(cursor.getColumnIndex("_id"))+ "word:"+cursor.getString(cursor.getColumnIndex("word")));
            map.put(Words.Word._ID, cursor.getString(cursor.getColumnIndex("_id")));
            map.put(Words.Word.COLUMN_NAME_WORD, cursor.getString(cursor.getColumnIndex("word")));
            list.add(map);
        }

        if(cursor.moveToFirst()){

        }
        return list;
    }
    //insertsql
    public void Insert(String strWord, String strMeaning, String strSample) {
        Log.v(TAG,strWord+":"+strMeaning+":"+strSample);

        String sql="insert into  words(word,meaning,sample) values(?,?,?)";

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Log.v(TAG, "have DBhelpered");
        db.execSQL(sql,new String[]{strWord,strMeaning,strSample});
        Log.v(TAG, "have inserted");
    }
    public void DeleteUseSql(String strId) {
        String sql="delete from words where _id='"+strId+"'";
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL(sql);
    }
    public void Delete(String strId) {
        String sql="delete from words where _id='"+strId+"'";
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL(sql);
    }
    public void UpdateUseSql(String strId, String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="update words set word=?,meaning=?,sample=? where _id=?";
        db.execSQL(sql, new String[]{strWord, strMeaning, strSample,strId});
    }
    public void Update(String strId, String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="update words set word=?,meaning=?,sample=? where _id=?";
        db.execSQL(sql, new String[]{strWord, strMeaning, strSample,strId});
    }
    public ArrayList<Map<String, String>> SearchUseSql(String strWordSearch){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="select * from words where word like ? order by word desc";
        Cursor c=db.rawQuery(sql,new String[]{"%"+strWordSearch+"%"});
        return ConvertCursor2WordList(c);
    }
    public ArrayList<Map<String, String>> Search(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="select * from words where word like ? order by word desc";
        Cursor c=db.rawQuery(sql,new String[]{"%"+strWordSearch+"%"});
        return ConvertCursor2WordList(c);
    }
}

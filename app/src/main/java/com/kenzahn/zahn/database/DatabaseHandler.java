package com.kenzahn.zahn.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.kenzahn.zahn.model.CardContentRes;
import com.kenzahn.zahn.model.FlashcardJsonList;
import com.kenzahn.zahn.model.PreferencesJsonRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "OnlineFlashcards.db";
    Context context;
    private static final String TABLE_FLASHCARD = "flashcard";
    private static final String TABLE_FLASHCARD_CONTENT = "flashcardcontent";
    private static final String TAG = "DatabaseHandler";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {


        db.execSQL("Create table " + TABLE_FLASHCARD + " ( FlashCardSetID INTEGER PRIMARY KEY, "
                + "CreateDate TEXT,"
                + "FlashCardTypeID TEXT,"
                + "FlashCardName TEXT,"
                + "Active TEXT,"
                + "Audio TEXT,"
                + "AudioFile TEXT,"
                + "CycleID TEXT,"
                + "ExamSectionID TEXT,"
                + "TotalNoOfCards TEXT,"
                + "ExpiryDate DATETIME,"
                + "CompletedCards INTEGER,"
                + "Status TEXT,"
                + "decksortOrder INTEGER,"
                + "OriginalDeckOrder INTEGER)");


        db.execSQL("Create table " + TABLE_FLASHCARD_CONTENT + " ( FlashCardID INTEGER PRIMARY KEY, "
                + "CreateDate TEXT,"
                + "FlashCardSetID TEXT,"
                + "FlashCardFront TEXT,"
                + "FlashCardBack TEXT,"
                + "SortOrder INTEGER,"
                + "ExamSectionID TEXT,"
                + "ExamSection TEXT,"
                + "FlashCardName TEXT,"
                + "isKnownContent INTEGER DEFAULT 0,"
                + "isKnownReadCount INTEGER DEFAULT 0,"
                + "OriginalCardOrder INTEGER)");



        Log.e(TAG, "table created...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "old$oldVersion");
        Log.e(TAG, "new$newVersion");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS'" + TABLE_FLASHCARD + "'");
        // Create tables again
        onCreate(db);
    }

    public void insertFlashCard(ArrayList<FlashcardJsonList> result, ArrayList<PreferencesJsonRes> preference) {
        try {
            Log.e(TAG, "result" + result.size());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
            for (int i = 0; i < result.size(); ++i)
            {
                try {
                    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("FlashCardSetID", (result.get(i)).getFlashCardSetID());
                    values.put("CreateDate", (result.get(i)).getCreateDate());
                    values.put("FlashCardTypeID", (result.get(i)).getFlashCardTypeID());
                    values.put("FlashCardName", (result.get(i)).getFlashCardName());
                    values.put("Active", (result.get(i)).getActive());
                    values.put("Audio", (result.get(i)).getAudio());
                    values.put("AudioFile", (result.get(i)).getAudioFile());
                    values.put("CycleID", (result.get(i)).getCycleID());
                    values.put("ExamSectionID", (result.get(i)).getExamSectionID());
                    values.put("TotalNoOfCards", (result.get(i)).getTotalNoOfCards());
                    Date date = inputFormatter.parse((result.get(i)).getExpiryDate());
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    String output = outputFormatter.format(date);
                   /* if (i == 0) {
                        output = "2019-10-21 18:54:14";
                    }*/

                    values.put("ExpiryDate", output);
                    values.put("CompletedCards", (preference.get(i)).getCompletedCards());
                    values.put("Status", (preference.get(i)).getStatus());
                    values.put("OriginalDeckOrder", (i+1));
                    values.put("decksortOrder", (i+1));
                    sqLiteDatabase.insert(TABLE_FLASHCARD, null, values);
                    sqLiteDatabase.close();
                    for (int j = 0;j<result.get(i).getCardContent().size();j++)
                    {
                        SQLiteDatabase sqLiteDatabasePref = this.getWritableDatabase();
                        ContentValues valuesPref = new ContentValues();
                        valuesPref.put("FlashCardID", (result.get(i)).getCardContent().get(j).getFlashCardID());
                        valuesPref.put("CreateDate", (result.get(i)).getCardContent().get(j).getCreateDate());
                        valuesPref.put("FlashCardSetID", (result.get(i)).getCardContent().get(j).getFlashCardSetID());
                        valuesPref.put("FlashCardFront", (result.get(i)).getCardContent().get(j).getFlashCardFront());
                        valuesPref.put("FlashCardBack", (result.get(i)).getCardContent().get(j).getFlashCardBack());
                        valuesPref.put("SortOrder", ""+j);
                        valuesPref.put("OriginalCardOrder", ""+j);
                        valuesPref.put("ExamSectionID", (result.get(i)).getCardContent().get(j).getExamSectionID());
                        valuesPref.put("ExamSection", (result.get(i)).getCardContent().get(j).getExamSection());
                        valuesPref.put("FlashCardName", (result.get(i)).getCardContent().get(j).getFlashCardName());
                        valuesPref.put("isKnownContent", (preference.get(i)).getCardContent().get(j).getIsKnownContent() ? 1 : 0);
                        valuesPref.put("isKnownReadCount", 0);
                        sqLiteDatabasePref.insert(TABLE_FLASHCARD_CONTENT, null, valuesPref);
                        sqLiteDatabasePref.close();
                    }

                    Log.e(TAG, "values recored inserted" + i);
                } catch (Exception var15) {
                    var15.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void insertFlashCard__(ArrayList<FlashcardJsonList> result, ArrayList<PreferencesJsonRes> preference) {
        try {
            Log.e(TAG, "result" + result.size());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            for (int i = 0; i < result.size(); ++i)
            {
                try {
                    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("FlashCardSetID", (result.get(i)).getFlashCardSetID());
                    values.put("CreateDate", (result.get(i)).getCreateDate());
                    values.put("FlashCardTypeID", (result.get(i)).getFlashCardTypeID());
                    values.put("FlashCardName", (result.get(i)).getFlashCardName());
                    values.put("Active", (result.get(i)).getActive());
                    values.put("Audio", (result.get(i)).getAudio());
                    values.put("AudioFile", (result.get(i)).getAudioFile());
                    values.put("CycleID", (result.get(i)).getCycleID());
                    values.put("ExamSectionID", (result.get(i)).getExamSectionID());
                    values.put("TotalNoOfCards", (result.get(i)).getTotalNoOfCards());
                    Date date = inputFormatter.parse((result.get(i)).getExpiryDate());
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String output = outputFormatter.format(date);
                   /* if (i == 0) {
                        output = "2019-10-21 18:54:14";
                    }*/

                    values.put("ExpiryDate", output);
                    values.put("CompletedCards", (preference.get(i)).getCompletedCards());
                    values.put("Status", (preference.get(i)).getStatus());
                    values.put("OriginalDeckOrder", ""+(i+1));
                    values.put("decksortOrder", ""+(i+1));
                    sqLiteDatabase.insert(TABLE_FLASHCARD, null, values);
                    sqLiteDatabase.close();
                    for (int j=0;j<result.get(i).getCardContent().size();j++)
                    {
                        Log.e("SubTotal",""+j);
                        SQLiteDatabase sqLiteDatabasePref = this.getWritableDatabase();
                        ContentValues valuesPref = new ContentValues();
                        valuesPref.put("FlashCardID", (result.get(i)).getCardContent().get(j).getFlashCardID());
                        valuesPref.put("CreateDate", (result.get(i)).getCardContent().get(j).getCreateDate());
                        valuesPref.put("FlashCardSetID", (result.get(i)).getCardContent().get(j).getFlashCardSetID());
                        valuesPref.put("FlashCardFront", (result.get(i)).getCardContent().get(j).getFlashCardFront());
                        valuesPref.put("FlashCardBack", (result.get(i)).getCardContent().get(j).getFlashCardBack());
                        valuesPref.put("SortOrder", (""+j));
                        valuesPref.put("OriginalCardOrder", (""+j));
                        valuesPref.put("ExamSectionID", (result.get(i)).getCardContent().get(j).getExamSectionID());
                        valuesPref.put("ExamSection", (result.get(i)).getCardContent().get(j).getExamSection());
                        valuesPref.put("FlashCardName", (result.get(i)).getCardContent().get(j).getFlashCardName());
                        valuesPref.put("isKnownContent", 0);
                        valuesPref.put("isKnownReadCount", 0);
                        sqLiteDatabasePref.insert(TABLE_FLASHCARD_CONTENT, null, valuesPref);
                        sqLiteDatabasePref.close();
                    }

                    Log.e(TAG, "values recored inserted" + i);
                } catch (Exception var15) {
                    var15.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public final ArrayList<FlashcardJsonList> getFlashCardList()
    {
        ArrayList<FlashcardJsonList> flashcardJsonList = new ArrayList<FlashcardJsonList>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD + " ORDER BY `decksortOrder` ASC";
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                FlashcardJsonList flashcardJsonList1 = new FlashcardJsonList();
                flashcardJsonList1.setFlashCardSetID(cursor.getString(0));
                flashcardJsonList1.setCreateDate(cursor.getString(1));
                flashcardJsonList1.setFlashCardTypeID(cursor.getString(2));
                flashcardJsonList1.setFlashCardName(cursor.getString(3));
                flashcardJsonList1.setActive(cursor.getString(4));
                flashcardJsonList1.setAudio(cursor.getString(5));
                flashcardJsonList1.setAudioFile(cursor.getString(6));
                flashcardJsonList1.setCycleID(cursor.getString(7));
                flashcardJsonList1.setExamSectionID(cursor.getString(8));
                flashcardJsonList1.setTotalNoOfCards(cursor.getString(9));
                flashcardJsonList1.setExpiryDate(cursor.getString(10));
                flashcardJsonList1.setCardContent(new ArrayList<CardContentRes>());
                flashcardJsonList1.setCompletedCards(cursor.getInt(11));
                flashcardJsonList1.setStatus(cursor.getString(12));
                flashcardJsonList1.setDecksortOrder(cursor.getString(13));
                flashcardJsonList1.setOriginalDeckOrder(cursor.getString(14));
                flashcardJsonList.add(flashcardJsonList1);
            } while (cursor.moveToNext());
        }

        return flashcardJsonList;
    }

    public final ArrayList<CardContentRes> getFlashCardListContent(String flashCardSetID) {
        ArrayList<CardContentRes> flashcardJsonList = new ArrayList<CardContentRes>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT + " WHERE flashCardSetID=" + flashCardSetID + " ORDER BY `SortOrder` ASC ";
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                CardContentRes flashcardJsonList1 = new CardContentRes();
                flashcardJsonList1.setFlashCardID(cursor.getString(0));
                flashcardJsonList1.setCreateDate(cursor.getString(1));
                flashcardJsonList1.setFlashCardSetID(cursor.getString(2));
                flashcardJsonList1.setFlashCardFront(cursor.getString(3));
                flashcardJsonList1.setFlashCardBack(cursor.getString(4));
                flashcardJsonList1.setSortOrder(cursor.getString(5));
                flashcardJsonList1.setExamSectionID(cursor.getString(6));
                flashcardJsonList1.setExamSection(cursor.getString(7));
                flashcardJsonList1.setFlashCardName(cursor.getString(8));
                flashcardJsonList1.setKnownContent(cursor.getInt(9)==1);
                flashcardJsonList1.setIsKnownReadCountvar(cursor.getInt(10));
                flashcardJsonList1.setOriginalCardOrder(cursor.getString(11));
                flashcardJsonList.add(flashcardJsonList1);
            } while (cursor.moveToNext());
        }

        return flashcardJsonList;
    }

    public final ArrayList<CardContentRes> getFlashCardListContent2(String flashCardSetID) {
        ArrayList<CardContentRes> flashcardJsonList = new ArrayList<CardContentRes>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT + " WHERE flashCardSetID=" + flashCardSetID + " ORDER BY `SortOrder` ASC ";
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                CardContentRes flashcardJsonList1 = new CardContentRes();
                flashcardJsonList1.setFlashCardID(cursor.getString(0));
                flashcardJsonList1.setCreateDate(cursor.getString(1));
                flashcardJsonList1.setFlashCardSetID(cursor.getString(2));
                flashcardJsonList1.setFlashCardFront(cursor.getString(3));
                flashcardJsonList1.setFlashCardBack(cursor.getString(4));
                flashcardJsonList1.setSortOrder(cursor.getString(5));
                flashcardJsonList1.setExamSectionID(cursor.getString(6));
                flashcardJsonList1.setExamSection(cursor.getString(7));
                flashcardJsonList1.setFlashCardName(cursor.getString(8));
                flashcardJsonList1.setKnownContent(cursor.getInt(9)==1);
                flashcardJsonList1.setIsKnownReadCountvar(cursor.getInt(10));
                flashcardJsonList1.setOriginalCardOrder(cursor.getString(11));
                flashcardJsonList.add(flashcardJsonList1);
            } while (cursor.moveToNext());
        }

        return flashcardJsonList;
    }


    public final ArrayList<CardContentRes> getFlashCardListContentLearned(String flashCardSetID)
    {
        ArrayList<CardContentRes> flashcardJsonList = new ArrayList<CardContentRes>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT + " WHERE flashCardSetID=" + flashCardSetID + " ORDER BY `FlashCardID` ASC ";
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, (String[]) null);
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getInt(9)==1)
                {
                    CardContentRes flashcardJsonList1 = new CardContentRes();
                    flashcardJsonList1.setFlashCardID(cursor.getString(0));
                    flashcardJsonList1.setCreateDate(cursor.getString(1));
                    flashcardJsonList1.setFlashCardSetID(cursor.getString(2));
                    flashcardJsonList1.setFlashCardFront(cursor.getString(3));
                    flashcardJsonList1.setFlashCardBack(cursor.getString(4));
                    flashcardJsonList1.setSortOrder(cursor.getString(5));
                    flashcardJsonList1.setExamSectionID(cursor.getString(6));
                    flashcardJsonList1.setExamSection(cursor.getString(7));
                    flashcardJsonList1.setFlashCardName(cursor.getString(8));
                    flashcardJsonList1.setKnownContent(cursor.getInt(9)==1);
                    flashcardJsonList1.setIsKnownReadCountvar(cursor.getInt(10));
                    flashcardJsonList1.setOriginalCardOrder(cursor.getString(11));
                    flashcardJsonList.add(flashcardJsonList1);
                }

            } while (cursor.moveToNext());
        }

        return flashcardJsonList;
    }








    public final ArrayList<CardContentRes> getFlashCardListContentToLearned(String flashCardSetID) {
        ArrayList<CardContentRes> flashcardJsonList = new ArrayList<CardContentRes>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT + " WHERE isKnownContent=0 AND flashCardSetID=" + flashCardSetID + " ORDER BY `SortOrder` ASC ";
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery,  null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(9)==0)
                {
                    CardContentRes flashcardJsonList1 = new CardContentRes();
                    flashcardJsonList1.setFlashCardID(cursor.getString(0));
                    flashcardJsonList1.setCreateDate(cursor.getString(1));
                    flashcardJsonList1.setFlashCardSetID(cursor.getString(2));
                    flashcardJsonList1.setFlashCardFront(cursor.getString(3));
                    flashcardJsonList1.setFlashCardBack(cursor.getString(4));
                    flashcardJsonList1.setSortOrder(cursor.getString(5));
                    flashcardJsonList1.setExamSectionID(cursor.getString(6));
                    flashcardJsonList1.setExamSection(cursor.getString(7));
                    flashcardJsonList1.setFlashCardName(cursor.getString(8));
                    flashcardJsonList1.setKnownContent(cursor.getInt(9)==1);
                    flashcardJsonList1.setIsKnownReadCountvar(cursor.getInt(10));
                    flashcardJsonList1.setOriginalCardOrder(cursor.getString(11));
                    flashcardJsonList.add(flashcardJsonList1);
                }

            } while (cursor.moveToNext());
        }

        return flashcardJsonList;
    }

    public final int getReadCountMainDeck(String flashCardSetID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT + " WHERE isKnownReadCount=1 AND FlashCardSetID=" + flashCardSetID + ' ';
        Log.e(TAG, "query" + selectquery);
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery,null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public final int getReadCount(String flashCardSetID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT + " WHERE isKnownContent=0 AND FlashCardSetID=" + flashCardSetID + ' ';
        Log.e(TAG, "query" + selectquery);
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public final int getReadCountToLearn(String flashCardSetID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT + " WHERE isKnownContent=1 AND flashCardSetID=" + flashCardSetID + " ORDER BY `SortOrder` ASC ";
        Log.e(TAG, "query" + selectquery);
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public final int getReadCountAll(String flashCardSetID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT + " WHERE flashCardSetID=" + flashCardSetID + " ORDER BY `SortOrder` ASC ";
        Log.e(TAG, "query" + selectquery);
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public final UpdatePre getUserPrefernce(int userid) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD + "";
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, null);
        UpdatePre updatePreMain = new UpdatePre();
        ArrayList preferencesJsonList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                Log.e(TAG, "values" + cursor.getString(0));
                PreferencesJson preferencesJson = new PreferencesJson();
                preferencesJson.FlashCardSetID = cursor.getString(0);
                preferencesJson.CompletedCards = cursor.getString(11);
                preferencesJson.DecksortOrder = cursor.getString(13);
                preferencesJson.TotalCardCount = cursor.getString(9);
                preferencesJson.Status = cursor.getString(12);
                SQLiteDatabase sqLiteDatabase2 = this.getWritableDatabase();
                String selectquery2 = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT + " WHERE flashCardSetID=" + cursor.getString(0) + " ORDER BY `SortOrder` ASC ";
                Log.e(TAG, "query" + selectquery);
                Cursor cursor1 = sqLiteDatabase2.rawQuery(selectquery2, (String[])null);
                ArrayList carJsonList = new ArrayList<CardContent>();
                if (cursor1.moveToFirst()) {
                    do {
                        CardContent cardContent = new CardContent();
                        boolean isKnown = cursor1.getInt(9) == 1;
                        cardContent.FlashCardID = cursor1.getString(0);
                        cardContent.SortOrder = cursor1.getString(5);
                        cardContent.IsKnownContent = isKnown;
                        cardContent.FlashCardSetID = cursor1.getString(2);
                        carJsonList.add(cardContent);
                    } while(cursor1.moveToNext());
                }

                preferencesJson.CardContent = carJsonList;
                preferencesJsonList.add(preferencesJson);
            } while(cursor.moveToNext());
        }

        updatePreMain.setPreferencesJson(preferencesJsonList);
        updatePreMain.setUserID(userid);
        Gson gson = new Gson();
        String values = gson.toJson(updatePreMain, UpdatePre.class);
        Log.e("values", "values" + values);
        return updatePreMain;
    }
    public final void updateQuery(String query)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        try {
            Log.e("query", "query" + query);
            Cursor cursor = database.rawQuery(query, null);
            Log.e("query", "query" + cursor.getCount());
            database.execSQL(query);
            database.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public final void deleteAllTables() {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL("delete from " + TABLE_FLASHCARD);
            database.execSQL("delete from " + TABLE_FLASHCARD_CONTENT);
            database.close();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public final int getActiveCard(int selectedCardId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD + " WHERE FlashCardSetID=" + selectedCardId + " AND ExpiryDate >= datetime('now') ORDER BY `decksortOrder` ASC";
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}

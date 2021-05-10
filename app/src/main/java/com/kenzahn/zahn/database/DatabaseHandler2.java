package com.kenzahn.zahn.database;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.kenzahn.zahn.model.CardContentRes;
import com.kenzahn.zahn.model.CardContentRes2;
import com.kenzahn.zahn.model.FlashcardJsonList;
import com.kenzahn.zahn.model.FlashcardJsonList2;
import com.kenzahn.zahn.model.PreferencesJsonRes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler2 extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "OnlineFlashcards.db2";
    Context context;
    private static final String TABLE_FLASHCARD = "flashcard2";
     public static final String TABLE_FLASHCARD_CONTENT2 = "flashcardcontent3";
    private static final String TAG = "DatabaseHandler2";

    public DatabaseHandler2(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {


        db.execSQL("Create table " + TABLE_FLASHCARD + " ( ExamID INTEGER PRIMARY KEY, "
                + "ExamTypeID TEXT,"
                + "Randomized TEXT,"
                + "Timed TEXT,"
                + "ExamHeader TEXT,"
                + "QuestionCount TEXT,"
                + "ExamName TEXT,"
                + "Version TEXT,"
                + "Active TEXT,"
                + "ExamModuleID TEXT,"
                + "CompletedCards INTEGER,"
                + "Status TEXT,"
                + "decksortOrder INTEGER,"
                + "OriginalDeckOrder INTEGER)");

        db.execSQL("Create table " + TABLE_FLASHCARD_CONTENT2 + " ( ExamQuestionID INTEGER PRIMARY KEY, "
                + "CreateDate TEXT,"
                + "AnswerA TEXT,"
                + "AnswerB TEXT,"
                + "SortOrder INTEGER,"
                + "AnswerC TEXT,"
                + "AnswerD TEXT,"
                + "ExamID TEXT,"
                + "CorrectAnswer TEXT,"
                + "Explanation TEXT,"
                + "Question TEXT,"
                + "ExamCaseID TEXT,"
                + "SelectedAnswer TEXT,"
                + "isKnownContent INTEGER DEFAULT 0,"
                + "isKnownReadCount INTEGER DEFAULT 0,"
                + "OriginalCardOrder INTEGER,"
                + "AnswerI TEXT,"
                + "AnswerII TEXT,"
                + "AnswerIII TEXT,"
                + "AnswerIV TEXT,"
                + "AnswerV TEXT,"
                + "AnswerVI TEXT,"
                + "AnswerE TEXT,"
                + "AnswerF TEXT)");



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


    public final ArrayList<FlashcardJsonList2> getFlashCardList() {
        ArrayList<FlashcardJsonList2> flashcardJsonList = new ArrayList<FlashcardJsonList2>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD + " ORDER BY `decksortOrder` ASC";
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, (String[]) null);
        if (cursor.moveToFirst())
        {
            do {
                FlashcardJsonList2 flashcardJsonList1 = new FlashcardJsonList2();
                flashcardJsonList1.setExamID(cursor.getString(0));
                flashcardJsonList1.setExamTypeID(cursor.getString(1));
                flashcardJsonList1.setRandomized(cursor.getString(2));
                flashcardJsonList1.setTimed(cursor.getString(3));
                flashcardJsonList1.setExamHeader(cursor.getString(4));
                flashcardJsonList1.setQuestionCount(cursor.getString(5));
                flashcardJsonList1.setExamName(cursor.getString(6));
                flashcardJsonList1.setVersion(cursor.getString(7));
                flashcardJsonList1.setActive(cursor.getString(8));
                flashcardJsonList1.setExamModuleID(cursor.getString(9));
                flashcardJsonList1.setCompletedCards(cursor.getInt(10));
                flashcardJsonList1.setCardContent(new ArrayList<CardContentRes2>());
                flashcardJsonList1.setStatus(cursor.getString(11));
                flashcardJsonList1.setDeckSortOrder(cursor.getString(12));
                flashcardJsonList1.setOriginalDeckOrder(cursor.getString(12));
                flashcardJsonList.add(flashcardJsonList1);
            } while (cursor.moveToNext());
        }

        return flashcardJsonList;
    }

    public final ArrayList<CardContentRes2> getFlashCardListContent(String examId)
    {
        Log.e("FlashCardId",""+examId);
        ArrayList<CardContentRes2> flashcardJsonList = new ArrayList<CardContentRes2>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT2 + " WHERE ExamID=" + examId + " ORDER BY `SortOrder` ASC ";
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(selectquery, null);

        if (cursor.moveToFirst())
        {
            do {
                CardContentRes2 flashcardJsonList1 = new CardContentRes2();
                flashcardJsonList1.setExamQuestionID(cursor.getString(0));
                flashcardJsonList1.setCreateDate(cursor.getString(1));
                flashcardJsonList1.setAnswerA(cursor.getString(2));
                flashcardJsonList1.setAnswerB(cursor.getString(3));
                flashcardJsonList1.setSortOrder(cursor.getString(4));
                flashcardJsonList1.setAnswerC(cursor.getString(5));
                flashcardJsonList1.setAnswerD(cursor.getString(6));
                flashcardJsonList1.setExamID(cursor.getString(7));
                flashcardJsonList1.setCorrectAnswer(cursor.getString(8));
                flashcardJsonList1.setExplanation(cursor.getString(9));
                flashcardJsonList1.setQuestion(cursor.getString(10));
                flashcardJsonList1.setExamCaseID(cursor.getString(11));
                flashcardJsonList1.setSelectedAnswer(cursor.getString(12));
                flashcardJsonList1.setKnownContent(cursor.getInt(13)==1);
                flashcardJsonList1.setIsKnownReadCountvar(cursor.getInt(14));
                flashcardJsonList1.setOriginalCardOrder(cursor.getString(15));
                flashcardJsonList1.setAnswerI(cursor.getString(16));
                flashcardJsonList1.setAnswerII(cursor.getString(17));
                flashcardJsonList1.setAnswerIII(cursor.getString(18));
                flashcardJsonList1.setAnswerIV(cursor.getString(19));
                flashcardJsonList1.setAnswerV(cursor.getString(20));
                flashcardJsonList1.setAnswerVI(cursor.getString(21));
                flashcardJsonList1.setAnswerE(cursor.getString(22));
                flashcardJsonList1.setAnswerF(cursor.getString(23));
                flashcardJsonList.add(flashcardJsonList1);
            } while (cursor.moveToNext());
        }

        return flashcardJsonList;
    }


    public final ArrayList<CardContentRes2> getFlashCardListContentLearned(String flashCardSetID)
    {
        ArrayList<CardContentRes2> flashcardJsonList = new ArrayList<CardContentRes2>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT2 + " WHERE ExamID=" + flashCardSetID + " ORDER BY `ExamQuestionID` ASC ";
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, (String[]) null);
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getInt(13)==1)
                {
                    CardContentRes2 flashcardJsonList1 = new CardContentRes2();
                    flashcardJsonList1.setExamQuestionID(cursor.getString(0));
                    flashcardJsonList1.setCreateDate(cursor.getString(1));
                    flashcardJsonList1.setAnswerA(cursor.getString(2));
                    flashcardJsonList1.setAnswerB(cursor.getString(3));
                    flashcardJsonList1.setSortOrder(cursor.getString(4));
                    flashcardJsonList1.setAnswerC(cursor.getString(5));
                    flashcardJsonList1.setAnswerD(cursor.getString(6));
                    flashcardJsonList1.setExamID(cursor.getString(7));
                    flashcardJsonList1.setCorrectAnswer(cursor.getString(8));
                    flashcardJsonList1.setExplanation(cursor.getString(9));
                    flashcardJsonList1.setQuestion(cursor.getString(10));
                    flashcardJsonList1.setExamCaseID(cursor.getString(11));
                    flashcardJsonList1.setSelectedAnswer(cursor.getString(12));
                    flashcardJsonList1.setKnownContent(cursor.getInt(13)==1);
                    flashcardJsonList1.setIsKnownReadCountvar(cursor.getInt(14));
                    flashcardJsonList1.setOriginalCardOrder(cursor.getString(15));
                    flashcardJsonList1.setAnswerI(cursor.getString(16));
                    flashcardJsonList1.setAnswerII(cursor.getString(17));
                    flashcardJsonList1.setAnswerIII(cursor.getString(18));
                    flashcardJsonList1.setAnswerIV(cursor.getString(19));
                    flashcardJsonList1.setAnswerV(cursor.getString(20));
                    flashcardJsonList1.setAnswerVI(cursor.getString(21));
                    flashcardJsonList1.setAnswerE(cursor.getString(22));
                    flashcardJsonList1.setAnswerF(cursor.getString(23));
                    flashcardJsonList.add(flashcardJsonList1);
                }

            } while (cursor.moveToNext());
        }

        return flashcardJsonList;
    }

    public final ArrayList<CardContentRes2> getFlashCardListContentToLearned(String flashCardSetID) {
        ArrayList<CardContentRes2> flashcardJsonList = new ArrayList<CardContentRes2>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT2 + " WHERE isKnownContent=0 AND ExamID=" + flashCardSetID + " ORDER BY `SortOrder` ASC ";
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery,  null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(13)==0)
                {
                    CardContentRes2 flashcardJsonList1 = new CardContentRes2();
                    flashcardJsonList1.setExamQuestionID(cursor.getString(0));
                    flashcardJsonList1.setCreateDate(cursor.getString(1));
                    flashcardJsonList1.setAnswerA(cursor.getString(2));
                    flashcardJsonList1.setAnswerB(cursor.getString(3));
                    flashcardJsonList1.setSortOrder(cursor.getString(4));
                    flashcardJsonList1.setAnswerC(cursor.getString(5));
                    flashcardJsonList1.setAnswerD(cursor.getString(6));
                    flashcardJsonList1.setExamID(cursor.getString(7));
                    flashcardJsonList1.setCorrectAnswer(cursor.getString(8));
                    flashcardJsonList1.setExplanation(cursor.getString(9));
                    flashcardJsonList1.setQuestion(cursor.getString(10));
                    flashcardJsonList1.setExamCaseID(cursor.getString(11));
                    flashcardJsonList1.setSelectedAnswer(cursor.getString(12));
                    flashcardJsonList1.setKnownContent(cursor.getInt(13)==1);
                    flashcardJsonList1.setIsKnownReadCountvar(cursor.getInt(14));
                    flashcardJsonList1.setOriginalCardOrder(cursor.getString(15));
                    flashcardJsonList1.setAnswerI(cursor.getString(16));
                    flashcardJsonList1.setAnswerII(cursor.getString(17));
                    flashcardJsonList1.setAnswerIII(cursor.getString(18));
                    flashcardJsonList1.setAnswerIV(cursor.getString(19));
                    flashcardJsonList1.setAnswerV(cursor.getString(20));
                    flashcardJsonList1.setAnswerVI(cursor.getString(21));
                    flashcardJsonList1.setAnswerE(cursor.getString(22));
                    flashcardJsonList1.setAnswerF(cursor.getString(23));
                    flashcardJsonList.add(flashcardJsonList1);
                }

            } while (cursor.moveToNext());
        }

        return flashcardJsonList;
    }

    public final int getReadCountMainDeck(String flashCardSetID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT2 + " WHERE isKnownReadCount=1 AND ExamID=" + flashCardSetID + ' ';
        Log.e(TAG, "query" + selectquery);
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery,null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public final int getReadCount(String flashCardSetID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT2 + " WHERE isKnownContent=0 AND ExamID=" + flashCardSetID + ' ';
        Log.e(TAG, "query" + selectquery);
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public final int getReadCountToLearn(String flashCardSetID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT2 + " WHERE isKnownContent=1 AND ExamID=" + flashCardSetID + " ORDER BY `SortOrder` ASC ";
        Log.e(TAG, "query" + selectquery);
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public final int getReadCountAll(String flashCardSetID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selectquery = "SELECT * FROM " + TABLE_FLASHCARD_CONTENT2 + " WHERE ExamID=" + flashCardSetID + " ORDER BY `SortOrder` ASC ";
        Log.e(TAG, "query" + selectquery);
        Cursor cursor = sqLiteDatabase.rawQuery(selectquery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
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
            database.execSQL("delete from " + TABLE_FLASHCARD_CONTENT2);
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

    public void insertFlashCard_(ArrayList<FlashcardJsonList2> result) {
        try {
            Log.e(TAG, "result" + result.size());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            for (int i = 0; i < result.size(); ++i)
            {
                try {
                    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("ExamID", (result.get(i)).getExamID());
                    values.put("ExamTypeID", (result.get(i)).getExamTypeID());
                    values.put("Randomized", (result.get(i)).getRandomized());
                    values.put("Timed", (result.get(i)).getTimed());
                    values.put("ExamHeader", (result.get(i)).getExamHeader());
                    values.put("QuestionCount", (result.get(i)).getQuestionCount());
                    values.put("ExamName", (result.get(i)).getExamName());
                    values.put("Version", (result.get(i)).getVersion());
                    values.put("Active", (result.get(i)).getActive());
                    values.put("ExamModuleID", (result.get(i)).getExamModuleID());
                   // Date date = inputFormatter.parse((result.get(i)).getExpiryDate());
                   // @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   // String output = outputFormatter.format(date);
                   /* if (i == 0) {
                        output = "2019-10-21 18:54:14";
                    }*/

                   // values.put("ExpiryDate", output);
                    values.put("CompletedCards",result.get(i).getCompletedCards());
                    values.put("Status", (result.get(i)).getStatus());
                    values.put("decksortOrder", (result.get(i)).getDeckSortOrder());
                    values.put("OriginalDeckOrder", (result.get(i)).getDeckSortOrder());
                    sqLiteDatabase.insert(TABLE_FLASHCARD, null, values);
                    sqLiteDatabase.close();
                    for (int j = 0;j<result.get(i).getCardContent().size();j++)
                    {
                        SQLiteDatabase sqLiteDatabasePref = this.getWritableDatabase();
                        ContentValues valuesPref = new ContentValues();
                        valuesPref.put("ExamQuestionID", (result.get(i)).getCardContent().get(j).getExamQuestionID());
                        valuesPref.put("CreateDate", (result.get(i)).getCardContent().get(j).getCreateDate());
                        valuesPref.put("AnswerA", (result.get(i)).getCardContent().get(j).getAnswerA());
                        valuesPref.put("AnswerB", (result.get(i)).getCardContent().get(j).getAnswerB());
                        valuesPref.put("SortOrder", (result.get(i)).getCardContent().get(j).getSortOrder());
                        valuesPref.put("AnswerC", (result.get(i)).getCardContent().get(j).getAnswerC());
                        valuesPref.put("AnswerD", (result.get(i)).getCardContent().get(j).getAnswerD());
                        valuesPref.put("ExamID", (result.get(i)).getCardContent().get(j).getExamID());
                        valuesPref.put("CorrectAnswer", (result.get(i)).getCardContent().get(j).getCorrectAnswer());
                        valuesPref.put("Explanation", (result.get(i).getCardContent().get(j).getExplanation()));
                        valuesPref.put("Question", (result.get(i)).getCardContent().get(j).getQuestion());
                        valuesPref.put("ExamCaseID", (result.get(i)).getCardContent().get(j).getExamCaseID());
                        valuesPref.put("SelectedAnswer", "SAns");
                        valuesPref.put("isKnownContent", 0);
                        valuesPref.put("isKnownReadCount",0);
                        valuesPref.put("OriginalCardOrder", (result.get(i)).getCardContent().get(j).getSortOrder());
                        valuesPref.put("AnswerI",(result.get(i)).getCardContent().get(j).getAnswerI());
                        valuesPref.put("AnswerII",(result.get(i)).getCardContent().get(j).getAnswerII());
                        valuesPref.put("AnswerIII",(result.get(i)).getCardContent().get(j).getAnswerIII());
                        valuesPref.put("AnswerIV",(result.get(i)).getCardContent().get(j).getAnswerIV());
                        valuesPref.put("AnswerV",(result.get(i)).getCardContent().get(j).getAnswerV());
                        valuesPref.put("AnswerVI",(result.get(i)).getCardContent().get(j).getAnswerVI());
                        valuesPref.put("AnswerE",(result.get(i)).getCardContent().get(j).getAnswerE());
                        valuesPref.put("AnswerF",(result.get(i)).getCardContent().get(j).getAnswerF());
                        sqLiteDatabasePref.insert(TABLE_FLASHCARD_CONTENT2, null, valuesPref);
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

      public void getData()
    {
        SQLiteDatabase db  = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor c = db.query(DatabaseHandler2.TABLE_FLASHCARD_CONTENT2, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                String  District = c.getString(1);
                String  District2 = c.getString(2);

            } while (c.moveToNext());
            }

    }

    public boolean doesTableExist(SQLiteDatabase db, String tableName)
    {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }





}

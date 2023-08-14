package com.example.lab11.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME= "Quiz.db";

    private SQLiteDatabase db;
    public Database( Context context) {
        super(context, DATABASE_NAME, null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " +
                UserContact.UserTable.TABLE_NAME1 + " ( " +
                UserContact.UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserContact.UserTable.COL_2 + " TEXT, " +
               UserContact.UserTable.COL_3 + " TEXT " + ")";
        db.execSQL(SQL_CREATE_USER_TABLE);
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuizContact.QuestionsTable.TABLE_NAME2 + " ( " +
                QuizContact.QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContact.QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuizContact.QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuizContact.QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuizContact.QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuizContact.QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" + ")";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        this.db=db;
        db.execSQL(" DROP TABLE IF EXISTS " + UserContact.UserTable.TABLE_NAME1);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + QuizContact.QuestionsTable.TABLE_NAME2);
        onCreate(db);
    }
    public long addUser(String user, String password){
         db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", user);
        contentValues.put("password", password);
        long res = db.insert("users", null , contentValues);
        db.close();
        return  res;
    }


    public boolean checkUser(String username, String password){
        String[] columns = {UserContact.UserTable.COL_2};
        db = getReadableDatabase();
        String selection = UserContact.UserTable.COL_2 + "=?" + "and " + UserContact.UserTable.COL_3 + "=?";
        String[] selectionArgs ={username, password} ;
        Cursor cursor = db.query(UserContact.UserTable.TABLE_NAME1, columns, selection, selectionArgs,
                null,
                null, null);
        int count = cursor.getCount();
        db.close();

        if(count>0)
            return true;
        else
            return  false;
    }


    private  void fillQuestionsTable (){
        Question q1 = new Question ("3+8" ,
                "4", "11", "12", 2);
        addQuestion(q1);
        Question q2 = new Question ("8+2",
                "10", "9", "11", 1);
        addQuestion(q2);
        Question q3 = new Question ("5-4" ,
                "6", "5", "1", 3);
        addQuestion(q3);
        Question q4 = new Question ("12-6" ,
                "18", "10", "6", 3);
        addQuestion(q4);
        Question q5 = new Question ("10+1" ,
                "11", "9", "3", 1);
        addQuestion(q5);
        Question q6 = new Question ("10-3" ,
                "4", "13", "7", 3);
        addQuestion(q6);
        Question q7 = new Question ("9+3" ,
                "11", "6", "12", 3);
        addQuestion(q7);
        Question q8 = new Question ("6-5" ,
                "11", "1", "12", 2);
        addQuestion(q8);
        Question q9 = new Question ("5+5" ,
                "10", "0", "8", 1);
        addQuestion(q9);
        Question q10 = new Question ("11-6" ,
                "8", "4", "5", 3);
        addQuestion(q10);
        Question q11 = new Question ("10+2" ,
                "8", "11", "12", 2);
        addQuestion(q11);
        Question q12 = new Question ("3*3",
                "10", "9", "11", 2);
        addQuestion(q12);
        Question q13 = new Question ("6*2" ,
                "6", "3", "12", 3);
        addQuestion(q13);
        Question q14 = new Question ("12-5" ,
                "17", "10", "7", 3);
        addQuestion(q14);
        Question q15 = new Question ("8-2" ,
                "6", "10", "5", 1);
        addQuestion(q15);
        Question q16 = new Question ("4*3" ,
                "1", "13", "12", 3);
        addQuestion(q16);
        Question q17 = new Question ("12-9" ,
                "11", "3", "12", 2);
        addQuestion(q17);
        Question q18 = new Question ("6+5" ,
                "11", "1", "12", 1);
        addQuestion(q18);
        Question q19 = new Question ("5*2" ,
                "10", "0", "8", 1);
        addQuestion(q19);
        Question q20 = new Question ("8-4" ,
                "8", "4", "5", 2);
        addQuestion(q20);
        Question q21 = new Question ("10/2" ,
                "4", "5", "12", 2);
        addQuestion(q21);
        Question q22 = new Question ("8/4",
                "2", "9", "11", 1);
        addQuestion(q22);
        Question q23 = new Question ("5*2" ,
                "6", "5", "10", 3);
        addQuestion(q23);
        Question q24 = new Question ("6-2" ,
                "8", "4", "6", 2);
        addQuestion(q24);
        Question q25 = new Question ("2*6" ,
                "11", "12", "3", 2);
        addQuestion(q25);
        Question q26 = new Question ("5/5" ,
                "4", "13", "1", 3);
        addQuestion(q26);
        Question q27 = new Question ("12/4" ,
                "3", "6", "12", 1);
        addQuestion(q27);
        Question q28 = new Question ("10/10" ,
                "11", "1", "12", 2);
        addQuestion(q28);
        Question q29 = new Question ("12-7" ,
                "5", "0", "8", 1);
        addQuestion(q29);
        Question q30 = new Question ("11-1" ,
                "8", "4", "10", 3);
        addQuestion(q30);

    }


    private void addQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuizContact.QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuizContact.QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuizContact.QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuizContact.QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuizContact.QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuizContact.QuestionsTable.TABLE_NAME2, null,cv);
    }


    @SuppressLint("Range")
    public ArrayList<Question> getAllQuestions(){
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ QuizContact.QuestionsTable.TABLE_NAME2, null);

        if(c.moveToFirst()){
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuizContact.QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContact.QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContact.QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuizContact.QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContact.QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

}

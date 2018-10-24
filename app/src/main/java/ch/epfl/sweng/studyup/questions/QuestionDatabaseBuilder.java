package ch.epfl.sweng.studyup.questions;

import android.arch.persistence.room.Room;
import android.content.Context;

public abstract class QuestionDatabaseBuilder {

    private static final String databaseName = "Question Database";
    private static QuestionDatabase instance = null;

    public static QuestionDatabase get(Context c){
        if (instance == null){
            instance = Room.databaseBuilder(c, QuestionDatabase.class, databaseName).build();
            return instance;
        }else{
            return instance;
        }
    }
}
package ch.epfl.sweng.studyup.questions;


import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;


public abstract class QuestionParser {

    private static final String TAG = "QuestionParser";

    /**
     * Retrieve a LiveData list of questions. Use the Observer pattern to be notified when the list is changed
     * @param c The context of the activity that calls this method
     * @return A LiveData wrapper of the list of questions from the Room Database
     */
    public static LiveData<List<Question>> parseQuestionsLiveData(Context c) {
        QuestionDAO database = QuestionDatabase.get(c).questionDAO();
        return database.getAllLiveData();
    }


    /**
     * Write the list of questions on the disk to save them.
     *
     * @param list    The list of questions
     * @param c       The context of the app (to the FilesDir)
     * @return if the write has succeeded or not
     */
    public static void writeQuestions(final List<Question> list, final Context c) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                    QuestionDatabase.get(c).questionDAO().nukeTable();
                QuestionDatabase.get(c).questionDAO().insertAll(list);
            }
        });
    }

}

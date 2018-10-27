package ch.epfl.sweng.studyup.questions;


import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


public abstract class QuestionParser {

    private static final String TAG = "QuestionParser";

    /**
     * Retrieve the list of questions from the data file
     *
     * @param c                 The context of the app (to get the FilesDir)
     * @return The list of questions or null if the file has not the correct format
     * @Deprecated Must not be called in the main thread otherwise an exception will occur !
     */

    @Deprecated
    public static List<Question> parseQuestions(Context c) {
        QuestionDAO database = QuestionDatabase.get(c).questionDAO();
        ArrayList<Question> list = new ArrayList<>();
        list.addAll(database.getAll());

        return list;
    }

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
                QuestionDatabase.get(c).questionDAO().insertAll(list);
            }
        });
    }

}

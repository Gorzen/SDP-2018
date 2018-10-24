package ch.epfl.sweng.studyup.questions;


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
     */
    public static List<Question> parseQuestions(Context c) {
        final QuestionDAO database = QuestionDatabaseBuilder.get(c).questionDAO();
        final ArrayList<Question> list = new ArrayList<>();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                list.addAll(database.getAll());
            }
        });

        return list;
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
                QuestionDatabaseBuilder.get(c).questionDAO().insertAll(list);
            }
        });
    }

}

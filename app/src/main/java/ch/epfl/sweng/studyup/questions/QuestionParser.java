package ch.epfl.sweng.studyup.questions;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public abstract class QuestionParser {

    /**
     * Format: line by line:
     * -The path of the image
     * -"true" if True/False question, "false" (or anything else) for MCQ
     * -The correct answer (unsigned int between 0 and 4)
     */

    public static final String fileName = "questions.info";
    private static final String TAG = "QuestionParser";

    /**
     * Retrieve the list of questions from the data file
     *
     * @param c                 The context of the app (to get the FilesDir)
     * @param checkIfFileExists Throw an IllegalArgumentException if the image in the file doesn't exist.
     * @return The list of questions or null if the file has not the correct format
     */
    public static List<Question> parseQuestions(Context c, boolean checkIfFileExists) {
        ArrayList<Question> list = new ArrayList<>();
        try {
            FileInputStream inputStream = c.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            Uri imageUri;
            boolean trueFalse;
            int answerNumber;
            while ((line = bufferedReader.readLine()) != null) {
                File nf = new File(line);
                if (checkIfFileExists && !nf.exists()) {
                    bufferedReader.close();
                    isr.close();
                    throw new FileNotFoundException("The image for the question has not been found");
                }
                imageUri = Uri.parse(line);

                if ((line = bufferedReader.readLine()) == null) {
                    bufferedReader.close();
                    isr.close();
                    Log.e(TAG, "While reading the questions file: second line is empty");
                    return null;
                }
                trueFalse = Boolean.parseBoolean(line);

                if ((line = bufferedReader.readLine()) == null) {
                    bufferedReader.close();
                    isr.close();
                    Log.e(TAG, "While reading the questions  file: third line is empty");
                    return null;
                }
                answerNumber = Integer.parseInt(line);

                list.add(new Question(imageUri, trueFalse, answerNumber));
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: \n");
            for (StackTraceElement elem : e.getStackTrace()) {
                System.err.println("-File:" + elem.getClassName() + " -Method:L" + elem.getMethodName() + " -Line:" + elem.getLineNumber());
            }
            return null;
        }
        return list;
    }

    /**
     * Retrieve the list of questions from the data file
     *
     * @param c The context of the app (to get the FilesDir)
     * @return The list of questions or null if the file has not the correct format
     * @throws FileNotFoundException if the file does not exist
     */
    public static List<Question> parseQuestions(Context c) throws FileNotFoundException {
        //the default value is true
        return parseQuestions(c, true);
    }

    /**
     * Write the list of questions on the disk to save them.
     *
     * @param list    The list of questions
     * @param c       The context of the app (to the FilesDir)
     * @param replace Overwrites the file if true
     * @return if the write has succeeded or not
     */
    public static boolean writeQuestions(List<Question> list, Context c, boolean replace) {
        try {
            File file = new File(c.getFilesDir(), fileName);
            //The createNewFile() method return true and create the file if and only if the file doesn't yet exists
            if (replace && !file.createNewFile()) {
                if (!file.delete()) return false;
                if (!file.createNewFile()) {
                    Log.e(TAG, "Unable to create the file.");
                    return false;
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            for (Question q : list) {
                String text = q.getQuestionUri().toString() + "\n" + q.isTrueFalseQuestion() + "\n" + q.getAnswer() + "\n";
                writer.write(text);
            }
            writer.close();

        } catch (IOException e) {
            Log.e(TAG, "IOException while writing the file\n" + e.getMessage());
            return false;
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException while writing the file\n" + e.getMessage());
            return false;
        }

        return true;
    }

}

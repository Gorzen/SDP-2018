package ch.epfl.sweng.studyup.question;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public abstract class QuestionParser {

    /**
     * Format: line by line:
     * -The path of the image
     * -"true" if True/False question, "false" (or anything else) for MCQ
     * -The correct answer (unsigned int between 0 and 4)
     */

    public static String fileName = "questions.info";
    private static final String TAG = "QuestionParser";

    /**
     * Retrieve the list of questions from the data file
     * @param c The context of the app (to get the FilesDir)
     * @return The list of questions or null if the file has not the correct format
     * @throws FileNotFoundException if the file does not exist
     */
    public static List<Question> parseQuestions(Context c) throws FileNotFoundException {
        FileInputStream inputStream = c.openFileInput(fileName);
        Toast toast = Toast.makeText(c, "Error while opening the file. It may be corrupted", Toast.LENGTH_SHORT);
        ArrayList<Question> list = new ArrayList<>();
        try {
            InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            Uri imageUri;
            boolean trueFalse;
            int answerNumber;

            while ((line = bufferedReader.readLine()) != null) {
                File nf = new File(line);
                if(!nf.exists()){throw new FileNotFoundException("The image for the question has not be found"); }
                imageUri = Uri.fromFile(new File(line));

                if ((line = bufferedReader.readLine()) == null) {
                    Log.e(TAG, "While reading the file: second line is empty");
                    toast.show();
                    return null;
                }
                trueFalse = Boolean.parseBoolean(line);

                if ((line = bufferedReader.readLine()) == null) {
                    Log.e(TAG, "While reading the file: third line is empty");
                    toast.show();
                    return null;
                }
                answerNumber = Integer.parseInt(line);

                list.add(new Question(imageUri, trueFalse, answerNumber));
            }

        }catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Unsupported encoding: \n" + e.getMessage());

            /*
                Version with stackTrace:
                Log.e(TAG, "Unsupported encoding: \n");
                for(StackTraceElement elem: e.getStackTrace()) {
                    System.err.println("-File:"+elem.getClassName()+" -Method:L"+elem.getMethodName()+" -Line:"+elem.getLineNumber());
                }
            */

            toast.show();
            return null;
        }catch(IOException e) {
            Log.e(TAG, "IOException while reading the file\n" + e.getMessage());
            toast.show();
            return null;
        }
        return list;
    }

    /**
     * Write the list of questions on the disk to save them.
     * @param list The list of questions
     * @param c The context of the app (to the FilesDir)
     * @param replace Overwrites the file if true
     * @return  if the write has succeeded or not
     */
    public static boolean writeQuestions(List<Question> list, Context c, boolean replace) {
        try{
            File file = new File(c.getFilesDir(), fileName);
            //The createNewFile() method return true and create the file if and only if the file doesn't yet exists
            if(!file.createNewFile() && replace) {
                if (!file.delete()) return false;
                if (!file.createNewFile()) {
                    System.err.println("Unable to create the file.");
                    return false;
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            for (Question q : list) {
                    String text = q.getQuestionUri().toString() + "\n" + q.isTrueFalseQuestion() + "\n" + q.getAnswer() + "\n";
                    writer.write(text);
            }
            writer.close();

        } catch(IOException e){
            Log.e(TAG, "IOException while writing the file\n" + e.getMessage());
            return false;
        } catch(SecurityException e) {
            Log.e(TAG, "SecurityException while writing the file\n" + e.getMessage());
            return false;
        }

        return true;
    }

}

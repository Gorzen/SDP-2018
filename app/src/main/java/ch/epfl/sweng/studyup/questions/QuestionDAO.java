package ch.epfl.sweng.studyup.questions;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface QuestionDAO {

    @Query("SELECT * FROM question")
    List<Question> getAll();

    @Query("SELECT * FROM question")
    LiveData<List<Question>> getAllLiveData();

    /* Not used for now (so that we don't have to test it)

     @Query("SELECT * FROM question WHERE uid LIKE :name LIMIT 1")
    Question findByName(String name);

     @Update
    void update(Question question);

    @Delete
    void delete(Question question);

     */

    @Query("DELETE FROM question")
    public void nukeTable();

    @Insert
    void insertAll(List<Question> questions);

}

package ch.epfl.sweng.studyup.questions;

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

    @Query("SELECT * FROM question WHERE uid LIKE :name LIMIT 1")
    Question findByName(String name);

    @Insert
    void insertAll(List<Question> products);

    @Update
    void update(Question question);

    @Delete
    void delete(Question question);

}

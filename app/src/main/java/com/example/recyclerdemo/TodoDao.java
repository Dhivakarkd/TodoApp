package com.example.recyclerdemo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert
    void insertTodo(TodoNotes todoNotes);

    @Insert
    void insertMultiTodo(List<TodoNotes> todoNotesList);

    @Query("SELECT * FROM TodoNotes_table")
    List<TodoNotes> getallTodo();

    @Query("SELECT * FROM TodoNotes_table")
    LiveData<List<TodoNotes>> getallLiveTodo();

    @Query("SELECT * FROM TodoNotes_table WHERE Todo_primaryID LIKE :id")
    TodoNotes findTodobyID(int id);

    @Query("SELECT * FROM TodoNotes_table WHERE Todo_descrption LIKE :description")
    TodoNotes findTodobyID(String description);

    @Update
    void updataTodo(TodoNotes todoNotes);

    @Delete
    void removeTodo(TodoNotes todoNotes);
}

package service;


import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {


   List<Task> getHistory();
     Task getTask(int id);


     Epic getEpic(int epicId);

     Subtask getSubtask(int subId) ;

     int addNewTask(Task task) ;
     int addNewEpic(Epic epic);

     int addNewSubtask(Subtask subtask, int epicId);

     void updateTask(Task task) ;

     void updateEpic(Epic epic) ;

     void updateSubtask(Subtask subtask) ;

     ArrayList<Task> getAllTask() ;
     ArrayList<Subtask> getAllSubtask() ;
     ArrayList<Epic> getAllEpic() ;

     void clearTask() ;

     void clearEpic() ;

     void clearSubtask(int subId) ;

     void deleteTask(int id);
     void deleteEpic(int epicId) ;

     void deleteSubtask(int subId) ;

     ArrayList<Subtask> getAllSubtaskInEpic(int epicId);


    void updateEpicStatus(int epicId);




}






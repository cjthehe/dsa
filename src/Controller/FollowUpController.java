/**
 * FollowUpController class
 * Author: NG WEI NEE
 */

 package Controller;

 import ADT.LinkedList;
 import ADT.AVLTree;
 import Entity.FollowUpTask;
 import java.time.LocalDate;
 
 public class FollowUpController {
     private int counter = 1;
 
     private final LinkedList<FollowUpTask> pending = new LinkedList<>();
     private final AVLTree<String, FollowUpTask> index = new AVLTree<>();
     private final LinkedList<FollowUpTask> all = new LinkedList<>();
 
     public FollowUpTask add(String patientId, String doctorId, String note) {
         String id = nextId();
         FollowUpTask t = new FollowUpTask(id, patientId, doctorId, note);
         index.insert(id, t);
         pending.add(t);      
         all.add(t);
         return t;
     }
 
     public FollowUpTask add(String patientId, String doctorId, String description, LocalDate dueDate) {
         String id = nextId();
         FollowUpTask t = new FollowUpTask(id, patientId, doctorId, description, dueDate);
         index.insert(id, t);
         pending.add(t);
         all.add(t);
         return t;
     }
 
     public boolean markCompleted(String taskId) {
         FollowUpTask t = index.search(taskId);
         if (t == null) return false;
         t.setCompleted(true);
         return true;
     }
 
     public boolean update(String taskId, String description, LocalDate dueDate) {
         FollowUpTask t = index.search(taskId);
         if (t == null) return false;
         if (description != null && !description.trim().isEmpty()) {
             t.setDescription(description.trim());
         }
         if (dueDate != null) {
             t.setDueDate(dueDate);
         }
         return true;
     }
 
     public boolean delete(String taskId) {
         FollowUpTask t = index.search(taskId);
         if (t == null) return false;
         index.delete(taskId);
         removeFromAllById(taskId);
         removeFromPendingById(taskId); 
         return true;
     }
 
     public FollowUpTask peek() {
         return pending.isEmpty() ? null : pending.get(0);
     }
 
     public LinkedList<FollowUpTask> listAll() {
         return all;
     }
 
     public LinkedList<FollowUpTask> listByStatus(String status) {
         LinkedList<FollowUpTask> result = new LinkedList<>();
         for (int i = 0; i < all.size(); i++) {
             FollowUpTask t = all.get(i);
             String s = t.getStatus();
             if (status == null || status.isEmpty()) {
                 result.add(t);
             } else if (s != null && s.equalsIgnoreCase(status)) {
                 result.add(t);
             }
         }
         return result;
     }
 
     public LinkedList<FollowUpTask> listOverdue(LocalDate today) {
         LinkedList<FollowUpTask> result = new LinkedList<>();
         for (int i = 0; i < all.size(); i++) {
             FollowUpTask t = all.get(i);
             LocalDate due = t.getDueDate();
             if (due != null && due.isBefore(today) && !t.isCompleted()) {
                 result.add(t);
             }
         }
         return result;
     }
 
     private String nextId() {
         return "T" + String.format("%03d", counter++);
     }
 
     private boolean removeFromPendingById(String taskId) {
         for (int i = 0; i < pending.size(); i++) {
             FollowUpTask cur = pending.get(i);
             if (cur.getTaskId().equals(taskId)) {
                 pending.remove(i);
                 return true;
             }
         }
         return false;
     }
 
     private boolean removeFromAllById(String taskId) {
         for (int i = 0; i < all.size(); i++) {
             FollowUpTask cur = all.get(i);
             if (cur.getTaskId().equals(taskId)) {
                 all.remove(i);
                 return true;
             }
         }
         return false;
        }
 }
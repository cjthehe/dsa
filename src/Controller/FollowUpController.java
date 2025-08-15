package Controller;

import ADT.LinkedList;
import ADT.AVLTree;
import Entity.FollowUpTask;

public class FollowUpController {
    private int counter = 1;

    private final LinkedList<FollowUpTask> pending = new LinkedList<>();
    private final AVLTree<String, FollowUpTask> index = new AVLTree<>();
    private final LinkedList<FollowUpTask> all = new LinkedList<>();

    public FollowUpTask add(String patientId, String doctorId, String note) {
        String id = nextId();
        FollowUpTask t = new FollowUpTask(id, patientId, doctorId, note);
        index.insert(id, t);
        pending.add(t);            // enqueue to tail
        all.add(t);
        return t;
    }

    public boolean markCompleted(String taskId) {
        FollowUpTask t = index.search(taskId);
        if (t == null) return false;
        t.setCompleted(true);
        // OPTIONAL: if you only want uncompleted items in the pending queue, uncomment:
        // removeFromPendingById(taskId);
        return true;
    }

    public boolean delete(String taskId) {
        FollowUpTask t = index.search(taskId);
        if (t == null) return false;
        index.delete(taskId);
        removeFromAllById(taskId);
        removeFromPendingById(taskId);   // <-- replace pending.remove(t)
        return true;
    }

    public FollowUpTask peek() {
        return pending.isEmpty() ? null : pending.get(0);
    }

    public LinkedList<FollowUpTask> listAll() {
        return all; // Return the ADT LinkedList directly
    }

    private String nextId() {
        return "F" + String.format("%04d", counter++);
    }

    // --- helper: remove by id using index + remove(int)
    private boolean removeFromPendingById(String taskId) {
        for (int i = 0; i < pending.size(); i++) {
            FollowUpTask cur = pending.get(i);
            // adjust getter name if different
            if (cur.getTaskId().equals(taskId)) {
                pending.remove(i);        // uses LinkedList.remove(int)
                return true;
            }
        }
        return false;
    }

    // --- helper: remove by id from all list
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






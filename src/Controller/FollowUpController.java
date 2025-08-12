package Controller;

import ADT.ArrayQueue;
import ADT.AVLTree;
import Entity.FollowUpTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple follow-up tracker with queue for pending and index for lookup.
 */
public class FollowUpController {
    private int counter = 1;
    private final ArrayQueue<FollowUpTask> pending = new ArrayQueue<>(1000);
    private final AVLTree<String, FollowUpTask> index = new AVLTree<>();
    private final List<FollowUpTask> all = new ArrayList<>();

    public FollowUpTask add(String patientId, String doctorId, String note) {
        String id = nextId();
        FollowUpTask t = new FollowUpTask(id, patientId, doctorId, note);
        index.insert(id, t);
        pending.enqueue(t);
        all.add(t);
        return t;
    }

    public boolean markCompleted(String taskId) {
        FollowUpTask t = index.search(taskId);
        if (t == null) return false;
        t.setCompleted(true);
        return true;
    }

    public boolean delete(String taskId) {
        FollowUpTask t = index.search(taskId);
        if (t == null) return false;
        index.delete(taskId);
        all.remove(t);
        return true;
    }

    public FollowUpTask peek() { return pending.peek(); }

    public List<FollowUpTask> listAll() { return new ArrayList<>(all); }

    private String nextId() { return "F" + String.format("%04d", counter++); }
}




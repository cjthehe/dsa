package asgm;

import java.util.ArrayList;

public class Stack {
    private ArrayList<Integer> stack;
    
    public Stack() {
        stack = new ArrayList<>();
    }
    
    public void push(int value) {
        stack.add(value);
    }
    
    public int pop() {
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        return stack.remove(stack.size() - 1);
    }
    
    public int peek() {
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        return stack.get(stack.size() - 1);
    }
    
    public boolean isEmpty() {
        return stack.isEmpty();
    }
    
    @Override
    public String toString() {
        return stack.toString();
    }
} 
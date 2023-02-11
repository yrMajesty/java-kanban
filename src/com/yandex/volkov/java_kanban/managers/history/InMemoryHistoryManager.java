package com.yandex.volkov.java_kanban.managers.history;

import com.yandex.volkov.java_kanban.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
            if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        Node<Task> node = linkLast(task);
        historyMap.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.get(id));
        historyMap.remove(id);
    }


    @Override
    public List<Task> getHistory() {
        return getTasks();
    }


    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }

        return tasks;
    }


    private Node<Task> linkLast(Task task) {
        Node<Task> newNode = new Node<>(tail, task, null);

        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }

        tail = newNode;

        return newNode;
    }


    private void removeNode(Node<Task> node) {
        if (node == null) return;
        if (node.equals(head)) {
            head = node.next;

            if (node.next != null) {
                node.next.prev = null;
            }
        } else {
            node.prev.next = node.next;

            if (node.next != null) {
                node.next.prev = node.prev;
            }
        }
    }


}

class Node<Task> {

    protected Task data;
    protected Node prev;
    protected Node next;

    Node(Node<Task> prev, Task data, Node<Task> next) {
        this.data = data;
        this.prev = prev;
        this.next = next;
    }


}






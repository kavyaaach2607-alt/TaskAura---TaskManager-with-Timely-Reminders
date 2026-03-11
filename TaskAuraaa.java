
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
/**
 * TASKAURA - Task Manager with Timely Reminders
 *
 * DSA Concepts Used: Linked List | Stack | Queue | HashMap |Linear Search | Bubble Sort
 *   1. Singly Linked List  - stores tasks
 *   2. Stack               - undo last task
 *   3. Queue               - reminder queue (FIFO)
 *   4. HashMap             - fast lookup by task ID
 *   5. Linear Search       - search by name
 *   6. Bubble Sort         - sort tasks by date
 *
 * How to run:
 *   javac TaskAura.java
 *   java  TaskAura
 */
// -------------------------------------------------------
// Task - holds all details of one task
// -------------------------------------------------------
class Task {
    static int nextId = 1;
    int     id;
    String  name;
    String  desc;
    String  date;
    String  day;
    String  time;
    boolean done;
    Task(String name, String desc, String date, String day, String time) {
        this.id   = nextId++;
        this.name = name;
        this.desc = desc;
        this.date = date;
        this.day  = day;
        this.time = time;
        this.done = false;
    }
    String status() {
        if (done) {
            return "DONE";
        }
        return "PENDING";
    }
    void show() {
        System.out.println("  +-----------------------------------------+");
        System.out.println("  | ID     : " + id);
        System.out.println("  | Task   : " + name);
        System.out.println("  | Desc   : " + desc);
        System.out.println("  | Day    : " + day);
        System.out.println("  | Date   : " + date);
        System.out.println("  | Time   : " + time);
        System.out.println("  | Status : " + status());
        System.out.println("  +-----------------------------------------+");
    }
}
// -------------------------------------------------------
// Node - for singly linked list
// -------------------------------------------------------
class Node {
    Task data;
    Node next;
    Node(Task t) {
        this.data = t;
        this.next = null;
    }
}
// -------------------------------------------------------
// SinglyLinkedList - stores all tasks
// -------------------------------------------------------
class SinglyLinkedList {
    Node head;
    int  size;
    SinglyLinkedList() {
        head = null;
        size = 0;
    }
    // Add task at end
    void addTask(Task t) {
        Node newNode = new Node(t);
        if (head == null) {
            head = newNode;
        } else {
            Node cur = head;
            while (cur.next != null) {
                cur = cur.next;
            }
            cur.next = newNode;
        }
        size++;
    }
    // Delete task by ID (linear search)
    boolean deleteTask(int id) {
        if (head == null) {
            return false;
        }
        if (head.data.id == id) {
            head = head.next;
            size--;
            return true;
        }
        Node cur = head;
        while (cur.next != null) {
            if (cur.next.data.id == id) {
                cur.next = cur.next.next;
                size--;
                return true;
            }
            cur = cur.next;
        }
        return false;
    }
    // Linear search by name
    ArrayList<Task> searchByName(String keyword) {
        ArrayList<Task> result = new ArrayList<Task>();
        Node cur = head;
        while (cur != null) {
            if (cur.data.name.toLowerCase().contains(keyword.toLowerCase())) {
                result.add(cur.data);
            }
            cur = cur.next;
        }
        return result;
    }
    // Get all tasks as list
    ArrayList<Task> getAll() {
        ArrayList<Task> list = new ArrayList<Task>();
        Node cur = head;
        while (cur != null) {
            list.add(cur.data);
            cur = cur.next;
        }
        return list;
    }
    // Bubble sort tasks by date (string comparison dd-MM-yyyy)
    void bubbleSort() {
        if (head == null || head.next == null) {
            return;
        }
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            Node cur = head;
            while (cur.next != null) {
                String d1 = cur.data.date;
                String d2 = cur.next.data.date;
                // convert dd-MM-yyyy to yyyyMMdd for comparison
                String s1 = d1.length() == 10 ? d1.substring(6) + d1.substring(3, 5) + d1.substring(0, 2) : d1;
                String s2 = d2.length() == 10 ? d2.substring(6) + d2.substring(3, 5) + d2.substring(0, 2) : d2;
                if (s1.compareTo(s2) > 0) {
                    Task temp     = cur.data;
                    cur.data      = cur.next.data;
                    cur.next.data = temp;
                    swapped       = true;
                }
                cur = cur.next;
            }
        }
    }
}
// -------------------------------------------------------
// MAIN APPLICATION
// -------------------------------------------------------
public class TaskAuraaa {
    static SinglyLinkedList    list    = new SinglyLinkedList();
    static Stack<Task>         stack   = new Stack<Task>();
    static Queue<Task>         queue   = new LinkedList<Task>();
    static HashMap<Integer, Task> map  = new HashMap<Integer, Task>();
    static Scanner             sc      = new Scanner(System.in);
    static Timer               timer   = new Timer(true);
    public static void main(String[] args) {
        showBanner();
        startReminderTimer();
        boolean running = true;
        while (running) {
            showMenu();
            System.out.print("  Enter choice: ");
            String input = sc.nextLine().trim();
            int choice = 0;
            try {
                choice = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("  Please enter a number.");
                continue;
            }
            if (choice == 1) {
                addTask();
            } else if (choice == 2) {
                deleteTask();
            } else if (choice == 3) {
                markDone();
            } else if (choice == 4) {
                viewAll();
            } else if (choice == 5) {
                viewPending();
            } else if (choice == 6) {
                searchTask();
            } else if (choice == 7) {
                sortByDate();
            } else if (choice == 8) {
                undoTask();
            } else if (choice == 9) {
                System.out.println("\n  Goodbye! Keep completing your tasks!\n");
                timer.cancel();
                running = false;
            } else {
                System.out.println("  Invalid choice. Enter 1 to 9.");
            }
        }
    }
    // -------------------------------------------------------
    static void showBanner() {
        System.out.println();
        System.out.println("  ========================================================");
        System.out.println("        TASKAURA - Task Manager with Timely Reminders");
        System.out.println("  ========================================================");
    }
    // -------------------------------------------------------
    static void showMenu() {
        System.out.println();
        System.out.println("  ------------------------------------------");
        System.out.println("                    MENU");
        System.out.println("  ------------------------------------------");
        System.out.println("  1. Add Task");
        System.out.println("  2. Delete Task");
        System.out.println("  3. Mark Task as Done");
        System.out.println("  4. View All Tasks");
        System.out.println("  5. View Pending Tasks");
        System.out.println("  6. Search Task by Name");
        System.out.println("  7. Sort Tasks by Date");
        System.out.println("  8. Undo Last Added Task");
        System.out.println("  9. Exit");
        System.out.println("  ------------------------------------------");
    }
    // -------------------------------------------------------
    static void addTask() {
        System.out.println("\n  -- Add New Task --");

        System.out.print("  Task Name        : ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("  Task name cannot be empty.");
            return;
        }
        System.out.print("  Description      : ");
        String desc = sc.nextLine().trim();

        System.out.print("  Day (e.g. MONDAY): ");
        String day = sc.nextLine().trim().toUpperCase();

        System.out.print("  Date (dd-MM-yyyy): ");
        String date = sc.nextLine().trim();

        System.out.print("  Time (HH:mm)     : ");
        String time = sc.nextLine().trim();

        Task t = new Task(name, desc, date, day, time);
        list.addTask(t);
        map.put(t.id, t);
        queue.offer(t);
        stack.push(t);
        System.out.println();
        System.out.println("  Task added successfully!");
        t.show();
    }
    // -------------------------------------------------------
    static void deleteTask() {
        System.out.println("\n  -- Delete Task --");
        System.out.print("  Enter Task ID to delete: ");
        int id = 0;
        try {
            id = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("  Invalid ID.");
            return;
        }
        if (!map.containsKey(id)) {
            System.out.println("  Task ID " + id + " not found.");
            return;
        }
        String name = map.get(id).name;
        map.remove(id);
        list.deleteTask(id);
        System.out.println("  Task '" + name + "' deleted successfully.");
    }
    // -------------------------------------------------------
    static void markDone() {
        System.out.println("\n  -- Mark Task as Done --");
        System.out.print("  Enter Task ID: ");
        int id = 0;
        try {
            id = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("  Invalid ID.");
            return;
        }
        Task t = map.get(id);
        if (t == null) {
            System.out.println("  Task ID " + id + " not found.");
            return;
        }
        t.done = true;
        System.out.println("  Task '" + t.name + "' marked as DONE!");
    }
    // -------------------------------------------------------
    static void viewAll() {
        System.out.println("\n  -- All Tasks --");
        ArrayList<Task> all = list.getAll();
        if (all.isEmpty()) {
            System.out.println("  No tasks found.");
            return;
        }
        for (Task t : all) {
            t.show();
        }
        System.out.println("  Total: " + all.size() + " task(s).");
    }
    // -------------------------------------------------------
    static void viewPending() {
        System.out.println("\n  -- Pending Tasks --");
        ArrayList<Task> all = list.getAll();
        int count = 0;
        for (Task t : all) {
            if (!t.done) {
                t.show();
                count++;
            }
        }
        if (count == 0) {
            System.out.println("  No pending tasks. All done!");
        } else {
            System.out.println("  Total PENDING: " + count + " task(s).");
        }
    }
    // -------------------------------------------------------
    static void searchTask() {
        System.out.println("\n  -- Search Task by Name --");
        System.out.print("  Enter keyword: ");
        String kw = sc.nextLine().trim();
        ArrayList<Task> results = list.searchByName(kw);
        if (results.isEmpty()) {
            System.out.println("  No tasks found for: " + kw);
            return;
        }
        System.out.println("  Found " + results.size() + " result(s):");
        for (Task t : results) {
            t.show();
        }
    }
    // -------------------------------------------------------
    static void sortByDate() {
        System.out.println("\n  -- Sort Tasks by Date (Bubble Sort) --");
        list.bubbleSort();
        ArrayList<Task> all = list.getAll();
        if (all.isEmpty()) {
            System.out.println("  No tasks to sort.");
            return;
        }
        System.out.println("  Tasks sorted by date (earliest first):");
        for (Task t : all) {
            t.show();
        }
    }
    // -------------------------------------------------------
    static void undoTask() {
        System.out.println("\n  -- Undo Last Added Task (Stack) --");
        if (stack.isEmpty()) {
            System.out.println("  Nothing to undo.");
            return;
        }
        Task t = stack.pop();
        map.remove(t.id);
        list.deleteTask(t.id);
        System.out.println("  Undone! Task '" + t.name + "' removed.");
    }
    // -------------------------------------------------------
    // Reminder Timer - checks every 30 seconds
    // If task is not done -> prints PENDING reminder
    // -------------------------------------------------------
    static void startReminderTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ArrayList<Task> all = list.getAll();
                if (all.isEmpty()) {
                    return;
                }
                for (Task t : all) {
                    if (!t.done) {
                        System.out.println();
                        System.out.println("  *** REMINDER ***");
                        System.out.println("  Task   : " + t.name);
                        System.out.println("  Date   : " + t.date + "  Day: " + t.day);
                        System.out.println("  Time   : " + t.time);
                        System.out.println("  Status : PENDING - Please complete this task!");
                        System.out.println("  *****************");
                        System.out.println();
                    }
                }
            }
        }, 30000, 30000);
    }
}

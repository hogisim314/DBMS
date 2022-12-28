import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Bptree {

    static class Node {
        static class pair implements Comparable<pair> {
            int key;
            int value;
            Node leftChildNode;

            pair(int key, int value, Node leftChildNode) {
                this.key = key;
                this.value = value;
                this.leftChildNode = leftChildNode;
            }

            @Override
            public int compareTo(Node.pair o) {
                if (this.key < o.key) {
                    return -1;
                } else if (this.key > o.key) {
                    return 1;
                }
                return 0;
            }
        }

        Node parent;// points to parent
        Node r;// rightmost child or right sibling node;
        Node l;
        List<pair> p;
        boolean isLeaf = false;

        Node() {
            parent = null;
            r = null;
            p = new ArrayList<>();
            l = null;
        }

        void sorting() {
            Collections.sort(this.p);
        }

    }

    public static int m = 0;
    static Node root;

    public static void main(String[] args) {
        String cmd = args[0];
        String indexFile = args[1];

        switch (cmd) {
            case "-c":
                m = Integer.parseInt(args[2]);
                createFile(indexFile);
                break;
            case "-i": // -i index_file data_file
                String dataFileForInsert = args[2];
                String line = "";

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(dataFileForInsert));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile, true));
                    while ((line = reader.readLine()) != null) {
                        String[] pair = line.split(",");
                        int key = Integer.parseInt(pair[0]);
                        int value = Integer.parseInt(pair[1]);
                        writer.write(key + "");
                        writer.write(",");
                        writer.write(value + "");
                        writer.write("\n");
                    }
                    writer.flush();
                    writer.close();
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "-d":
                String dataFileForDelete = args[2];
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(dataFileForDelete));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile, true));
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.write("\n");
                    }
                    writer.close();
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case "-s":
                int key = Integer.parseInt(args[2]);
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(indexFile));
                    line = reader.readLine();
                    m = Integer.parseInt(line);
                    while ((line = reader.readLine()) != null) {// insertionionionionionionionionion
                        if (line == "") {
                            break;
                        }
                        if (line.contains(",")) {
                            String pair[] = line.split(",");
                            Integer k = Integer.parseInt(pair[0]);
                            Integer v = Integer.parseInt(pair[1]);
                            insertion(k, v);
                        } else {// deletion
                            Integer k = Integer.parseInt(line);
                            deletion(k);
                        }
                    }
                    singleKeySearch(key);
                    reader.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "-r":
                int start_key = Integer.parseInt(args[2]);
                int end_key = Integer.parseInt(args[3]);
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(indexFile));
                    line = reader.readLine();
                    m = Integer.parseInt(line);
                    while ((line = reader.readLine()) != null) {// insertion
                        if (line.contains(",")) {
                            String pair[] = line.split(",");
                            Integer k = Integer.parseInt(pair[0]);
                            Integer v = Integer.parseInt(pair[1]);
                            insertion(k, v);
                        } else {// deletion
                            Integer k = Integer.parseInt(line); 
                            deletion(k);
                        }
                    }
                    rangedSearch(start_key, end_key);
                    reader.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    public static void createFile(String file) {
        try {
            FileWriter fw = new FileWriter(file, false);
            fw.write(Integer.toString(m));
            fw.write("\n");
            fw.flush();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Node singleKeySearch(int findKey) {
        List<Integer> keys = new ArrayList<>();
        Node cur = root;
        while (cur.isLeaf == false) {
            if (findKey >= cur.p.get(cur.p.size() - 1).key) {
                keys.add(cur.p.get(cur.p.size() - 1).key);
                cur = cur.r;
            } else if (findKey < cur.p.get(0).key) {
                keys.add(cur.p.get(0).key);
                cur = cur.p.get(0).leftChildNode;
            } else {
                for (int i = 0; i < cur.p.size() - 1; i++) {
                    if (cur.p.get(i).key <= findKey && findKey < cur.p.get(i + 1).key) {
                        keys.add(cur.p.get(i).key);
                        cur = cur.p.get(i + 1).leftChildNode;
                        break;
                    }
                }
            }
        }

        int i = 0;
        int key;
        System.out.println();
        while (i < cur.p.size() && (key = cur.p.get(i).key) <= findKey) {
            if (key == findKey) {
                for (int j = 0; j < keys.size(); j++) {
                    System.out.print(keys.get(j) + " ");
                }
                System.out.println();
                System.out.println(cur.p.get(i).value);
                return cur;
            } else {
                i++;
            }
        }
        for (int j = 0; j < keys.size(); j++) {
            System.out.print(keys.get(j) + " ");
        }
        System.out.println();
        System.out.println("NOT FOUND");
        return cur;
    }

    public static void rangedSearch(int start, int end) {
        Node left = findLocation(start);
        Node right = findLocation(end);
        Node cur = left;
        if (left == right) {
            for (int i = 0; i < cur.p.size(); i++) {
                if (start <= cur.p.get(i).key && cur.p.get(i).key <= end) {
                    System.out.println(cur.p.get(i).key + "," + cur.p.get(i).value);
                }
            }
        } else {
            while (cur != right) {
                for (int i = 0; i < cur.p.size(); i++) {
                    if (start <= cur.p.get(i).key) {
                        System.out.println(cur.p.get(i).key + "," + cur.p.get(i).value);
                    }
                }
                cur = cur.r;
            }
            for (int i = 0; i < cur.p.size(); i++) {
                if (cur.p.get(i).key <= end) {
                    System.out.println(cur.p.get(i).key + "," + cur.p.get(i).value);
                }
            }
        }

    }

    public static void insertion(int key, int value) {
        if (root == null) {
            root = new Node();
            root.p.add(new Node.pair(key, value, null));
            root.isLeaf = true;
            return;
        } else {
            Node locat = findLocation(key);
            locat.p.add(new Node.pair(key, value, null));
            locat.sorting();
            if (locat.p.size() == m) {
                locat = leafNodeSplit(locat);
            }
            while (locat.p.size() == m) {
                locat = nonLeafNodeSplit(locat);
            }

        }

    }

    public static Node findLocation(int key) {
        Node cur = root;
        while (cur.isLeaf == false) {
            if (key >= cur.p.get(cur.p.size() - 1).key) {
                cur = cur.r;
            } else if (key < cur.p.get(0).key) {
                cur = cur.p.get(0).leftChildNode;
            } else {
                for (int i = 0; i < cur.p.size() - 1; i++) {
                    if (cur.p.get(i).key <= key && key < cur.p.get(i + 1).key) {
                        cur = cur.p.get(i + 1).leftChildNode;
                        break;
                    }
                }
            }
        }
        return cur;
    }

    public static Node leafNodeSplit(Node overflowNode) {
        int idx = (m / 2);
        int change_key = overflowNode.p.get(idx).key;// find where to split and go up
        Node parent;
        int locat;// where to insertionionionionionionionion a key into the parent node;
        Node newnode = new Node();
        newnode.isLeaf = true;
        int size = overflowNode.p.size();
        for (int i = idx; i < size; i++) {// copy
            newnode.p.add(overflowNode.p.get(i));
        }
        for (int i = size - 1; i >= idx; i--) {// remove
            overflowNode.p.remove(i);
        }

        if (overflowNode.parent == null) {// this means root
            parent = new Node();
            overflowNode.parent = parent;
            root = parent;
            parent.p.add(new Node.pair(change_key, 0, overflowNode));
            parent.r = newnode;
        } else {// general case
            parent = overflowNode.parent;
            locat = wheretoInsert(parent, change_key);
            if (locat == 0) {// first index
                parent.p.add(locat, new Node.pair(change_key, 0, overflowNode));
                parent.p.get(1).leftChildNode = newnode;
                newnode.r = overflowNode.r;
                newnode.r.l = newnode;
            } else if (locat == parent.p.size()) {// last index
                parent.p.add(new Node.pair(change_key, 0, overflowNode));
                parent.r = newnode;
                if (overflowNode.r != null) {
                    newnode.r = overflowNode.r;
                    newnode.r.l = newnode;
                }
            } else {// or else
                parent.p.add(locat, new Node.pair(change_key, 0, overflowNode));
                parent.p.get(locat + 1).leftChildNode = newnode;
                newnode.r = overflowNode.r;
                newnode.r.l = newnode;
            }
        }
        newnode.l = overflowNode;
        overflowNode.r = newnode;
        newnode.parent = parent;
        return parent;
    }

    public static int wheretoInsert(Node node, int key) {
        int idx = -1;
        if (key > node.p.get(node.p.size() - 1).key) {
            idx = node.p.size();
            return idx;
        }
        for (int i = 0; i < node.p.size(); i++) {
            if (key < node.p.get(i).key) {
                idx = i;
                break;
            }
        }
        return idx;
    }

    public static void printTree(Node root) {
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (q.isEmpty() == false) {
            Node top = q.peek();
            if (top.isLeaf == false) {
                for (int i = 0; i < top.p.size(); i++) {
                    System.out.print(top.p.get(i).key + " ");
                    q.add(top.p.get(i).leftChildNode);
                }
                q.add(top.r);
            } else {
                for (int i = 0; i < top.p.size(); i++) {
                    System.out.print(top.p.get(i).key + " ");
                }
            }
            System.out.println("    ");
            q.remove();
        }
        System.out.println();
    }

    public static Node isInNonLeaf(int key) {
        Node cur = root;
        while (cur.isLeaf == false) {
            for (int i = 0; i < cur.p.size(); i++) {
                if (cur.p.get(i).key == key) {
                    return cur;
                }
            }
            if (key >= cur.p.get(cur.p.size() - 1).key) {
                cur = cur.r;
            } else if (key < cur.p.get(0).key) {
                cur = cur.p.get(0).leftChildNode;
            } else {
                for (int i = 0; i < cur.p.size() - 1; i++) {
                    if (cur.p.get(i).key <= key && key < cur.p.get(i + 1).key) {
                        cur = cur.p.get(i + 1).leftChildNode;
                        break;
                    }
                }
            }
            continue;
        }

        if (cur.isLeaf == true) {
            return null;
        }
        return null;
    }

    public static int findIdx(Node cur, int key) { // for Delete, the key must be in the Node cur!!
        for (int i = 0; i < cur.p.size(); i++) {
            if (cur.p.get(i).key == key) {
                return i;
            }
        }
        return -1;
    }

    public static void deletion(int key) {

        Node locat = findLocation(key);
        boolean underflow = checkUnderflow(locat);// check
        int idx = findIdx(locat, key);// idx where the key is located.

        if (locat == root) {
            locat.p.remove(idx);
            return;
        }
        if (underflow == false) {// underflow아님
            locat.p.remove(idx);
            if (idx == 0) {
                Node nonLeafNodeChange = isInNonLeaf(key);
                int parentIdx = findIdx(nonLeafNodeChange, key);
                nonLeafNodeChange.p.get(parentIdx).key = locat.p.get(0).key;
            }
        } else {// underflow
            if (locat.r != null && !checkUnderflow(locat.r) && locat.r.parent == locat.parent) {// borrow from right
                borrowFromRight(locat, idx);
                Node nonLeafNodeChange2 = isInNonLeaf(locat.p.get(locat.p.size() - 1).key);
                int parentIdx2 = findIdx(nonLeafNodeChange2, locat.p.get(locat.p.size() - 1).key);
                nonLeafNodeChange2.p.get(parentIdx2).key = locat.r.p.get(0).key;
                if (idx == 0) {
                    Node nonLeafNodeChange1 = isInNonLeaf(key);
                    if (nonLeafNodeChange1 != null) {
                        int parentIdx1 = findIdx(nonLeafNodeChange1, key);
                        nonLeafNodeChange1.p.get(parentIdx1).key = locat.p.get(0).key;
                    }
                }
            } else if (locat.l != null && !checkUnderflow(locat.l) && locat.l.parent == locat.parent) {// left borrow
                borrowFromLeft(locat, idx);
                if (idx == 0 && locat.p.size() == 1) {
                    Node nonLeafNodeChange = isInNonLeaf(key);
                    int parentIdx = findIdx(nonLeafNodeChange, key);
                    nonLeafNodeChange.p.get(parentIdx).key = locat.p.get(0).key;
                } else if (idx != 0) {
                    Node nonLeafNodeChange = isInNonLeaf(locat.p.get(1).key);
                    int parentIdx = findIdx(nonLeafNodeChange, locat.p.get(1).key);
                    nonLeafNodeChange.p.get(parentIdx).key = locat.p.get(0).key;
                }
            } else {//
                locat.p.remove(idx);
                int parentIdx = findParentIdx(locat.parent, locat);
                if (locat.r != null && locat.r.parent == locat.parent) {// merge with right
                    for (int i = 0; i < locat.p.size(); i++) {
                        locat.r.p.add(locat.p.get(i));
                    }
                    locat.r.sorting();
                    Node nonLeafNodeChange = isInNonLeaf(key);
                    if (nonLeafNodeChange != null) {
                        int nonLeafNodeIdx = findIdx(nonLeafNodeChange, key);
                        nonLeafNodeChange.p.get(nonLeafNodeIdx).key = locat.r.p.get(0).key;
                    }

                    locat.parent.p.remove(parentIdx);

                    if (locat.l != null) {
                        locat.l.r = locat.r;
                        locat.r.l = locat.l;
                    } else if (locat.l == null) {
                        locat.r.l = null;
                    }

                    if (locat.parent == root) {
                        if (root.p.size() == 0) {
                            root = locat.r;
                            root.parent = null;
                        }
                    } else {
                        if (checkUnderflowafterDelete(locat.parent)) {
                            Node left = findLeftSibling(locat.parent);
                            Node right = findRightSibling(locat.parent);
                            if (left != null) {
                                if (key == 1730) {
                                    System.out.println();
                                }
                                nonLeafNodeMerge(locat.parent, left, locat.parent.parent, "Left");
                            } else if (right != null) {
                                nonLeafNodeMerge(locat.parent, right, locat.parent.parent, "Right");
                            }
                        }
                    }
                } else if (locat.l != null && locat.parent == locat.l.parent) {// merge with left
                    for (int i = 0; i < locat.p.size(); i++) {
                        locat.l.p.add(locat.p.get(i));
                    }
                    locat.l.sorting();

                    if (locat.r == null)
                        locat.l.r = null;
                    else {
                        locat.l.r = locat.r;
                        locat.r.l = locat.l;
                    }
                    if (parentIdx == locat.parent.p.size()) {
                        locat.parent.r = locat.l;
                    } else {
                        locat.parent.p.get(parentIdx).leftChildNode = locat.l;
                    }

                    locat.parent.p.remove(parentIdx - 1);

                    if (locat.parent == root) {
                        if (root.p.size() == 0) {
                            root = locat.l;
                            root.parent = null;
                        }
                    } else {
                        if (checkUnderflowafterDelete(locat.parent)) {
                            Node left = findLeftSibling(locat.parent);
                            Node right = findRightSibling(locat.parent);
                            if (left != null) {
                                nonLeafNodeMerge(locat.parent, left, locat.parent.parent, "Left");
                            } else if (right != null) {
                                nonLeafNodeMerge(locat.parent, right, locat.parent.parent, "Right");
                            }
                        }
                    }
                }
            }
        }
    }

    public static int findParentIdx(Node parent, Node child) {
        if (parent.r == child) {
            return parent.p.size();
        } else {
            for (int i = 0; i < parent.p.size(); i++) {
                if (parent.p.get(i).leftChildNode == child) {
                    return i;
                }
            }
        }
        return 0;
    }

    public static void printLeftLeafNode(int key) {
        Node cur = findLocation(64);
        while (cur.l != null) {
            System.out.print(cur.p.get(0).key + " ");
            cur = cur.l;
        }
        System.out.println(cur.p.get(0).key);
    }

    public static void borrowFromRight(Node cur, int idx) {
        int changeKey = cur.r.p.get(0).key;
        int changeValue = cur.r.p.get(0).value;
        cur.p.remove(idx);
        cur.p.add(new Node.pair(changeKey, changeValue, null));
        cur.r.p.remove(0);
    }

    public static void borrowFromLeft(Node cur, int idx) {
        int changeKey = cur.l.p.get(cur.l.p.size() - 1).key;// 바꿀키
        int changeValue = cur.l.p.get(cur.l.p.size() - 1).value;
        cur.p.remove(idx);
        cur.p.add(new Node.pair(changeKey, changeValue, null));
        cur.sorting();
        cur.l.p.remove(cur.l.p.size() - 1);
    }

    public static boolean checkUnderflow(Node node) {
        if (node.p.size() <= Math.ceil((float) m / 2) - 1)
            return true;

        return false;
    }

    public static boolean checkUnderflowafterDelete(Node node) {
        if (node.p.size() < Math.ceil((float) m / 2) - 1)
            return true;
        return false;
    }

    public static Node findLeftSibling(Node node) {
        Node parent = node.parent;
        if (parent == null) {
            return null;
        }
        if (parent.p.get(0).leftChildNode == node)
            return null;
        else if (parent.r == node) {
            return parent.p.get(parent.p.size() - 1).leftChildNode;
        } else {
            for (int i = 1; i < parent.p.size(); i++) {
                if (parent.p.get(i).leftChildNode == node) {
                    return parent.p.get(i - 1).leftChildNode;
                }
            }
        }
        return null;
    }

    public static Node findRightSibling(Node node) {
        Node parent = node.parent;
        if (parent == null) {
            return null;
        }
        if (parent.r == node)
            return null;
        else if (parent.p.get(parent.p.size() - 1).leftChildNode == node) {
            return parent.r;
        } else {
            for (int i = 0; i <= parent.p.size() - 2; i++) {
                if (parent.p.get(i).leftChildNode == node) {
                    return parent.p.get(i + 1).leftChildNode;
                }
            }
        }
        return null;
    }

    public static Node nonLeafNodeSplit(Node overflowNode) {// nonleafnodesplit도 leafnodesplit처럼 case 나눌 필요 있음.
        int idx = (m / 2);
        int change_key = overflowNode.p.get(idx).key;
        Node newRightPointer = overflowNode.p.get(idx).leftChildNode;
        Node parent;
        int locat;
        Node newnode = new Node();
        int size = overflowNode.p.size();
        for (int i = idx + 1; i < size; i++) {
            newnode.p.add(overflowNode.p.get(i));
            overflowNode.p.get(i).leftChildNode.parent = newnode;
        }
        for (int i = size - 1; i >= idx; i--) {// remove
            overflowNode.p.remove(i);
        }
        if (overflowNode.parent == null) {
            parent = new Node();
            overflowNode.parent = parent;
            root = parent;
            parent.p.add(new Node.pair(change_key, 0, overflowNode));
            parent.r = newnode;
        } else {
            parent = overflowNode.parent;
            locat = wheretoInsert(parent, change_key);
            if (locat == 0) {
                parent.p.add(0, new Node.pair(change_key, 0, overflowNode));
                parent.p.get(1).leftChildNode = newnode;
            } else if (locat == parent.p.size()) {
                parent.p.add(new Node.pair(change_key, 0, overflowNode));
                parent.r = newnode;
            } else {
                parent.p.add(locat, new Node.pair(change_key, 0, overflowNode));
                parent.p.get(locat + 1).leftChildNode = newnode;
            }

        }

        newnode.parent = parent;
        newnode.r = overflowNode.r;
        overflowNode.r.parent = newnode;
        overflowNode.r = newRightPointer;

        return parent;
    }

    public static void nonLeafNodeMerge(Node cur, Node side, Node parent, String dir) {
        int parentIdx = findParentIdx(parent, cur);

        if (dir == "Left") {
            int k = parent.p.get(parentIdx - 1).key;
            side.p.add(new Node.pair(k, 0, side.r));

            parent.p.remove(parentIdx - 1);

            for (int i = 0; i < cur.p.size(); i++) {
                side.p.add(cur.p.get(i));
                cur.p.get(i).leftChildNode.parent = side;
            }
            cur.r.parent = side;
            side.r = cur.r;

            if (parent.p.size() == 0 || parentIdx == parent.p.size() + 1) {
                parent.r = side;
            } else {
                parent.p.get(parentIdx - 1).leftChildNode = side;
            }

            if (parent == root && parent.p.size() == 0) {
                root = side;
                root.parent = null;
            } else if (checkUnderflowafterDelete(parent) && parent != root) {
                if (findLeftSibling(parent) != null && parent.parent != null) {
                    nonLeafNodeMerge(parent, findLeftSibling(parent), parent.parent, "Left");
                } else if (findRightSibling(parent) != null && parent.parent != null) {
                    nonLeafNodeMerge(parent, findRightSibling(parent), parent.parent, "Right");
                }
            }
        } else if (dir == "Right") {
            cur.p.add(parent.p.get(parentIdx));
            cur.p.get(cur.p.size() - 1).leftChildNode = cur.r;
            parent.p.remove(parentIdx);
            cur.r = side.r;
            side.r.parent = cur;

            for (int i = 0; i < side.p.size(); i++) {
                cur.p.add(side.p.get(i));
                side.p.get(i).leftChildNode.parent = cur;
            }

            if (parent.p.size() == 0 || parentIdx == parent.p.size()) {
                parent.r = cur;
            } else {
                parent.p.get(parentIdx).leftChildNode = cur;
            }

            if (parent == root && parent.p.size() == 0) {
                root = parent;
                parent.parent = null;
            } else if (parent != root && checkUnderflowafterDelete(parent)) {
                if (findLeftSibling(parent) != null && parent.parent != null) {
                    nonLeafNodeMerge(parent, findLeftSibling(parent), parent.parent, "Left");
                } else if (findRightSibling(parent) != null && parent.parent != null) {
                    nonLeafNodeMerge(parent, findRightSibling(parent), parent.parent, "Right");
                }
            }
        }
    }
}
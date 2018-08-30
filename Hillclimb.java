/*
Chaitrali Londhe
421075
*/
package hillclimb;

import java.util.*;

public class Hillclimb {

    public static Scanner scan = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.print("How many nodes are there in the graph? :");
        int nodes_count;
        nodes_count = scan.nextInt();
        Algorithm a = new Algorithm(nodes_count); 
        a.getter(); //input
        a.method(); //simple hillclimb algorithm
    }
}

class Node { //node in graph
    int no;
    int heuristic;
    int parent;

    public Node(int no, int heuristic) {
        this.no = no;
        this.heuristic = heuristic;
        parent = 0;
    }   
}

class Algorithm {
    int GOAL;
    int START;
    int graph[][];
    HashMap<Integer,Node> nodetrack = new HashMap<>();
    public Scanner scan = new Scanner(System.in);
    
    Algorithm(int nodes_count) {
        graph = new int[nodes_count][nodes_count];
    }
    
    Node movegen(Node parent)  
    {
        int n = parent.no,ph = parent.heuristic;
        Node node = null;
        for(int i = 0; i<graph.length; i++)
        {
            if(graph[n-1][i] == 1)
            {
                node = nodetrack.get(i+1);
                if(node.heuristic < ph)
                {
                    node.parent = n;
                    return node;
                }
            }
        }
        return null;
    }
    
    public void method() //fuction to search goal state
    {
        if(START == GOAL)
        {
            System.out.println("found!! root is goal state");
            return;
        }
        Node root = nodetrack.get(START);
        Node next = movegen(root);
        int flag=0;
        while(next!=null)
        {
            if(next.no!=GOAL)
            {
                next = movegen(next);
                flag = 0;
            }
            else
            {
                flag = 1;
                break;
            }
        }
        if(flag == 1)
        {
           System.out.println("found!! path is:");
           Node curr;
           int val = GOAL;
           while(val>0)
           {
               System.out.print(val+"->");
               curr = nodetrack.get(val);
               val = curr.parent;
           }
        }
        else
        {
           System.out.println("not found!!"); 
        }
    }

    public void getter() { //function to get input
        int hval;
        System.out.print("\nEnter GOAL: ");
        GOAL = scan.nextInt();
        System.out.print("\nEnter START: ");
        START = scan.nextInt();
        System.out.println("Enter your graph:");
        for (int i=0;i<graph.length;i++) {
            for(int j=0;j<graph.length;j++){
                if(i>=j) continue;
                System.out.printf("\nIs there an edge between nodes %d and %d :",i+1,j+1);
                graph[i][j] = scan.nextInt();
                graph[j][i] = graph[i][j];
            }
        }
        System.out.print("Enter heuristic values for each node: ");
        
       for(int i = 1; i <= graph.length; i++)
       {
           System.out.printf("\n Enter H value for node %d :",i);
           hval = scan.nextInt();
           Node n = new Node(i,hval);
           nodetrack.put(i,n);
       }
                
        System.out.println("The entered graph is:");
        int k = 1;
        for(int i[] : graph) {
            System.out.print(k+"\t");
            k++;
            for (int j : i) {
                System.out.print(j+" ");
            }
            System.out.println();
        }
    }
}
/* OUTPUT
CASE 1: goal exists and found successfully
How many nodes are there in the graph? :4

Enter GOAL: 4

Enter START: 1
Enter your graph:

Is there an edge between nodes 1 and 2 :1

Is there an edge between nodes 1 and 3 :1

Is there an edge between nodes 1 and 4 :0

Is there an edge between nodes 2 and 3 :0

Is there an edge between nodes 2 and 4 :0

Is there an edge between nodes 3 and 4 :1
Enter heuristic values for each node: 
 Enter H value for node 1 :19

 Enter H value for node 2 :20

 Enter H value for node 3 :10

 Enter H value for node 4 :9
The entered graph is:
1	0 1 1 0 
2	1 0 0 0 
3	1 0 0 1 
4	0 0 1 0 
found!! path is:
4->3->1->

CASE 2: goal exits but algorithm stuck in local minima

How many nodes are there in the graph? :4

Enter GOAL: 4

Enter START: 1
Enter your graph:

Is there an edge between nodes 1 and 2 :1

Is there an edge between nodes 1 and 3 :1

Is there an edge between nodes 1 and 4 :0

Is there an edge between nodes 2 and 3 :0

Is there an edge between nodes 2 and 4 :0

Is there an edge between nodes 3 and 4 :1
Enter heuristic values for each node: 
 Enter H value for node 1 :19

 Enter H value for node 2 :10

 Enter H value for node 3 :20

 Enter H value for node 4 :9
The entered graph is:
1	0 1 1 0 
2	1 0 0 0 
3	1 0 0 1 
4	0 0 1 0 
not found!!

*/
package eightpuzzle;

import java.util.*;

public class Eightpuzzle {
    public static void main(String[] args) {
        Algorithm a = new Algorithm();
        a.initstart();
        a.setgoal();
        a.method();       
    }  
}

class Node { //node in graph
    int no;
    int state[][] = new int[3][3];
    int heuristic;
    int gval;
    int fval;
    int parent;

    public Node(int no) {
        this.no = no;
        heuristic = 0;
        parent = 0;
        gval = 0;
        fval = heuristic;
    }   
}

class HeuristicComparator implements Comparator<Node>{ //to sort nodes in increasing order of heuristic in open priority queue

            @Override
            public int compare(Node s1, Node s2) {
                if (s1.fval > s2.fval)
                    return 1;
                else if (s1.fval < s2.fval)
                    return -1;
                                return 0;
                }
        }

class Algorithm {
  int track = 1;
  int goal[][] = new int[3][3];
  Node start= new Node(track);
  HashMap<Integer,Node> nodetrack = new HashMap<>();
  ArrayList<Node> closed = new ArrayList<>();
  ArrayList<Integer> openl = new ArrayList<>();
  PriorityQueue<Node> open = new PriorityQueue<Node>(10,new HeuristicComparator());
    
  public void initstart()
  {
      Scanner scan = new Scanner(System.in);
      System.out.println("enter the 8 puzzle grid (enter 0 for blank tile): ");
      for(int i = 0; i<3; i++)
      {
          for(int j = 0; j<3; j++)
          {
              start.state[i][j] = scan.nextInt();
          }
      }
      nodetrack.put(track, start);
  }
  
  public void setgoal()
  {
      goal[0][0] = 1;
      goal[0][1] = 2;
      goal[0][2] = 3;
      goal[1][2] = 4;
      goal[2][2] = 5;
      goal[2][1] = 6;
      goal[2][0] = 7;
      goal[1][0] = 8;
      goal[1][1] = 0;
      System.out.println("Goal state is: ");
      print(goal);
      System.out.println();
  }
  
  void print(int gr[][])
  {
      for(int i = 0; i<3; i++)
      {
          for(int j = 0; j<3; j++)
          {
              System.out.print(gr[i][j]+" ");
          }
          System.out.println();
      } 
  }
  
  boolean checkgoal(int gr[][])
  {
      for(int i = 0; i<3; i++)
      {
          for(int j = 0; j<3; j++)
          {
              if(gr[i][j]!=goal[i][j]) return false;
          }
      }       
      return true;
  }
  
  int misplacecnt(int gr[][])
  {
      int cnt = 0;
      for(int i = 0; i<3; i++)
      {
          for(int j = 0; j<3; j++)
          {
              if(gr[i][j]!=goal[i][j]) cnt++;
          }
      } 
      return cnt;
  }
  
  void movegen(Node node)
  {
      int i=0,j=0;
      String val=null;
      outerloop:
      for(i = 0; i<3; i++)
      {
          for(j = 0; j<3; j++)
          {
              if(node.state[i][j] == 0) 
              {
                  val = Integer.toString(i)+Integer.toString(j);
                  break outerloop;
              }
          }
      }
      
      System.out.println("possible states of grid:");
      
      switch(val)
      {
          case "00": //right down
                     right(node,i,j);
                     down(node,i,j);
              break;
          case "01": //left right down
                     left(node,i,j); 
                     right(node,i,j); 
                     down(node,i,j);
              break;
          case "02": //left down
                     left(node,i,j);  
                     down(node,i,j);
              break;
          case "10": //up down right
                     right(node,i,j);
                     up(node,i,j); 
                     down(node,i,j);
              break;
          case "11": //left right up down 
                     left(node,i,j); 
                     right(node,i,j);
                     up(node,i,j); 
                     down(node,i,j);
              break;
          case "12": //up down left
                     left(node,i,j); 
                     up(node,i,j); 
                     down(node,i,j);
              break;
          case "20": //up right
                     right(node,i,j);
                     up(node,i,j); 
              break;
          case "21": //up left right
                     left(node,i,j); 
                     right(node,i,j);
                     up(node,i,j); 
              break;
          case "22": //up left
                     left(node,i,j); 
                     up(node,i,j); 
              break;
      }      
  }
  
  void up(Node n,int i,int j)
  {
      Node ch  = new Node(track++);
      if(!open.contains(ch) && !closed.contains(ch))
      {
        for(int k=0; k < 3; k++)
            ch.state[k] = n.state[k].clone();
        ch.state[i][j] = ch.state[i-1][j];
        ch.state[i-1][j] = 0;
        ch.heuristic = misplacecnt(ch.state);
        ch.gval = 1 + n.gval;
        ch.fval = ch.gval + ch.heuristic;
        ch.parent = n.no;
        open.add(ch);
        nodetrack.put(track, ch);
        print(ch.state);
        System.out.println("fval : "+ch.fval);
        System.out.println();
      }
  }
  
  void down(Node n,int i,int j)
  {
      Node ch = new Node(track++);
      if(!open.contains(ch) && !closed.contains(ch))
      {
        for(int k=0; k < 3; k++)
            ch.state[k] = n.state[k].clone();
        ch.state[i][j] = ch.state[i+1][j];
        ch.state[i+1][j] = 0;
        ch.heuristic = misplacecnt(ch.state);
        ch.gval = 1 + n.gval;
        ch.fval = ch.gval + ch.heuristic;
        ch.parent = n.no;
        open.add(ch);
        nodetrack.put(track, ch);
        print(ch.state);
        System.out.println("fval : "+ch.fval);
        System.out.println();
      }
  }
  
  void left(Node n,int i,int j)
  {
      Node ch = new Node(track++);
      if(!open.contains(ch) && !closed.contains(ch))
      {     
        for(int k=0; k < 3; k++)
            ch.state[k] = n.state[k].clone();
        ch.state[i][j] = ch.state[i][j-1];
        ch.state[i][j-1] = 0;
        ch.heuristic = misplacecnt(ch.state);
        ch.gval = 1 + n.gval;
        ch.fval = ch.gval + ch.heuristic;
        ch.parent = n.no;
        open.add(ch);
        nodetrack.put(track, ch);
        print(ch.state);
        System.out.println("fval : "+ch.fval);
        System.out.println();
      }
  }

  void right(Node n,int i,int j)
  {
      Node ch = new Node(track++);
      if(!open.contains(ch) && !closed.contains(ch))
      {
        for(int k=0; k < 3; k++)
            ch.state[k] = n.state[k].clone(); 
        ch.state[i][j] = ch.state[i][j+1];
        ch.state[i][j+1] = 0;
        ch.heuristic = misplacecnt(ch.state);
        ch.gval = 1 + n.gval;
        ch.fval = ch.gval + ch.heuristic;
        ch.parent = n.no;
        open.add(ch);
        nodetrack.put(track, ch);
        print(ch.state);
        System.out.println("fval : "+ch.fval);
        System.out.println();
      }
  }  
    
  void method()
  {
      open.add(start);
        while(!open.isEmpty())
        {
            Node node = open.remove();
            System.out.println("grid chosen to expand: ");
            print(node.state);
            if(checkgoal(node.state))
            {
                System.out.println("found");
                return;
            }
            else
            {
                closed.add(node);
                movegen(node);
            }
        }   
  }
  
}

/*
OUTPUT:
enter the 8 puzzle grid (enter 0 for blank tile): 
2 8 3
1 6 4
7 0 5
Goal state is: 
1 2 3 
8 0 4 
7 6 5 

grid chosen to expand: 
2 8 3 
1 6 4 
7 0 5 
possible states of grid:
2 8 3 
1 6 4 
0 7 5 
fval : 7

2 8 3 
1 6 4 
7 5 0 
fval : 7

2 8 3 
1 0 4 
7 6 5 
fval : 4

grid chosen to expand: 
2 8 3 
1 0 4 
7 6 5 
possible states of grid:
2 8 3 
0 1 4 
7 6 5 
fval : 6

2 8 3 
1 4 0 
7 6 5 
fval : 7

2 0 3 
1 8 4 
7 6 5 
fval : 6

2 8 3 
1 6 4 
7 0 5 
fval : 7

grid chosen to expand: 
2 8 3 
0 1 4 
7 6 5 
possible states of grid:
2 8 3 
1 0 4 
7 6 5 
fval : 6

0 8 3 
2 1 4 
7 6 5 
fval : 7

2 8 3 
7 1 4 
0 6 5 
fval : 8

grid chosen to expand: 
2 0 3 
1 8 4 
7 6 5 
possible states of grid:
0 2 3 
1 8 4 
7 6 5 
fval : 6

2 3 0 
1 8 4 
7 6 5 
fval : 8

2 8 3 
1 0 4 
7 6 5 
fval : 6

grid chosen to expand: 
2 8 3 
1 0 4 
7 6 5 
possible states of grid:
2 8 3 
0 1 4 
7 6 5 
fval : 8

2 8 3 
1 4 0 
7 6 5 
fval : 9

2 0 3 
1 8 4 
7 6 5 
fval : 8

2 8 3 
1 6 4 
7 0 5 
fval : 9

grid chosen to expand: 
0 2 3 
1 8 4 
7 6 5 
possible states of grid:
2 0 3 
1 8 4 
7 6 5 
fval : 8

1 2 3 
0 8 4 
7 6 5 
fval : 6

grid chosen to expand: 
2 8 3 
1 0 4 
7 6 5 
possible states of grid:
2 8 3 
0 1 4 
7 6 5 
fval : 8

2 8 3 
1 4 0 
7 6 5 
fval : 9

2 0 3 
1 8 4 
7 6 5 
fval : 8

2 8 3 
1 6 4 
7 0 5 
fval : 9

grid chosen to expand: 
1 2 3 
0 8 4 
7 6 5 
possible states of grid:
1 2 3 
8 0 4 
7 6 5 
fval : 5

0 2 3 
1 8 4 
7 6 5 
fval : 8

1 2 3 
7 8 4 
0 6 5 
fval : 8

grid chosen to expand: 
1 2 3 
8 0 4 
7 6 5 
found
*/

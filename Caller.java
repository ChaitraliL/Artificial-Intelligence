import java.util.*;

public class Caller{
	public static void main(String[] args){
		Puzzle obj = new Puzzle();
		obj.method();
	}
}

class Node{
	private int[][] state = new int[3][3];
	private int h;
	private int g;
	private int f;
	private Node parent;

	Node(int[][] state){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				this.state[i][j] = state[i][j];
			}
		}

		this.parent = null;
	}

	void setH(int h){ this.h = h; }
	void setG(int g){ this.g = g; }
	void setF(){ f = g + h; }
	void setParent(Node parent) { this.parent = parent; }

	int[][] getState(){ return this.state; }
	int getH() { return this.h; }
	int getG() { return this.g; }
	int getF() { return this.f; }
	Node getParent() { return this.parent; }
}

class Puzzle{
	private int[][] start_state = {{2,8,3},{1,6,4},{7,0,5}}; //1
	//private int[][] start_state = {{0,2,3},{1,6,4},{8,7,5}}; //2
	private int[][] end_state = {{1,2,3},{8,0,4},{7,6,5}}; //3

	// private int[][] start_state = {{0,1,4},{6,5,3},{7,2,8}}; //4
	// private int[][] end_state={{4,5,6},{1,2,3},{7,8,0}}; //5

	//combinations to try (1,3) (2,3) (4,5)
	private ArrayList<Node> open = new ArrayList<>();
	private ArrayList<Node> closed = new ArrayList<>();
	private ArrayList<Node> path = new ArrayList<>();

	void printState(int[][] state){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				System.out.print(state[i][j]+" ");
			}
			System.out.println();
		}
	}

	int calculateHeuristic(int[][] state){
		int h = 0;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(end_state[i][j] != state[i][j]){
					h++;
				}
			}
		}
		return h;
	}

	Boolean compareState(int[][] a, int[][] b){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(a[i][j] != b[i][j]){
					return false;
				}
			}
		}
		return true;
	}

	Node pickBest(){
		if(open.isEmpty()) return null;
		Node best = open.get(0);
		for(Node n : open){
			if(n.getF() < best.getF()){
				best = n;
			}
		}
		return best;
	}

	void printTrace(Node current){
		while(current != null){
			path.add(current);
			current = current.getParent();
		}
		Collections.reverse(path);
		for(Node n : path){
			System.out.println();
			printState(n.getState());
		}
	}

	void movgen(Node parent){
		int k = 0, l = 0;
		int[][] parent_state = parent.getState();
		String val = null;
		outerloop:
		for(k = 0; k < 3; k++){
			for(l = 0; l < 3; l++){
				if(parent_state[k][l] == 0){
					val = Integer.toString(k+1)+Integer.toString(l+1);
					break outerloop;
				}
			}
		}

		switch(val){
			case "11":	createChild(parent,k,l,"right");
						createChild(parent,k,l,"down");
						break;
			case "12":	createChild(parent,k,l,"left");
						createChild(parent,k,l,"right");
						createChild(parent,k,l,"down");
						break;
			case "13":	createChild(parent,k,l,"left");
						createChild(parent,k,l,"down");
						break;
			case "21":	createChild(parent,k,l,"right");
						createChild(parent,k,l,"up");
						createChild(parent,k,l,"down");
						break;
			case "22":	createChild(parent,k,l,"left");
						createChild(parent,k,l,"right");
						createChild(parent,k,l,"up");
						createChild(parent,k,l,"down");
						break;
			case "23":	createChild(parent,k,l,"left");
						createChild(parent,k,l,"up");
						createChild(parent,k,l,"down");
						break;
			case "31":	createChild(parent,k,l,"right");
						createChild(parent,k,l,"up");
						break;
			case "32":	createChild(parent,k,l,"left");
						createChild(parent,k,l,"right");
						createChild(parent,k,l,"up");
						break;
			case "33":	createChild(parent,k,l,"left");
						createChild(parent,k,l,"up");
						break;
		}
	}

	Boolean seenBefore(int[][] state){
		for(Node n : open){
			if(compareState(state,n.getState())) return true;
		}

		for(Node n : closed){
			if(compareState(state,n.getState())) return true;
		}

		return false;
	}

	void createChild(Node parent, int k, int l, String move){
		int[][] parent_state = parent.getState();
		int[][] child_state = new int[3][3];
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				child_state[i][j] = parent_state[i][j];

		if(move.equals("up")){
			child_state[k][l] = child_state[k - 1][l];
			child_state[k - 1][l] = 0;
		}
		else if(move.equals("down")){
			child_state[k][l] = child_state[k + 1][l];
			child_state[k + 1][l] = 0;
		}
		else if(move.equals("left")){
			child_state[k][l] = child_state[k][l - 1];
			child_state[k][l - 1] = 0;
		}
		else if(move.equals("right")){
			child_state[k][l] = child_state[k][l + 1];
			child_state[k][l + 1] = 0;
		}

		if(!seenBefore(child_state)){
			Node child_node = new Node(child_state);
			child_node.setParent(parent);
			child_node.setH(calculateHeuristic(child_state));
			child_node.setG(parent.getG() + 1);
			child_node.setF();
			open.add(child_node);

/*			System.out.println();
			printState(child_state);
			System.out.println("h: "+child_node.getH()+" g: "+child_node.getG()+" f: "+child_node.getF());*/
		}
	}

	void method(){
		System.out.println("The start state is: ");
		printState(start_state);
		System.out.println("The end state is: ");
		printState(end_state);

		Node current = new Node(start_state);
		current.setH(calculateHeuristic(start_state));
		current.setF();
		open.add(current);
		while(!compareState(current.getState(),end_state)){
			movgen(current);
			open.remove(current);
			closed.add(current);
			current = pickBest();
			if(current == null){
				System.out.println("goal state not achievable!!");
				return;
			}
		}
		System.out.println("goal state reached!!\n\ntrack: ");
		printTrace(current);
	}
}

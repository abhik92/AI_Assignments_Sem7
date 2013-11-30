import java.util.ArrayList;


public class Node {
	public String str;
	int f_val;
	int g_val;
	int h_val;
	Node parent;
	int char_op;
	char op_code;
	ArrayList<Node> neigh;

	public Node(String str,Node parent,int g,int h,int char_op,char op_code){
		this.str=str;
		this.parent=parent;
		this.g_val=g;
		this.h_val=h;
		this.f_val=g+h;
		this.char_op=char_op;
		this.op_code=op_code;
		this.neigh=new ArrayList<Node>();
	}

	public Node() {
		// TODO Auto-generated constructor stub
	}
	
	
	
}

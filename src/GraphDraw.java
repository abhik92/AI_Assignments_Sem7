/* Simple graph drawing class
Bert Huang
COMS 3137 Data Structures and Algorithms, Spring 2009

This class is really elementary, but lets you draw 
reasonably nice graphs/trees/diagrams. Feel free to 
improve upon it!
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GraphDraw extends JFrame {
	int width;
	int height;

	ArrayList<Node> nodes;
	ArrayList<edge> edges;

	ArrayList<String> str;
	ArrayList<Integer> ep;
	
	public GraphDraw() { //Constructor
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nodes = new ArrayList<Node>();
		edges = new ArrayList<edge>();
		str= new ArrayList<String>();
		width = 30;
		height = 30;
		ep = new ArrayList<Integer>();
	}

	public GraphDraw(String name) { //Construct with label
		this.setTitle(name);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nodes = new ArrayList<Node>();
		edges = new ArrayList<edge>();
		str= new ArrayList<String>();
		width = 30;
		height = 30;
		ep = new ArrayList<Integer>();
	}

	class Node {
		int x, y;
		String name;

		public Node(String myName, int myX, int myY) {
			x = myX;
			y = myY;
			name = myName;
		}
	}

	class edge {
		int i,j;

		public edge(int ii, int jj) {
			i = ii;
			j = jj;	    
		}
	}

	public void addNode(String name, int x, int y) { 
		//add a node at pixel (x,y)
		nodes.add(new Node(name,x,y));
		this.repaint();
	}
	public Node getNode(String name){
		for(int i=0;i<nodes.size();i++){
			if(nodes.get(i).name.equals(name))
				return nodes.get(i);
		}
		return null;
	}
	public edge getEdge(int a,int b){
		for(int k=0;k<edges.size();k++){
			if((edges.get(k).i==a && edges.get(k).j==b) ||(edges.get(k).i==b && edges.get(k).j==a) )
				return edges.get(k);
		}
		return null;
	}
	public void addEdge(int i, int j) {
		//add an edge between nodes i and j
		edges.add(new edge(i,j));
		this.repaint();
	}

	public void paint(Graphics g) { // draw the nodes and edges
		FontMetrics f = g.getFontMetrics();
		int nodeHeight = Math.max(height, f.getHeight());

	
		g.setColor(Color.black);
		for (edge e : edges) {

			if(isEdge(e.i,e.j)){
					
			}
			else{
				g.setColor(Color.black);
				
				g.drawLine(nodes.get(e.i).x, nodes.get(e.i).y,
					nodes.get(e.j).x, nodes.get(e.j).y);
		
			}
		}
		for (edge e : edges) {

			if(isEdge(e.i,e.j)){
				g.setColor(Color.BLUE);
				g.drawLine(nodes.get(e.i).x, nodes.get(e.i).y,
						nodes.get(e.j).x, nodes.get(e.j).y);

			}

		}
		for (Node n : nodes) {
			if(str.contains(n.name)){

			}
			int nodeWidth = Math.max(width, f.stringWidth(n.name)+width/2);
			if(str.contains(n.name)){
				g.setColor(Color.yellow);
				g.fillOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
						nodeWidth, nodeHeight);
			
			}
			else{
				g.setColor(Color.white);
				g.fillOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
						nodeWidth, nodeHeight);
			}
			if(str.contains(n.name)){
				g.setColor(Color.red);
				g.drawOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
						nodeWidth, nodeHeight);

				g.drawString(n.name, n.x-f.stringWidth(n.name)/2,
						n.y+f.getHeight()/2);
			
			}
			else{
				g.setColor(Color.black);
				g.drawOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
						nodeWidth, nodeHeight);

				g.drawString(n.name, n.x-f.stringWidth(n.name)/2,
						n.y+f.getHeight()/2);
			}
		}

	}
	public boolean isEdge(int a,int b){
		for(int i=0;i<ep.size()-1;i++){
			if((ep.get(i)==a && ep.get(i+1)==b)||(ep.get(i+1)==a && ep.get(i)==b))
				return true;
		}
		return false;
	}
	
}


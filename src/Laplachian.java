import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import cern.colt.matrix.linalg.EigenvalueDecomposition;

import Jama.Matrix;


public class Laplachian {


	ArrayList<Node> closed;
	ArrayList<Node> open;
	Matrix matr;
	int max_i;
	int max_j;
	double[][] matrx;
	int row_size;
	ArrayList<Node> lst;
	double[][] arr;
	
	public Laplachian(ArrayList<Node> open,ArrayList<Node> closed){
		this.closed= new ArrayList<Node>();
		this.open = new ArrayList<Node>();
		this.closed=closed;
		this.open=open;
		//this.matr= new Matrix(null);
	}
	
	public void makeMatrice(){
		int n= closed.size()+open.size();
		arr= new double[n][n];
		lst= new ArrayList<Node>();
		lst.addAll(open);
		lst.addAll(closed);
		
		for(int i=0;i<lst.size();i++){
			for(int j=0;j<lst.size();j++){
				if(getIndex(lst.get(i).neigh,lst.get(j).str)!=-1){
					arr[i][j]+=1;
					arr[j][i]+=1;
					arr[i][i]-=1;
					arr[j][j]-=1;
				}				
			}
		}		
		this.matr= new Matrix(arr);
	}
	public int getIndex(ArrayList<Node> lst,String string){
		for(int i=0;i<lst.size();i++){
			if(lst.get(i).str.equals(string))
				return i;
		}
		//System.out.println("NOT COME HERE");
		return -1;

	}
	
	public void getEigenVector(){
		Jama.EigenvalueDecomposition eigi = new Jama.EigenvalueDecomposition(matr);
		Matrix M=eigi.getV();
		matrx=M.getArray();
		row_size=M.getRowDimension();
		ArrayList<Double> val = getVal(M);
		ArrayList<Double> val2=new ArrayList<Double>();
		val2.addAll(val);
		Collections.sort(val2);
		max_i= getIndex2(val,val2.get(val2.size()-1));
		max_j= getIndex2(val,val2.get(val2.size()-2));
				
	}
	
	private int getIndex2(ArrayList<Double> val, Double double1) {
		// TODO Auto-generated method stub
		for(int i=0;i<val.size();i++){
			if(val.get(i)==double1) return i;
		}
		return -1;
	}

	private ArrayList<Double> getVal(Matrix m) {
		ArrayList<Double> val = new ArrayList<Double>();
		for(int j=0;j<m.getColumnDimension();j++){
			val.add( 0.0);
			for(int i=0;i<m.getRowDimension();i++){
				val.set(j,val.get(j)+Math.pow(m.get(i, j),2));
			}
		}
		
		return val;
	}

	public void drawGraph(ArrayList<Node> path){

		GraphDraw frame = new GraphDraw("Test Window");
		for(int i=0;i<path.size();i++){
			frame.str.add(path.get(i).str);
		}
		for(int i=0;i<path.size();i++){
			frame.ep.add(getIndex(lst,path.get(i).str));
		}
		double min= Double.MAX_VALUE;
		double max= Double.MIN_VALUE;
			for(int i=0;i<row_size;i++){
				if(matrx[i][max_i]<min){
					min=matrx[i][max_i];	
				}
				if(matrx[i][max_j]<min){
					min=matrx[i][max_j];	
				}
				if(matrx[i][max_i]>max){
					max=matrx[i][max_i];	
				}
				if(matrx[i][max_j]>max){
					max=matrx[i][max_j];	
				}
			}
			double mul= 200.0/max;
			for(int i=0;i<row_size;i++){
			for(int j=0;j<row_size;j++){
					matrx[i][j]+=(-min)+1;	
			}
		}
		int HIE=700;
		int WID=700;
		frame.setSize(HIE,WID+1000);
		
		frame.setVisible(true);
		

		double area= 1.0*HIE*WID/lst.size();
		int col= (int)Math.sqrt(area)/2;
		int row=col;
		int inc=2*col;
		for(int i=0;i<lst.size();i++){
			frame.addNode(lst.get(i).str, (int)(mul*matrx[i][max_i]), (int)(mul*matrx[i][max_j]));
			/*frame.addNode(lst.get(i).str, row,col);
			col+=inc;
			if(col>WID){
				col=inc/2;
				row+=inc;
			}*/
		}
		for(int i=0;i<arr.length;i++){
			for(int j=0;j<arr.length;j++){
				if(arr[i][j]>=1) 
					frame.addEdge(i, j);
			}
			
		}

		
/*		frame.addNode("a", 50,50);
		frame.addNode("b", 100,100);
		frame.addNode("longNode", 200,200);
		frame.addEdge(0,1);
		frame.addEdge(0,2);
*/
		
		frame.repaint();
		
		
		
		/*Graphics g = frame.getGraphics();
		g.setColor(Color.RED);
		FontMetrics f = g.getFontMetrics();
		
			int nodeHeight = Math.max(frame.height, f.getHeight());
			GraphDraw.Node n=frame.getNode(lst.get(0).str);
			GraphDraw.edge e=frame.getEdge(0,2);
			int nodeWidth = Math.max(frame.width, f.stringWidth(n.name)+frame.width/2);
			g.setColor(Color.RED);
		   // g.drawLine(frame.nodes.get(e.i).x, frame.nodes.get(e.i).y,
		    //		frame.nodes.get(e.j).x, frame.nodes.get(e.j).y);
		    g.setColor(Color.RED);
		    g.fillOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
			       nodeWidth, nodeHeight);
		    g.setColor(Color.RED);
		    g.drawOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
			       nodeWidth, nodeHeight);
		    
		    g.drawString(n.name, n.x-f.stringWidth(n.name)/2,
				 n.y+f.getHeight()/2);  
		    //frame.setVisible(true);
		    frame.update(g);*/
	}
}

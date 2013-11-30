import java.util.Random;
import java.util.Scanner;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String str;
		
		Scanner in= new Scanner(System.in);
		//Size M
		int m= in.nextInt();		
		str=generate(m);
		
		String goal = str;
		
		int n= in.nextInt();
		double alpha= in.nextDouble();
		
		//Generating a new squence
		GenSequence gen=new GenSequence(str.toUpperCase(),n,alpha);		
	
		str=gen.GenSeq(str);
		System.out.println("Generated Sequence: :"+str);
		System.out.println("Goal Sequence: :"+goal);
		
		// A* algorithm
		Search search= new Search(goal.toUpperCase());
		
		search.start_search(str);
		
		
		//GUI
		//Laplachian lap_graph = new Laplachian(search.open,search.closed);
		//lap_graph.makeMatrice();
		//lap_graph.getEigenVector();
		//lap_graph.drawGraph(search.path);
		}

	public static String generate(int m) {
		// TODO Auto-generated method stub
		char[] charset = new char[4];
		charset[0]='A';
		charset[1]='C';
		charset[2]='T';
		charset[3]='G';
		
		Random r= new Random();
		String str="";
		for(int i=0;i<m;i++){
			str=str+charset[r.nextInt(4)];
		}
		return str;
	}

}

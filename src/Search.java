import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class Search {
	ArrayList<Node> closed;
	ArrayList<Node> open;
	ArrayList<Character> dict;
	ArrayList<Node> path;
	String goal;
	private static int insertionCost = 1, deletionCost = 1, substitutionCost = 1, swapCost = 1;

	// Get index of string from Open List
	public int getOpen(String string){
		for(int i=0;i<open.size();i++){
			if(open.get(i).str.equals(string))
				return i;
		}
		System.out.println("NOT COME HERE");
		return -1;

	}
	// Get index of string from closed list
	public int getClosed(String string){
		for(int i=0;i<closed.size();i++){
			if(closed.get(i).str.equals(string))
				return i;
		}
		System.out.println("NOT COME HERE");
		return -1;

	}
	// check if already present in open list
	public boolean openContains(String string){
		for(int i=0;i<open.size();i++){
			if(open.get(i).str.equals(string))
				return true;
		}
		return false;
	}

	// check if closed list contains the node string
	public boolean closedContains(String string){
		for(int i=0;i<closed.size();i++){
			if(closed.get(i).str.equals(string))
				return true;
		}
		return false;
	}

	// more generic function, if the node list contains a node of that string
	public boolean listContains(ArrayList<Node> lst, String string){
		for(int i=0;i<lst.size();i++){
			if(lst.get(i).str.equals(string))
				return true;
		}
		return false;
	}
	
	//just constructor setting goal state
	public Search(String str){
		//initializing closed and open lists
		closed= new ArrayList<Node>();
		open=new ArrayList<Node>();
		
		//the dictionary 
		dict = new ArrayList<Character>();
		dict.add('A');
		dict.add('C');
		dict.add('T');
		dict.add('G');
		
		// goalstate is the final state we want to achieve 
		goal=str;
	}

	// we have set insertion , deletion,...costs. That would determine the edge length
	public int getCost(char x){
		switch(x){
		case 'i': return insertionCost;
		case 'd': return deletionCost;
		case 's': return substitutionCost;
		case 't': return swapCost;
		case 'x': return 0;
		}
		System.out.println("Ideally not come here");
		return 0;
	}

	
	// A* algo
	public void start_search(String start) {
		//Initializing head node
		Node startNode= new Node(start,null,0,getEditDistance(start, goal),-1,'x');
		//adding head node to open
		open.add(startNode);

		while(open.size()!=0){
			//getIndex with lowest f value
			int index=getIndex(open);
			//get Node with lowest f value and remove from open,adding to closed
			Node curr= open.get(index);
			open.remove(index);
			closed.add(curr);

			//if goal reached
			if(curr.str.equals(goal)){
				
				//Reconstruct path				
				System.out.println("Done");
				
				path = new ArrayList<Node>();
				path.add(curr);
				Node n=curr;
				while(n.parent!=null){
					path.add(0, n.parent);
					n=n.parent;
				}
				print(path);
				return;
			
			}

			ArrayList<Node> neigh= moveGen(curr);

			for(int i=0;i<neigh.size();i++){
				String nodeStr= neigh.get(i).str;
				
				// not belonging to open nor closed
				if(!openContains(nodeStr) && !closedContains(nodeStr)){
					open.add(neigh.get(i));					
				}

				//node belonging to open
				else if(openContains(nodeStr)){					
					int indexOpen=getOpen(nodeStr);
					neigh.set(i, open.get(indexOpen));
					if(curr.g_val + getCost(open.get(indexOpen).op_code) < open.get(indexOpen).g_val){
						open.get(indexOpen).parent=curr;
						open.get(indexOpen).g_val=curr.g_val + getCost(open.get(indexOpen).op_code);
						open.get(indexOpen).f_val=open.get(indexOpen).g_val+open.get(indexOpen).h_val;						
					}
				}
				
				//node belonging to closed
				else if(closedContains(nodeStr)){
					int indexClosed=getClosed(nodeStr);
					neigh.set(i, closed.get(indexClosed));
					if(curr.g_val + getCost(closed.get(indexClosed).op_code) < closed.get(indexClosed).g_val){
						closed.get(indexClosed).parent=curr;
						closed.get(indexClosed).g_val=curr.g_val + getCost(closed.get(indexClosed).op_code);
						closed.get(indexClosed).f_val=closed.get(indexClosed).g_val + closed.get(indexClosed).h_val;						
						propagate(closed.get(indexClosed));
					}
				}
			}
		}
	}



	public void propagate(Node node) {
		// propagating values to closed node and recurively
		ArrayList<Node> neighGen= moveGen(node);
		ArrayList<Node> neigh= node.neigh;
		for(int i=0;i<neigh.size();i++){			
			int val= node.g_val+ getCost(neighGen.get(i).op_code);
			if(val < neigh.get(i).g_val){
				neigh.get(i).parent=node;
				neigh.get(i).g_val=val;
				if(closedContains(neigh.get(i).str)){
					propagate(neigh.get(i));
				}
			}
		}		
	}

	// getting index in open list of lowest f val
	public int getIndex(ArrayList<Node> open2) {
		int min=0;
		int min_val=Integer.MAX_VALUE;
		for(int i=0;i<open2.size();i++){
			if(open2.get(i).f_val < min_val){
				min_val=open2.get(i).f_val;
				min=i;
			}
		}		
		return min;
	}

	// generating all neighbours 
	public ArrayList<Node> moveGen(Node curr){
		ArrayList<Node> neigh= new ArrayList<Node>();
		neigh.addAll(addIns(curr));
		neigh.addAll(addDel(curr));
		neigh.addAll(addTrans(curr));
		neigh.addAll(addSubs(curr));		
		curr.neigh=neigh;
		return neigh;
	}

	public void print(ArrayList<Node> n){
		for(int i=0;i<n.size();i++){
			System.out.println(n.get(i).str +" "+n.get(i).op_code);
		}
		System.out.println();
	}


	// adding all substitutions
	public ArrayList<Node> addSubs(Node curr) {
		// TODO Auto-generated method stub
		ArrayList<Node> neigh= new ArrayList<Node>();
		for(int i=0;i<dict.size();i++){
			for(int j=0;j<curr.str.length();j++){
				if(dict.get(i)==curr.str.charAt(j))
					continue;
				String s_neg = curr.str.substring(0,j)+dict.get(i)+curr.str.substring(j+1,curr.str.length());			
				Node n_neg = new Node(s_neg,curr,curr.g_val+substitutionCost,getEditDistance(s_neg,goal),j,'s');
				if(!listContains(neigh, s_neg))
					neigh.add(n_neg);
			}
		}		
		return neigh;
	}

	//adding all transpositions
	public ArrayList<Node> addTrans(Node curr) {
		// TODO Auto-generated method stub
		ArrayList<Node> neigh= new ArrayList<Node>();
		for(int j=1;j<curr.str.length()-2;j++){
			String s_neg = curr.str.substring(0,j)+curr.str.charAt(j+1)+curr.str.charAt(j)+curr.str.substring(j+2,curr.str.length());
			Node n_neg = new Node(s_neg,curr,curr.g_val+swapCost,getEditDistance(s_neg,goal),j,'t');
			if(!listContains(neigh, s_neg))
				neigh.add(n_neg);
		}
		if(curr.str.length() >=2){
			String s_neg = curr.str.charAt(1)+ "" +curr.str.charAt(0) + curr.str.substring(2,curr.str.length());
			Node n_neg = new Node(s_neg,curr,curr.g_val+swapCost,getEditDistance(s_neg,goal),0,'t');
			if(!listContains(neigh, s_neg))
				neigh.add(n_neg);
		}
		if(curr.str.length() >=2){
			String s_neg =curr.str.substring(0,curr.str.length()-2)+ curr.str.charAt(curr.str.length()-1)+curr.str.charAt(curr.str.length()-2);
			Node n_neg = new Node(s_neg,curr,curr.g_val+swapCost,getEditDistance(s_neg,goal),0,'t');
			if(!listContains(neigh, s_neg))
				neigh.add(n_neg);
		}	
			return neigh;
		}

//	adding all deletions
		public ArrayList<Node> addDel(Node curr) {
			// TODO Auto-generated method stub
			ArrayList<Node> neigh= new ArrayList<Node>();
			for(int j=0;j<curr.str.length();j++){
				String s_neg = curr.str.substring(0,j)+curr.str.substring(j+1,curr.str.length());
				Node n_neg = new Node(s_neg,curr,curr.g_val+deletionCost,getEditDistance(s_neg,goal),j,'d');
				if(!listContains(neigh, s_neg))
					neigh.add(n_neg);
			}
			return neigh;}


		//adding all insertions
		public ArrayList<Node> addIns(Node curr) {
			ArrayList<Node> neigh= new ArrayList<Node>();
			for(int i=0;i<dict.size();i++){
				for(int j=0;j<curr.str.length()+1;j++){
					String s_neg = curr.str.substring(0,j)+dict.get(i)+curr.str.substring(j,curr.str.length());
					Node n_neg = new Node(s_neg,curr,curr.g_val+insertionCost,getEditDistance(s_neg,goal),j,'i');
					if(!listContains(neigh, s_neg))
						neigh.add(n_neg);
				}
			}		
			return neigh;
		}

// Edit distance, i.e. our heuristic function
		public static int getEditDistance(String source, String target) {
			int count=0;;
			for(int i=0; i< source.length() && i<target.length();i++){
				if(source.charAt(i)!=target.charAt(i)){
					count++;
				}
			}
			return count;
		}	
			/*
			
			if (source.length() == 0){
				return target.length()*insertionCost;
			}
			if (target.length() == 0){
				return source.length()*deletionCost;
			}

			int[][] dp = new int[source.length()][target.length()];
			Map<Character, Integer> sourceIndex = new HashMap<Character, Integer>();
			if (source.charAt(0) != target.charAt(0)){
				dp[0][0] = Math.min(substitutionCost, deletionCost + insertionCost);
			}
			sourceIndex.put(source.charAt(0), 0);
			for (int i = 1; i < source.length(); i++) {
				int deleteDistance = dp[i - 1][0] + deletionCost;
				int insertDistance = (i + 1) * deletionCost + substitutionCost;
				int matchDistance = i * deletionCost + (source.charAt(i) == target.charAt(0) ? 0 : substitutionCost);
				dp[i][0] = Math.min(Math.min(deleteDistance, insertDistance), matchDistance);
			}
			for (int j = 1; j < target.length(); j++) {
				int deleteDistance = dp[0][j - 1] + insertionCost;
				int insertDistance = (j + 1) * insertionCost + deletionCost;
				int matchDistance = j * insertionCost + (source.charAt(0) == target.charAt(j) ? 0 : substitutionCost);
				dp[0][j] = Math.min(Math.min(deleteDistance, insertDistance), matchDistance);
			}
			for (int i = 1; i < source.length(); i++) {
				int maxSourceLetterMatchIndex = source.charAt(i) == target.charAt(0) ? 0 : -1;
				for (int j = 1; j < target.length(); j++) {
					Integer candidateSwapIndex = sourceIndex.get(target.charAt(j));
					int jSwap = maxSourceLetterMatchIndex;
					int deleteDistance = dp[i - 1][j] + deletionCost;
					int insertDistance = dp[i][j - 1] + insertionCost;
					int matchDistance = dp[i - 1][j - 1];
					if (source.charAt(i) != target.charAt(j)) {
						matchDistance += substitutionCost;
					} 
					else {
						maxSourceLetterMatchIndex = j;
					}
					int swapDistance;
					if (candidateSwapIndex != null && jSwap != -1) {
						int iSwap = candidateSwapIndex;
						int preSwapCost;
						if (iSwap == 0 && jSwap == 0) {
							preSwapCost = 0;
						} 
						else {
							preSwapCost = dp[Math.max(0, iSwap - 1)][Math.max(0, jSwap - 1)];
						}
						swapDistance = preSwapCost + (i-iSwap-1) * deletionCost + (j-jSwap-1) * insertionCost + swapCost;
					} 
					else {
						swapDistance = Integer.MAX_VALUE;
					}
					dp[i][j] = Math.min(Math.min(Math.min(deleteDistance, insertDistance),matchDistance), swapDistance);
				}
				sourceIndex.put(source.charAt(i), i);
			}
			return dp[source.length() - 1][target.length() - 1];
		}
		
	}
	*/
}
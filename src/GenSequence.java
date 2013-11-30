import java.util.ArrayList;
import java.util.Random;


public class GenSequence {
	ArrayList<Character> dict ;
	String str;
	int n;
	double alpha;
	
	public GenSequence(String str, int n, double alpha){
		dict = new ArrayList<Character>();
		this.str=str;
		this.n=n;
		this.alpha=alpha;
		//for(int i=0;i<str.length();i++){
		//	if(!dict.contains(str.charAt(i)))
		//		dict.add(str.charAt(i));		
		//}
		dict.add('A');
		dict.add('C');
		dict.add('T');
		dict.add('G');
	}
	
	public String GenSeq(String str){
		String str_new = remove(str,n);
		str_new = permute(str_new);
		return str_new;
	}
	
	public String permute(String str_new) {
		Random r= new Random();
		
		for(int i=0;i<str_new.length();i++){
			if(r.nextDouble() >alpha){
				//transposition
				if(r.nextDouble() > 0.2){
					if(i<str_new.length()-1){
						str_new=str_new.substring(0,i)+str.charAt(i+1)+str.charAt(i)+str_new.substring(i+2,str_new.length());						
					}	
				}
				//substitution (deletion and insertion)
				else{
					int y=r.nextInt(dict.size());
					while(dict.get(y)==str_new.charAt(i)){
						y=r.nextInt(dict.size());
					}
					
					str_new=str_new.substring(0,i)+dict.get(y)+str_new.substring(i+1,str_new.length());
				}
			}
		}
		
		return str_new;
	}

	public String remove(String str, int n){
		if(n>str.length()) return str;
		String gen=str;
		int remove= str.length()-n;
		Random r= new Random();
		for(int i=0;i<remove;i++){
			int pos= r.nextInt(gen.length());
			int k=0;
			String s=gen;
			gen="";
			for(int j=0;j<s.length();j++){
				if(j!=pos)
					gen+=s.charAt(j);
			}
		}		
		return gen;
	}
	
	
}

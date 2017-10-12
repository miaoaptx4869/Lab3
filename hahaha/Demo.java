package hahaha;

import java.util.*;
import java.io.File;
import java.util.Random;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Properties;

public class Demo {
	public static int [][] arrays = new int[100][100];
	public static String[] newwords = new String[100];
	public static int place(String[] words , String word){
		int i;
		for (i = 0 ; i < words.length ; i ++){
			if (word.equalsIgnoreCase(words[i])){
				return i;
			}
		}
		return -1;
	}
	public static void showDirectedGraph(int[][] arrays , String[] newwords){
		Graphviz gv = new Graphviz();
	    gv.addln(gv.start_graph());

		System.out.println("展示有向图：");
		int i , j;
		//String result = "";
		for (i = 0 ; i < arrays.length ; i ++){
			for (j = 0 ; j < arrays.length ; j ++){
				if (arrays[i][j] > 0){
					gv.addln(newwords[i] + " -> " + newwords[j] + "[label = " + arrays[i][j] + "]" + ";");
				}
			}
		}
		gv.addln(gv.end_graph());
		String type = "png";
		File out = new File("Graph." + type); 
	    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
	}
	public static String queryBridgeWords(String word1 , String word2){
		int i , j = 0 , k , place1 , place2;
		String[] bridgewords = new String[100];
		String result = new String();
		place1 = place(newwords , word1);
		place2 = place(newwords , word2);
		if (place1 != -1 && place2 != -1){
			for (i = 0 ; i < newwords.length ; i ++){
				if (arrays[place1][i] > 0){
					if (arrays[i][place2] > 0){
						bridgewords[j] = newwords[i];
						j ++;
					}
				}
			}
			if (j == 0 ){
				result = "No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!";
			}
			else if (j == 1){
				result = "The bridge words from \"" + word1 +  "\" to \"" + word2 + "\" is:" + bridgewords[0];
			}
			else if (j > 1){
				result = "The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are:";
				for (k = 0 ; k < j - 1; k ++){
					result = result + bridgewords[k] + ",";
				}
				result = result + "and " + bridgewords[j - 1] + "\n";
			}
		}
		else if (place1 != -1){
			result = "No \"" + word2 + "\" in the graph!";
		}
		else if (place2 != -1){
			result = result + "No \"" + word1 + "\" in the graph!";
		}
		else{
			result = result + "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
		}
		return result;
	}
	public static String findBridgeWords(String word1 , String word2){
		int i , j = 0 , k , place1 , place2;
		String[] bridgewords = new String[100];
		String result = new String();
		place1 = place(newwords , word1);
		place2 = place(newwords , word2);
		if (place1 != -1 && place2 != -1){
			for (i = 0 ; i < newwords.length ; i ++){
				if (arrays[place1][i] > 0){
					if (arrays[i][place2] > 0){
						bridgewords[j] = newwords[i];
						j ++;
					}
				}
			}
			if (j == 0 ){
				return "#";
			}
			else if (j == 1){
				return bridgewords[0];
			}
			else if (j > 1){
				long t = System.currentTimeMillis();
				Random rd = new Random(t);
				k = (int)(rd.nextInt()*j);
				return bridgewords[k];
			}
		}
		else{
			return "#";
		}
		return "#";
	}
	public static String generateNewText(String inputText){
		String newst = inputText.replaceAll("[\\p{Punct}]", " ");
	    String news = newst.replaceAll("[^a-zA-Z\\s]", "");
	    String[] newchars = news.split("\\s+");
	    int i;
	    String bw = new String();
	    if (newchars.length == 1){
	    	System.out.println(newchars[0]);
	    	return "#";
	    }
	    for( i = 0 ; i < newchars.length - 1 ; i ++){
	    	System.out.print(newchars[i] + " ");
	    	bw = findBridgeWords(newchars[i] , newchars[i + 1]);
	    	if (bw != "#"){
	    		System.out.print(bw + " ");
	    	}
		}
	    System.out.println(newchars[newchars.length - 1]);
	    return "#";
	}
	public static String calcShortestPath(String word1 , String word2){
		int v = newwords.length;
		int[][] P = new int[v][v];
		int[][] A = new int[v][v];
		int i , j , k;
		for (i = 0 ; i < v ; i ++){
			for (j = 0 ; j < v ; j ++){
				if (arrays[i][j] == 0){
					A[i][j] = 100;
					P[i][j] = j;
				}
				else{
					A[i][j] = arrays[i][j];
					P[i][j] = -1;
				}
			}
		}
		for (k = 0 ; k < v ; k ++){
			for (i = 0 ; i < v ; i ++){
				for (j = 0 ; j < v ; j ++){
					if (A[i][k] + A[k][j] < A[i][j]){
						//System.out.println( A[i][k] + " " + A[k][j]);
						A[i][j] = A[i][k] + A[k][j];
						P[i][j] = k;
						
					}
				}
			}
		}
		int bnf , enf , temp , pathvalue , sum = 0;
		bnf = place(newwords , word1);
		enf = place(newwords , word2);
		if (bnf == -1 || enf == -1){
			System.out.println("单词不存在！！");
			return "#";
		}
		if (bnf == enf){
			System.out.println( "\n最短路径的长度为0" );
			return "#";
			
		}
		temp = P[bnf][enf];
		pathvalue = bnf;
	    Graphviz gv = new Graphviz();
		gv.addln(gv.start_graph());
		for (i = 0 ; i < newwords.length ; i ++){
			for ( j = 0 ; j < newwords.length ;j ++){
				if (arrays[i][j] > 0){
					gv.addln(newwords[i] + " -> " + newwords[j] + "[label = " + arrays[i][j] + "]" + ";");
				}
			}
		}
		if (arrays[bnf][enf] > 0){
	    	gv.addln(newwords[bnf] + " -> " + newwords[enf] + "[color = " + "\"blue\"" +"]" + ";");;
	    	System.out.println(word1 + "->" + word2 + "\n最短路径长度为1");
	    	return "#";
	    }
	    int n = 0;
		int[] path = new int[newwords.length];
	    if (temp != -1){
	    	path[0] = bnf;
	    	n ++;
	    	int temp1 = temp;
    		int[] oldpath = new int[newwords.length];
    		int oldn = 1;
    		while(temp1 != -1){
    			oldpath[oldn] = temp1;
		        temp1 = P[bnf][temp1];
		        oldn ++;
    		}
    		for (i = 2; i <= oldn - 1 ; i ++){
    			path[i - 1] = oldpath[oldn - i + 1];
    		}
    		n = oldn - 1;
	    	while(temp != -1)
		    {
		        sum = sum + A[pathvalue][enf];
		        path[n] = temp;
		        temp = P[temp][enf];
		        pathvalue = temp;
		        n ++;
		    }
	    }
	    if (sum == 0)
	    {
	        System.out.println("不可达");
	    }
	    else{
	    	path[n] = enf;
	    	for (i = 0 ; i < n ; i ++){
	    		gv.addln(newwords[path[i]] + " -> " + newwords[path[i + 1]] + "[color = " + "\"blue\"" +"]" + ";");;
	    		System.out.print(newwords[path[i]] + " -> ");
	    	}
	    	System.out.println(newwords[enf]);
	    	System.out.println("最短路径长度为" + A[bnf][enf]);
	    }
	    gv.addln(gv.end_graph());
		String type = "png";
		File out = new File("ShortestPath." + type); 
	    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );      
	    return "#";
	}
	public static String randomWalk() throws Exception {
		 
		 int [][] newarrays = new int[arrays.length][arrays.length];
		 int i , j , k , kn , flag = 1;
		 PrintWriter out = new PrintWriter("my.txt");
		 for(i = 0 ; i < arrays.length ; i ++){
			 for(j = 0 ; j < arrays.length ; j ++){
				 newarrays[i][j] = arrays[i][j];
			 }
		 }
		 j = arrays.length;
		 java.util.Random r = new java.util.Random();
		 k = r.nextInt(j);
		 out.print(newwords[k] + " ");
		 Scanner in = new Scanner(System.in);
		 System.out.print("是否要继续(Y or N)?");
		 String inpu = in.nextLine() ;
		 while(inpu.equals("Y")){
			 int [] lines = new int[arrays.length];
			 j = 0;
			 for (i = 0 ; i < arrays.length ; i ++){
				 if(newarrays[k][i] != 0){
					 lines[j] = i;
					 j ++;
				 }
			 }
			 if (j == 0){
				 System.out.print("已经没有路了！");
				 break;
			 }
			 java.util.Random rn = new java.util.Random();
			 kn = rn.nextInt(j);
		     out.print(newwords[lines[kn]] + " ");
			 System.out.print("是否要继续(Y or N)?");
			 String inp = in.nextLine();
			 if(inp.equals("N")){
				 break;
			 }
			 if (newarrays[k][lines[kn]] == -1){
				 break;
			 }
			 else{
				 newarrays[k][lines[kn]] = -1;
				 k = lines[kn];
			 }
		 }
		 out.close();
		 return "#";
	}
	public static void main(String[] args) throws Exception{
    
    Scanner in = new Scanner(System.in);
    System.out.println("Where?");
    String place = in.nextLine();
    System.out.println("What's your name?");
    String name = in.nextLine();
    String newalpha ="";
    File file = new File(place + name);
    Scanner input = new Scanner(file);
    while (input.hasNext()){
    	String alpha = input.nextLine();
    	newalpha = newalpha + alpha + " ";
    }
    input.close();
    String newstr = newalpha.replaceAll("[\\p{Punct}]", " ");
    String newstring = newstr.replaceAll("[^a-zA-Z\\s]", "");
    String newnew = newstring.toLowerCase();
    String[] words = newnew.split("\\s+");
    int num = words.length;
    int i , j , k , sign = 1 , number = 0;
    newwords[0] = words[0];
    for (i = 1 ; i < num ; i ++){
    	sign = 1;
    	for (k = 0 ; k <= number ; k ++){
    		if (newwords[k].equalsIgnoreCase(words[i])){
    			sign = 0;
    		}
    	}
    	if (sign == 1){
    		number ++;
    		newwords[number] = words[i];
    	}
    }
    	
    for (i = 0 ; i < num - 1; i ++){
    	j = place(newwords , words[i]);
    	k = place(newwords , words[i + 1]);
    	if (j != k){
    		arrays[j][k] ++;
    	}
    	
    place(newwords , "yy");	
    }
    
    while(true){
    	System.out.println("选择要选择的功能：");
    	System.out.println("1.展示有向图：");
    	System.out.println("2.查询桥接词：");
    	System.out.println("3.根据桥接词生成新文本：");
    	System.out.println("4.计算两个单词间的最短路径：");
    	System.out.println("5.随机游走：");
    	System.out.println("6.退出");
    	System.out.println("请输入你的选择：");
    	int choice = in.nextInt();
    	switch(choice){
    	case 1:
    		System.out.println("1.展示有向图：");
    		showDirectedGraph(arrays , newwords);
    		break;
    	case 2:
    		System.out.println("2.查询桥接词：");
    		System.out.println("请输入你要查找的单词：");
    		String word1 = in.next();
    		String word2 = in.next();
    		String result = new String();
    		String newword1 = word1.toLowerCase();
    		String newword2 = word2.toLowerCase();
    		result = queryBridgeWords(newword1 , newword2);
    		System.out.println(result);
    		break;
    	case 3:
    		System.out.println("3.根据桥接词生成新文本：");
    		System.out.println("Please input:");
    		in.nextLine();
    		String newin = in.nextLine();
    		generateNewText(newin);
    		break;
    	case 4:
    		System.out.println(num);
    		System.out.println("4.计算两个单词间的最短路径：");
    		System.out.println("请输入你要查找的最短路径：");
    		String worded1 = in.next();
    		System.out.println("输入下一单词（若只有一个单词输入#）：");
    		String worded2 = in.next();
    		if (worded2.equals("#")){
    			int place0 = place(newwords , worded1);
    			for (i = 0 ; i < number ; i ++){
    				if (i != place0){
    					calcShortestPath(worded1 , newwords[i]);
    				}
    			}
    		}
    		else{
    			calcShortestPath(worded1 , worded2);
    		}
    		
    		break;
    	case 5:
    		System.out.println("5.随机游走：");
    	 	randomWalk();
    	 	break;
    	case 6:
    		System.out.println("结束！");
    		in.close();
    		System.exit(1);
    		break;
    	default:
    		System.out.println("ERROR!");
    		System.exit(1);
    	} 
    }
	}

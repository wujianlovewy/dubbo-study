package cn.edu.wj.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 图的理解： https://segmentfault.com/a/1190000002685939#articleHeader3
 */
public class AMWGraph {

	private List vertextList; // 存储点的链表
	private int numOfEdges; // 边的数目
	private int edges[][]; // 邻接矩阵

	public AMWGraph(int vertextNum) {
		vertextList = new ArrayList(vertextNum);
		this.edges = new int[vertextNum][vertextNum];
		this.numOfEdges = 0;
		isVisited = new boolean[vertextNum];
	}

	// 插入节点
	public void insertVertex(String vertext) {
		vertextList.add(vertext);
	}

	// 插入边
	public void insertEdge(int v1, int v2, int weight) {
		edges[v1][v2] = weight;
		this.numOfEdges++;
	}

	// 删除边
	public void deleteEdge(int v1, int v2) {
		edges[v1][v2] = 0;
		this.numOfEdges--;
	}

	// 获取节点的数量
	public int getNumOfVertex() {
		return this.vertextList.size();
	}

	// 获取边的数量
	public int getNumOfEdges() {
		return this.numOfEdges;
	}

	// 返回节点的数据
	public Object getValueByIndex(int index) {
		return this.vertextList.get(index);
	}

	// 返回(v1,v2)的权值
	public int getWeight(int v1, int v2) {
		return this.edges[v1][v2];
	}

	// 获取第一个节点的下标
	public int getFirstNeighbor(int index) {
		for (int j = 0; j < this.vertextList.size(); j++) {
			if (this.edges[index][j] > 0){
				//System.out.print("[F:("+index+","+j+")]- ");
				return j;
			}
		}
		return -1;
	}

	// 根据前一个节点获取下一个节点
	public int getNextNeighbor(int v1, int v2) {
		for (int j = v2 + 1; j < this.vertextList.size(); j++) {
			if (this.edges[v1][j] > 0){
				//System.out.print("[N:("+v1+","+j+")]- ");
				return j;
			}
		}
		return -1;
	}
	
	private boolean[] isVisited = null;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void broadFirstSearch(int index){
		System.out.print(this.getValueByIndex(index)+" ");
		this.isVisited[index] = true; 
		LinkedList queue = new LinkedList();
		queue.add(index);
		while(!queue.isEmpty()){
			
			int queueIndex = (Integer)queue.removeFirst();
			int nextIndex = this.getFirstNeighbor(queueIndex);
			while(nextIndex!=-1){
				if(!this.isVisited[nextIndex]){
					this.isVisited[nextIndex] = true;
					queue.add(nextIndex);
					System.out.print(this.getValueByIndex(nextIndex)+" ");
				}
				nextIndex = this.getNextNeighbor(queueIndex, nextIndex);
			}
		}
	}
	
	public void clearVisited(){
		Arrays.fill(isVisited, false);
	}
	
	//图的广度遍历
	public void broadFirstSearch(){
		for(int i=0;i<this.vertextList.size(); i++){
			if(!this.isVisited[i]){
				this.broadFirstSearch(i);
			}
		}
	}
	
	private void depthFirstSearch(int index){
		System.out.print(this.getValueByIndex(index)+" ");
		this.isVisited[index] = true; 
		int nextIndex = this.getFirstNeighbor(index);
		while(nextIndex!=-1){
			if(!this.isVisited[nextIndex]){
				this.depthFirstSearch(nextIndex);
			}
			nextIndex = this.getNextNeighbor(index, nextIndex);
		}
	}
	
	//图的深度遍历
	public void depthFirstSearch(){
		for(int i=0;i<this.vertextList.size();i++){
			if(!this.isVisited[i]){
				this.depthFirstSearch(i);
			}
		}
		System.out.println();
	}
	
	public void printGraph(){
		  System.out.println("打印出邻接矩阵: ");
	        for(int i=0; i<this.vertextList.size();i++){
	        	for(int j=0; j<this.vertextList.size(); j++){
	        		if(this.edges[i][j]>0){
	        			System.out.print(1+" ");
	        		}else{
	        			System.out.print(0+" ");
	        		}
	        	}
	        	System.out.println();
	        }
	}

	public static void main(String args[]) {
		test2();
	}
	
	public static void test2(){
		int n=8;//分别代表结点个数和边的数目
        String labels[]={"1","2","3","4","5","6","7","8"};//结点的标识
        AMWGraph graph=new AMWGraph(n);
        for(String label:labels) {
            graph.insertVertex(label);//插入结点
        }
        //插入九条边
        graph.insertEdge(0, 1, 1);
        graph.insertEdge(0, 2, 1);
        graph.insertEdge(1, 3, 1);
        graph.insertEdge(1, 4, 1);
        graph.insertEdge(3, 7, 1);
        graph.insertEdge(4, 7, 1);
        graph.insertEdge(2, 5, 1);
        graph.insertEdge(2, 6, 1);
        graph.insertEdge(5, 6, 1);
        graph.insertEdge(1, 0, 1);
        graph.insertEdge(2, 0, 1);
        graph.insertEdge(3, 1, 1);
        graph.insertEdge(4, 1, 1);
        graph.insertEdge(7, 3, 1);
        graph.insertEdge(7, 4, 1);
        graph.insertEdge(6, 2, 1);
        graph.insertEdge(5, 2, 1);
        graph.insertEdge(6, 5, 1);
        
        graph.printGraph();
        
        System.out.println("深度优先搜索序列为：");
        graph.depthFirstSearch();
        System.out.println("========================");
        
        System.out.println("广度优先搜索序列为：");
        graph.clearVisited();
        graph.broadFirstSearch();
        System.out.println();
	}
	
	public static void test1(){
		int n = 4;// 分别代表结点个数和边的数目
		String labels[] = { "V0", "V1", "V2", "V3" };// 结点的标识
		AMWGraph graph = new AMWGraph(n);
		for (String label : labels) {
			graph.insertVertex(label);// 插入结点
		}
		
		// 插入四条边
		graph.insertEdge(0, 1, 2);
		graph.insertEdge(0, 2, 5);
		graph.insertEdge(2, 3, 8);
		graph.insertEdge(3, 0, 7);

		System.out.println("结点个数是：" + graph.getNumOfVertex());
		System.out.println("边的个数是：" + graph.getNumOfEdges());
		
		System.out.println("获取第["+0+"]行第一个节点下标是: "+graph.getFirstNeighbor(0));
		System.out.println("获取第[0,2]行第一个节点下标是: "+graph.getNextNeighbor(0, 2));

		graph.deleteEdge(0, 1);// 删除<V1,V2>边
		System.out.println("删除<V1,V2>边后...");
		System.out.println("结点个数是：" + graph.getNumOfVertex());
		System.out.println("边的个数是：" + graph.getNumOfEdges());
	}

}

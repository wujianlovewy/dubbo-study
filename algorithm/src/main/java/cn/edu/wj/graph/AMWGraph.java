package cn.edu.wj.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * ͼ����⣺ https://segmentfault.com/a/1190000002685939#articleHeader3
 */
public class AMWGraph {

	private List vertextList; // �洢�������
	private int numOfEdges; // �ߵ���Ŀ
	private int edges[][]; // �ڽӾ���

	public AMWGraph(int vertextNum) {
		vertextList = new ArrayList(vertextNum);
		this.edges = new int[vertextNum][vertextNum];
		this.numOfEdges = 0;
		isVisited = new boolean[vertextNum];
	}

	// ����ڵ�
	public void insertVertex(String vertext) {
		vertextList.add(vertext);
	}

	// �����
	public void insertEdge(int v1, int v2, int weight) {
		edges[v1][v2] = weight;
		this.numOfEdges++;
	}

	// ɾ����
	public void deleteEdge(int v1, int v2) {
		edges[v1][v2] = 0;
		this.numOfEdges--;
	}

	// ��ȡ�ڵ������
	public int getNumOfVertex() {
		return this.vertextList.size();
	}

	// ��ȡ�ߵ�����
	public int getNumOfEdges() {
		return this.numOfEdges;
	}

	// ���ؽڵ������
	public Object getValueByIndex(int index) {
		return this.vertextList.get(index);
	}

	// ����(v1,v2)��Ȩֵ
	public int getWeight(int v1, int v2) {
		return this.edges[v1][v2];
	}

	// ��ȡ��һ���ڵ���±�
	public int getFirstNeighbor(int index) {
		for (int j = 0; j < this.vertextList.size(); j++) {
			if (this.edges[index][j] > 0){
				//System.out.print("[F:("+index+","+j+")]- ");
				return j;
			}
		}
		return -1;
	}

	// ����ǰһ���ڵ��ȡ��һ���ڵ�
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
	
	//ͼ�Ĺ�ȱ���
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
	
	//ͼ����ȱ���
	public void depthFirstSearch(){
		for(int i=0;i<this.vertextList.size();i++){
			if(!this.isVisited[i]){
				this.depthFirstSearch(i);
			}
		}
		System.out.println();
	}
	
	public void printGraph(){
		  System.out.println("��ӡ���ڽӾ���: ");
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
		int n=8;//�ֱ����������ͱߵ���Ŀ
        String labels[]={"1","2","3","4","5","6","7","8"};//���ı�ʶ
        AMWGraph graph=new AMWGraph(n);
        for(String label:labels) {
            graph.insertVertex(label);//������
        }
        //���������
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
        
        System.out.println("���������������Ϊ��");
        graph.depthFirstSearch();
        System.out.println("========================");
        
        System.out.println("���������������Ϊ��");
        graph.clearVisited();
        graph.broadFirstSearch();
        System.out.println();
	}
	
	public static void test1(){
		int n = 4;// �ֱ����������ͱߵ���Ŀ
		String labels[] = { "V0", "V1", "V2", "V3" };// ���ı�ʶ
		AMWGraph graph = new AMWGraph(n);
		for (String label : labels) {
			graph.insertVertex(label);// ������
		}
		
		// ����������
		graph.insertEdge(0, 1, 2);
		graph.insertEdge(0, 2, 5);
		graph.insertEdge(2, 3, 8);
		graph.insertEdge(3, 0, 7);

		System.out.println("�������ǣ�" + graph.getNumOfVertex());
		System.out.println("�ߵĸ����ǣ�" + graph.getNumOfEdges());
		
		System.out.println("��ȡ��["+0+"]�е�һ���ڵ��±���: "+graph.getFirstNeighbor(0));
		System.out.println("��ȡ��[0,2]�е�һ���ڵ��±���: "+graph.getNextNeighbor(0, 2));

		graph.deleteEdge(0, 1);// ɾ��<V1,V2>��
		System.out.println("ɾ��<V1,V2>�ߺ�...");
		System.out.println("�������ǣ�" + graph.getNumOfVertex());
		System.out.println("�ߵĸ����ǣ�" + graph.getNumOfEdges());
	}

}

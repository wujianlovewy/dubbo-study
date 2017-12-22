package cn.edu.wj.tree;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTree<T> {
	
	//���ڵ�
	private TreeNode rootNode;
	//���ĸ߶�
	private int treeHeight;
	//�ܵĽڵ���
	private int nodeNum;
	
	public BinaryTree(T... arr){
		this.rootNode = buildBinaryTree(1, arr);
		this.treeHeight = this.height(rootNode);
		this.nodeNum = this.size(rootNode);
	}
	
	//�ݹ�������ĸ߶�
	public int height(TreeNode treeNode){
		if(treeNode==null){
			return 0;
		}
		int lHeight = height(treeNode.left);
		int rHeight = height(treeNode.right);
		return (lHeight > rHeight ? (lHeight+1) : (rHeight+1));
	}
	
	//�ݹ�������ڵ���
	public int size(TreeNode treeNode){
		if(treeNode==null){
			return 0;
		}
		return 1+size(treeNode.left)+size(treeNode.right);
	}
	
	/**
	 * �������鹹��������
	 * @param arr
	 * @return
	 */
	TreeNode buildBinaryTree(int index, T... arr){
		if(arr!=null && arr.length!=0 && index<arr.length){
			TreeNode treeNode = new TreeNode(arr[index]);
			treeNode.left = buildBinaryTree(2*index, arr);
			treeNode.right = buildBinaryTree(2*index+1, arr);
			return treeNode;
		}
		return null;
	}
	
	public void visted(TreeNode node){
		node.isVisted = true;
		System.out.print(node.getValue()+" ");
	}
	
	//ǰ�����
	public void preOrder(TreeNode treeNode){
		if(treeNode!=null){
			this.visted(treeNode);
			this.preOrder(treeNode.left);
			this.preOrder(treeNode.right);
		}
	}
	
	//�������
	public void inOrder(TreeNode treeNode){
		if(treeNode!=null){
			this.inOrder(treeNode.left);
			this.visted(treeNode);
			this.inOrder(treeNode.right);
		}
	}
	
	//�������
	public void postOrder(TreeNode treeNode){
		if(treeNode!=null){
			this.postOrder(treeNode.left);
			this.postOrder(treeNode.right);
			this.visted(treeNode);
		}
	}
	
	//ʹ��stackʵ��ǰ�����
	public void preTraversal(TreeNode treeNode){
		Stack<TreeNode> stack = new Stack<TreeNode>();
		stack.push(treeNode);
		while(!stack.isEmpty()){
			TreeNode node = stack.pop();
			while(node!=null){
				this.visted(node);
				if(node.right!=null){
					stack.push(node.right);
				}
				node = node.left;
			}
		}
	}
	
	//ʹ��stackʵ��ǰ�����2
	public void preTraversal2(TreeNode treeNode){
		Stack<TreeNode> stack = new Stack<TreeNode>();
		TreeNode node = treeNode;
		while(stack.size()>0 || node!=null){
			while(node!=null){
				stack.push(node);
				node = node.left;
			}
			if(stack.size()>0){
				node = stack.pop();
				this.visted(node);
				node = node.right;
			}
		}
	}
	
	public void depthOrderTraversal(TreeNode treeNode){
		ArrayDeque<TreeNode> queue = new ArrayDeque<BinaryTree<T>.TreeNode>();
		queue.push(treeNode);
		while(!queue.isEmpty()){
			TreeNode node = queue.pop();
			if(node!=null){
				this.visted(node);
				if(node.right!=null){
					queue.push(node.right);
				}
				if(node.left!=null){
					queue.push(node.left);
				}
			}
		}
	}
	
	//����������߹�����ȱ���
	public void levelIterator(TreeNode treeNode){
		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		queue.offer(treeNode);
		while(!queue.isEmpty()){
			TreeNode node = queue.poll();
			if(node!=null){
				this.visted(node);
			}
			if(node.left!=null){
				queue.offer(node.left);
			}
			if(node.right!=null){
				queue.offer(node.right);
			}
		}
	}
	
	/**  
     *                  13 
     *                 /  \ 
     *               65    5 
     *              /  \    \ 
     *             97  25   37 
     *            /    /\   / 
     *           22   4 28 32 
     */  
	public static void main(String[] args) {
		Integer[] treeData = new Integer[]{0,13,65,5,97,25,0,37,22,0,4,28,0,0,32,0};
		BinaryTree<Integer> tree = new BinaryTree<Integer>(treeData);
		
		System.out.println("���ĸ߶�: "+tree.getTreeHeight()+", �ܽڵ���:"+tree.getNodeNum());
		System.out.println(tree.rootNode.left.getValue());
		
		//ǰ���С��������Ҳ��������ȱ���
		System.out.println("����������: ");
		tree.postOrder(tree.rootNode);
		System.out.println();
		
		System.out.println("����������: ");
		tree.inOrder(tree.rootNode);
		System.out.println();
		
		System.out.println("����������: ");
		tree.levelIterator(tree.rootNode);
		
		System.out.println();
		System.out.println("ǰ��������: ");
		tree.preOrder(tree.rootNode);

		System.out.println();
		System.out.println("stackǰ��������: ");
		tree.preTraversal(tree.rootNode);
		
		System.out.println();
		System.out.println("stack2ǰ��������: ");
		tree.preTraversal2(tree.rootNode);
		
		System.out.println();
		System.out.println("depthOrderTraversal�������: ");
		tree.depthOrderTraversal(tree.rootNode);
		
	}
	
	public int getTreeHeight() {
		return treeHeight;
	}

	public int getNodeNum() {
		return nodeNum;
	}

	public TreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(TreeNode rootNode) {
		this.rootNode = rootNode;
	}

	class TreeNode{
		T value; //ֵ
		boolean isVisted=false;
		public TreeNode left; 
		public TreeNode right;
		
		public TreeNode(T value) {
			this.value = value;
		}

		public T getValue() {
			return value;
		}
		
	}
	
}

package cn.edu.wj.tree;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTree<T> {
	
	//根节点
	private TreeNode rootNode;
	//树的高度
	private int treeHeight;
	//总的节点数
	private int nodeNum;
	
	public BinaryTree(T... arr){
		this.rootNode = buildBinaryTree(1, arr);
		this.treeHeight = this.height(rootNode);
		this.nodeNum = this.size(rootNode);
	}
	
	//递归遍历树的高度
	public int height(TreeNode treeNode){
		if(treeNode==null){
			return 0;
		}
		int lHeight = height(treeNode.left);
		int rHeight = height(treeNode.right);
		return (lHeight > rHeight ? (lHeight+1) : (rHeight+1));
	}
	
	//递归遍历树节点数
	public int size(TreeNode treeNode){
		if(treeNode==null){
			return 0;
		}
		return 1+size(treeNode.left)+size(treeNode.right);
	}
	
	/**
	 * 根据数组构建二叉树
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
	
	//前序遍历
	public void preOrder(TreeNode treeNode){
		if(treeNode!=null){
			this.visted(treeNode);
			this.preOrder(treeNode.left);
			this.preOrder(treeNode.right);
		}
	}
	
	//中序遍历
	public void inOrder(TreeNode treeNode){
		if(treeNode!=null){
			this.inOrder(treeNode.left);
			this.visted(treeNode);
			this.inOrder(treeNode.right);
		}
	}
	
	//后序遍历
	public void postOrder(TreeNode treeNode){
		if(treeNode!=null){
			this.postOrder(treeNode.left);
			this.postOrder(treeNode.right);
			this.visted(treeNode);
		}
	}
	
	//使用stack实现前序遍历
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
	
	//使用stack实现前序遍历2
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
	
	//层序遍历或者广度优先遍历
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
		
		System.out.println("树的高度: "+tree.getTreeHeight()+", 总节点数:"+tree.getNodeNum());
		System.out.println(tree.rootNode.left.getValue());
		
		//前、中、后序遍历也是深度优先遍历
		System.out.println("后序遍历结果: ");
		tree.postOrder(tree.rootNode);
		System.out.println();
		
		System.out.println("中序遍历结果: ");
		tree.inOrder(tree.rootNode);
		System.out.println();
		
		System.out.println("层序遍历结果: ");
		tree.levelIterator(tree.rootNode);
		
		System.out.println();
		System.out.println("前序遍历结果: ");
		tree.preOrder(tree.rootNode);

		System.out.println();
		System.out.println("stack前序遍历结果: ");
		tree.preTraversal(tree.rootNode);
		
		System.out.println();
		System.out.println("stack2前序遍历结果: ");
		tree.preTraversal2(tree.rootNode);
		
		System.out.println();
		System.out.println("depthOrderTraversal遍历结果: ");
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
		T value; //值
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

package editortrees;

import java.util.ArrayList;
import java.util.Stack;

// A node in a height-balanced binary tree with rank. [Milestone 3 version]
// Except for the NULL_sNODE (if you choose to use one), one node cannot
// belong to two different trees.

public class Node {
	
	enum Code {SAME, LEFT, RIGHT};
	
	// The fields would normally be private, but for the purposes of this class, 
	// we want to be able to test the results of the algorithms in addition to the
	// "publicly visible" effects
	
	char element;            
	Node left, right; // subtrees
	int rank;         // inorder position of this node within its own subtree.
	Code balance; 
	Node parent;  // You may want this field.
	// Feel free to add other fields that you find useful

	// You will probably want to add several other methods

	public Node() {
		this.element = ' ';
		this.left = null;
		this.right = null;
		this.rank = -1;
		this.balance = null;
		this.parent = EditTree.NULL_NODE;
	}
	
	public Node(char c, int rank) {
		this.element = c;
		this.left = EditTree.NULL_NODE;
		this.right = EditTree.NULL_NODE;
		this.rank = rank;
		this.balance = Code.SAME;
		this.parent = EditTree.NULL_NODE;
	}
	
	// For the following methods, you should fill in the details so that they work correctly
	public int height() {
		if(this == EditTree.NULL_NODE) {
			return -1;
		}
		if(this.balance == Code.LEFT) {
			return 1 + this.left.height();
		}
		else {
			return 1 + this.right.height();
		}
	}

	public int size() {
		if(this == EditTree.NULL_NODE) {
			return 0;
		}
		Node currentLeft = this;
		Node currentRight = this;
		int rightAdd = 0;
		while (currentLeft.left != EditTree.NULL_NODE) {
			currentLeft = currentLeft.left;
		}
		while (currentRight.right != EditTree.NULL_NODE) {
			rightAdd += currentRight.rank + 1;
			currentRight = currentRight.right;
		}
		return 1 + (currentRight.rank + rightAdd) - (currentLeft.rank);
	}

	public char get(int pos, int addRank) {
		if(this == EditTree.NULL_NODE) {
			throw new IndexOutOfBoundsException();
		}
		if(pos < 0) {
			throw new IndexOutOfBoundsException();
		}
		if(pos > this.rank + addRank) {
			return this.right.get(pos, addRank + this.rank + 1);
		}
		if(pos < this.rank + addRank) {
			return this.left.get(pos, addRank);
		}
		else {
			return this.element;
		}
	}

	public void toAL(ArrayList<Character> list) {
		if (this.left != EditTree.NULL_NODE) {
			this.left.toAL(list);
		}
		list.add(this.element);
		if (this.right != EditTree.NULL_NODE) {
			this.right.toAL(list);
		}
	}
	
	public int add(char c) {
		if (this.right != EditTree.NULL_NODE) {
			return this.right.add(c);
		}
		else {
			this.right = new Node(c, 0);
			this.right.parent = this;
			Node currentNode = this;
			while(currentNode != EditTree.NULL_NODE) {
				if(currentNode.balance == Code.SAME) {
					currentNode.balance = Code.RIGHT;
					currentNode = currentNode.parent;
				}
				else if(currentNode.balance == Code.LEFT) {
					currentNode.balance = Code.SAME;
					break;
				}
				else {
					EditTree.singleLeftRotation(currentNode, currentNode.right);
					return 1;
				}
			}
		}
		return 0;
	}

	public int add(char c, int pos, int addRank) {
		int rotations = 0;
		if(pos <= this.rank + addRank && this.left != EditTree.NULL_NODE) {
			this.rank++;
			return this.left.add(c, pos, addRank);
		}
		else if (pos <= this.rank + addRank && this.left == EditTree.NULL_NODE){
			this.rank++;
			this.left = new Node(c, 0);
			this.left.parent = this;
		}
		else if(pos == 1 + this.rank + addRank && this.right == EditTree.NULL_NODE) {
			this.right = new Node(c, 0);
			this.right.parent = this;
		}
		else if(pos > this.rank + addRank && this.right != EditTree.NULL_NODE) {
			return this.right.add(c, pos, addRank + this.rank + 1);
		}
		else {
			throw new IndexOutOfBoundsException();
		}
		Node currentNode = this;
		while(currentNode != EditTree.NULL_NODE) {
			if(currentNode.balance == Code.SAME) {
				if(currentNode.rank + addRank >= pos) {
					currentNode.balance = Code.LEFT;
				}
				else {
					currentNode.balance = Code.RIGHT;
				}
			}
			else if(currentNode.balance == Code.RIGHT && currentNode.rank + addRank >= pos) {
				currentNode.balance = Code.SAME;
				break;
			}
			else if(currentNode.balance == Code.LEFT && currentNode.rank + addRank < pos) {
				currentNode.balance = Code.SAME;
				break;
			}
			else {
				if(currentNode.rank + addRank >= pos && currentNode.left.rank + addRank >= pos) {
					EditTree.singleRightRotation(currentNode, currentNode.left);
					rotations++;
					break;
				}
				else if(currentNode.rank + addRank < pos && currentNode.right != EditTree.NULL_NODE && currentNode.right.rank + addRank + currentNode.rank + 1 < pos) {
					EditTree.singleLeftRotation(currentNode, currentNode.right);
					rotations++;
					break;
				}
				else if(currentNode.rank + addRank >= pos && currentNode.left != EditTree.NULL_NODE && currentNode.left.rank + addRank < pos) {
					EditTree.doubleRightRotation(currentNode.left, currentNode.left.right);
					rotations += 2;
					break;
				}
				else if(currentNode.rank + addRank < pos && currentNode.right.rank + currentNode.right.rank + addRank + currentNode.rank + 1 >= pos) {
					EditTree.doubleLeftRotation(currentNode.right, currentNode.right.left);
					rotations += 2;
					break;
				}
			}
			if (currentNode.parent.right == currentNode) {
				addRank -= currentNode.parent.rank + 1;
			}
			currentNode = currentNode.parent;
		}
		return rotations;
	}
	
	@Override
	public String toString() {
		return Character.toString(this.element);
	}
	
	public void inOrder(int addRank) {
		if (this == EditTree.NULL_NODE) {
			return;
		}
		if (this.left != EditTree.NULL_NODE) {
			this.left.inOrder(addRank);
		}
		System.out.print(this.rank + addRank + "'" + this + "' ");
		if (this.right != EditTree.NULL_NODE) {
			this.right.inOrder(addRank + this.rank + 1);
		}
	}
	
	public String inOrder(int addRank, String result) {
		if (this == EditTree.NULL_NODE) {
			return "";
		}
		if (this.left != EditTree.NULL_NODE) {
			result += this.left.inOrder(addRank, result);
		}
		result += this.element;
		if (this.right != EditTree.NULL_NODE) {
			result += this.right.inOrder(addRank + this.rank + 1, result);
		}
		return result;
	}
	
	public char delete(int pos, int[] numOfRotations, int addRank)
	{
		char charDeleted = '%';
		
		//Find and delete the node
		if(this.rank + addRank > pos) {
			this.rank--;
			if(this.left.rank + addRank == pos) {
				if(this.left.left == EditTree.NULL_NODE && this.left.right == EditTree.NULL_NODE) {
					charDeleted = this.left.element;
					this.left = EditTree.NULL_NODE;
				}
				else if(this.left.left != EditTree.NULL_NODE && this.left.right == EditTree.NULL_NODE) {
					charDeleted = this.left.element;
					this.left = this.left.left;
					this.left.parent = this;
				}
				else if(this.left.left == EditTree.NULL_NODE && this.left.right != EditTree.NULL_NODE) {
					charDeleted = this.left.element;
					this.left = this.left.right;
					this.left.parent = this;
				}
				else {
					charDeleted = this.left.element;
					Node newRoot = this.left.right;
					while (newRoot.left != EditTree.NULL_NODE) {
						newRoot = newRoot.left;
					}
					Node oldRoot = this.left;
					this.left = newRoot;
					if(newRoot.parent.left == newRoot) {
						newRoot.parent.left = newRoot.right;
					}
					else {
						newRoot.parent.right = newRoot.right;
					}
					if (newRoot.right != EditTree.NULL_NODE) {
						newRoot.right.parent = newRoot.parent;
					}
					newRoot.right = oldRoot.right;
					newRoot.right.parent = newRoot;
					newRoot.left = oldRoot.left;
					newRoot.left.parent = newRoot;
					newRoot.rank = newRoot.left.size();
				}
			}
			else {
				return this.left.delete(pos, numOfRotations, addRank);
			}
		}
		else if(this.rank + addRank < pos) {
			addRank += this.rank + 1;
			if(this.right.rank + addRank == pos) {
				if(this.right.left == EditTree.NULL_NODE && this.right.right == EditTree.NULL_NODE) {
					charDeleted = this.right.element;
					this.right = EditTree.NULL_NODE;
				}
				else if(this.right.left != EditTree.NULL_NODE && this.right.right == EditTree.NULL_NODE) {
					charDeleted = this.right.element;
					this.right = this.right.left;
					this.right.parent = this;
				}
				else if(this.right.left == EditTree.NULL_NODE && this.right.right != EditTree.NULL_NODE) {
					charDeleted = this.right.element;
					this.right = this.right.right;
					this.right.parent = this;
				}
				else {
					charDeleted = this.right.element;
					Node newRoot = this.right.right;
					while (newRoot.left != EditTree.NULL_NODE) {
						newRoot = newRoot.left;
					}
					Node oldRoot = this.right;
					this.right = newRoot;
					if(newRoot.parent.left == newRoot) {
						newRoot.parent.left = newRoot.right;
					}
					else {
						newRoot.parent.right = newRoot.right;
					}
					if (newRoot.right != EditTree.NULL_NODE) {
						newRoot.right.parent = newRoot.parent;
					}
					newRoot.right = oldRoot.right;
					newRoot.right.parent = newRoot;
					newRoot.left = oldRoot.left;
					newRoot.left.parent = newRoot;
					newRoot.rank = newRoot.left.size();
				}
				addRank -= this.rank + 1;
			}
			else {
				return this.right.delete(pos, numOfRotations, addRank);
			}
		}
		else {
			throw new IndexOutOfBoundsException();
		}
		
		Node currentNode = this;
		while(currentNode != EditTree.NULL_NODE) {
			if((currentNode.balance == Code.LEFT && currentNode.rank + addRank < pos) || (currentNode.balance == Code.RIGHT && currentNode.rank + addRank >= pos)) {
				Node lowestNode = currentNode;
				int localAddRank = addRank;
				while(lowestNode.left != EditTree.NULL_NODE || lowestNode.right != EditTree.NULL_NODE) {
					if(lowestNode.balance == Code.RIGHT) {
						localAddRank += lowestNode.rank + 1;
						lowestNode = lowestNode.right;
					}
					else {
						lowestNode = lowestNode.left;
					}
				}
				int lowestRank = lowestNode.rank + localAddRank;
				if(currentNode.left.height() > currentNode.right.height() + 1 || currentNode.left.height() + 1 < currentNode.right.height()) {
					if(currentNode.rank + addRank > lowestRank && currentNode.left.rank + addRank > lowestRank) {
						EditTree.singleRightRotation(currentNode, currentNode.left);
						currentNode = currentNode.parent;
						numOfRotations[0]++;
					}
					else if(currentNode.rank + addRank < lowestRank && currentNode.right != EditTree.NULL_NODE && currentNode.right.rank + currentNode.rank + 1 + addRank < lowestRank) {
						EditTree.singleLeftRotation(currentNode, currentNode.right);
						currentNode = currentNode.parent;
						numOfRotations[0]++;
					}
					else if(currentNode.rank + addRank > lowestRank && currentNode.left != EditTree.NULL_NODE && currentNode.left.rank + addRank < lowestRank) {
						EditTree.doubleRightRotation(currentNode.left, currentNode.left.right);
						currentNode = currentNode.parent;
						numOfRotations[0] += 2;
					}
					else if(currentNode.rank + addRank < lowestRank && currentNode.right.rank + currentNode.rank + 1 + addRank > lowestRank) {
						EditTree.doubleLeftRotation(currentNode.right, currentNode.right.left);
						currentNode = currentNode.parent;
						numOfRotations[0] += 2;
					}
				}
			}
			else {
				if(currentNode.left.height() > currentNode.right.height()) {
					currentNode.balance = Code.LEFT;
				}
				else if(currentNode.right.height() > currentNode.left.height()) {
					currentNode.balance = Code.RIGHT;
				}
				else {
					currentNode.balance = Code.SAME;
				}
			}
			if (currentNode.parent.right == currentNode) {
				addRank -= currentNode.parent.rank + 1;
			}
			currentNode = currentNode.parent;
		}
		
		return charDeleted;
	}
	
	public void copyTree(Node newRoot)
	{
		if(this==EditTree.NULL_NODE)
			return;
		Node copy=new Node(this.element, this.rank);
		copy.balance=this.balance;
		copy.parent=newRoot;
		if(this.parent.left==this)
			newRoot.left=copy;
		else
			newRoot.right=copy;
		this.left.copyTree(copy);
		this.right.copyTree(copy);
	}

}
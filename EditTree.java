package editortrees;

import java.util.ArrayList;
import java.util.Stack;

import editortrees.Node.Code;

// A height-balanced binary tree with rank that could be the basis for a text editor. [Milestone 3 version]

public class EditTree {

	private Node root;
	public final static Node NULL_NODE = new Node();
	private int numOfRotations;

	/**
	 * Construct an empty tree
	 */
	public EditTree() {
		this.root = NULL_NODE;
		this.numOfRotations = 0;
	}

	/**
	 * Construct a single-node tree whose element is c
	 * 
	 * @param c
	 */
	public EditTree(char c) {
		this.root = new Node(c, 0);
		this.numOfRotations = 0;
	}

	/**
	 * Create an EditTree whose toString is s. This can be done in O(N) time,
	 * where N is the length of the tree (repeatedly calling insert() would be 
	 * O(N log N), so you need to find a more efficient way to do this.
	 * 
	 * @param s
	 */
	public EditTree(String s) 
	{
		this();
		this.root=buildTree(s, this.root);
		this.numOfRotations = 0;
	}
	
	/**
	 * Helper function for EditTree(String). 
	 * Recursively creates a balanced tree containing the contents of s
	 * in in-order traversal, then returns the root node.
	 * 
	 * @param s
	 * @return the root of the new tree
	 */
	private static Node buildTree(String s, Node parent)
	{
		char midChar=s.charAt(s.length()/2);
		Node newNode=new Node(midChar, 0);
		newNode.parent=parent;
		
		String leftStr=s.substring(0,s.length()/2);
		newNode.rank = leftStr.length();
		if(leftStr.length()>0)
			newNode.left=buildTree(leftStr,newNode);
		String rightStr=s.substring(s.length()/2+1);
		if(rightStr.length()>0)
		{
			newNode.right=buildTree(rightStr,newNode);
		}
		
		//assign balance codes
		if(newNode.left==NULL_NODE && newNode.right!=NULL_NODE)
			newNode.balance=Code.RIGHT;
		else if(newNode.left!=NULL_NODE && newNode.right==NULL_NODE)
			newNode.balance=Code.LEFT;
		else if(newNode.left!=NULL_NODE && newNode.right!=NULL_NODE)
		{
			if(newNode.left.height()>newNode.right.height())
				newNode.balance=Code.LEFT;
			else if(newNode.left.height()<newNode.right.height())
				newNode.balance=Code.RIGHT;
		}
		return newNode;
	}

	/**
	 * Make this tree be a copy of e, with all new nodes, but the same shape and
	 * contents.
	 * 
	 * @param e
	 */
	public EditTree(EditTree e) 
	{
		if(e.root==NULL_NODE)
		{
			this.root=NULL_NODE;
		}
		else
		{
			this.root=new Node(e.root.element,e.root.rank);
			this.root.balance=e.root.balance;
			e.root.left.copyTree(this.root);
			e.root.right.copyTree(this.root);
		}
		this.numOfRotations=0;
	}

	/**
	 * 
	 * @return the height of this tree
	 */
	public int height() {
		return this.root.height();
	}

	/**
	 * 
	 * returns the total number of rotations done in this tree
	 * since it was created.  A double rotation counts as two.
	 *
	 * @return number of rotations since tree was created.
	 */
	public int totalRotationCount() {
		return this.numOfRotations;
	}

	/**
	 * return the string produced by an inorder traversal of this tree
	 */
	@Override
	public String toString() {
		if(this.root == EditTree.NULL_NODE) {
			return "";
		}
		String toReturn = "";
		ArrayList<Character> list = new ArrayList<Character>();
		this.root.toAL(list);
		for (Character i : list) {
			toReturn = toReturn + (Character) i;
		}
		return toReturn;
	}

	/**
	 * 
	 * @param pos
	 *            position in the tree
	 * @return the character at that position
	 * @throws IndexOutOfBoundsException
	 */
	public char get(int pos) throws IndexOutOfBoundsException {
		return this.root.get(pos, 0);
	}

	/**
	 * 
	 * @param c
	 *            character to add to the end of this tree.
	 */
	public void add(char c) {
		if (this.root == NULL_NODE) {
			this.root = new Node(c, 0);
		}
		else {
			this.numOfRotations+= this.root.add(c);
		}
		if (this.root.parent != EditTree.NULL_NODE) {
			this.root = this.root.parent;
		}
//		this.root.inOrder(0);
//		System.out.println();
	}

	/**
	 * 
	 * @param c
	 *            character to add
	 * @param pos
	 *            character added in this inorder position
	 * @throws IndexOutOfBoundsException
	 *             id pos is negative or too large for this tree
	 */
	public void add(char c, int pos) throws IndexOutOfBoundsException {
//		System.out.print(pos + " : ");
//		this.root.inOrder(0);
//		System.out.println();
		if (pos < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (this.root == NULL_NODE && pos == 0) {
			this.root = new Node(c, 0);
		}
		else if(this.root == NULL_NODE && pos != 0) {
			throw new IndexOutOfBoundsException();
		}
		else {
			this.numOfRotations += this.root.add(c, pos, 0);
		}
		while (this.root.parent != EditTree.NULL_NODE) {
			this.root = this.root.parent;
		}
//		System.out.print(pos + " = ");
//		this.root.inOrder(0);
//		System.out.println();
	}

	/**
	 * 
	 * @return the number of nodes in this tree
	 */
	public int size() {
		return this.root.size();
	}
	
	/**
	 * 
	 * @param pos
	 *            position of character to delete from this tree
	 * @return the character that is deleted
	 * @throws IndexOutOfBoundsException
	 */
	public char delete(int pos) throws IndexOutOfBoundsException {
		if(this.root == EditTree.NULL_NODE) {
			throw new IndexOutOfBoundsException();
		}
		if (pos < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (pos >= this.size()) {
			throw new IndexOutOfBoundsException();
		}
		char charDeleted = '%';
		if (this.size() == 1) {
			charDeleted = this.root.element;
			this.root = EditTree.NULL_NODE;
			return charDeleted;
		}
		if (pos == this.root.rank) {
			if(this.root.left == EditTree.NULL_NODE && this.root.right == EditTree.NULL_NODE) {
				charDeleted = this.root.element;
				this.root = EditTree.NULL_NODE;
			}
			else if(this.root.left != EditTree.NULL_NODE && this.root.right == EditTree.NULL_NODE) {
				charDeleted = this.root.element;
				this.root = this.root.left;
				this.root.parent = EditTree.NULL_NODE;
			}
			else if(this.root.left == EditTree.NULL_NODE && this.root.right != EditTree.NULL_NODE) {
				charDeleted = this.root.element;
				this.root = this.root.right;
				this.root.parent = EditTree.NULL_NODE;
			}
			else {
				charDeleted = this.root.element;
				Node newRoot = this.root.right;
				while (newRoot.left != NULL_NODE) {
					newRoot = newRoot.left;
				}
				Node oldRoot = this.root;
				this.root = newRoot;
				if(newRoot.parent.left == newRoot) {
					newRoot.parent.left = newRoot.right;
				}
				else {
					newRoot.parent.right = newRoot.right;
				}
				if (newRoot.right != NULL_NODE) {
					newRoot.right.parent = newRoot.parent;
				}
				newRoot.right = oldRoot.right;
				newRoot.right.parent = newRoot;
				newRoot.left = oldRoot.left;
				newRoot.left.parent = newRoot;
				this.root.parent = EditTree.NULL_NODE;
				if(this.root.balance == Code.SAME) {
					this.root.balance = Code.LEFT;
				}
				else if(this.root.balance == Code.RIGHT) {
					this.root.balance = Code.SAME;
				}
			}
			this.root.rank = this.root.left.size();
			Node lowestNode = this.root;
			int localAddRank = 0;
			while(lowestNode.left != EditTree.NULL_NODE || lowestNode.right != EditTree.NULL_NODE) {
				if(lowestNode.balance == Code.LEFT) {
					lowestNode = lowestNode.left;
				}
				else {
					localAddRank += lowestNode.rank + 1;
					lowestNode = lowestNode.right;
				}
			}
			int lowestRank = lowestNode.rank + localAddRank;
			if(this.root.left.height() > this.root.right.height() + 1 || this.root.right.height() > this.root.left.height() + 1) {
				if(this.root.rank >= lowestRank && this.root.left.rank >= lowestRank) {
					EditTree.singleRightRotation(this.root, this.root.left);
					this.numOfRotations++;
				}
				else if(this.root.rank < lowestRank && this.root.right != EditTree.NULL_NODE && this.root.right.rank + this.root.rank + 1 < lowestRank) {
					EditTree.singleLeftRotation(this.root, this.root.right);
					this.numOfRotations++;
				}
				else if(this.root.rank >= lowestRank && this.root.left != EditTree.NULL_NODE && this.root.left.rank < lowestRank) {
					EditTree.doubleRightRotation(this.root.left, this.root.left.right);
					this.numOfRotations += 2;
				}
				else if(this.root.rank < lowestRank && this.root.right.rank + this.root.rank + 1 >= lowestRank) {
					EditTree.doubleLeftRotation(this.root.right, this.root.right.left);
					this.numOfRotations += 2;
				}
			}
		}
		else {
			int[] numOfRotations = {0};
			charDeleted = this.root.delete(pos, numOfRotations, 0);
			this.numOfRotations += numOfRotations[0];
		}
		while(this.root != EditTree.NULL_NODE && this.root.parent != EditTree.NULL_NODE) {
			this.root = this.root.parent;
		}
		return charDeleted;
	}

	/**
	 * This method operates in O(length*log N), where N is the size of this
	 * tree.
	 * 
	 * @param pos
	 *            location of the beginning of the string to retrieve
	 * @param length
	 *            length of the string to retrieve
	 * @return string of length that starts in position pos
	 * @throws IndexOutOfBoundsException
	 *             unless both pos and pos+length-1 are legitimate indexes
	 *             within this tree.
	 */
	public String get(int pos, int length) throws IndexOutOfBoundsException {
		if(pos < 0 || length < 0 || pos + length > this.size()) {
			throw new IndexOutOfBoundsException();
		}
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < length; i++) {
			builder.append(this.get(i + pos));
		}
		return builder.toString();
	}

	/**
	 * This method is provided for you, and should not need to be changed. If
	 * split() and concatenate() are O(log N) operations as required, delete
	 * should also be O(log N)
	 * 
	 * @param start
	 *            position of beginning of string to delete
	 * 
	 * @param length
	 *            length of string to delete
	 * @return an EditTree containing the deleted string
	 * @throws IndexOutOfBoundsException
	 *             unless both start and start+length-1 are in range for this
	 *             tree.
	 */
	public EditTree delete(int start, int length)
			throws IndexOutOfBoundsException {
		if (start < 0 || start + length >= this.size())
			throw new IndexOutOfBoundsException(
					(start < 0) ? "negative first argument to delete"
							: "delete range extends past end of string");
		EditTree t2 = this.split(start);
		EditTree t3 = t2.split(length);
		this.concatenate(t3);
		return t2;
	}

	/**
	 * Append (in time proportional to the log of the size of the larger tree)
	 * the contents of the other tree to this one. Other should be made empty
	 * after this operation.
	 * 
	 * @param other
	 * @throws IllegalArgumentException
	 *             if this == other
	 */
	public void concatenate(EditTree other) throws IllegalArgumentException {
		if(this.equals(other)) {
			throw new IllegalArgumentException();
		}
		if(this.root == EditTree.NULL_NODE) {
			this.root = other.root;
			other.root = EditTree.NULL_NODE;
			return;
		}
		if(other.root == EditTree.NULL_NODE) {
			return;
		}
		EditTree t, u;
		if(this.height() >= other.height()) {
			t = this;
			u = other;
			Node q = u.root;
			while(q.left != EditTree.NULL_NODE) {
				q = q.left;
			}
			u.delete(0);
			q.parent = EditTree.NULL_NODE;
			EditTree v = u;
			this.paste(t, q, v);
			other.root = EditTree.NULL_NODE;
			if(this.root.parent != EditTree.NULL_NODE) {
				this.root = this.root.parent;
			}
		}
		else {
			t = this;
			u = other;
			Node q = t.root;
			while(q.right != EditTree.NULL_NODE) {
				q = q.right;
			}
			t.delete(t.size() - 1);
			q.parent = EditTree.NULL_NODE;
			EditTree v = t;
			this.paste(v, q, u);
			this.root = other.root;
			if(this.root.parent != EditTree.NULL_NODE) {
				this.root = this.root.parent;
			}
			other.root = EditTree.NULL_NODE;
		}
	}

	private void paste(EditTree t, Node q, EditTree v) {
		int originalNumOfRotations = this.numOfRotations;
		int addRank = 0;
		if(t.height() >= v.height()) {
			Node p = t.root;
			int hp = p.height();
			Node parent = p.parent;
			while(hp - v.height() > 1) {
				if(p.balance == Code.LEFT) {
					hp -= 2;
				}
				else {
					hp -= 1;
				}
				addRank += p.rank + 1;
				parent = p;
				p = p.right;
			}
			q.left = p;
			p.parent = q;
			q.right = v.root;
			if(v.root != EditTree.NULL_NODE) {
				v.root.parent = q;
			}
			if(t.root.parent != EditTree.NULL_NODE) {
				t.root = t.root.parent;
			}
			if(hp == v.height()) {
				q.balance = Code.SAME;
			}
			else {
				q.balance = Code.LEFT;
			}
			if(parent != EditTree.NULL_NODE) {
				parent.right = q;
			}
			q.rank = q.left.size();
			int pos = p.size() + addRank;
			q.parent = parent;
			Node currentNode = q;
			while(currentNode != EditTree.NULL_NODE) {
				if((currentNode.balance == Code.LEFT && currentNode.rank + addRank >= pos) || (currentNode.balance == Code.RIGHT && currentNode.rank + addRank < pos)) {
					if(currentNode.left.height() > currentNode.right.height() + 1 || currentNode.right.height() > currentNode.left.height() + 1) {
						if(currentNode.rank + addRank >= pos && currentNode.left.rank + addRank >= pos) {
							EditTree.singleRightRotation(currentNode, currentNode.left);
							this.numOfRotations++;
							break;
						}
						else if(currentNode.rank + addRank < pos && currentNode.right != EditTree.NULL_NODE && currentNode.right.rank + addRank + currentNode.rank + 1 < pos) {
							EditTree.singleLeftRotation(currentNode, currentNode.right);
							this.numOfRotations++;
							break;
						}
						else if(currentNode.rank + addRank >= pos && currentNode.left != EditTree.NULL_NODE && currentNode.left.rank + addRank < pos) {
							EditTree.doubleRightRotation(currentNode.left, currentNode.left.right);
							this.numOfRotations += 2;
							break;
						}
						else if(currentNode.rank + addRank < pos && currentNode.right.rank + currentNode.right.rank + addRank + currentNode.rank + 1 >= pos) {
							EditTree.doubleLeftRotation(currentNode.right, currentNode.right.left);
							this.numOfRotations += 2;
							break;
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
				if(this.numOfRotations == originalNumOfRotations) {
					currentNode = currentNode.parent;
				}
				else {
					break;
				}
			}
		}
		else {
			Node p = v.root;
			int hp = p.height();
			Node parent = p.parent;
			while(hp - t.height() > 1) {
				if(p.balance == Code.RIGHT) {
					hp -= 2;
				}
				else {
					hp -= 1;
				}
				parent = p;
				p = p.left;
			}
			q.right = p;
			p.parent = q;
			q.left = t.root;
			if(t.root != EditTree.NULL_NODE) {
				t.root.parent = q;
			}
			if(v.root.parent != EditTree.NULL_NODE) {
				v.root = v.root.parent;
			}
			if(q.left.height() > q.right.height()) {
				q.balance = Code.LEFT;
			}
			else if(q.right.height() > q.left.height()) {
				q.balance = Code.RIGHT;
			}
			else {
				q.balance = Code.SAME;
			}
			if(parent != EditTree.NULL_NODE) {
				parent.left = q;
			}
			q.rank = 0;
			int pos = p.size() + addRank;
			q.parent = parent;
			Node currentNode = q;
			while(currentNode != EditTree.NULL_NODE) {
				if((currentNode.balance == Code.LEFT && currentNode.rank + addRank >= pos) || (currentNode.balance == Code.RIGHT && currentNode.rank + addRank < pos)) {
					if(currentNode.left.height() > currentNode.right.height() + 1 || currentNode.right.height() > currentNode.left.height() + 1) {
						if(currentNode.rank + addRank >= pos && currentNode.left.rank + addRank >= pos) {
							EditTree.singleRightRotation(currentNode, currentNode.left);
							this.numOfRotations++;
							break;
						}
						else if(currentNode.rank + addRank < pos && currentNode.right != EditTree.NULL_NODE && currentNode.right.rank + addRank + currentNode.rank + 1 < pos) {
							EditTree.singleLeftRotation(currentNode, currentNode.right);
							this.numOfRotations++;
							break;
						}
						else if(currentNode.rank + addRank >= pos && currentNode.left != EditTree.NULL_NODE && currentNode.left.rank + addRank < pos) {
							EditTree.doubleRightRotation(currentNode.left, currentNode.left.right);
							this.numOfRotations += 2;
							break;
						}
						else if(currentNode.rank + addRank < pos && currentNode.right.rank + currentNode.right.rank + addRank + currentNode.rank + 1 >= pos) {
							EditTree.doubleLeftRotation(currentNode.right, currentNode.right.left);
							this.numOfRotations += 2;
							break;
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
				if(this.numOfRotations == originalNumOfRotations) {
					currentNode = currentNode.parent;
				}
				else {
					break;
				}
			}
		}
		
	}

	/**
	 * This operation must be done in time proportional to the height of this
	 * tree.
	 * 
	 * @param pos
	 *            where to split this tree
	 * @return a new tree containing all of the elements of this tree whose
	 *         positions are >= position. Their nodes are removed from this
	 *         tree.
	 * @throws IndexOutOfBoundsException
	 */
	public EditTree split(int pos) throws IndexOutOfBoundsException {
		Node currentNode = this.root;
		EditTree newTree = new EditTree();
		Node newRightTreeRoot = null;
		Node newLeftTreeRoot = null;
		int addRank = 0;
		Node currentRightNode = null;
		Node currentLeftNode = null;
		while (true) {
			if (currentNode.rank + addRank > pos) {
				if (newRightTreeRoot == null) {
					newRightTreeRoot = currentNode;
					if (currentNode.parent != NULL_NODE) {
						currentNode.parent.right = NULL_NODE;
					}
					currentNode.parent = NULL_NODE;
				}
				else {
					if (currentNode.parent.left != currentNode) {
						currentRightNode.left = currentNode;
						currentNode.parent = currentRightNode;
					}
				}
				currentRightNode = currentNode;
				currentNode = currentNode.left;
			}
			else if (currentNode.rank + addRank < pos) {
				if (newLeftTreeRoot == null) {
					newLeftTreeRoot = currentNode;
					if (currentNode.parent != NULL_NODE) {
						currentNode.parent.left = NULL_NODE;
					}
					currentNode.parent = NULL_NODE;
				}
				else {
					if (currentNode.parent.right != currentNode) {
						currentLeftNode.right = currentNode;
						currentNode.parent = currentLeftNode;
					}
				}
				currentLeftNode = currentNode;
				addRank += currentNode.rank + 1;
				currentNode = currentNode.right;
			}
			else {
				if (currentLeftNode == null && currentRightNode == null) {
					newRightTreeRoot = currentNode;
					currentRightNode = currentNode;
					newLeftTreeRoot = currentNode.left;
					currentLeftNode = currentNode.left;
					currentNode.left.parent = NULL_NODE;
					currentNode.left = NULL_NODE;
				}
				else if (currentLeftNode == null && currentRightNode != null) {
					newLeftTreeRoot = currentNode.left;
					currentLeftNode = currentNode.left;
					currentNode.left.parent = NULL_NODE;
					currentNode.left = NULL_NODE;
					currentRightNode.left = currentNode;
					currentNode.parent = currentRightNode;
					currentRightNode = currentNode;
				}
				else if (currentLeftNode != null && currentRightNode == null) {
					newRightTreeRoot = currentNode;
					currentNode.parent.right = NULL_NODE;
					currentNode.parent = NULL_NODE;
					currentLeftNode.right = currentNode.left;
					currentNode.left.parent = currentLeftNode;
					currentLeftNode = currentNode.left;
					currentNode.left = NULL_NODE;
				}
				else {
					currentLeftNode.right = currentNode.left;
					currentNode.left.parent = currentLeftNode;
					currentLeftNode = currentNode.left;
					currentNode.left = NULL_NODE;
					currentRightNode.left = currentNode;
					currentNode.parent = currentRightNode;
					currentRightNode = currentNode;
				}
				break;
			}
		}
		this.root = newLeftTreeRoot;
		newTree.root = newRightTreeRoot;
		currentNode = newTree.root;
		while (currentNode != NULL_NODE) {
			currentNode.rank = currentNode.left.size();
			currentNode = currentNode.left;
		}
		return newTree;
	}

	/**
	 * Don't worry if you can't do this one efficiently.
	 * 
	 * @param s
	 *            the string to look for
	 * @return the position in this tree of the first occurrence of s; -1 if s
	 *         does not occur
	 */
	public int find(String s) 
	{
		if(this.size() == 0 && s == "") {
			return 0;
		}
		String treeString=this.toString();
		for(int i=0;i<treeString.length()-s.length()+1;i++)
		{
			if(treeString.substring(i,i+s.length()).equals(s))
				return i;
		}
		return -1;
	}

	/**
	 * 
	 * @param s
	 *            the string to search for
	 * @param pos
	 *            the position in the tree to begin the search
	 * @return the position in this tree of the first occurrence of s that does
	 *         not occur before position pos; -1 if s does not occur
	 */
	public int find(String s, int pos) {
		if(this.size() == 0 && s == "") {
			return 0;
		}
		String treeString=this.toString().substring(pos, this.toString().length());
		for(int i=0;i<treeString.length()-s.length()+1;i++)
		{
			if(treeString.substring(i,i+s.length()).equals(s))
				return i+pos;
		}
		return -1;
	}

	/**
	 * @return The root of this tree.
	 */
	public Node getRoot() {
		return this.root;
	}

	public static void singleLeftRotation(Node parent, Node child) {
		Node oldChildLeft = null;
		Node oldParentParent = null;
		if(child.left != EditTree.NULL_NODE) {
			oldChildLeft = child.left;
		}
		if(parent.parent != EditTree.NULL_NODE) {
			oldParentParent = parent.parent;
		}
		child.left = parent;
		parent.parent = child;
		if(oldChildLeft != null) {
			parent.right = oldChildLeft;
			oldChildLeft.parent = parent;
		}
		else {
			parent.right = EditTree.NULL_NODE;
		}
		if(oldParentParent != null) {
			if(oldParentParent.right == parent) {
				oldParentParent.right = child;
				child.parent = oldParentParent;
			}
			else if(oldParentParent.left == parent) {
				oldParentParent.left = child;
				child.parent = oldParentParent;
			}
		}
		else {
			child.parent = EditTree.NULL_NODE;
		}
		//Update child balance code
		if(child.left.height() > child.right.height()) {
			child.balance = Code.LEFT;
		}
		else if(child.left.height() < child.right.height()) {
			child.balance = Code.RIGHT;
		}
		else {
			child.balance = Code.SAME;
		}
		
		//Update parent balance code
		if(parent.left.height() > parent.right.height()) {
			parent.balance = Code.LEFT;
		}
		else if(parent.left.height() < parent.right.height()) {
			parent.balance = Code.RIGHT;
		}
		else {
			parent.balance = Code.SAME;
		}
		child.rank = child.left.size();
		parent.rank = parent.left.size();
	}

	public static void singleRightRotation(Node parent, Node child) {
		Node oldChildRight = null;
		Node oldParentParent = null;
		if(child.right != EditTree.NULL_NODE) {
			oldChildRight = child.right;
		}
		if(parent.parent != EditTree.NULL_NODE) {
			oldParentParent = parent.parent;
		}
		child.right = parent;
		parent.parent = child;
		if(oldChildRight != null) {
			parent.left = oldChildRight;
			oldChildRight.parent = parent;
		}
		else {
			parent.left = EditTree.NULL_NODE;
		}
		if(oldParentParent != null) {
			if(oldParentParent.right == parent) {
				oldParentParent.right = child;
				child.parent = oldParentParent;
			}
			else if(oldParentParent.left == parent) {
				oldParentParent.left = child;
				child.parent = oldParentParent;
			}
		}
		else {
			child.parent = EditTree.NULL_NODE;
		}
		//Update child balance code
		if(child.left.height() > child.right.height()) {
			child.balance = Code.LEFT;
		}
		else if(child.left.height() < child.right.height()) {
			child.balance = Code.RIGHT;
		}
		else {
			child.balance = Code.SAME;
		}
		
		//Update parent balance code
		if(parent.left.height() > parent.right.height()) {
			parent.balance = Code.LEFT;
		}
		else if(parent.left.height() < parent.right.height()) {
			parent.balance = Code.RIGHT;
		}
		else {
			parent.balance = Code.SAME;
		}
		parent.rank = parent.left.size();
		child.rank = child.left.size();
	}

	public static void doubleRightRotation(Node parent, Node child) {
		Node oldParentParent = parent.parent;
		Node oldChildLeft = null;
		if (child.left != NULL_NODE) {
			oldChildLeft = child.left;
		}
		child.left = parent;
		parent.parent = child;
		if (oldChildLeft != null) {
			parent.right = oldChildLeft;
			oldChildLeft.parent = parent;
		}
		else {
			parent.right = NULL_NODE;
		}
		child.parent = oldParentParent;
		EditTree.singleRightRotation(oldParentParent, child);
		
		//Update highest balance code
		if(oldParentParent.left.height() > oldParentParent.right.height()) {
			oldParentParent.balance = Code.LEFT;
		}
		else if(oldParentParent.left.height() < oldParentParent.right.height()) {
			oldParentParent.balance = Code.RIGHT;
		}
		else {
			oldParentParent.balance = Code.SAME;
		}
		
		//Update middle balance code
		if(child.left.height() > child.right.height()) {
			child.balance = Code.LEFT;
		}
		else if(child.left.height() < child.right.height()) {
			child.balance = Code.RIGHT;
		}
		else {
			child.balance = Code.SAME;
		}
		
		//Update lowest balance code
		if(parent.left.height() > parent.right.height()) {
			parent.balance = Code.LEFT;
		}
		else if(parent.left.height() < parent.right.height()) {
			parent.balance = Code.RIGHT;
		}
		else {
			parent.balance = Code.SAME;
		}
		child.rank = child.left.size();
		parent.rank = parent.left.size();
	}

	public static void doubleLeftRotation(Node parent, Node child) {
		Node oldParentParent = parent.parent;
		Node oldChildRight = null;
		if (child.right != NULL_NODE)
			oldChildRight = child.right;
		child.right = parent;
		parent.parent = child;
		if (oldChildRight != null) {
			parent.left = oldChildRight;
			oldChildRight.parent = parent;
		}
		else {
			parent.left = NULL_NODE;
		}
		child.parent = oldParentParent;
		oldParentParent.right = child;
		EditTree.singleLeftRotation(oldParentParent, child);
		
		//Update middle balance code
		if(child.left.height() > child.right.height()) {
			child.balance = Code.LEFT;
		}
		else if(child.left.height() < child.right.height()) {
			child.balance = Code.RIGHT;
		}
		else {
			child.balance = Code.SAME;
		}
		
		//Update lowest balance code
		if(parent.left.height() > parent.right.height()) {
			parent.balance = Code.LEFT;
		}
		else if(parent.left.height() < parent.right.height()) {
			parent.balance = Code.RIGHT;
		}
		else {
			parent.balance = Code.SAME;
		}
		child.rank = child.left.size();
		parent.rank = parent.left.size();
	}
	
	public String inOrder() {
		return this.root.inOrder(0, "");
	}
}

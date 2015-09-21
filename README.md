# editor-tree
EditTree class written in Java

This repo contains the code for an EditTree data structure. This data structure is represented as a height-balanced (e.g. h(left) is within one from h(right)) binary tree in which items are ranked and placed in the tree according to their rank. It could be useful as the basis for a text editor in which each character is a node in the tree, and the rank keeps the characters in order. The EditTree is implemented by using a class called Binary Node, which contains pointers to its children. Each node also contains a balance code to determine which subtree has a greater height (if either does). The tree handles inconsistencies in subtree height by performing rotations, keeping nodes in order and following the height-balanced requirement.

This project was completed in CSSE230 - Data Structures and Algorithm Analysis at Rose-Hulman Institute of Technology.
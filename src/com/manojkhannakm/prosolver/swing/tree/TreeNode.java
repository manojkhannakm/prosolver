package com.manojkhannakm.prosolver.swing.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;

/**
 * @author Manoj Khanna
 */

public class TreeNode extends DefaultMutableTreeNode {

    public TreeNode getChildWith(Object userObject) {
        Enumeration enumeration = children();
        while (enumeration.hasMoreElements()) {
            TreeNode treeNode = (TreeNode) enumeration.nextElement();
            if (userObject.equals(treeNode.getUserObject())) {
                return treeNode;
            }
        }

        return null;
    }

    public int getBranchCount() {
        Enumeration enumeration = children();
        int branchCount = 0;
        while (enumeration.hasMoreElements()) {
            if (!((TreeNode) enumeration.nextElement()).isLeaf()) {
                branchCount++;
            }
        }

        return branchCount;
    }

}

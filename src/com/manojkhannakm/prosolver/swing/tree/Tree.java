package com.manojkhannakm.prosolver.swing.tree;

import javax.swing.tree.TreePath;
import java.util.Enumeration;

/**
 * @author Manoj Khanna
 */

public class Tree extends com.intellij.ui.treeStructure.Tree {

    public Tree(TreeModel treeModel) {
        super(treeModel);
    }

    @Override
    public TreeModel getModel() {
        return (TreeModel) super.getModel();
    }

    public void expandNode(TreeNode treeNode) {
        if (treeNode.isLeaf()) {
            return;
        }

        expandPath(new TreePath(treeNode.getPath()));

        Enumeration enumeration = treeNode.children();
        while (enumeration.hasMoreElements()) {
            expandNode((TreeNode) enumeration.nextElement());
        }
    }

    public void expandTree() {
        expandNode((TreeNode) getModel().getRoot());
    }

    public void collapseNode(TreeNode treeNode) {
        if (treeNode.isLeaf()) {
            return;
        }

        Enumeration enumeration = treeNode.children();
        while (enumeration.hasMoreElements()) {
            collapseNode((TreeNode) enumeration.nextElement());
        }

        collapsePath(new TreePath(treeNode.getPath()));
    }

    public void collapseTree() {
        collapseNode((TreeNode) getModel().getRoot());
    }

}

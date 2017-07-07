package com.manojkhannakm.prosolver.swing.tree;

import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author Manoj Khanna
 */

public class TreeModel extends DefaultTreeModel {

    private TreeSorter treeSorter;
    private TreeFilter treeFilter;

    public TreeModel(TreeNode rootNode) {
        super(rootNode);
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (treeSorter == null && treeFilter == null) {
            return super.getChild(parent, index);
        }

        return sortNodes(filterNodes(getChildNodes((TreeNode) parent))).get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        if (treeSorter == null && treeFilter == null) {
            return super.getChildCount(parent);
        }

        return filterNodes(getChildNodes((TreeNode) parent)).size();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (treeSorter == null && treeFilter == null) {
            return super.getIndexOfChild(parent, child);
        }

        //noinspection SuspiciousMethodCalls
        return sortNodes(filterNodes(getChildNodes((TreeNode) parent))).indexOf(child);
    }

    private ArrayList<TreeNode> getChildNodes(TreeNode parentNode) {
        ArrayList<TreeNode> childNodeList = new ArrayList<>();
        Enumeration enumeration = parentNode.children();
        while (enumeration.hasMoreElements()) {
            childNodeList.add((TreeNode) enumeration.nextElement());
        }

        return childNodeList;
    }

    private ArrayList<TreeNode> sortNodes(ArrayList<TreeNode> childNodeList) {
        if (treeSorter == null) {
            return childNodeList;
        }

        childNodeList.sort((o1, o2) -> treeSorter.sort(o1, o2));

        return childNodeList;
    }

    private boolean filterParentNodes(TreeNode treeNode) {
        if (treeNode == null) {
            return false;
        }

        //noinspection SimplifiableIfStatement
        if (treeFilter.filter(treeNode)) {
            return true;
        }

        return filterParentNodes((TreeNode) treeNode.getParent());
    }

    private boolean filterChildNodes(TreeNode treeNode) {
        if (treeFilter.filter(treeNode)) {
            return true;
        }

        for (TreeNode childNode : getChildNodes(treeNode)) {
            if (filterChildNodes(childNode)) {
                return true;
            }
        }

        return false;
    }

    private ArrayList<TreeNode> filterNodes(ArrayList<TreeNode> childNodeList) {
        if (treeFilter == null) {
            return childNodeList;
        }

        if (!childNodeList.isEmpty()) {
            if (filterParentNodes((TreeNode) childNodeList.get(0).getParent())) {
                return childNodeList;
            }
        }

        Iterator<TreeNode> iterator = childNodeList.iterator();
        //noinspection Java8CollectionRemoveIf
        while (iterator.hasNext()) {
            if (!filterChildNodes(iterator.next())) {
                iterator.remove();
            }
        }

        return childNodeList;
    }

    public void setTreeSorter(TreeSorter treeSorter) {
        this.treeSorter = treeSorter;
    }

    public void setTreeFilter(TreeFilter treeFilter) {
        this.treeFilter = treeFilter;
    }

}

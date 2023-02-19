package com.xmg.tree;

import com.sun.istack.internal.Nullable;
import lombok.Getter;
import lombok.Setter;

public class RBTree<K, V> {

    //根节点
    private Node<K, V> root;

    /**
     * 左旋
     * @param node
     */
    private void leftRotate(Node<K, V> node) {
        if (node == null) {
            return;
        }
        final Node<K, V> r = node.getRightNode();
        final Node<K, V> pNode = node.getPNode();
        if (r == null) {
            return;
        }
        node.setRightNode(r.getLeftNode());
        r.setLeftNode(node);
        r.setPNode(pNode);
        if (pNode != null) {
            if (pNode.leftNode == node) {
                pNode.leftNode=r;
            }else {
                pNode.rightNode = r;
            }
        }else {
            root = r;
        }
    }


    /**
     * 右旋
     *
     * @param node
     */
    private void rightRotate(Node<K, V> node) {

    }

    @Getter
    @Setter
    static class Node<K, V> {
        //父节点
        private Node<K, V> pNode;
        //左子节点
        private Node<K, V> leftNode;
        //右子节点
        private Node<K, V> rightNode;

        private K k;
        private V v;

        private void setLeftNode(@Nullable Node<K, V> node) {
            this.leftNode = node;
            if (node != null) {
                node.pNode = this;
            }
            if (this.pNode == node) {
                this.pNode = null;
            }
        }

        private void setRightNode(@Nullable Node<K, V> node) {
            this.rightNode = node;
            if (node != null) {
                node.pNode = this;
            }
            if (this.pNode == node) {
                this.pNode = null;
            }
        }
    }
}

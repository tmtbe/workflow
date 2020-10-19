package com.thoughtworks.flow

class TreeNode(val parentTreeNode: TreeNode? = null) {
    var caseSelect: CaseSelect? = null
    var case: CASE? = null
    val children: HashMap<CASE, TreeNode> = HashMap()
    var nowChild: TreeNode? = null
    fun addChild(child: TreeNode) {
        if (!children.containsKey(child.case!!)) {
            nowChild = child
            children[child.case!!] = child
        }
    }
}
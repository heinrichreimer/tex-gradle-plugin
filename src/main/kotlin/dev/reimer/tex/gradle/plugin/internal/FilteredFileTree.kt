package dev.reimer.tex.gradle.plugin.internal

import org.gradle.api.file.FileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.file.FileVisitor
import org.gradle.api.internal.file.AbstractFileCollection
import org.gradle.api.internal.file.AbstractFileTree
import org.gradle.api.internal.file.FileCollectionStructureVisitor
import org.gradle.api.internal.tasks.TaskDependencyResolveContext

/**
 * See [AbstractFileTree.FilteredFileTreeImpl].
 */
class FilteredFileTree(
    private val fileTree: FileTree,
    private val spec: (FileTreeElement) -> Boolean
) : AbstractFileTree() {

    private val abstractFileTree = fileTree as? AbstractFileCollection

    override fun getDisplayName() = abstractFileTree?.displayName ?: ""

    override fun visitDependencies(context: TaskDependencyResolveContext) = context.add(fileTree)

    override fun visit(visitor: FileVisitor): FileTree {
        fileTree.visit(object : FileVisitor {
            override fun visitDir(dirDetails: FileVisitDetails) {
                if (spec(dirDetails)) visitor.visitDir(dirDetails)
            }

            override fun visitFile(fileDetails: FileVisitDetails) {
                if (spec(fileDetails)) visitor.visitFile(fileDetails)
            }
        })
        return this
    }

    override fun visitStructure(visitor: FileCollectionStructureVisitor) {
        abstractFileTree?.visitStructure(visitor)
    }

}
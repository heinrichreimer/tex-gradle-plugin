package org.danilopianini.gradle.latex.task

import org.danilopianini.gradle.latex.Latex
import org.danilopianini.gradle.latex.LatexArtifact
import org.danilopianini.gradle.latex.command.PdfCommand
import org.danilopianini.gradle.latex.configuration.PdfConfiguration
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.process.internal.ExecAction

open class PdfTask : Exec(), PdfConfiguration {

    @get:Input
    final override val pdfCommand: Property<PdfCommand> = project.objects.property(PdfCommand::class.java)

    @get:Input
    final override val quiet: Property<Boolean> = project.objects.property(Boolean::class.java)

    @get:InputFile
    final override val tex: RegularFileProperty = project.objects.fileProperty()

    @get:Input
    @get:OutputFile
    final override val pdf: RegularFileProperty = project.objects.fileProperty()

    init {
        group = Latex.TASK_GROUP
        description = "Uses pdfLaTeX to compile ${tex.get()} into ${pdf.get()}"
    }

    fun fromArtifact(artifact: LatexArtifact) {
        pdfCommand.set(artifact.pdfCommand)
        quiet.set(artifact.quiet)
        tex.set(artifact.tex)
        pdf.set(artifact.pdf)
    }

    @TaskAction
    override fun exec() {
        val action: ExecAction = execActionFactory.newExecAction()
        pdfCommand.get().execute(action, this)
    }
}
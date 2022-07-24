package net.codebot.pdfviewer

data class PathLog(
    val id: Int,
    val experiences: ArrayList<Experience>
)

data class Experience(
    val experienceType: ExperienceType,
    val x: Int,
    val y: Int
)

enum class ExperienceType{
    MOVE_TO,
    LINE_TO
}
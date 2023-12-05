package uk.ac.tees.mad.w9615750.dataclass

data class Allergen(
    val allergenId: String = "",
    val type: String = "",
    val name: String = "",
    val symptoms: List<Symptom> = listOf()
)



/**
 * Created by chrystian.lozada.local (21/11/25)
 */
package com.mobilitydevai.sce.domain.entity

data class VoteEntity(
    val userId: String = "",
    val vote: String = "", // "like"
    val timestamp: String = ""
)

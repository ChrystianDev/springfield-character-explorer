/**
 * Created by chrystian.lozada.local (21/11/25)
 */
package com.mobilitydevai.sce.domain.entity

data class ImageEntity(
    val imageId: String = "",
    val url: String = "",
    val description: String = "",
    val likeCount: Long = 0,
    val timestamp: String = ""
)

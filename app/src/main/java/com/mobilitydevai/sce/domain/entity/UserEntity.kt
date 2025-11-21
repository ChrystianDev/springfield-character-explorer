/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.domain.entity

data class UserEntity(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val like: Long = 0,
    val unlike: Long = 0,
    val imageId: Long = 0
)

package tj.winditask.data.local.entities

data class UserEntity(
    val name: String?,
    val phone: String,
    val username: String,
    val city: String?,
    val birthday: String?,
    val id: Int,
    val avatar: String?,
    val vk: String?,
    val instagram: String?,
    val avatars: UserAvatarEntity? = null
)

data class UserAvatarEntity(
    val avatar: String? = null,
    val bigAvatar: String? = null,
    val miniAvatar: String? = null
)
package tj.winditask.data.model

data class Profile(
    val name: String?,
    val phone: String,
    val username: String,
    val city: String?,
    val birthday: String?,
    val vk: String?,
    val instagram: String?,
    val id: Int,
    val avatar: String?,
    val avatars: ProfileAvatar? = null
)

data class ProfileAvatar(
    val avatar: String?,
    val bigAvatar: String? = null,
    val miniAvatar: String? = null
)
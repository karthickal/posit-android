package tv.justplay.android

sealed class UIModel<out T, F : Exception> {

    object LoadingModel : UIModel<Nothing, Exception>()

    data class SuccessModel<T>(
        val data: T
    ) : UIModel<T, Exception>()

    object FailureModel : UIModel<Nothing, Exception>()

}
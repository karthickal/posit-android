package tv.justplay.android.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import tech.posit.android.data.repository.model.ProductInfo
import tech.posit.android.posit.Posit
import tv.justplay.android.UIModel

class ProductRecommendationViewModel : ViewModel() {

    val products: LiveData<UIModel<List<ProductInfo>, Exception>> = liveData {
        emit(UIModel.LoadingModel)

        val result = Posit.getAllProductsAsync()
        if (result != null) {
            emit(UIModel.SuccessModel(result))
        } else {
            emit(UIModel.FailureModel)
        }
    }
}
package ru.practicum.android.diploma.presentation.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.root.place.AreaState

class SelectRegionViewModel(
    private val hhInteractor: HhInteractor,
    private val sharedPreferencesInteractor: SharedPreferencesInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<AreaState>()
    fun observeState(): LiveData<AreaState> = stateLiveData

    fun getRegions() {
        viewModelScope.launch {
            hhInteractor.getAreas().collect { areas ->
                val regions = ArrayList<Area>()
                val country = getCountry() // Получаю страну из SharedPrefs
                if (country != null) { // Если страна назначена
                    for (area in areas) { // Перебираю все страны
                        if (area.id == country.id) { // Сравниваю с назначенной
                            regions.addAll(area.areas!!) // Добавляю регионы этой страны в список
                        }
                    }
                } else { // Если страна не назначена
                    for (area in areas) {
                        regions.addAll(area.areas!!) // Добавляю регионы всех стран в список
                    }
                }
            }
        }
    }

    private fun filter(list: List<Area>, s: String): ArrayList<Area> {
        val result = ArrayList<Area>()
        result.addAll(list.filter { it.name.contains(s, true) })
        return result
    }

    private fun getCountry(): Area? {
        return sharedPreferencesInteractor.getCountry()
    }

    fun setRegion(area: Area) {
        sharedPreferencesInteractor.setRegion(area)
    }
}

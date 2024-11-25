package ru.practicum.android.diploma.presentation.filter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry

class FilterViewModel(
    private val sharedPreferencesInteractor: SharedPreferencesInteractor,
) : ViewModel() {

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    private val _isApplyButtonEnabled = MutableStateFlow(false)
    val isApplyButtonEnabled: StateFlow<Boolean> = _isApplyButtonEnabled.asStateFlow()

    private val _isResetButtonVisible = MutableStateFlow(false)
    val isResetButtonVisible: StateFlow<Boolean> = _isResetButtonVisible.asStateFlow()

    private var initialFilterState: FilterState = FilterState()

    init {
        getInitialState()
        updateButtonStates()
    }

    fun getInitialState() {
        val savedSalary = sharedPreferencesInteractor.getSalary()
        val savedIndustry = sharedPreferencesInteractor.getIndustry()
        val savedCountry = sharedPreferencesInteractor.getCountry()
        val savedRegion = sharedPreferencesInteractor.getRegion()
        val savedShowOnlyWithSalary = sharedPreferencesInteractor.getShowOnlyWithSalary()
        val locationString = createLocationString(savedCountry, savedRegion)

        initialFilterState = FilterState(
            salary = savedSalary,
            industry = savedIndustry,
            country = savedCountry,
            region = savedRegion,
            showOnlyWithSalary = savedShowOnlyWithSalary,
            locationString = locationString
        )

        _filterState.value = initialFilterState.copy()
    }

    fun updateLocation(country: Area?, region: Area?) {
        val currentState = _filterState.value
        val locationString = createLocationString(country, region)

        val newState = currentState.copy(
            country = country,
            region = region,
            locationString = locationString
        )

        _filterState.value = newState

        updateButtonStates()
    }

    fun updateSalary(salary: Int?) {
        val currentState = _filterState.value
        val newState = currentState.copy(salary = salary)
        _filterState.value = newState
        updateButtonStates()
    }

    fun updateIndustries(industry: Industry?) {
        val currentState = _filterState.value
        val newState = currentState.copy(industry = industry)
        _filterState.value = newState
    }

    fun toggleShowOnlyWithSalary() {
        val currentState = _filterState.value
        val newState = currentState.copy(
            showOnlyWithSalary = !currentState.showOnlyWithSalary
        )
        _filterState.value = newState
        updateButtonStates()
    }

    fun applyFilter() {
        val currentState = _filterState.value

        sharedPreferencesInteractor.setSalary(currentState.salary)

        sharedPreferencesInteractor.setIndustry(currentState.industry)

        sharedPreferencesInteractor.setCountry(currentState.country)
        sharedPreferencesInteractor.setRegion(currentState.region)

        currentState.country?.let { sharedPreferencesInteractor.setCountry(it) }
        currentState.region?.let { sharedPreferencesInteractor.setRegion(it) }

        sharedPreferencesInteractor.setShowOnlyWithSalary(currentState.showOnlyWithSalary)

        initialFilterState = currentState.copy()

        updateButtonStates()
    }

    fun resetFilter() {
        _filterState.value = FilterState()

        sharedPreferencesInteractor.setSalary(null)
        sharedPreferencesInteractor.setIndustry(null)
        sharedPreferencesInteractor.setCountry(null)
        sharedPreferencesInteractor.setRegion(null)
        sharedPreferencesInteractor.setShowOnlyWithSalary(false)

        initialFilterState = _filterState.value

        updateButtonStates()
    }

    private fun createLocationString(country: Area?, region: Area?): String {
        val locationParts = mutableListOf<String>()

        country?.name?.let { locationParts.add(it) }
        region?.name?.let { locationParts.add(it) }

        return locationParts.joinToString(", ")
    }

    private fun updateButtonStates() {
        val currentState = _filterState.value

        _isResetButtonVisible.value = listOf<Any?>(
            currentState.salary,
            currentState.industry,
            currentState.country,
            currentState.region,
            currentState.showOnlyWithSalary.takeIf { it }
        ).any { it != null }

        _isApplyButtonEnabled.value = isFilterChanged()
    }

    fun isFilterChanged(): Boolean {
        val currentState = _filterState.value
        return initialFilterState.salary != currentState.salary ||
            initialFilterState.industry != currentState.industry ||
            initialFilterState.country != currentState.country ||
            initialFilterState.region != currentState.region ||
            initialFilterState.showOnlyWithSalary != currentState.showOnlyWithSalary
    }

    fun hasActiveFilters(): Boolean {
        val currentState = _filterState.value
        val hasFilters = currentState.salary != null ||
            currentState.industry != null ||
            currentState.country != null ||
            currentState.region != null ||
            currentState.showOnlyWithSalary
        return hasFilters
    }
}

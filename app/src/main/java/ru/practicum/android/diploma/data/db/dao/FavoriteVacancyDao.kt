package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity

@Dao
interface FavoriteVacancyDao {

    @Insert(entity = FavoriteVacancyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteVacancy(vacancy: FavoriteVacancyEntity)

    @Query("DELETE FROM favorite_vacancy_table WHERE id = :id")
    suspend fun deleteFavoriteVacancyById(id: String)

    @Query("SELECT * FROM favorite_vacancy_table ORDER BY id DESC")
    fun getAllFavoriteVacancies(): Flow<List<FavoriteVacancyEntity>>

    @Query("SELECT id FROM favorite_vacancy_table")
    suspend fun getFavoriteVacancyIds(): List<String>

    @Query("SELECT * FROM favorite_vacancy_table where id = :id")
    fun getFavoriteVacancyById(id: String): Flow<FavoriteVacancyEntity?>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_vacancy_table WHERE id = :id)")
    suspend fun isVacancyFavorite(id: String): Boolean

}

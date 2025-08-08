package com.practicum.playlistmaker.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
class SearchHistoryStorage(private val sharedPreferences: SharedPreferences) : SearchHistoryRepository {
    private val gson = Gson()
    private val maxHistorySize = 10

    // Загружаем историю поиска
    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString("searchHistory", "[]")
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(json, type)
    }

    // Сохраняем историю поиска
    override fun saveHistory(track: Track) {
        val history = getHistory().toMutableList()

        // Убираем дубликаты и перемещаем трек на верхний уровень
        // Совместимая замена метода removeIf для старых версий Android
        val iterator = history.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().trackId == track.trackId) {
                iterator.remove()  // Убираем дубликат
            }
        }

        // Добавляем новый трек в начало
        history.add(0, track)

        // Ограничиваем размер списка
        if (history.size > maxHistorySize) {
            history.removeAt(history.size - 1)
        }

        // Сохраняем обновленную историю в SharedPreferences
        val json = gson.toJson(history)
        sharedPreferences.edit().putString("searchHistory", json).apply()
    }

    // Очищаем историю
    override fun clearHistory() {
        sharedPreferences.edit().remove("searchHistory").apply()
    }
}


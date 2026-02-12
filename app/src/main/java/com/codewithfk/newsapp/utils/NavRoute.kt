package com.codewithfk.newsapp.utils

import com.codewithfk.newsapp.data.model.News
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder

object NavRoute {

    const val HOME = "/home"
    const val DETAILS = "/details/news={news}&isLocal={isLocal}"

    fun createNewsDetailsRoute(news: News, isLocal: Boolean = false): String {
        val json = Gson().toJson(news)
        val encoded = URLEncoder.encode(json, "utf-8")
        return "/details/news=$encoded&isLocal=$isLocal"
    }

    fun getNewsFromRoute(json: String): News {
        val decoded = URLDecoder.decode(json, "utf-8")
        return Gson().fromJson(decoded, News::class.java)
    }
}

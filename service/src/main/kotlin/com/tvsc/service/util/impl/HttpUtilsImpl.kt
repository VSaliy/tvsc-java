package com.tvsc.service.util.impl

import com.tvsc.service.util.HttpUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.BufferedSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Supplier

/**
 *
 * @author Taras Zubrei
 */
@Component
open class HttpUtilsImpl @Autowired constructor(val okHttpClient: OkHttpClient, val executor: Executor) : HttpUtils {
    override fun get(url: String): CompletableFuture<String> = getResponse(url).thenApply { it.string() }
    private fun getResponse(url: String) = CompletableFuture.supplyAsync(Supplier {
        okHttpClient.newCall(Request.Builder().url(url).build()).execute().body()
    }, executor)
}
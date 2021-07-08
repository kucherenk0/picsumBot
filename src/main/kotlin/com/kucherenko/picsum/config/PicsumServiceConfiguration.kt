package com.kucherenko.picsum.config

import com.kucherenko.picsum.bot.PicsumBot
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContexts
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import org.telegram.telegrambots.meta.TelegramBotsApi
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext


@Configuration
class PicsumServiceConfiguration {

    @Bean
    fun telegramBotsApi(picsumBot: PicsumBot): TelegramBotsApi {
        val telegramBotsApi = TelegramBotsApi()
        telegramBotsApi.registerBot(picsumBot)
        return telegramBotsApi
    }

    //WARN! using this configuration in production environment is not secure
    //Picsum.photo has self-signed sert that's why we need to accept any sert
    @Bean
    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class, KeyManagementException::class)
    fun restTemplate(): RestTemplate? {
        val acceptingTrustStrategy =
            TrustStrategy { chain: Array<X509Certificate?>?, authType: String? -> true }
        val sslContext: SSLContext = SSLContexts.custom()
            .loadTrustMaterial(null, acceptingTrustStrategy)
            .build()
        val csf = SSLConnectionSocketFactory(sslContext)
        val httpClient = HttpClients.custom()
            .setSSLSocketFactory(csf)
            .build()
        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.httpClient = httpClient
        return RestTemplate(requestFactory)
    }

}

package mdtaieb.fr.myweather.Modules

import dagger.Module
import dagger.Provides
import mdtaieb.fr.myweather.Configs.Constants
import mdtaieb.fr.myweather.Networking.WeatherApiImpl
import mdtaieb.fr.myweather.Services.WeatherService
import mdtaieb.fr.myweather.Services.WeatherServiceImpl
import mdtaieb.fr.myweather.Views.ViewModels.WeatherViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

/**
 * Created by mohamedtaieb on 12/06/2018.
 */
@Module class WeatherModule {
    @Provides
    fun provideApplicationContext(): WeatherApp {
        return WeatherApp.instance
    }

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Named("apiUrl")
    fun provideApiUrl(): String {
        return Constants.API_URL
    }

    @Provides
    @Named("apiKey")
    fun provideApiKey(): String {
        return Constants.API_KEY
    }

    @Provides
    fun provideOkHttpClient(@Named("connectionTimeOutSecs") connectionTimeOutSecs: Long,
                            @Named("readTimeOutSecs") readTimeOutSecs: Long,
                            @Named("interceptor") interceptor: Interceptor?): OkHttpClient {

        val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(connectionTimeOutSecs, TimeUnit.SECONDS)
                .readTimeout(readTimeOutSecs, TimeUnit.SECONDS)
                .build()

        if (interceptor != null) {
            val interceptorBuilder = okHttpClient.newBuilder()
            interceptorBuilder.addInterceptor(interceptor)

            return interceptorBuilder.build()
        }

        return okHttpClient

    }

    @Provides
    @Named("interceptor")
    fun providesInterceptor(): Interceptor {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return logInterceptor
    }

    @Provides
    @Named("readTimeOutSecs")
    fun provideReadTimeOutSecs() = 15L

    @Provides
    @Named("connectionTimeOutSecs")
    fun provideConnectionTimeOutSecs() = 15L

    @Provides
    fun provideRestAPI(converterFactory: Converter.Factory,
                       @Named("apiUrl") apiUrl: String,
                       okHttpClient: OkHttpClient,
                       @Named("apiKey") apiKey: String): WeatherApiImpl {

        return WeatherApiImpl(converterFactory = converterFactory,
                apiUrl = apiUrl,
                okHttpClient = okHttpClient,
                apiKey = apiKey)

    }

    @Provides
    fun provideWeatherLiteService(weatherApi: WeatherApiImpl): WeatherService {
        return WeatherServiceImpl(weatherApi = weatherApi)
    }

    @Provides
    fun provideWeatherLiteViewModel(weatherService: WeatherService): WeatherViewModel {
        return WeatherViewModel(weatherService = weatherService)
    }
}
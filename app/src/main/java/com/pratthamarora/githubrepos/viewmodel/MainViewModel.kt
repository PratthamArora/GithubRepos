package com.pratthamarora.githubrepos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pratthamarora.githubrepos.model.AuthToken
import com.pratthamarora.githubrepos.model.GithubService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token
    val error = MutableLiveData<String>()

    fun getToken(clientId: String, clientSecret: String, code: String) {
        compositeDisposable.add(
            GithubService.getApi().getAuthToken(clientId, clientSecret, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AuthToken>() {
                    override fun onSuccess(t: AuthToken) {
                        _token.value = t.accessToken
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        error.value = e.localizedMessage
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
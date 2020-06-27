package com.pratthamarora.githubrepos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pratthamarora.githubrepos.model.GithubService
import com.pratthamarora.githubrepos.model.data.AuthToken
import com.pratthamarora.githubrepos.model.data.GithubComments
import com.pratthamarora.githubrepos.model.data.GithubPR
import com.pratthamarora.githubrepos.model.data.GithubRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val error = MutableLiveData<String>()

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token
    private val _repos = MutableLiveData<List<GithubRepo>>()
    val repos: LiveData<List<GithubRepo>>
        get() = _repos
    private val _prs = MutableLiveData<List<GithubPR>>()
    val prs: LiveData<List<GithubPR>>
        get() = _prs
    private val _comments = MutableLiveData<List<GithubComments>>()
    val comments: LiveData<List<GithubComments>>
        get() = _comments

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

    fun loadRepos(token: String) {
        compositeDisposable.add(
            GithubService.getUserData(token).getAllRepos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<GithubRepo>>() {
                    override fun onSuccess(t: List<GithubRepo>) {
                        _repos.value = t
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        error.value = e.localizedMessage
                    }

                })
        )
    }

    fun loadPRs(token: String, owner: String?, repo: String?) {
        if (owner != null && repo != null) {
            compositeDisposable.add(
                GithubService.getUserData(token).getPRs(owner, repo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<List<GithubPR>>() {
                        override fun onSuccess(t: List<GithubPR>) {
                            _prs.value = t
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            error.value = e.localizedMessage
                        }

                    })
            )
        }
    }

    fun loadComments(token: String, owner: String?, repo: String?, issue: String?) {
        if (owner != null && repo != null && issue != null) {
            compositeDisposable.add(
                GithubService.getUserData(token).getComments(owner, repo, issue)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<List<GithubComments>>() {
                        override fun onSuccess(t: List<GithubComments>) {
                            _comments.value = t
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            error.value = e.localizedMessage
                        }

                    })
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
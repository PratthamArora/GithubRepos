package com.pratthamarora.githubrepos.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.pratthamarora.githubrepos.R
import com.pratthamarora.githubrepos.model.GithubRepo
import com.pratthamarora.githubrepos.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    var authToken: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        repositoriesSpinner.isEnabled = false
        repositoriesSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayListOf("No repositories available")
        )
        repositoriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Load PullRequests
                if (parent?.selectedItem is GithubRepo) {
                    val currentRepo = parent.selectedItem as GithubRepo
                    authToken?.let {
                        viewModel.loadPRs(it, currentRepo.owner.login, currentRepo.name)
                    }
                }
            }
        }


        prsSpinner.isEnabled = false
        prsSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayListOf("Please select repository")
        )
        prsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Load comments
            }
        }

        commentsSpinner.isEnabled = false
        commentsSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayListOf("Please select PR")
        )


        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.token.observe(this, Observer { token ->
            if (token.isNotEmpty()) {
                authToken = token
                Toast.makeText(this, "Authenticated Successfully", Toast.LENGTH_SHORT).show()
                loadReposButton.isEnabled = true
            } else {
                Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.repos.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                val spinnerAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    it
                )
                repositoriesSpinner.apply {
                    adapter = spinnerAdapter
                    isEnabled = true
                }
            } else {
                val spinnerAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayListOf("No Repos")
                )
                repositoriesSpinner.apply {
                    adapter = spinnerAdapter
                    isEnabled = false
                }
            }
        })

        viewModel.prs.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                val spinnerAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    it
                )
                prsSpinner.apply {
                    adapter = spinnerAdapter
                    isEnabled = true
                }
            } else {
                val spinnerAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayListOf("No Pull Requests")
                )
                prsSpinner.apply {
                    adapter = spinnerAdapter
                    isEnabled = false
                }
            }

        })
    }

    fun onAuthenticate(view: View) {
        val oAuthUrl = getString(R.string.oauthUrl)
        val clientId = getString(R.string.clientId)
        val callbackUrl = getString(R.string.callbackUrl)
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("$oAuthUrl?client_id=$clientId&scope=repo&redirect_uri=$callbackUrl")
        ).also { startActivity(it) }
    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data
        val callbackUrl = getString(R.string.callbackUrl)
        if (uri != null && uri.toString().startsWith(callbackUrl)) {
            val code = uri.getQueryParameter("code")
            code?.let {
                val clientId = getString(R.string.clientId)
                val clientSecret = getString(R.string.clientSecret)
                viewModel.getToken(clientId, clientSecret, code)
            }
        }
    }

    fun onLoadRepos(view: View) {
        authToken?.let {
            viewModel.loadRepos(it)
        }
    }

    fun onPostComment(view: View) {

    }

}
package com.example.rickandmortygo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rickandmortygo.presentation.characters.CharactersFragment
import com.example.rickandmortygo.presentation.collection.CollectionFragment
import com.example.rickandmortygo.presentation.episodes.EpisodesFragment
import com.example.rickandmortygo.presentation.locations.LocationsFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import com.google.firebase.auth.*

class MainActivity : AppCompatActivity() {

    //permet de ne pas recharger le fragment deja selectionné
    private var selectedTab = 0
    private lateinit var collection : CollectionFragment;
    private var REQUEST_CODE_SCAN = 2;
    private val GGL_SIGN_IN = 1

    private var menuConst: Menu? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = resources.getString(R.string.collection)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //ajout du premier fragment
        collection = CollectionFragment()
        supportFragmentManager.beginTransaction().add(R.id.content,
            collection
        ).commit()

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuConst = menu
        menuInflater.inflate(R.menu.firebase_menu, menuConst)
        if(mAuth?.currentUser == null){
            menuConst?.findItem(R.id.deconnexion)?.isVisible = false
        } else {
            menuConst?.findItem(R.id.connexion)?.isVisible = false
        }
        return super.onCreateOptionsMenu(menuConst)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title.equals("Connexion")){
            signInWithGoogle()
        }
        if (item.title.equals("Déconnexion")){
            signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, GGL_SIGN_IN)
    }

    private fun signOut() {
        mAuth!!.signOut()
        mGoogleSignInClient!!.signOut().addOnCompleteListener(
            this
        ) { updateUI() }
    }

    private fun updateUI() {
        if(mAuth?.currentUser == null){
            menuConst?.findItem(R.id.deconnexion)?.isVisible = false
            menuConst?.findItem(R.id.connexion)?.isVisible = true
        } else {
            menuConst?.findItem(R.id.deconnexion)?.isVisible = true
            menuConst?.findItem(R.id.connexion)?.isVisible = false
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->

        if (selectedTab != menuItem.itemId) {
            selectedTab = menuItem.itemId
            when (menuItem.itemId) {
                R.id.navigation_collection -> {
                    collection = CollectionFragment()
                    title = resources.getString(R.string.collection)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content, collection, "Collection")
                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_characters -> {
                    val fragment = CharactersFragment()
                    title = resources.getString(R.string.characters)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content, fragment, "Personnages")
                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_episodes -> {
                    val fragment = EpisodesFragment()
                    title = resources.getString(R.string.episodes)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content, fragment, "Episodes")
                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_places -> {
                    val fragment = LocationsFragment()
                    title = resources.getString(R.string.locations)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content, fragment, "Lieux")
                        .addToBackStack(null)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
        }
        false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GGL_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                Toast.makeText(
                    this@MainActivity, "Authentication failed 1.",
                    Toast.LENGTH_SHORT
                ).show()
                updateUI()
            }
        }
        else {
            if (resultCode == RESULT_OK && data?.data != null) {
                super.onActivityResult(requestCode, resultCode, data)
            }
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                collection.fetchScannedCharacter(result, this)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth!!.currentUser
                    updateUI()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this@MainActivity, "Authentication failed 2.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI()
                }
            }
        collection.getUserData()
    }
}
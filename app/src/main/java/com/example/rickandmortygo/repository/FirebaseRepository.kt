package com.example.rickandmortygo.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.rickandmortygo.data.Character
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


object FirebaseRepository {

    fun sendUserData(characters : List<Character>){
        characters.forEach {
            character ->
            sendCharacter(character)
        }
    }

    fun sendCharacter(character: Character) {
        val db :FirebaseFirestore = FirebaseFirestore.getInstance()

        db.collection("characters")
            .add(character)
            .addOnSuccessListener(OnSuccessListener<DocumentReference> {
                    documentReference ->
                Log.d(TAG,"DocumentSnapshot added with ID: " + documentReference.id)
            })
            .addOnFailureListener(OnFailureListener { e -> Log.w(TAG, "Error adding document", e) })
    }

    fun getUserData():Boolean {
        val db :FirebaseFirestore = FirebaseFirestore.getInstance()
        var success : Boolean = false;
        db.collection("characters")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (character in task.result) {
                        Log.d(TAG, character.id + " => " + character.data)
                    }
                    success = true
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                    success = false
                }
            }
        return success
    }
}
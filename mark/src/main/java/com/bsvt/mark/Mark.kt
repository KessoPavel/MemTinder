package com.bsvt.mark

import com.google.firebase.database.FirebaseDatabase

class Mark {
    private var referenceName = "unknown"
    private var childName = "unknown"
    private var mark = "unknown"

    fun referenceName(name: String?) = apply {
        name?.let {
            referenceName = it
        }
    }

    fun markName(name: String?) = apply {
        name?.let {
            childName = it
        }
    }

    fun mark(mark: String?) = apply {
        mark?.let {
            this.mark = it
        }
    }

    fun send() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(referenceName)
        myRef.child(childName).setValue(mark)
    }
}
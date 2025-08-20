package com.recipe.explorer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

import com.recipe.explorer.R

/**
 * Profile tab without ViewBinding.
 */
class ProfileFragment : Fragment() {

    companion object {
        // PUBLIC_INTERFACE
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val appVersion = view.findViewById<TextView>(R.id.app_version)
        val profileHint = view.findViewById<TextView>(R.id.profile_hint)
        appVersion.text = "Recipe Explorer v1.0"
        profileHint.text = "Add, edit, and favorite your recipes."
    }
}

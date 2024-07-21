package com.hit.gymtime.ui.user_preferences

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import com.hit.gymtime.R


class MinimumMultiSelectListPreference(
    context: Context,
    attrs: AttributeSet
) : MultiSelectListPreference(context, attrs), Preference.OnPreferenceChangeListener {

    companion object {
        private const val MINIMUM_SELECTION = 1
    }

    init {
        onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        val values = newValue as? Set<*> ?: return false
        return if (values.size >= MINIMUM_SELECTION) {
            true
        } else {
            Toast.makeText(context,
                context.getString(R.string.please_choose_at_least_one_city), Toast.LENGTH_SHORT).show()
            false
        }
    }
}
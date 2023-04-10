package com.nvalenti.journalite.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.nvalenti.journalite.R
import com.nvalenti.journalite.databinding.DayPickerBinding
import java.time.DayOfWeek


class DayPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val binding: DayPickerBinding

    var days = mutableSetOf<DayOfWeek>()
        private set

    init {
        orientation = HORIZONTAL
        binding = DayPickerBinding.inflate(LayoutInflater.from(context), this)

        context.theme.obtainStyledAttributes(
            attrs, R.styleable.DayPicker,
            0, 0
        ).apply {
            try {
                binding.monday.isSelected = getBoolean(R.styleable.DayPicker_monday, false)
                binding.tuesday.isSelected =
                    getBoolean(R.styleable.DayPicker_tuesday, false)
                binding.wednesday.isSelected =
                    getBoolean(R.styleable.DayPicker_wednesday, false)
                binding.thursday.isSelected =
                    getBoolean(R.styleable.DayPicker_thursday, false)
                binding.friday.isSelected = getBoolean(R.styleable.DayPicker_friday, false)
                binding.saturday.isSelected =
                    getBoolean(R.styleable.DayPicker_saturday, false)
                binding.sunday.isSelected = getBoolean(R.styleable.DayPicker_sunday, false)
            } finally {
                recycle()
            }
        }

        binding.monday.setOnCheckedChangeListener { _, isChecked ->
            setDay(DayOfWeek.MONDAY, isChecked)
        }
        binding.tuesday.setOnCheckedChangeListener { _, isChecked ->
            setDay(DayOfWeek.TUESDAY, isChecked)
        }
        binding.wednesday.setOnCheckedChangeListener { _, isChecked ->
            setDay(DayOfWeek.WEDNESDAY, isChecked)
        }
        binding.thursday.setOnCheckedChangeListener { _, isChecked ->
            setDay(DayOfWeek.THURSDAY, isChecked)
        }
        binding.friday.setOnCheckedChangeListener { _, isChecked ->
            setDay(DayOfWeek.FRIDAY, isChecked)
        }
        binding.saturday.setOnCheckedChangeListener { _, isChecked ->
            setDay(DayOfWeek.SATURDAY, isChecked)
        }
        binding.sunday.setOnCheckedChangeListener { _, isChecked ->
            setDay(DayOfWeek.SUNDAY, isChecked)
        }
    }

    fun setDaysTo(inDays: MutableSet<DayOfWeek>) {
        days.clear()
        days.addAll(inDays)
        days.forEach { setDay(it, true) }
        syncDaysToData()
    }

    private fun setDay(day: DayOfWeek, isSet: Boolean) {
        if (isSet) days.add(day) else days.remove(day)
    }

    private fun syncDaysToData() {
        binding.monday.isChecked = days.contains(DayOfWeek.MONDAY)
        binding.tuesday.isChecked = days.contains(DayOfWeek.TUESDAY)
        binding.wednesday.isChecked = days.contains(DayOfWeek.WEDNESDAY)
        binding.thursday.isChecked = days.contains(DayOfWeek.THURSDAY)
        binding.friday.isChecked = days.contains(DayOfWeek.FRIDAY)
        binding.saturday.isChecked = days.contains(DayOfWeek.SATURDAY)
        binding.sunday.isChecked = days.contains(DayOfWeek.SUNDAY)
    }
}
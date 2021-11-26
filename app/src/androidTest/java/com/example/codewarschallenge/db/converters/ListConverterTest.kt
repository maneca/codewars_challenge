package com.example.codewarschallenge.db.converters

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ListConverterTest {

    private lateinit var listConverter: ListConverter

    companion object {
        const val languages =
            "[\"ruby\",\"c#\",\".net\",\"javascript\",\"coffeescript\",\"nodejs\",\"rails\"]"
    }

    @Before
    fun setup() {
        listConverter = ListConverter()
    }

    @Test
    fun convertFromStringToList() {
        val list = listConverter.fromStringToList(languages)
        Assert.assertEquals(list.size, 7)
    }

    @Test
    fun convertFromArrayListToString() {
        val lang = listConverter.fromListToString(
            listOf(
                "ruby",
                "c#",
                ".net",
                "javascript",
                "coffeescript",
                "nodejs",
                "rails"
            )
        )

        Assert.assertTrue(languages == lang)

    }
}
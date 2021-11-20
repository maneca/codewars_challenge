package com.example.codewarschallenge.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.codewarschallenge.R
import com.example.codewarschallenge.db.model.CompletedChallenge
import com.example.codewarschallenge.viewmodels.CompletedChallengesViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class CompletedChallengesFragment : Fragment() {
    private val completedChallengesViewModel by viewModel<CompletedChallengesViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val completedChallenges = completedChallengesViewModel.completedChallenges.value
                val loading = completedChallengesViewModel.showLoading.value
                val error = completedChallengesViewModel.showError.value
                val page = completedChallengesViewModel.page.value

                when{
                    loading ->
                        CircularProgressIndicator(modifier = Modifier
                            .wrapContentSize(),
                            color = Color.Blue,
                            strokeWidth = 5.dp)
                    !error.isNullOrBlank() -> ErrorDialog(message = error)
                    else ->
                        Scaffold{
                            CompleteChallengesList(completedChallenges)
                        }
                }
            }
        }

    }

    @Composable
    fun CompleteChallengesList(challenges: List<CompletedChallenge>){
        LazyColumn(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
        ) {
            itemsIndexed(items = challenges){
                index, challenge ->
                    completedChallengesViewModel.onChangeChallengesScrollPosition(index)
                    Challenge(challenge = challenge)
            }
        }
    }

    @Composable
    fun Challenge(challenge : CompletedChallenge){
        Card(
            modifier = Modifier
                .background(shape = RoundedCornerShape(10.dp), color = Color.LightGray)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(2.dp)
                .clickable {
                    //val bundle = Bundle()
                    //bundle.putParcelable("country_data_row", country)
                    //findNavController().navigate(R.id.view_country_details, bundle)
                },
            elevation = 4.dp,
        ){
            Row(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically){
                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()) {
                            Text(text = challenge.name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(stringResource(R.string.completed_at)+ " ")
                                    }
                                    append(challenge.completedAt)
                                })
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(stringResource(R.string.completed_lang)+ " ")
                                    }
                                    append(challenge.completedLanguages.joinToString { it })
                                })
                    }
            }
        }
    }

    @Composable
    fun ErrorDialog(message: String){
        val openDialog = remember { mutableStateOf(false)  }

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = getString(R.string.error_title))
            },
            text = {
                Text(message)
            },
            confirmButton = {
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text(getString(R.string.dismiss_button))
                }
            }
        )
    }
}
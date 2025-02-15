package com.practicum.playlistmaker.ui.playlist.add_to_playlist.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAddplaylistBinding
import com.practicum.playlistmaker.ui.playlist.add_to_playlist.view_model.AddPlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class AddPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentAddplaylistBinding
    private val addPlayListViewModel by viewModel<AddPlayListViewModel>()
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private var pickImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddplaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.finish_creating_playlist))
            setMessage(getString(R.string.all_unsaved_data_will_be_lost))
            setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
            setPositiveButton(getString(R.string.finish)) { _, _ ->
                findNavController().navigateUp()
            }
        }

        binding.playListNameEditText.doOnTextChanged { s: CharSequence?, _, _, _ ->
            val descriptionNotEmpty = !binding.playListDescriptionEditText.text.isNullOrEmpty()
            binding.playlistCreateButton.isEnabled = !s.isNullOrEmpty() || descriptionNotEmpty
        }

        binding.toolbar.setOnClickListener {
            if (checkUnsavedData()) {
                confirmDialog.show()
            } else {
                findNavController().popBackStack()
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.addImage.setImageURI(uri)
                    pickImageUri = uri
                }
            }
        binding.addImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.playlistCreateButton.setOnClickListener {
            val name = binding.playListNameEditText.text.toString()
            val description = binding.playListDescriptionEditText.text.toString()
            var image: String? = null
            if (pickImageUri != null) {
                image = Calendar.getInstance().timeInMillis.toString() + ".jpg"
                saveImageToPrivateStorage(pickImageUri!!, image)
            }
            if (name.isNotEmpty()) {
                addPlayListViewModel.createPlayList(
                    name = name,
                    description = description,
                    image = image
                )
                Toast.makeText(
                    requireContext(),
                    String.format(resources.getText(R.string.playlist_created).toString(), name),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (checkUnsavedData()) {
                    confirmDialog.show()
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun checkUnsavedData(): Boolean {
        return (
                pickImageUri != null
                        || binding.playListNameEditText.text.toString().isNotEmpty()
                        || binding.playListDescriptionEditText.text.toString().isNotEmpty()
                )
    }

    private fun saveImageToPrivateStorage(uri: Uri, fileName: String) {

        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                Consts.PLAY_LISTS_IMAGES_DIRECTORY
            )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, fileName)
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, Consts.IMAGE_QUALITY, outputStream)
        inputStream!!.close()
        outputStream.close()
    }

}
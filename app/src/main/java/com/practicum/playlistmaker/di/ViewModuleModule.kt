package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.bottom_sheet.view_model.PlayerBottomSheetViewModel
import com.practicum.playlistmaker.ui.mediatech.favorite.view_model.MediaFavoriteTracksViewModel
import com.practicum.playlistmaker.ui.playlist.view_model.MediaPlaylistsViewModel
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.playlist.add_to_playlist.view_model.AddPlayListViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModel = module {

    viewModel {
        PlayerViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(),get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        MediaFavoriteTracksViewModel(get())
    }

    viewModel {
        MediaPlaylistsViewModel(get())
    }

    viewModel {
        PlayerBottomSheetViewModel(get())
    }

    viewModel {
        AddPlayListViewModel(get())
    }

}
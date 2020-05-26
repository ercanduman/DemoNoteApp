package com.enbcreative.demonoteapp

import androidx.multidex.MultiDexApplication
import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.network.WebApi
import com.enbcreative.demonoteapp.data.prefs.Preferences
import com.enbcreative.demonoteapp.data.repository.NoteRepository
import com.enbcreative.demonoteapp.data.repository.UserRepository
import com.enbcreative.demonoteapp.ui.auth.AuthViewModelFactory
import com.enbcreative.demonoteapp.ui.main.notes.NotesViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MainApplication : MultiDexApplication(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MainApplication))
        bind() from singleton { WebApi() }
        bind() from singleton { Preferences(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { UserRepository(instance(), instance()) }
        bind() from singleton { NoteRepository(instance(), instance(), instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { NotesViewModelFactory(instance()) }
    }
}
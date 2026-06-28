package com.poultry.broiler.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poultry.broiler.domain.model.ProjectType
import com.poultry.broiler.domain.usecase.CreateProjectUseCase
import com.poultry.broiler.domain.usecase.DeleteProjectUseCase
import com.poultry.broiler.domain.usecase.DuplicateProjectUseCase
import com.poultry.broiler.domain.usecase.GetProjectsUseCase
import com.poultry.broiler.domain.usecase.SearchProjectsUseCase
import com.poultry.broiler.domain.usecase.UpdateProjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home Screen (Projects tab).
 *
 * Exposes [uiState] as a single [StateFlow] following Constitution Art 1.2.4
 * (UDF via StateFlow). User actions arrive via [onIntent] as sealed
 * [HomeIntent] events. One-shot navigation signals flow through
 * [navigationEvent].
 *
 * @property uiState The current screen state.
 * @property navigationEvent One-shot navigation events (wizard / dashboard).
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getProjectsUseCase: GetProjectsUseCase,
        private val searchProjectsUseCase: SearchProjectsUseCase,
        private val createProjectUseCase: CreateProjectUseCase,
        private val updateProjectUseCase: UpdateProjectUseCase,
        private val duplicateProjectUseCase: DuplicateProjectUseCase,
        private val deleteProjectUseCase: DeleteProjectUseCase,
    ) : ViewModel() {
        private val _searchQuery = MutableStateFlow("")
        val searchQuery: MutableStateFlow<String> = _searchQuery

        private val _navigationEvent = Channel<HomeNavigation>(Channel.BUFFERED)
        val navigationEvent = _navigationEvent.receiveAsFlow()

        private val projectsFlow =
            _searchQuery
                .debounce(SEARCH_DEBOUNCE_MS)
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        getProjectsUseCase()
                    } else {
                        searchProjectsUseCase(query)
                    }
                }

        val uiState =
            combine(
                _searchQuery,
                projectsFlow,
            ) { query, projects ->
                when {
                    query.isBlank() && projects.isEmpty() -> HomeUiState.Empty
                    query.isBlank() ->
                        HomeUiState.Content(
                            projects = projects,
                            searchQuery = query,
                            isSearchActive = false,
                            noSearchResults = false,
                        )
                    else ->
                        HomeUiState.Content(
                            projects = projects,
                            searchQuery = query,
                            isSearchActive = true,
                            noSearchResults = projects.isEmpty(),
                        )
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(SUBSCRIBE_TIMEOUT_MS),
                initialValue = HomeUiState.Loading,
            )

        /**
         * Dispatch a user [intent] for processing.
         */
        fun onIntent(intent: HomeIntent) {
            when (intent) {
                is HomeIntent.CreateProject -> handleCreateProject(intent)
                is HomeIntent.UpdateSearchQuery -> handleUpdateSearchQuery(intent)
                is HomeIntent.OpenProject -> handleOpenProject(intent)
                is HomeIntent.EditProject -> handleEditProject(intent)
                is HomeIntent.DuplicateProject -> handleDuplicateProject(intent)
                is HomeIntent.DeleteProject -> handleDeleteProject(intent)
                HomeIntent.ClearSearch -> handleClearSearch()
            }
        }

        private fun handleCreateProject(intent: HomeIntent.CreateProject) {
            viewModelScope.launch {
                val result = createProjectUseCase(intent.name, intent.type, intent.location)
                if (result.isSuccess) {
                    val project = result.getOrThrow()
                    val navEvent =
                        when (project.type) {
                            ProjectType.NEW_INSTALLATION -> HomeNavigation.ToWizard(project.id)
                            ProjectType.EXISTING_ASSESSMENT -> HomeNavigation.ToDashboard(project.id)
                        }
                    _navigationEvent.send(navEvent)
                }
            }
        }

        private fun handleUpdateSearchQuery(intent: HomeIntent.UpdateSearchQuery) {
            _searchQuery.value = intent.query
        }

        private fun handleOpenProject(intent: HomeIntent.OpenProject) {
            val state = uiState.value
            if (state is HomeUiState.Content) {
                val project = state.projects.find { it.id == intent.projectId } ?: return
                viewModelScope.launch {
                    val navEvent =
                        when (project.type) {
                            ProjectType.NEW_INSTALLATION -> HomeNavigation.ToWizard(project.id)
                            ProjectType.EXISTING_ASSESSMENT -> HomeNavigation.ToDashboard(project.id)
                        }
                    _navigationEvent.send(navEvent)
                }
            }
        }

        private fun handleEditProject(intent: HomeIntent.EditProject) {
            viewModelScope.launch {
                updateProjectUseCase(intent.projectId, intent.name, intent.location)
            }
        }

        private fun handleDuplicateProject(intent: HomeIntent.DuplicateProject) {
            viewModelScope.launch {
                duplicateProjectUseCase(intent.projectId)
            }
        }

        private fun handleDeleteProject(intent: HomeIntent.DeleteProject) {
            viewModelScope.launch {
                deleteProjectUseCase(intent.projectId)
            }
        }

        private fun handleClearSearch() {
            _searchQuery.value = ""
        }

        companion object {
            private const val SEARCH_DEBOUNCE_MS = 300L
            private const val SUBSCRIBE_TIMEOUT_MS = 5000L
        }
    }

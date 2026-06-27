package com.poultry.broiler.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poultry.broiler.R
import com.poultry.broiler.domain.model.Project
import com.poultry.broiler.presentation.home.components.DeleteConfirmationDialog
import com.poultry.broiler.presentation.home.components.EditProjectDialog
import com.poultry.broiler.presentation.home.components.NewProjectDialog
import com.poultry.broiler.presentation.home.components.NewProjectPlaceholderCard
import com.poultry.broiler.presentation.home.components.PerformanceSummaryCard
import com.poultry.broiler.presentation.home.components.ProjectCard
import com.poultry.broiler.presentation.home.components.ProjectContextMenu
import com.poultry.broiler.presentation.home.components.ProjectSearchBar
import com.poultry.broiler.presentation.theme.LocalSpacing

private val MinColumnWidth = 160.dp

/**
 * Primary screen composable for the Projects tab.
 *
 * Gets its own [HomeViewModel] via Hilt, renders the search bar, project grid,
 * empty states, and FAB. Dialogs and the context menu are managed via local
 * state; data operations flow through [HomeViewModel.onIntent].
 *
 * @param onNavigateToWizard Navigation callback for New Installation projects.
 * @param onNavigateToDashboard Navigation callback for Existing Assessment projects.
 */
@Composable
fun HomeScreen(
    onNavigateToWizard: (projectId: String) -> Unit,
    onNavigateToDashboard: (projectId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val spacing = LocalSpacing.current

    var showNewProjectDialog by remember { mutableStateOf(false) }
    var editProject by remember { mutableStateOf<Project?>(null) }
    var deleteProject by remember { mutableStateOf<Project?>(null) }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { nav ->
            when (nav) {
                is HomeNavigation.ToWizard -> onNavigateToWizard(nav.projectId)
                is HomeNavigation.ToDashboard -> onNavigateToDashboard(nav.projectId)
            }
        }
    }

    if (showNewProjectDialog) {
        NewProjectDialog(
            onDismiss = { showNewProjectDialog = false },
            onConfirm = { name, type, location ->
                showNewProjectDialog = false
                viewModel.onIntent(HomeIntent.CreateProject(name, type, location))
            },
        )
    }

    editProject?.let { project ->
        EditProjectDialog(
            project = project,
            onDismiss = { editProject = null },
            onConfirm = { name, location ->
                editProject = null
                viewModel.onIntent(HomeIntent.EditProject(project.id, name, location))
            },
        )
    }

    deleteProject?.let { project ->
        DeleteConfirmationDialog(
            projectName = project.name,
            onConfirm = {
                deleteProject = null
                viewModel.onIntent(HomeIntent.DeleteProject(project.id))
            },
            onDismiss = { deleteProject = null },
        )
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showNewProjectDialog = true },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.cd_fab_new_project),
                    )
                },
                text = { Text(stringResource(R.string.home_new_project)) },
            )
        },
    ) { innerPadding ->
        when (val state = uiState) {
            is HomeUiState.Loading ->
                LoadingContent(
                    modifier = Modifier.padding(innerPadding),
                )

            is HomeUiState.Empty ->
                EmptyContent(
                    onNewProject = { showNewProjectDialog = true },
                    modifier = Modifier.padding(innerPadding),
                )

            is HomeUiState.Content -> {
                Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    ProjectSearchBar(
                        query = state.searchQuery,
                        onQueryChange = { viewModel.onIntent(HomeIntent.UpdateSearchQuery(it)) },
                    )
                    if (state.noSearchResults) {
                        NoSearchResultsContent(modifier = Modifier.fillMaxSize())
                    } else {
                        ProjectBentoGrid(
                            projects = state.projects,
                            onProjectClick = { project ->
                                viewModel.onIntent(HomeIntent.OpenProject(project.id))
                            },
                            onEdit = { editProject = it },
                            onDuplicate = { project ->
                                viewModel.onIntent(HomeIntent.DuplicateProject(project.id))
                            },
                            onDelete = { deleteProject = it },
                            onNewProject = { showNewProjectDialog = true },
                        )
                    }
                }
            }

            is HomeUiState.Error ->
                ErrorContent(
                    message = state.message,
                    modifier = Modifier.padding(innerPadding),
                )
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyContent(
    onNewProject: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        NewProjectPlaceholderCard(onClick = onNewProject)
        Text(
            text = stringResource(R.string.home_empty_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = spacing.lg),
        )
        Text(
            text = stringResource(R.string.home_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = spacing.sm),
        )
    }
}

@Composable
private fun NoSearchResultsContent(modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.home_search_no_results),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(spacing.xl),
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(spacing.xl),
        )
    }
}

@Composable
private fun ProjectBentoGrid(
    projects: List<Project>,
    onProjectClick: (Project) -> Unit,
    onEdit: (Project) -> Unit,
    onDuplicate: (Project) -> Unit,
    onDelete: (Project) -> Unit,
    onNewProject: () -> Unit,
) {
    val spacing = LocalSpacing.current
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(MinColumnWidth),
        modifier = Modifier.fillMaxSize(),
        contentPadding =
            androidx.compose.foundation.layout.PaddingValues(
                horizontal = spacing.md,
                vertical = spacing.sm,
            ),
        verticalItemSpacing = spacing.sm,
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        item {
            NewProjectPlaceholderCard(onClick = onNewProject)
        }
        if (projects.isNotEmpty()) {
            item {
                PerformanceSummaryCard(
                    onOpenReports = { onProjectClick(projects.first()) },
                )
            }
        }
        items(
            items = projects,
            key = { it.id },
        ) { project ->
            ProjectGridItem(
                project = project,
                onClick = { onProjectClick(project) },
                onEdit = { onEdit(project) },
                onDuplicate = { onDuplicate(project) },
                onDelete = { onDelete(project) },
            )
        }
    }
}

@Composable
private fun ProjectGridItem(
    project: Project,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box {
        ProjectCard(
            project = project,
            onClick = onClick,
            onLongClick = { menuExpanded = true },
        )
        ProjectContextMenu(
            expanded = menuExpanded,
            onDismiss = { menuExpanded = false },
            onEdit = onEdit,
            onDuplicate = onDuplicate,
            onDelete = onDelete,
        )
    }
}

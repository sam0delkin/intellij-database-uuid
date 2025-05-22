package com.github.sam0delkin.intellijdatabaseuuid.dataConsumer

import com.github.sam0delkin.intellijdatabaseuuid.configurable.Settings
import com.intellij.database.console.JdbcConsole
import com.intellij.database.datagrid.*
import com.intellij.database.datagrid.DataRequest.TxContext
import com.intellij.database.editor.DatabaseTableFileEditor
import com.intellij.database.run.ui.grid.GridMainPanel
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager


class MyDataConsumer : DataConsumer {
    override fun afterLastRowAdded(context: GridDataRequest.Context, total: Int) {
        var settings: Settings? = null
        if (context is TxContext) {
            context.request.promise.onSuccess {
                val owner = context.request.owner
                var grid: DataGrid? = null

                if (owner is JdbcConsole) {
                    settings = owner.project.service<Settings>()
                    owner.contents.forEach {
                        if (it.component is GridMainPanel) {
                            grid = (it.component as GridMainPanel).grid
                        }
                    }
                }

                if (owner is DataGridSessionClient) {
                    val view = owner.view
                    val editor = FileEditorManager.getInstance(view.project).selectedEditor
                    settings = view.project.service<Settings>()
                    if (editor is DatabaseTableFileEditor) {
                        grid = editor.dataGrid
                    }
                }

                if (null != grid && null != settings && settings!!.pluginEnabled) {
                    grid!!.visibleColumns.asList().map {
                        val displayType = grid!!.getDisplayType(it)
                        if (displayType == settings!!.fromType && null != settings!!.toType) {
                            grid!!.setDisplayType(it, settings!!.toType!!)
                        }
                    }
                }
            }
        }
    }
}
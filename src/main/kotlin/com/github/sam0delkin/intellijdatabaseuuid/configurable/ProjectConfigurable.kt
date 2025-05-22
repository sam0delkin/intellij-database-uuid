package com.github.sam0delkin.intellijdatabaseuuid.configurable

import com.intellij.database.extractors.BinaryDisplayType
import com.intellij.database.extractors.DisplayType
import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import javax.swing.ComboBoxModel
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.event.ListDataListener

class ProjectConfigurable(
    private val project: Project,
) : Configurable {
    private lateinit var enabled: Cell<JBCheckBox>
    private lateinit var fromType: Cell<JComboBox<DisplayType>>
    private lateinit var toType: Cell<JComboBox<DisplayType>>

    private class DisplayTypeComboBoxModel : ComboBoxModel<DisplayType> {
        private val values = arrayOf(
            BinaryDisplayType.UUID,
            BinaryDisplayType.UUID_SWAP,
            BinaryDisplayType.TEXT,
            BinaryDisplayType.HEX,
            BinaryDisplayType.HEX_ASCII
        )
        private var selectedItem: DisplayType? = null

        override fun getSize() = values.size
        override fun getElementAt(index: Int) = values[index]
        override fun addListDataListener(l: ListDataListener?) {
        }

        override fun removeListDataListener(l: ListDataListener?) {
        }

        override fun setSelectedItem(anItem: Any?) {
            this.selectedItem = anItem as DisplayType
        }

        override fun getSelectedItem(): Any? {
            return selectedItem
        }
    }

    override fun createComponent(): JComponent {
        val panel =
            panel {
                row("Plugin Enabled") {
                    enabled = checkBox("")
                }
                row("From Type") {
                    fromType = comboBox(DisplayTypeComboBoxModel())
                }
                row("To Type") {
                    toType = comboBox(DisplayTypeComboBoxModel())
                }
            }

        return panel
    }

    override fun isModified(): Boolean {
        return settings.pluginEnabled != this.enabled.component.isSelected
                || settings.fromType != this.fromType.component.selectedItem
                || settings.toType != this.toType.component.selectedItem
    }

    override fun apply() {
        settings.pluginEnabled = this.enabled.component.isSelected
        settings.fromType = this.fromType.component.selectedItem as DisplayType?
        settings.toType = this.toType.component.selectedItem as DisplayType?
    }

    override fun reset() {
        this.enabled.component.setSelected(settings.pluginEnabled)
        this.fromType.component.selectedItem = settings.fromType
        this.toType.component.selectedItem = settings.toType
    }

    override fun getDisplayName(): String {
        return "Intellij UUID"
    }

    private val settings: Settings
        get() = project.service<Settings>()
}
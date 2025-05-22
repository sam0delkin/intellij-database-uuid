package com.github.sam0delkin.intellijdatabaseuuid.configurable

import com.intellij.database.extractors.BinaryDisplayType
import com.intellij.database.extractors.DisplayType
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@Service(Service.Level.PROJECT)
@State(
    name = "IntellijUuid",
    storages = [Storage("intellij_uuid.xml")],
)
class Settings : PersistentStateComponent<Settings> {
    var pluginEnabled: Boolean = false
    var fromType: DisplayType? = BinaryDisplayType.UUID_SWAP
    var toType: DisplayType? = BinaryDisplayType.UUID

    @Override
    override fun getState(): Settings = this

    override fun loadState(settings: Settings) {
        XmlSerializerUtil.copyBean(settings, this)
    }
}
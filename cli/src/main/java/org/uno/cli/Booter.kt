/*
 * Copyright (C) 2020  Fábio Furtado
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.uno.cli

import org.uno.data.ConfigReader
import org.uno.data.SystemInfo
import org.uno.data.SystemInfoUnix
import org.uno.data.SystemInfoWindows

/**
 * This class is responsible for booting the cli.
 *
 * @author Fábio Furtado
 */
object Booter {

    private const val DEFAULT_USER_NAME = "player"

    /**
     * Boots the cli.
     */
    @JvmStatic
    fun boot() {
        val userName: String
        val system: SystemInfo
        val configFileUserName = ConfigReader.get("user", "name") as String?
        val os = os
        when {
            configFileUserName != null -> {
                userName = configFileUserName
            }
            os == OS.UNIX -> {
                system = SystemInfoUnix()
                userName = system.getUserName()
            }
            os == OS.WINDOWS -> {
                system = SystemInfoWindows()
                userName = system.getUserName()
            }
            else -> userName = DEFAULT_USER_NAME
        }
        val cli = CLI(userName)
        cli.start()
    }

    private val os: OS?
        get() {
            val osName = System.getProperty("os.name").toLowerCase()
            val unix = arrayOf("linux", "mac", "bsd")
            if (osName.contains("windows"))
                return OS.WINDOWS
            for (name in unix) {
                if (name == osName) return OS.UNIX
            }
            return null
        }

    private enum class OS {
        UNIX, WINDOWS
    }
}
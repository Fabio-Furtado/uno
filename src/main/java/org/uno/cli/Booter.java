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

package org.uno.cli;

import org.uno.data.ConfigReader;
import org.uno.data.SystemInfo;
import org.uno.data.SystemInfoUnix;
import org.uno.data.SystemInfoWindows;


/**
 * This class is responsible for booting the cli.
 *
 * @author Fábio Furtado 
 */
public final class Booter {

    private static final String DEFAULT_USER_NAME = "player";

    private enum OS {
        UNIX, WINDOWS
    }

    /**
     * Boots the cli
     */
    public static void boot() {
        final String userName;
        final SystemInfo system;
        String configFileUserName;
        CLI cli = new CLI();
        configFileUserName = (String) ConfigReader.get("user", "name");
        OS os = getOS();

        if (configFileUserName != null) {
            userName = configFileUserName;
        } else if (os == OS.UNIX) {
            system = new SystemInfoUnix();
            userName = system.getUserName();
        } else if (os == os.WINDOWS) {
            system = new SystemInfoWindows();
            userName = system.getUserName();
        } else
            userName = DEFAULT_USER_NAME;
        cli.start(userName);
    }

    private static OS getOS() {
        final String osName = System.getProperty("os.name").toLowerCase();
        final String[] unix = new String[] {"linux", "mac", "bsd"};

        if (osName.contains("windows"))
            return OS.WINDOWS;

        for (String name : unix) {
            if (name.equals(osName))
                return OS.UNIX;
        }
        return null;
    }

}

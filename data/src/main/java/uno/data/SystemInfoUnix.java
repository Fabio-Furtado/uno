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

package uno.data;


/**
 * This class should be used to store useful information about the running
 * unix-like System. It is a singleton.
 */
public class SystemInfoUnix implements SystemInfo {

    private final String userName;
    private final String userHome;
    private final String userConfigHome;
    private final String programConfigHome;
    private static final SystemInfo instance = new SystemInfoUnix();


    /**
     * Creates a new instance.
     */
    private SystemInfoUnix() {
        this.userName = System.getenv("USER");
        this.userHome = System.getProperty("user.home");
        String customConfigHome = System.getenv("XDG_CONFIG_HOME");
        userConfigHome = customConfigHome != null? customConfigHome : userHome;
        String programConfigFolderName = "uno";
        programConfigHome = userConfigHome + "/" + programConfigFolderName;
    }

    /**
     * Returns the singleton instance.
     */
    public static SystemInfo getInstance() {
        return instance;
    }

    /**
     * @see SystemInfo#getUserName()
     */
    @Override
    public String getUserName() {
        return userName;
    }

    /**
     * @see SystemInfo#getUserHome()
     */
    @Override
    public String getUserHome() {
        return userHome;
    }

    /**
     * @see SystemInfo#getUserConfigHome()
     */
    @Override
    public String getUserConfigHome() {
        return userConfigHome;
    }

    /**
     * @see SystemInfo#getProgramConfigHome()
     */
    @Override
    public String getProgramConfigHome() {
        return programConfigHome;
    }


}

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
 * windows System.
 */
@Deprecated
public class SystemInfoWindows implements SystemInfo {

    private final String userName;

    private static final SystemInfo instance = new SystemInfoWindows();

    /**
     * Creates a new instance.
     */
    private SystemInfoWindows() {
        this.userName = System.getenv("USERNAME");
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
        // TODO("To be implemented")
        return null;
    }

    /**
     * @see SystemInfo#getUserConfigHome()
     */
    @Override
    public String getUserConfigHome() {
        // TODO("To be implemented")
        return null;
    }

    /**
     * @see SystemInfo#getProgramConfigHome()
     */
    @Override
    public String getProgramConfigHome() {
        // TODO("To be implemented")
        return null;
    }
}

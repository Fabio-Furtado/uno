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

public interface SystemInfo {

    /**
     * Returns the name of the current system user.
     */
    String getUserName();

    /**
     * Returns the home folder for the current system user.
     */
    String getUserHome();

    /**
     * Returns the config home for the current system user.
     */
    String getUserConfigHome();

    /**
     * Returns the folder where the program stores it's config files.
     */
    String getProgramConfigHome();
}

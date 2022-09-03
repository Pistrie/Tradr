/*
 * Tradr
 * Copyright (c) 2022-2022, Sylvester Roos <pistrie@duck.com>
 *
 * Tradr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Tradr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Tradr.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.yabs.tradr.utils;

public class CheckIban {
    public static boolean checkIban(String iban) {
        return iban.matches("^NL[0-9]{2} ?[A-Z0-9]{4} ?[0-9]{4} ?[0-9]{4} ?[0-9]{2}$");
    }
}

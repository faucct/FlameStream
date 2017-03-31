/**
 * This file is part of Wikiforia.
 *
 * Wikiforia is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Wikiforia is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Wikiforia. If not, see <http://www.gnu.org/licenses/>.
 */
 package nlp.wikipedia.lang;

//Autogenerated from Wikimedia sources at 2015-04-16T13:55:11+00:00

public class KgConfig extends TemplateConfig {
	public KgConfig() {
		addNamespaceAlias(1, "Disolo");
		addNamespaceAlias(2, "Kisadi");
		addNamespaceAlias(3, "Disolo_kisadi");
		addNamespaceAlias(5, "Disolo_Wikipedia");
		addNamespaceAlias(6, "Fisye");
		addNamespaceAlias(7, "Disolo_fisye");
		addNamespaceAlias(12, "Lusadisu");
		addNamespaceAlias(13, "Disolo_lusadisu");
		addNamespaceAlias(14, "Kalasi");
		addNamespaceAlias(15, "Disolo_kalasi");

	}

	@Override
	protected String getSiteName() {
		return "Wikipedia";
	}

	@Override
	protected String getWikiUrl() {
		return "http://kg.wikipedia.org/";
	}

	@Override
	public String getIso639() {
		return "kg";
	}
}
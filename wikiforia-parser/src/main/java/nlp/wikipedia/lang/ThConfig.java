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

public class ThConfig extends TemplateConfig {
	public ThConfig() {
		addNamespaceAlias(-2, "สื่อ");
		addNamespaceAlias(-1, "พิเศษ");
		addNamespaceAlias(1, "พูดคุย");
		addNamespaceAlias(2, "ผู้ใช้");
		addNamespaceAlias(3, "คุยกับผู้ใช้");
		addNamespaceAlias(5, "คุยเรื่องWikipedia");
		addNamespaceAlias(6, "ไฟล์", "ภาพ");
		addNamespaceAlias(7, "คุยเรื่องไฟล์", "คุยเรื่องภาพ");
		addNamespaceAlias(8, "มีเดียวิกิ");
		addNamespaceAlias(9, "คุยเรื่องมีเดียวิกิ");
		addNamespaceAlias(10, "แม่แบบ");
		addNamespaceAlias(11, "คุยเรื่องแม่แบบ");
		addNamespaceAlias(12, "วิธีใช้");
		addNamespaceAlias(13, "คุยเรื่องวิธีใช้");
		addNamespaceAlias(14, "หมวดหมู่");
		addNamespaceAlias(15, "คุยเรื่องหมวดหมู่");

		addI18nCIAlias("redirect", "#เปลี่ยนทาง", "#REDIRECT");
		addI18nCIAlias("notoc", "__ไม่มีสารบัญ__", "__NOTOC__");
		addI18nCIAlias("nogallery", "__ไม่มีแกลเลอรี่__", "__NOGALLERY__");
		addI18nCIAlias("noeditsection", "__ไม่มีแก้เฉพาะส่วน__", "__NOEDITSECTION__");
		addI18nAlias("currentmonth", "เดือนปัจจุบัน", "CURRENTMONTH", "CURRENTMONTH2");
		addI18nAlias("currentmonthname", "ชื่อเดือนปัจจุบัน", "CURRENTMONTHNAME");
	}

	@Override
	protected String getSiteName() {
		return "Wikipedia";
	}

	@Override
	protected String getWikiUrl() {
		return "http://th.wikipedia.org/";
	}

	@Override
	public String getIso639() {
		return "th";
	}
}
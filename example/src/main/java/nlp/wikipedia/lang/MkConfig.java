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

public class MkConfig extends TemplateConfig {
	public MkConfig() {
		addNamespaceAlias(-2, "Медиум", "Медија");
		addNamespaceAlias(-1, "Специјална", "Специјални");
		addNamespaceAlias(1, "Разговор");
		addNamespaceAlias(2, "Корисник");
		addNamespaceAlias(3, "Разговор_со_корисник");
		addNamespaceAlias(5, "Разговор_за_Wikipedia");
		addNamespaceAlias(6, "Податотека", "Слика");
		addNamespaceAlias(7, "Разговор_за_податотека", "Разговор_за_слика");
		addNamespaceAlias(8, "МедијаВики");
		addNamespaceAlias(9, "Разговор_за_МедијаВики");
		addNamespaceAlias(10, "Шаблон");
		addNamespaceAlias(11, "Разговор_за_шаблон");
		addNamespaceAlias(12, "Помош");
		addNamespaceAlias(13, "Разговор_за_помош");
		addNamespaceAlias(14, "Категорија");
		addNamespaceAlias(15, "Разговор_за_категорија");

		addI18nCIAlias("redirect", "#пренасочување", "#види", "#Пренасочување", "#ПРЕНАСОЧУВАЊЕ", "#REDIRECT");
		addI18nCIAlias("notoc", "__БЕЗСОДРЖИНА__", "__NOTOC__");
		addI18nCIAlias("nogallery", "__БЕЗГАЛЕРИЈА__", "__NOGALLERY__");
		addI18nCIAlias("forcetoc", "__СОСОДРЖИНА__", "__FORCETOC__");
		addI18nCIAlias("toc", "__СОДРЖИНА__", "__TOC__");
		addI18nCIAlias("noeditsection", "__БЕЗ_УРЕДУВАЊЕ_НА_ПОДНАСЛОВИ__", "__NOEDITSECTION__");
		addI18nAlias("currentmonth", "ТЕКОВЕНМЕСЕЦ", "CURRENTMONTH", "CURRENTMONTH2");
		addI18nAlias("currentmonth1", "ТЕКОВЕНМЕСЕЦ1", "CURRENTMONTH1");
		addI18nAlias("currentmonthname", "ТЕКОВЕНМЕСЕЦИМЕ", "CURRENTMONTHNAME");
		addI18nAlias("currentmonthnamegen", "ТЕКОВЕНМЕСЕЦИМЕРОД", "CURRENTMONTHNAMEGEN");
		addI18nAlias("currentmonthabbrev", "ТЕКОВЕНМЕСЕЦСКР", "CURRENTMONTHABBREV");
		addI18nAlias("currentday", "ТЕКОВЕНДЕН", "CURRENTDAY");
		addI18nAlias("currentday2", "ТЕКОВЕНДЕН2", "CURRENTDAY2");
		addI18nAlias("currentdayname", "ТЕКОВЕНДЕНИМЕ", "CURRENTDAYNAME");
		addI18nAlias("currentyear", "ТЕКОВНАГОДИНА", "CURRENTYEAR");
		addI18nAlias("currenttime", "ТЕКОВНОВРЕМЕ", "CURRENTTIME");
		addI18nAlias("currenthour", "ТЕКОВЕНЧАС", "CURRENTHOUR");
		addI18nAlias("localmonth", "МЕСЕЦ_ЛОКАЛНО", "LOCALMONTH", "LOCALMONTH2");
		addI18nAlias("localmonth1", "МЕСЕЦ_ЛОКАЛНО1", "LOCALMONTH1");
		addI18nAlias("localmonthname", "МЕСЕЦИМЕ_ЛОКАЛНО", "LOCALMONTHNAME");
		addI18nAlias("localmonthnamegen", "МЕСЕЦИМЕ_ЛОКАЛНО_ГЕНИТИВ", "LOCALMONTHNAMEGEN");
		addI18nAlias("localmonthabbrev", "МЕСЕЦИМЕ_ЛОКАЛНО_КРАТЕНКА", "LOCALMONTHABBREV");
		addI18nAlias("localday", "ДЕН_ЛОКАЛНО", "LOCALDAY");
		addI18nAlias("localday2", "ДЕН2_ЛОКАЛНО", "LOCALDAY2");
		addI18nAlias("localdayname", "ИМЕНАДЕН_ЛОКАЛНО", "LOCALDAYNAME");
		addI18nAlias("localyear", "ГОДИНА_ЛОКАЛНО", "LOCALYEAR");
		addI18nAlias("localtime", "ВРЕМЕ_ЛОКАЛНО", "LOCALTIME");
		addI18nAlias("localhour", "ЧАС_ЛОКАЛНО", "LOCALHOUR");
		addI18nAlias("numberofpages", "БРОЈНАСТРАНИЦИ", "NUMBEROFPAGES");
		addI18nAlias("numberofarticles", "БРОЈСТАТИИ", "БРОЈНАСТАТИИ", "NUMBEROFARTICLES");
		addI18nAlias("numberoffiles", "БРОЈНАПОДАТОТЕКИ", "NUMBEROFFILES");
		addI18nAlias("numberofusers", "БРОЈНАКОРИСНИЦИ", "NUMBEROFUSERS");
		addI18nAlias("numberofactiveusers", "БРОЈНААКТИВНИКОРИСНИЦИ", "NUMBEROFACTIVEUSERS");
		addI18nAlias("numberofedits", "БРОЈНАУРЕДУВАЊА", "NUMBEROFEDITS");
		addI18nAlias("numberofviews", "БРОЈНАПРЕГЛЕДУВАЊА", "NUMBEROFVIEWS");
		addI18nAlias("pagename", "СТРАНИЦА", "PAGENAME");
		addI18nAlias("pagenamee", "СТРАНИЦАИ", "PAGENAMEE");
		addI18nAlias("namespace", "ИМЕПРОСТОР", "ИМЕНСКИПРОСТОР", "NAMESPACE");
		addI18nAlias("talkspace", "РАЗГОВОРПРОСТОР", "TALKSPACE");
		addI18nAlias("fullpagename", "ЦЕЛОСНОИМЕНАСТРАНИЦА", "FULLPAGENAME");
		addI18nAlias("subpagename", "ПОТСТРАНИЦА", "SUBPAGENAME");
		addI18nAlias("basepagename", "ОСНОВНАСТРАНИЦА", "BASEPAGENAME");
		addI18nAlias("talkpagename", "СТРАНИЦАЗАРАЗГОВОР", "TALKPAGENAME");
		addI18nAlias("subjectpagename", "ИМЕНАСТАТИЈА", "SUBJECTPAGENAME", "ARTICLEPAGENAME");
		addI18nCIAlias("msg", "ПОР:", "MSG:");
		addI18nCIAlias("subst", "ЗАМЕНИ:", "SUBST:");
		addI18nCIAlias("safesubst", "БЕЗБЗАМЕНИ", "SAFESUBST:");
		addI18nCIAlias("msgnw", "ИЗВЕШТNW:", "MSGNW:");
		addI18nAlias("img_thumbnail", "мини", "мини-слика", "thumbnail", "thumb");
		addI18nAlias("img_manualthumb", "мини-слика=$1", "мини=$1", "thumbnail=$1", "thumb=$1");
		addI18nAlias("img_right", "десно", "д", "right");
		addI18nAlias("img_left", "лево", "л", "left");
		addI18nAlias("img_none", "н", "нема", "none");
		addI18nAlias("img_width", "$1пкс", "$1п", "$1px");
		addI18nAlias("img_center", "центар", "ц", "center", "centre");
		addI18nAlias("img_framed", "рамка", "ворамка", "framed", "enframed", "frame");
		addI18nAlias("img_frameless", "безрамка", "frameless");
		addI18nAlias("img_lang", "јаз=$1", "lang=$1");
		addI18nAlias("img_page", "страница=$1", "страница_$1", "page=$1", "page $1");
		addI18nAlias("img_upright", "исправено", "исправено=$1", "исправено_$1", "upright", "upright=$1", "upright $1");
		addI18nAlias("img_border", "граничник", "граница", "border");
		addI18nAlias("img_baseline", "основналинија", "baseline");
		addI18nAlias("img_sub", "долениндекс", "дол", "sub");
		addI18nAlias("img_super", "горениндекс", "гор", "super", "sup");
		addI18nAlias("img_top", "врв", "најгоре", "top");
		addI18nAlias("img_text_top", "текст-врв", "текст-најгоре", "text-top");
		addI18nAlias("img_middle", "средина", "middle");
		addI18nAlias("img_bottom", "дно", "најдолу", "bottom");
		addI18nAlias("img_text_bottom", "текст-дно", "текст-најдолу", "text-bottom");
		addI18nAlias("img_link", "врска=$1", "link=$1");
		addI18nAlias("img_alt", "алт=$1", "alt=$1");
		addI18nAlias("img_class", "класа=$1", "class=$1");
		addI18nAlias("sitename", "ИМЕНАМРЕЖНОМЕСТО", "SITENAME");
		addI18nCIAlias("localurl", "ЛОКАЛНААДРЕСА:", "LOCALURL:");
		addI18nCIAlias("localurle", "ЛОКАЛНААДРЕСАИ:", "LOCALURLE:");
		addI18nCIAlias("articlepath", "ПАТЕКАНАСТАТИЈА", "ARTICLEPATH");
		addI18nCIAlias("pageid", "НАЗНАКАНАСТРАНИЦА", "PAGEID");
		addI18nCIAlias("server", "ОПСЛУЖУВАЧ", "SERVER");
		addI18nCIAlias("servername", "ИМЕНАОПСЛУЖУВАЧ", "SERVERNAME");
		addI18nCIAlias("scriptpath", "ПАТЕКАНАСКРИПТА", "SCRIPTPATH");
		addI18nCIAlias("stylepath", "СТИЛСКАПАТЕКА", "STYLEPATH");
		addI18nCIAlias("grammar", "ГРАМАТИКА:", "GRAMMAR:");
		addI18nCIAlias("gender", "ПОЛ:", "GENDER:");
		addI18nCIAlias("notitleconvert", "__БЕЗПРЕТВОРАЊЕНАСЛОВ__", "__NOTITLECONVERT__", "__NOTC__");
		addI18nCIAlias("nocontentconvert", "__БЕЗПРЕТВОРАЊЕСОДРЖИНА__", "__NOCONTENTCONVERT__", "__NOCC__");
		addI18nAlias("currentweek", "ТЕКОВНАСЕДМИЦА", "CURRENTWEEK");
		addI18nAlias("currentdow", "ТЕКОВЕНДЕНВОСЕДМИЦАТА", "CURRENTDOW");
		addI18nAlias("localweek", "СЕДМИЦА_ЛОКАЛНО", "LOCALWEEK");
		addI18nAlias("localdow", "ЛОКАЛЕНДЕНВОСЕДМИЦАТА", "LOCALDOW");
		addI18nAlias("revisionid", "НАЗНАКАНАРЕВИЗИЈА", "REVISIONID");
		addI18nAlias("revisionday", "ДЕННАРЕВИЗИЈА", "REVISIONDAY");
		addI18nAlias("revisionday2", "ДЕННАРЕВИЗИЈА2", "REVISIONDAY2");
		addI18nAlias("revisionmonth", "МЕСЕЦНАРЕВИЗИЈА", "REVISIONMONTH");
		addI18nAlias("revisionmonth1", "МЕСЕЦНАРЕВИЗИЈА1", "REVISIONMONTH1");
		addI18nAlias("revisionyear", "ГОДИНАНАРЕВИЗИЈА", "REVISIONYEAR");
		addI18nAlias("revisiontimestamp", "ВРЕМЕНАРЕВИЗИЈА", "REVISIONTIMESTAMP");
		addI18nAlias("revisionuser", "КОРИСНИКНАНАРЕВИЗИЈА", "REVISIONUSER");
		addI18nAlias("revisionsize", "ГОЛЕМИНАНАРЕВИЗИЈА", "REVISIONSIZE");
		addI18nCIAlias("plural", "МНОЖИНА:", "PLURAL:");
		addI18nCIAlias("fullurl", "ПОЛНАURL:", "FULLURL:");
		addI18nCIAlias("fullurle", "ПОЛНАURLE:", "FULLURLE:");
		addI18nCIAlias("canonicalurl", "КАНОНСКАURL:", "CANONICALURL:");
		addI18nCIAlias("canonicalurle", "КАНОНСКАURLE:", "CANONICALURLE:");
		addI18nCIAlias("lcfirst", "ПРВОМБ", "LCFIRST:");
		addI18nCIAlias("ucfirst", "ПРВОГБ", "UCFIRST:");
		addI18nCIAlias("lc", "МБ", "LC:");
		addI18nCIAlias("uc", "ГБ", "UC:");
		addI18nCIAlias("raw", "СИРОВО:", "RAW:");
		addI18nAlias("displaytitle", "ПРИКАЖИНАСЛОВ", "DISPLAYTITLE");
		addI18nAlias("rawsuffix", "П", "R");
		addI18nCIAlias("nocommafysuffix", "БЕЗПОДЕЛ", "NOSEP");
		addI18nAlias("newsectionlink", "__ВРСКАНОВПОДНАСЛОВ__", "__NEWSECTIONLINK__");
		addI18nAlias("nonewsectionlink", "__БЕЗВРСКАНОВПОДНАСЛОВ__", "__NONEWSECTIONLINK__");
		addI18nAlias("currentversion", "ТЕКОВНАВЕРЗИЈА", "CURRENTVERSION");
		addI18nCIAlias("urlencode", "ШИФРИРАЈURL:", "URLENCODE:");
		addI18nCIAlias("anchorencode", "ШИФРИРАЈКОТВА", "ANCHORENCODE");
		addI18nAlias("currenttimestamp", "ОЗНАЧЕНОТЕКОВНОВРЕМЕ", "CURRENTTIMESTAMP");
		addI18nAlias("localtimestamp", "ОЗНАЧЕНОЛОКАЛНОВРЕМЕ", "LOCALTIMESTAMP");
		addI18nAlias("directionmark", "ОЗНАКАЗАНАСОКА", "DIRECTIONMARK", "DIRMARK");
		addI18nCIAlias("language", "#ЈАЗИК:", "#LANGUAGE:");
		addI18nAlias("contentlanguage", "ЈАЗИКНАСОДРЖИНАТА", "CONTENTLANGUAGE", "CONTENTLANG");
		addI18nAlias("pagesinnamespace", "СТРАНИЦИВОИМЕНСКИПРОСТОР", "PAGESINNAMESPACE:", "PAGESINNS:");
		addI18nAlias("numberofadmins", "БРОЈНААДМИНИСТРАТОРИ", "NUMBEROFADMINS");
		addI18nCIAlias("formatnum", "ФОРМАТБРОЈ", "FORMATNUM");
		addI18nCIAlias("padleft", "ПОСТАВИЛЕВО", "PADLEFT");
		addI18nCIAlias("padright", "ПОСТАВИДЕСНО", "PADRIGHT");
		addI18nCIAlias("special", "специјална", "специјални", "special");
		addI18nAlias("defaultsort", "ОСНОВНОПОДРЕДУВАЊЕ:", "DEFAULTSORT:", "DEFAULTSORTKEY:", "DEFAULTCATEGORYSORT:");
		addI18nCIAlias("filepath", "ПОДАТОТЕЧНАПАТЕКА:", "FILEPATH:");
		addI18nCIAlias("tag", "ознака", "tag");
		addI18nAlias("hiddencat", "__СКРИЕНАКАТ__", "__СКРИЕНАКАТЕГОРИЈА__", "__HIDDENCAT__");
		addI18nAlias("pagesincategory", "СТРАНИЦИВОКАТЕГОРИЈА", "СТРАНИЦИВОКАТ", "PAGESINCATEGORY", "PAGESINCAT");
		addI18nAlias("pagesize", "ГОЛЕМИНА_НА_СТРАНИЦА", "PAGESIZE");
		addI18nAlias("index", "__ИНДЕКС__", "__INDEX__");
		addI18nAlias("noindex", "__БЕЗИНДЕКС__", "__NOINDEX__");
		addI18nAlias("numberingroup", "БРОЈВОГРУПА", "NUMBERINGROUP", "NUMINGROUP");
		addI18nAlias("staticredirect", "__СТАТИЧНОПРЕНАСОЧУВАЊЕ__", "__STATICREDIRECT__");
		addI18nAlias("protectionlevel", "НИВОНАЗАШТИТА", "PROTECTIONLEVEL");
		addI18nCIAlias("formatdate", "форматдатум", "formatdate", "dateformat");
		addI18nCIAlias("url_path", "ПАТЕКА", "PATH");
		addI18nCIAlias("url_wiki", "ВИКИ", "WIKI");
		addI18nCIAlias("url_query", "БАРАЊЕ", "QUERY");
		addI18nCIAlias("defaultsort_noerror", "безгрешки", "noerror");
		addI18nCIAlias("defaultsort_noreplace", "беззамена", "noreplace");
		addI18nCIAlias("pagesincategory_all", "сите", "all");
		addI18nCIAlias("pagesincategory_pages", "страници", "pages");
		addI18nCIAlias("pagesincategory_subcats", "поткатегории", "subcats");
		addI18nCIAlias("pagesincategory_files", "податотеки", "files");
	}

	@Override
	protected String getSiteName() {
		return "Wikipedia";
	}

	@Override
	protected String getWikiUrl() {
		return "http://mk.wikipedia.org/";
	}

	@Override
	public String getIso639() {
		return "mk";
	}
}
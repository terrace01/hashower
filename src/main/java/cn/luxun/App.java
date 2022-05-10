package cn.luxun;

import cn.luxun.config.GitHubApiManage;
import cn.luxun.utils.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

public class App {

	public static void main(String[] args) throws Exception {


		GitHubApiManage config = new GitHubApiManage();
		config.setJsonArray(JSONObject.parseArray(HttpUtil.get(config.getApiUrl() + config.getStartred())));

		genrateHtml();


	}

	/**
	 * 生成HTML
	 *
	 * @throws Exception
	 */
	public static void genrateHtml() throws Exception {

		GitHubApiManage config = new GitHubApiManage();
		config.setJsonArray(JSONObject.parseArray(HttpUtil.get(config.getApiUrl() + config.getStartred())));

		String repos = getRepos();
		String topStarred = getTopStarred(config.getJsonArray());
		String randomStarred = getRandStarred(config.getJsonArray());

		String text = "";
		try (Scanner sc = new Scanner(new FileReader("readme.template.md"))) {
			while (sc.hasNextLine()) {  //按行读取字符串
				String line = sc.nextLine();

				String table = "<table>" + "<thead align=\"center\">" + "<tr border: none;>" + "<td><b>\uD83C\uDF81 Projects</b></td>" + "<td><b>⭐ Stars</b></td>" + "<td><b>\uD83D\uDCDA Forks</b></td>" + "<td><b>\uD83D\uDECE Issues</b></td>" + "<td><b>\uD83D\uDCEC Pull requests</b></td>" + "<td><b>\uD83D\uDCA1 Last Commit</b></td>" + "</tr>" + "</thead>" + "<tbody>" + repos + "</tbody>" + "</table>";
				DateTimeFormatter fmt = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd/MM/yy hh:mm a").toFormatter(Locale.US);

				LocalDateTime date = LocalDateTime.now();

				String updateTime = "<p align=center>此文件" + " <i>README</i> <b>间隔 3 小时</b>" + "自动刷新生成！<br>刷新于：" + fmt.format(date) + "<br>下一次刷新：" + fmt.format(date.plusHours(3)) + "</p>";
				line = line.replace("<!-- opensource_dashboard:active -->", table);
				line = line.replace("<!-- update_time -->", updateTime);
				line = line.replace("<!-- recent_star_inject -->", topStarred);
				line = line.replace("<!-- random_github_stars_inject -->", randomStarred);

				text += line + "\n";
			}
		}
		writeFile(text);
	}

	/**
	 * 获取最近点赞项目并返回HTML
	 *
	 * @return
	 * @throws Exception
	 */
	public static String getTopStarred(JSONArray jsonArray) throws Exception {
		return generateStarredHtml(jsonArray);
	}

	/**
	 * 获取随机点赞项目并返回HTML
	 *
	 * @return
	 * @throws Exception
	 */
	public static String getRandStarred(JSONArray jsonArray) throws Exception {
		List<Integer> index = new ArrayList<Integer>();
		JSONArray jsonArray1 = new JSONArray();
		String text = "";
		for (int i = 0; i < 20; i++) {
			int random = (int) (Math.random() * jsonArray.size());
			if (!index.contains(random)) {
				index.add(random);
				text = text + jsonArray.get(random);
			}
			if (index.size() == 5) {
				break;
			}

		}
		text = "[" + text + "]";
		return generateStarredHtml(JSONObject.parseArray(text));
	}

	/**
	 * 生成点赞项目的HTML
	 *
	 * @param jsonArray
	 * @return
	 */
	@Test
	public static String generateStarredHtml(JSONArray jsonArray) {
		String html = "";
		for (int i = 0; i < 5; i++) {
			Object o = jsonArray.get(i);
			System.out.println(o);
			JSONObject object = (JSONObject) JSONObject.parse(o.toString());
			String li = "<li>" + "<a href=" + object.get("html_url") + ">" + object.get("full_name") + "</a><p>" + object.get("description") + "</p>" + "</li>";

			html += li;
		}
		return html = "<ul>" + html + "</ul>";
	}

	/**
	 * 获取仓库信息并生成HTML
	 *
	 * @return
	 * @throws Exception
	 */
	public static String getRepos() throws Exception {
		GitHubApiManage config = new GitHubApiManage();
		String html = "";
		for (String repo : config.getRepos()) {
			String url = "https://github.com/" + repo;
			String tr = "<tr>" + "  <td><a href=\"" + url + "\"><b>" + repo + "</b></a></td>" + "<td><img alt=\"Stars\" src=\"https://img.shields.io/github/stars/" + repo + "?style=flat-square&labelColor=343b41\"/></td>" + "<td><img alt=\"Forks\" src=\"https://img.shields.io/github/forks/" + repo + "?style=flat-square&labelColor=343b41\"/></td>" + "<td><a href=\"https://github.com/" + repo + "/issues\" target=\"_blank\">" + "<img alt=\"Issues\" src=\"https://img.shields.io/github/issues/" + repo + "?style=flat-square&labelColor=343b41\"/></a></td>" + "<td><a href=\"https://github.com/" + repo + "/pulls\" target=\"_blank\">" + "<img alt=\"Pull Requests\" src=\"https://img.shields.io/github/issues-pr/" + repo + "?style=flat-square&labelColor=343b41\"/></a></td>" + "<td><a href=\"https://github.com/" + repo + "/commits\" target=\"_blank\"><img alt=\"Last Commits\" src=\"https://img.shields.io/github/last-commit/" + repo + "?style=flat-square&labelColor=343b41\"/></a></td>" + "</tr>";
			html += tr;

		}
		return html;
	}

	/**
	 * 将文本写道文件
	 *
	 * @param text
	 */
	public static void writeFile(String text) {
		try (InputStream is = new ByteArrayInputStream(text.getBytes()); OutputStream os = new FileOutputStream("README.md")) {  //注意OS的第二参数，是否追加。
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			System.out.println("写入完成");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
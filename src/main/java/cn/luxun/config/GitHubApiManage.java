package cn.luxun.config;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class GitHubApiManage {

	private String apiUrl = "https://api.github.com/";

	private String githubName = "hashower";

	private String startred = "users/" + githubName + "/starred";

	private String[] repos = {
			"hashower/reggie_take_out",
			"hashower/Smart_School",
			"hashower/SpringBoot-Blog",
			"hashower/SpringBoot-b2c-mall",
	};

	private JSONArray jsonArray;

}

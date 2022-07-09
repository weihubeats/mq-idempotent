package com.mq.idempotent.core.alert.strategy;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.mq.idempotent.core.spi.Join;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author : wh
 * @date : 2022/6/15 18:05
 * @description:
 */
@Join
public class LarkAlarmStrategy implements AlertStrategy {

	private static final String tete = "{\"msg_type\":\"interactive\",\"card\":{\"config\":{\"wide_screen_mode\":true},\"header\":{\"template\":\"greed\",\"title\":{\"content\":\"mq 幂等报警\",\"tag\":\"plain_text\"}},\"elements\":[{\"fields\":[{\"is_short\":true,\"text\":{\"content\":\"** 唯一key：** %s\",\"tag\":\"lark_md\"}},{\"is_short\":true,\"text\":{\"content\":\"** methodName：** %s\",\"tag\":\"lark_md\"}},{\"is_short\":true,\"text\":{\"content\":\"** 异常** %s\",\"tag\":\"lark_md\"}}%s],\"tag\":\"div\"},{\"tag\":\"hr\"}]}}";

	private static final String ALL = ",{\"tag\":\"div\",\"text\":{\"content\":\"<at id=all></at> \",\"tag\":\"lark_md\"}}";

	private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient()
			.newBuilder().connectTimeout(50L, TimeUnit.SECONDS)
			.readTimeout(60L, TimeUnit.SECONDS)
			.build();
	@Override
	public boolean sendMsg(String text) {
		RequestBody body = RequestBody.create(
				MediaType.parse("application/json"), "");
		Request request = new Request.Builder()
				.url("")
				.post(body)
				.build();
		try {
			OK_HTTP_CLIENT.newCall(request).execute();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

}

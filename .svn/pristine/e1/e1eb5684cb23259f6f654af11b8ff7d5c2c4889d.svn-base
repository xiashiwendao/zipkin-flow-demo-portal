package brave.webmvc;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@RestController
@Configuration
@CrossOrigin // So that javascript can be hosted elsewhere
public class Frontend {
	
	@Autowired
	HttpClient httpclient;
	
	@RequestMapping("/{name}")
	public String callBackend(@PathVariable("name") String name) throws Throwable {
		
		//String address = "http://localhost:9080/brave-webmvc4-example/api/" + name;
		//String address = "http://localhost:8083/app/cloud/muji2" + name;
		String address = "http://localhost:9080/dubbo-consumer-web/dubbo/" + name;
		HttpPost httppost = new HttpPost(address);
		HttpClient httpclient = HttpClientFactory.getHttpClient();
		httppost.setHeader("Content-Type", "application/json; charset=utf-8");

		// 生成 HTTP POST 实体
		StringEntity stringEntity = new StringEntity("", ContentType.TEXT_PLAIN);
		stringEntity.setContentEncoding("UTF-8");
		stringEntity.setContentType("application/json");// 发送json数据需要设置contentType
		httppost.setEntity(stringEntity);

		// 发送Post,并返回一个HttpResponse对象
		HttpResponse httpResponse = httpclient.execute(httppost);
		HttpEntity httpEntity2 = httpResponse.getEntity();

		String outJson = null;
		outJson = EntityUtils.toString(httpEntity2);

		return outJson;
		/*
		HttpPost httppost = new HttpPost("http://localhost:8083/app/cloud/muji2");
		HttpResponse response = httpclient.execute(httppost);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println("result is: " + result);
		
		return result;
		*/
	}
}

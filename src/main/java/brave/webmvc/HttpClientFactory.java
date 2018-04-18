package brave.webmvc;

import brave.Tracing;
import brave.context.log4j2.ThreadContextCurrentTraceContext;
import brave.http.HttpTracing;
import brave.httpclient.TracingHttpClientBuilder;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import brave.sampler.Sampler;
import brave.spring.webmvc.DelegatingTracingFilter;

import org.apache.http.client.HttpClient;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

/**
 * This adds tracing configuration to any web mvc controllers or rest template
 * clients.
 *
 * <p>
 * This is a {@link Initializer#getRootConfigClasses() root config class}, so
 * the {@linkplain DelegatingTracingFilter} added in
 * {@link Initializer#getServletFilters()} can wire up properly.
 */
public class HttpClientFactory {
	private static HttpClientFactory instance;
	private static HttpClient httpClient;
	private static Object lock = new Object();

	public static HttpClient getHttpClient() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new HttpClientFactory();
					httpClient = instance.httClient();
				}
			}
		}
		
		return httpClient;
	}

	public HttpClient httClient() {
		return TracingHttpClientBuilder.create(httpTracing()).build();
	}

	/** Configuration for how to send spans to Zipkin */
	Sender sender() {
		//return OkHttpSender.create("http://10.4.120.77:9411/api/v2/spans");
		return OkHttpSender.create("http://localhost:9411/api/v2/spans");
	}

	/** Configuration for how to buffer spans into messages for Zipkin */
	AsyncReporter<Span> spanReporter() {
		return AsyncReporter.create(sender());
	}

	/** Controls aspects of tracing such as the name that shows up in the UI */
	Tracing tracing() {
		return Tracing.newBuilder().localServiceName("httpClient-brave").sampler(Sampler.ALWAYS_SAMPLE)
				.propagationFactory(ExtraFieldPropagation.newFactory(B3Propagation.FACTORY, "user-name"))
				// puts trace IDs into logs
				.currentTraceContext(ThreadContextCurrentTraceContext.create()).spanReporter(spanReporter()).build();
	}

	// decides how to name and tag spans. By default they are named the same as
	// the http method.
	public HttpTracing httpTracing() {
		return HttpTracing.create(tracing());
	}
}
